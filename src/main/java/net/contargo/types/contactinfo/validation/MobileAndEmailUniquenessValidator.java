package net.contargo.types.contactinfo.validation;

import net.contargo.types.Loggable;
import net.contargo.types.contactinfo.ContactInfoConsumer;
import net.contargo.types.contactinfo.ContactInformation;
import net.contargo.types.contactinfo.normalization.EmailAddressNormalizer;
import net.contargo.types.contactinfo.normalization.PhoneNumberNormalizer;

import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class MobileAndEmailUniquenessValidator implements ContactInfoConsumer, Loggable, UniquenessValidator {

    private final ConcurrentMap<String, String> userUuidToMail;
    private final ConcurrentMap<String, String> userUuidToMobile;
    private final ConcurrentMap<String, Set<String>> mobileToUserUuids;
    private final ConcurrentMap<String, Set<String>> mailToUserUuids;
    private final PhoneNumberNormalizer phoneNumberNormalizer;
    private final EmailAddressNormalizer emailAddressNormalizer;
    private boolean isConsumingRegistrations = true;

    public MobileAndEmailUniquenessValidator(PhoneNumberNormalizer phoneNumberNormalizer,
        EmailAddressNormalizer emailAddressNormalizer) {

        this.phoneNumberNormalizer = phoneNumberNormalizer;
        this.emailAddressNormalizer = emailAddressNormalizer;

        this.userUuidToMail = new ConcurrentHashMap<>();
        this.userUuidToMobile = new ConcurrentHashMap<>();
        this.mailToUserUuids = new ConcurrentHashMap<>();
        this.mobileToUserUuids = new ConcurrentHashMap<>();
    }


    public MobileAndEmailUniquenessValidator(PhoneNumberNormalizer phoneNumberNormalizer,
        EmailAddressNormalizer emailAddressNormalizer, final boolean isConsumingRegistrations) {

        this(phoneNumberNormalizer, emailAddressNormalizer);
        this.isConsumingRegistrations = isConsumingRegistrations;
    }

    @Override
    public void consume(final List<ContactInformation> allContactInformation) {

        allContactInformation.forEach(this::extractMobileAndMailFromProfile);
    }


    @Override
    public void consume(ContactInformation contactInformation) {

        extractMobileAndMailFromProfile(contactInformation);
    }


    private void extractMobileAndMailFromProfile(final ContactInformation contactInformation) {

        if (contactInformation == null) {
            return;
        }

        final String userUuid = contactInformation.getUserUuid();

        final String oldMail = emailAddressNormalizer.normalizeEmailAddress(userUuidToMail.get(userUuid));
        final String newMail = emailAddressNormalizer.normalizeEmailAddress(contactInformation.getEmail());

        if (StringUtils.isNotBlank(newMail) && StringUtils.isBlank(oldMail)) {
            handleNewMailAddress(newMail, userUuid);
        } else if (StringUtils.isNotBlank(oldMail) && StringUtils.isBlank(newMail)) {
            handleRemovedMailAddress(userUuid, oldMail);
        } else {
            if (!oldMail.equals(newMail)) {
                handleChangedMailAddress(newMail, userUuid, oldMail);
            }
        }

        final String oldMobile = phoneNumberNormalizer.normalizeNumber(userUuidToMobile.get(userUuid)).orElse("");
        final String newMobile = phoneNumberNormalizer.normalizeNumber(contactInformation.getMobile()).orElse("");

        if (StringUtils.isNotBlank(newMobile) && StringUtils.isBlank(oldMobile)) {
            handleNewMobile(newMobile, userUuid);
        } else if (StringUtils.isBlank(newMobile) && StringUtils.isNotBlank(oldMobile)) {
            handleRemovedMobile(userUuid, oldMobile);
        } else {
            if (!oldMobile.equals(newMobile)) {
                handleChangedMobile(newMobile, userUuid, oldMobile);
            }
        }
    }


    private void handleChangedMobile(final String newMobile, final String userUuid, final String oldMobile) {

        userUuidToMobile.put(userUuid, newMobile);
        mobileToUserUuids.getOrDefault(oldMobile, Collections.emptySet()).remove(userUuid);
        mobileToUserUuids.putIfAbsent(newMobile, new HashSet<>());
        mobileToUserUuids.get(newMobile).add(userUuid);
    }


    private void handleChangedMailAddress(final String newMail, final String userUuid, final String oldMail) {

        userUuidToMail.put(userUuid, newMail);
        mailToUserUuids.getOrDefault(oldMail, Collections.emptySet()).remove(userUuid);
        mailToUserUuids.putIfAbsent(newMail, new HashSet<>());
        mailToUserUuids.get(newMail).add(userUuid);
    }


    private void handleRemovedMobile(final String userUuid, final String oldMobile) {

        userUuidToMobile.remove(userUuid);

        if (StringUtils.isNotBlank(oldMobile)) {
            mobileToUserUuids.getOrDefault(oldMobile, Collections.emptySet()).remove(userUuid);
        }
    }


    private void handleNewMobile(final String newMobile, final String userUuid) {

        userUuidToMobile.put(userUuid, newMobile);

        // add reverse mapping
        if (!mobileToUserUuids.containsKey(newMobile)) {
            mobileToUserUuids.putIfAbsent(newMobile, new HashSet<>());
        }

        mobileToUserUuids.get(newMobile).add(userUuid);
    }


    private void handleRemovedMailAddress(final String userUuid, final String oldMail) {

        userUuidToMail.remove(userUuid);

        if (StringUtils.isNotBlank(oldMail)) {
            mailToUserUuids.getOrDefault(oldMail, Collections.emptySet()).remove(userUuid);
        }
    }


    private void handleNewMailAddress(final String newMail, String userUuid) {

        userUuidToMail.put(userUuid, newMail);

        // add reverse mapping
        if (!mailToUserUuids.containsKey(newMail)) {
            mailToUserUuids.putIfAbsent(newMail, new HashSet<>());
        }

        mailToUserUuids.get(newMail).add(userUuid);
    }


    @Override
    public boolean consumesRegistrations() {

        return this.isConsumingRegistrations;
    }


    @Override
    public void remove(ContactInformation contactInformation) {

        handleRemovedMailAddress(contactInformation.getUserUuid(),
            emailAddressNormalizer.normalizeEmailAddress(contactInformation.getEmail()));
        handleRemovedMobile(contactInformation.getUserUuid(),
            phoneNumberNormalizer.normalizeNumber(contactInformation.getMobile()).orElse(""));
    }


    @Override
    public List<ValidationResult> checkUniqueness(final ContactInformation contactInformation) {

        final boolean mobileUnique = isMobileUnique(contactInformation.getUserUuid(), contactInformation.getMobile());

        List<ValidationResult> messages = new ArrayList<>();

        if (!mobileUnique) {
            messages.add(ValidationResult.NON_UNIQUE_MOBILE);
        }

        final boolean uniqueEmail = isEmailUnique(contactInformation.getUserUuid(), contactInformation.getEmail());

        if (!uniqueEmail) {
            messages.add(ValidationResult.NON_UNIQUE_EMAIL);
        }

        return messages;
    }


    @Override
    public boolean isEmailUnique(final String userUuid, final String email) {

        final String normalizedEmail = emailAddressNormalizer.normalizeEmailAddress(email);

        return isValueUniqueForKey(normalizedEmail, userUuid, mailToUserUuids);
    }


    @Override
    public boolean isMobileUnique(final String userUuid, final String mobile) {

        final String normalizedMobileNumber = phoneNumberNormalizer.normalizeNumber(mobile).orElse("");

        return isValueUniqueForKey(normalizedMobileNumber, userUuid, mobileToUserUuids);
    }


    private boolean isValueUniqueForKey(final String key, final String value,
        final Map<String, Set<String>> dataToCheck) {

        if (dataToCheck.containsKey(key)) {
            Set<String> values = dataToCheck.get(key);

            // only one presence and mapped to the requester's userUuid
            if (!values.isEmpty()) { // there are values for this key

                if (values.size() == 1 && values.contains(value)) { // only one value equals to the requested value: unique
                    return true;
                } else { // more values or one not equal to the requested one: not unique
                    logger().info("detected non-unique value {}. claimed by {} but already taken by {}.", key, value,
                        String.join(",", values));

                    return false;
                }
            } else {
                return true;
            }
        } else { // no mappings yet -> unique
            return true;
        }
    }
}
