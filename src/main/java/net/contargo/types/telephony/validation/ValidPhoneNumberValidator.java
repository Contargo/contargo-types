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
public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public void initialize(ValidPhoneNumber a) {

        // nothing to do
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext cvc) {

        PhoneNumber phoneNumber = new PhoneNumber(value);

        // We must assume non-null, non-empty is validated elsewhere
        if (StringUtils.isBlank(value) || phoneNumber.getRawPhoneNumber().trim().isEmpty()) {
            return true;
        }

        return phoneNumber.isPhoneNumber() && !isPhoneNumberOnlyZeros(phoneNumber);
    }


    private boolean isPhoneNumberOnlyZeros(PhoneNumber phoneNumber) {

        return phoneNumber.getRawPhoneNumber().matches("0+$");
    }
}
