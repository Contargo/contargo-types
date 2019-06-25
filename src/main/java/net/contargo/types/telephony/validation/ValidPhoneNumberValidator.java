package net.contargo.types.telephony.validation;

import net.contargo.types.telephony.PhoneNumber;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Validates whether the provided string is a valid phone number.
 *
 * @author  Robin Jayasinghe - jayasinghe@synyx.de
 * @author  Olle Törnström - toernstroem@synyx.de
 */
public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, PhoneNumber> {

    private static final int PHONE_NUMBER_SIZE = 64;

    @Override
    public void initialize(ValidPhoneNumber a) {

        // nothing to do
    }


    @Override
    public boolean isValid(PhoneNumber value, ConstraintValidatorContext cvc) {

        // We must assume non-null, non-empty is validated elsewhere
        if (value == null) {
            return true;
        }

        List<PhoneNumberValidationResult> validationResults = checkPhoneNumber(value);

        validationResults.forEach(validationResult -> {
            final String messageTemplate;
            String propertyName = "phone";

            if (value.isMobile()) {
                propertyName = "mobile";
            }

            switch (validationResult) {
                case ZERO_NUMBER:
                    messageTemplate = "{ZERO_NUMBER}";
                    break;

                case CAN_NOT_FORMATTED:
                    messageTemplate = "{CAN_NOT_FORMATTED}";
                    break;

                case TOO_LARGE:
                    messageTemplate = "{TOO_LARGE}";
                    break;

                default:
                    throw new IllegalArgumentException("unknown validation result for contact info");
            }

            reportConstraintViolation(cvc, messageTemplate, propertyName);
        });

        return validationResults.isEmpty();
    }


    private List<PhoneNumberValidationResult> checkPhoneNumber(final PhoneNumber phoneNumber) {

        List<PhoneNumberValidationResult> phoneNumberValidationResults = new ArrayList<>();

        if (!StringUtils.isBlank(phoneNumber.getRawPhoneNumber())) {
            if (phoneNumber.containsOnlyZeros()) {
                phoneNumberValidationResults.add(PhoneNumberValidationResult.ZERO_NUMBER);
            }

            if (!phoneNumber.canBeFormatted()) {
                phoneNumberValidationResults.add(PhoneNumberValidationResult.CAN_NOT_FORMATTED);
            }

            if (phoneNumber.getRawPhoneNumber().length() > PHONE_NUMBER_SIZE) {
                phoneNumberValidationResults.add(PhoneNumberValidationResult.TOO_LARGE);
            }
        }

        return phoneNumberValidationResults;
    }


    private void reportConstraintViolation(final ConstraintValidatorContext context, final String messageTemplate,
        final String propertyName) {

        context.buildConstraintViolationWithTemplate(messageTemplate)
            .addPropertyNode(propertyName).addConstraintViolation();
    }
}
