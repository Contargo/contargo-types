package net.contargo.types.telephony.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Validates whether the provided string is a valid phone number.
 *
 * @author  Robin Jayasinghe - jayasinghe@synyx.de
 * @author  Olle Törnström - toernstroem@synyx.de
 */
public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    @Override
    public void initialize(ValidPhoneNumber a) {

        // nothing to do
    }


    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext cvc) {

        // We must assume non-null, non-empty is validated elsewhere
        if (Objects.isNull(phoneNumber) || phoneNumber.trim().isEmpty()) {
            return true;
        }

        String phoneNumberWithoutWhitespace = phoneNumber.replaceAll("\\s+", "");

        try {
            phoneNumberUtil.parse(phoneNumberWithoutWhitespace, "DE");

            return true;
        } catch (NumberParseException e1) {
            return false;
        }
    }
}
