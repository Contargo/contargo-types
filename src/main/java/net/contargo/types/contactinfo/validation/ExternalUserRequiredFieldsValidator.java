package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInformation;

import java.util.ArrayList;
import java.util.List;


public class ExternalUserRequiredFieldsValidator implements CompletenessValidator {

    private static boolean hasText(final String value) {

        return value != null && value.length() > 0;
    }


    @Override
    public List<ValidationResult> checkCompleteness(final ContactInformation contactInformation) {

        List<ValidationResult> messages = new ArrayList<>();

        final boolean missingEmail = !hasText(contactInformation.getEmail());
        final boolean missingMobile = !hasText(contactInformation.getMobile());
        final boolean missingPhone = !hasText(contactInformation.getPhone());

        if (missingEmail && missingMobile) {
            messages.add(ValidationResult.MISSING_EMAIL);
            messages.add(ValidationResult.MISSING_MOBILE);

            // will trigger default message without messages on mail or mobile property since they are empty
        }

        // Communication with unique mobile requires some phone number
        if (missingPhone && missingMobile) {
            messages.add(ValidationResult.MISSING_PHONE);
            messages.add(ValidationResult.MISSING_MOBILE);
        }

        return messages;
    }
}
