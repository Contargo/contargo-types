package net.contargo.types.telephony.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Validates whether the provided string is a valid phone number.
 *
 * @author  Robin Jayasinghe - jayasinghe@synyx.de
 */
public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    @Override
    public void initialize(ValidPhoneNumber a) {

        // nothing to do
    }


    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext cvc) {

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        final String phoneNumberWithoutWhitespace = phoneNumber.replaceAll("\\s+", "");

        try {
            phoneNumberUtil.parse(phoneNumberWithoutWhitespace, "DE");

            return true;
        } catch (NumberParseException e1) {
            return false;
        }
    }
}
