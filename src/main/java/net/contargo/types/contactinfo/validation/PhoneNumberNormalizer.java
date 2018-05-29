package net.contargo.types.contactinfo.validation;

import net.contargo.types.telephony.formatting.PhoneNumberFormatter;
import net.contargo.types.telephony.formatting.PhoneNumberFormattingException;
import net.contargo.types.util.Loggable;

import java.util.Optional;


public class PhoneNumberNormalizer implements Loggable {

    public Optional<String> normalizeNumber(String phoneNumber) {

        try {
            return Optional.of(new PhoneNumberFormatter().parseAndFormatToE164Format(phoneNumber));
        } catch (PhoneNumberFormattingException e) {
            logger().warn("Failed to parse and format number", e);

            return Optional.empty();
        }
    }
}
