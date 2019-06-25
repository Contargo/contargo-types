package net.contargo.types.telephony.validation;

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
public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, PhoneNumber> {

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

        return value.isPhoneNumber() && !isPhoneNumberOnlyZeros(value);
    }


    private boolean isPhoneNumberOnlyZeros(PhoneNumber phoneNumber) {

        return phoneNumber.getRawPhoneNumber().matches("0+$");
    }
}
