package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInfoConsumer;
import net.contargo.types.contactinfo.ContactInformation;
import net.contargo.types.util.Loggable;

import java.util.*;


public class RequiredContactInfoValidationService implements Loggable, ContactInfoConsumer {

    enum ValidationResult {

        MISSING_PHONE,
        MISSING_MOBILE,
        MISSING_EMAIL,
        NON_UNIQUE_PHONE, // TODO: implement this case
        NON_UNIQUE_MOBILE,
        NON_UNIQUE_EMAIL
    }

    private final Map<String, String> userUuidToMail;
    private final Map<String, String> userUuidToMobile;
    private final Map<String, Set<String>> mobileToUserUuids;
    private final Map<String, Set<String>> mailToUserUuids;
    private final PhoneNumberNormalizer phoneNumberNormalizer;
    private final EmailAddressNormalizer emailAddressNormalizer;

    public RequiredContactInfoValidationService(PhoneNumberNormalizer phoneNumberNormalizer,
        EmailAddressNormalizer emailAddressNormalizer) {

        this.phoneNumberNormalizer = phoneNumberNormalizer;
        this.emailAddressNormalizer = emailAddressNormalizer;

        this.userUuidToMail = new HashMap<>();
        this.userUuidToMobile = new HashMap<>();
        this.mailToUserUuids = new HashMap<>();
        this.mobileToUserUuids = new HashMap<>();
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

        if (hasText(contactInformation.getEmail())) {
            final String mail = emailAddressNormalizer.normalizeEmailAddress(contactInformation.getEmail());
            userUuidToMail.put(contactInformation.getUserUuid(), contactInformation.getEmail());

            // add reverse mapping
            if (!mailToUserUuids.containsKey(mail)) {
                mailToUserUuids.put(mail, new HashSet<>());
            }

            mailToUserUuids.get(mail).add(userUuid);
        }

        if (hasText(contactInformation.getMobile())) {
            final String mobile = phoneNumberNormalizer.normalizeNumber(contactInformation.getMobile()).orElse("");
            userUuidToMobile.put(contactInformation.getUserUuid(), contactInformation.getMobile());

            // add reverse mapping
            if (!mobileToUserUuids.containsKey(mobile)) {
                mobileToUserUuids.put(mobile, new HashSet<>());
            }

            mobileToUserUuids.get(mobile).add(userUuid);
        }
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
            if (values.size() == 1 && values.contains(value)) {
                return true;
            } else if (values.size() > 1) { // value set contains more than one value -> not unique
                return false;
            } else { // value set is empty -> unique
                return true;
            }
        } else { // no mappings yet -> unique
            return true;
        }
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
