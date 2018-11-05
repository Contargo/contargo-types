package net.contargo.types.contactinfo.normalization;

import net.contargo.types.Loggable;
import net.contargo.types.telephony.formatting.PhoneNumberFormatter;
import net.contargo.types.telephony.formatting.PhoneNumberFormattingException;

import java.util.Optional;


public class PhoneNumberNormalizer implements Loggable {

    public Optional<String> normalizeNumber(String phoneNumber) {

        if (phoneNumber == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(new PhoneNumberFormatter().parseAndFormatToE164Format(phoneNumber));
        } catch (PhoneNumberFormattingException e) {
            logger().warn("Failed to parse and format number {}: {}", phoneNumber, e.getMessage());

            return Optional.empty();
        }
    }
}
