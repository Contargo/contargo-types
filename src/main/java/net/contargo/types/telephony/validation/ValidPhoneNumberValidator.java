package net.contargo.types.telephony.validation;

import net.contargo.types.Loggable;
import net.contargo.types.telephony.PhoneNumber;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Validates whether the provided string is a valid phone number.
 *
 * @author  Robin Jayasinghe - jayasinghe@synyx.de
 * @author  Olle Törnström - toernstroem@synyx.de
 */
public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String>, Loggable {

    private static final int PHONE_NUMBER_SIZE = 64;

    @Override
    public void initialize(ValidPhoneNumber a) {
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext cvc) {

        if (StringUtils.isEmpty(value)) {
            return true;
        }

        final PhoneNumber phoneNumber = new PhoneNumber(value);

        boolean isValid = true;

        if (!StringUtils.isBlank(phoneNumber.getRawPhoneNumber())) {
            if (phoneNumber.containsOnlyZeros()) {
                reportConstraintViolation(cvc, "{PHONE_NUMBER_IS_ZERO_NUMBER}");
                isValid = false;
            }

            if (!phoneNumber.canBeFormatted()) {
                reportConstraintViolation(cvc, "{PHONE_NUMBER_CANNOT_BE_FORMATTED}");
                isValid = false;
            }

            if (phoneNumber.getRawPhoneNumber().length() > PHONE_NUMBER_SIZE) {
                reportConstraintViolation(cvc, "{PHONE_NUMBER_TOO_LARGE}");
                isValid = false;
            }

            if (!phoneNumber.isValidNumber()) {
                // TODO: to be discussed if the phone numbers has to be validated harder. currently only log info if the number is not valid.
                logger().info("PHONE_NUMBER_INVALID - the given number '{}' ist not valid!",
                    phoneNumber.getRawPhoneNumber());
            }
        }

        return isValid;
    }


    private void reportConstraintViolation(final ConstraintValidatorContext context, final String messageTemplate) {

        context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
    }
}
