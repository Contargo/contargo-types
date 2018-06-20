package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInfoConsumer;
import net.contargo.types.contactinfo.ContactInformation;
import net.contargo.types.util.Loggable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class RequiredContactInfoValidationService implements Loggable, ContactInfoConsumer {

    public enum ValidationResult {

        MISSING_PHONE,
        MISSING_MOBILE,
        MISSING_EMAIL,
        NON_UNIQUE_PHONE, // TODO: implement this case
        NON_UNIQUE_MOBILE,
        NON_UNIQUE_EMAIL
    }

    private final ConcurrentMap<String, String> userUuidToMail;
    private final ConcurrentMap<String, String> userUuidToMobile;
    private final ConcurrentMap<String, Set<String>> mobileToUserUuids;
    private final ConcurrentMap<String, Set<String>> mailToUserUuids;
    private final PhoneNumberNormalizer phoneNumberNormalizer;
    private final EmailAddressNormalizer emailAddressNormalizer;

    public RequiredContactInfoValidationService(PhoneNumberNormalizer phoneNumberNormalizer,
        EmailAddressNormalizer emailAddressNormalizer) {

        this.phoneNumberNormalizer = phoneNumberNormalizer;
        this.emailAddressNormalizer = emailAddressNormalizer;

        this.userUuidToMail = new ConcurrentHashMap<>();
        this.userUuidToMobile = new ConcurrentHashMap<>();
        this.mailToUserUuids = new ConcurrentHashMap<>();
        this.mobileToUserUuids = new ConcurrentHashMap<>();
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

        if (hasText(newMail) && !hasText(oldMail)) {
            handleNewMailAddress(newMail, userUuid);
        } else if (hasText(oldMail) && !hasText(newMail)) {
            handleRemovedMailAddress(userUuid, oldMail);
        } else {
            if (!oldMail.equals(newMail)) {
                handleChangedMailAddress(newMail, userUuid, oldMail);
            }
        }

        final String oldMobile = phoneNumberNormalizer.normalizeNumber(userUuidToMobile.get(userUuid)).orElse("");
        final String newMobile = phoneNumberNormalizer.normalizeNumber(contactInformation.getMobile()).orElse("");

        if (hasText(newMobile) && !hasText(oldMobile)) {
            handleNewMobile(newMobile, userUuid);
        } else if (!hasText(newMobile) && hasText(oldMobile)) {
            handleRemovedMobile(userUuid, oldMobile);
        } else {
            if (!oldMobile.equals(newMobile)) {
                handleChangedMobile(newMobile, userUuid);
            }
        }
    }


    private void handleChangedMobile(final String newMobile, final String userUuid) {

        userUuidToMobile.put(userUuid, newMobile);
        mobileToUserUuids.getOrDefault(newMobile, Collections.emptySet()).remove(userUuid);
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
        mobileToUserUuids.getOrDefault(oldMobile, Collections.emptySet()).remove(userUuid);
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
        mailToUserUuids.getOrDefault(oldMail, Collections.emptySet()).remove(userUuid);
    }


    private void handleNewMailAddress(final String newMail, String userUuid) {

        userUuidToMail.put(userUuid, newMail);

        // add reverse mapping
        if (!mailToUserUuids.containsKey(newMail)) {
            mailToUserUuids.putIfAbsent(newMail, new HashSet<>());
        }

        mailToUserUuids.get(newMail).add(userUuid);
    }


    private static boolean hasText(final String value) {

        return value != null && value.length() > 0;
    }


    public boolean validate(final ContactInformation contactInformation, List<ValidationResult> messages) {

        final boolean missingEmail = !hasText(contactInformation.getEmail());
        final boolean missingMobile = !hasText(contactInformation.getMobile());
        final boolean missingPhone = !hasText(contactInformation.getPhone());

        if (missingEmail && missingMobile) {
            messages.add(ValidationResult.MISSING_EMAIL);
            messages.add(ValidationResult.MISSING_MOBILE);

            // will trigger default message without messages on mail or mobile property since they are empty
            return false;
        }

        if (missingEmail) {
            // only unique mobile numbers allowed when no email is given
            final boolean mobileUnique = isMobileUnique(contactInformation.getUserUuid(),
                    contactInformation.getMobile());

            if (!mobileUnique) {
                messages.add(ValidationResult.NON_UNIQUE_MOBILE);
            }

            return mobileUnique;
        }

        final boolean uniqueEmail = isEmailUnique(contactInformation.getUserUuid(), contactInformation.getEmail());

        if (!uniqueEmail) {
            messages.add(ValidationResult.NON_UNIQUE_EMAIL);

            return false;
        }

        // Communication with unique mobile requires some phone number
        if (missingPhone && missingMobile) {
            messages.add(ValidationResult.MISSING_PHONE);
            messages.add(ValidationResult.MISSING_MOBILE);

            return false;
        }

        return true;
    }


    private static boolean isValueUniqueForKey(final String key, final String value,
        final Map<String, Set<String>> dataToCheck) {

        if (dataToCheck.containsKey(key)) {
            Set<String> values = dataToCheck.get(key);

            // only one presence and mapped to the requester's userUuid
            if (!values.isEmpty()) { // there are values for this key

                if (values.size() == 1 && values.contains(value)) { // only one value equals to the requested value: unique
                    return true;
                } else { // more values or one not equal to the requested one: not unique
                    return false;
                }
            } else {
                return true;
            }
        } else { // no mappings yet -> unique
            return true;
        }
    }


    @Override
    public void remove(ContactInformation contactInformation) {

        handleRemovedMailAddress(contactInformation.getUserUuid(), contactInformation.getEmail());
        handleRemovedMobile(contactInformation.getUserUuid(), contactInformation.getMobile());
    }


    private boolean isEmailUnique(final String userUuid, final String email) {

        final String normalizedEmail = emailAddressNormalizer.normalizeEmailAddress(email);

        return isValueUniqueForKey(normalizedEmail, userUuid, mailToUserUuids);
    }


    private boolean isMobileUnique(final String userUuid, final String mobile) {

        final String normalizedMobileNumber = phoneNumberNormalizer.normalizeNumber(mobile).orElse("");

        return isValueUniqueForKey(normalizedMobileNumber, userUuid, mobileToUserUuids);
    }
}
