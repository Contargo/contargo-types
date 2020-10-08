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


/**
 * A UniquenessValidator implementation that consumes ContactInfo objects that are usually provided by async listeners
 * of COLA system events.
 */
public class ReactiveUniquenessValidator implements ContactInfoConsumer, Loggable, UniquenessValidator {

    private final ConcurrentMap<String, String> userUUIDToMail;
    private final ConcurrentMap<String, String> userUUIDToMobile;
    private final ConcurrentMap<String, Set<String>> mobileToUserUUIDs;
    private final ConcurrentMap<String, Set<String>> mailToUserUUIDs;

    private final PhoneNumberNormalizer phoneNumberNormalizer;
    private final EmailAddressNormalizer emailAddressNormalizer;

    private boolean isConsumingRegistrations = true;

    public ReactiveUniquenessValidator(PhoneNumberNormalizer phoneNumberNormalizer,
        EmailAddressNormalizer emailAddressNormalizer) {

        this.phoneNumberNormalizer = phoneNumberNormalizer;
        this.emailAddressNormalizer = emailAddressNormalizer;

        this.userUUIDToMail = new ConcurrentHashMap<>();
        this.userUUIDToMobile = new ConcurrentHashMap<>();
        this.mailToUserUUIDs = new ConcurrentHashMap<>();
        this.mobileToUserUUIDs = new ConcurrentHashMap<>();
    }


    public ReactiveUniquenessValidator(PhoneNumberNormalizer phoneNumberNormalizer,
        EmailAddressNormalizer emailAddressNormalizer, final boolean isConsumingRegistrations) {

        this(phoneNumberNormalizer, emailAddressNormalizer);
        this.isConsumingRegistrations = isConsumingRegistrations;
    }

    @Override
    public void consume(final List<ContactInformation> allContactInformation) {

        if (allContactInformation == null) {
            return;
        }

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

        final String userUUID = contactInformation.getUserUUID();

        final String oldMail = emailAddressNormalizer.normalizeEmailAddress(userUUIDToMail.get(userUUID));
        final String newMail = emailAddressNormalizer.normalizeEmailAddress(contactInformation.getEmail());

        if (StringUtils.isNotBlank(newMail) && StringUtils.isBlank(oldMail)) {
            handleNewMailAddress(newMail, userUUID);
        } else if (StringUtils.isNotBlank(oldMail) && StringUtils.isBlank(newMail)) {
            handleRemovedMailAddress(userUUID, oldMail);
        } else {
            if (!oldMail.equals(newMail)) {
                handleChangedMailAddress(newMail, userUUID, oldMail);
            }
        }

        final String oldMobile = phoneNumberNormalizer.normalizeNumber(userUUIDToMobile.get(userUUID)).orElse("");
        final String newMobile = contactInformation.getMobile().getInternationalFormatOfPhoneNumber().orElse("");

        if (StringUtils.isNotBlank(newMobile) && StringUtils.isBlank(oldMobile)) {
            handleNewMobile(newMobile, userUUID);
        } else if (StringUtils.isBlank(newMobile) && StringUtils.isNotBlank(oldMobile)) {
            handleRemovedMobile(userUUID, oldMobile);
        } else {
            if (!oldMobile.equals(newMobile)) {
                handleChangedMobile(newMobile, userUUID, oldMobile);
            }
        }
    }


    private void handleChangedMobile(final String newMobile, final String userUUID, final String oldMobile) {

        userUUIDToMobile.put(userUUID, newMobile);

        mobileToUserUUIDs.computeIfPresent(oldMobile,
            (k, userUUIDs) -> {
                userUUIDs.remove(userUUID);

                return userUUIDs;
            });

        mobileToUserUUIDs.computeIfAbsent(newMobile, k -> new HashSet<>()).add(userUUID);
    }


    @Override
    public void reset() {

        userUUIDToMail.clear();
        userUUIDToMobile.clear();
        mobileToUserUUIDs.clear();
        mailToUserUUIDs.clear();

        logger().info("cleared all internal data structures");
    }


    private void handleChangedMailAddress(final String newMail, final String userUUID, final String oldMail) {

        userUUIDToMail.put(userUUID, newMail);

        mailToUserUUIDs.computeIfPresent(oldMail, (k, v) -> {
                v.remove(userUUID);

                return v;
            });

        mailToUserUUIDs.computeIfAbsent(newMail, k -> new HashSet<>()).add(userUUID);
    }


    private void handleRemovedMobile(final String userUUID, final String oldMobile) {

        userUUIDToMobile.remove(userUUID);

        if (StringUtils.isNotBlank(oldMobile)) {
            mobileToUserUUIDs.computeIfPresent(oldMobile,
                (k, userUUIDs) -> {
                    userUUIDs.remove(userUUID);

                    return userUUIDs;
                });
        }
    }


    private void handleNewMobile(final String newMobile, final String userUUID) {

        userUUIDToMobile.put(userUUID, newMobile);

        // add reverse mapping
        mobileToUserUUIDs.computeIfAbsent(newMobile, k -> new HashSet<>()).add(userUUID);
    }


    private void handleRemovedMailAddress(final String userUUID, final String oldMail) {

        userUUIDToMail.remove(userUUID);

        if (StringUtils.isNotBlank(oldMail)) {
            mailToUserUUIDs.computeIfPresent(oldMail,
                (k, userUUIDs) -> {
                    userUUIDs.remove(userUUID);

                    return userUUIDs;
                });
        }
    }


    private void handleNewMailAddress(final String newMail, String userUUID) {

        userUUIDToMail.put(userUUID, newMail);

        // add reverse mapping
        mailToUserUUIDs.computeIfAbsent(newMail, k -> new HashSet<>()).add(userUUID);
    }


    @Override
    public boolean consumesRegistrations() {

        return this.isConsumingRegistrations;
    }


    @Override
    public void remove(ContactInformation contactInformation) {

        handleRemovedMailAddress(contactInformation.getUserUUID(),
            emailAddressNormalizer.normalizeEmailAddress(contactInformation.getEmail()));
        handleRemovedMobile(contactInformation.getUserUUID(),
            contactInformation.getMobile().getInternationalFormatOfPhoneNumber().orElse(""));
    }


    @Override
    public List<ValidationResult> checkUniqueness(final ContactInformation contactInformation) {

        final boolean mobileUnique = isMobileUnique(contactInformation.getUserUUID(),
                contactInformation.getMobile().getInternationalFormatOfPhoneNumber().orElse(""));

        List<ValidationResult> messages = new ArrayList<>();

        if (!mobileUnique) {
            messages.add(ValidationResult.NON_UNIQUE_MOBILE);
        }

        final boolean uniqueEmail = isEmailUnique(contactInformation.getUserUUID(), contactInformation.getEmail());

        if (!uniqueEmail) {
            messages.add(ValidationResult.NON_UNIQUE_EMAIL);
        }

        return messages;
    }


    @Override
    public boolean isEmailUnique(final String userUUID, final String email) {

        final String normalizedEmail = emailAddressNormalizer.normalizeEmailAddress(email);

        return isValueUniqueForKey(normalizedEmail, userUUID, mailToUserUUIDs);
    }


    @Override
    public boolean isMobileUnique(final String userUUID, final String mobile) {

        return isValueUniqueForKey(mobile, userUUID, mobileToUserUUIDs);
    }


    protected boolean isValueUniqueForKey(final String key, final String value,
        final ConcurrentMap<String, Set<String>> dataToCheck) {

        // Use local copy of values, since modifications on the mapped set may occur
        HashSet<String> values = new HashSet<>(dataToCheck.getOrDefault(key, Collections.emptySet()));

        // No values for key: implied unique (we don't know)
        if (values.isEmpty()) {
            return true;
        }

        // Exactly one value equal to the requested value: distinct unique, for key.
        if (values.size() == 1 && values.contains(value)) {
            return true;
        }

        // More values or one that is not equal to the requested: distinct not unique, for key.
        logger().info("detected non-unique value {}. claimed by {} but already taken by {}.", key, value,
            String.join(",", values));

        return false;
    }
}
