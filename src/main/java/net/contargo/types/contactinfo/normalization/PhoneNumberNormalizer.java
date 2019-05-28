package net.contargo.types.contactinfo.normalization;

import net.contargo.types.Loggable;
import net.contargo.types.telephony.PhoneNumber;

import org.springframework.util.StringUtils;

import java.util.Optional;


public class PhoneNumberNormalizer implements Loggable {

    public Optional<String> normalizeNumber(String phoneNumber) {

        if (StringUtils.hasText(phoneNumber)) {
            return Optional.empty();
        }

        return new PhoneNumber(phoneNumber).getInternationalFormatOfPhoneNumber();
    }
}
