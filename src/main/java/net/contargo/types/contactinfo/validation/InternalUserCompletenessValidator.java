package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInformation;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Checks the completeness of a {@link net.contargo.types.contactinfo.ContactInfoConsumer} object according to the
 * rules for internal COLA users:
 *
 * <ul>
 *   <li>Must have a e-mail address.</li>
 * </ul>
 */
public class InternalUserCompletenessValidator implements CompletenessValidator {

    @Override
    public List<ValidationResult> checkCompleteness(ContactInformation contactInformation) {

        List<ValidationResult> messages = new ArrayList<>();

        final boolean missingEmail = StringUtils.isEmpty(contactInformation.getEmail());

        if (missingEmail) {
            messages.add(ValidationResult.MISSING_EMAIL);

            // will trigger default message without messages on mail or mobile property since they are empty
        }

        return messages;
    }
}
