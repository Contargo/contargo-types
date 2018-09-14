package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInformation;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class ExternalUserCompletenessValidator implements CompletenessValidator {

    @Override
    public List<ValidationResult> checkCompleteness(final ContactInformation contactInformation) {

        List<ValidationResult> messages = new ArrayList<>();

        final boolean missingEmail = StringUtils.isEmpty(contactInformation.getEmail());
        final boolean missingMobile = StringUtils.isEmpty(contactInformation.getMobile());
        final boolean missingPhone = StringUtils.isEmpty(contactInformation.getPhone());

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
