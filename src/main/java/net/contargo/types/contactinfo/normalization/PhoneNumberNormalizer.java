package net.contargo.types.contactinfo.normalization;

import net.contargo.types.Loggable;
import net.contargo.types.telephony.PhoneNumber;

import org.apache.commons.lang.StringUtils;

import java.util.Optional;


public class PhoneNumberNormalizer implements Loggable {

    public Optional<String> normalizeNumber(String phoneNumber) {

        if (StringUtils.isBlank(phoneNumber)) {
            return Optional.empty();
        }

        return new PhoneNumber(phoneNumber).getInternationalFormatOfPhoneNumber();
    }
}
