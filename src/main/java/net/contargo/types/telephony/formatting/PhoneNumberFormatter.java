package net.contargo.types.telephony.formatting;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;


public class PhoneNumberFormatter {

    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public String parseAndFormatToE164Format(final String phoneNumber) throws PhoneNumberFormattingException {

        final String phoneNumberWithoutWhitespace = phoneNumber.replaceAll("\\s+", "");

        final Phonenumber.PhoneNumber parsedNumber;

        parsedNumber = parsePhoneNumber(phoneNumber, phoneNumberWithoutWhitespace);

        final String formattedNumber = phoneNumberUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

        return formattedNumber;
    }


    private Phonenumber.PhoneNumber parsePhoneNumber(String phoneNumber, String phoneNumberWithoutWhitespace)
        throws PhoneNumberFormattingException {

        Phonenumber.PhoneNumber parsedNumber;

        try {
            parsedNumber = phoneNumberUtil.parse(phoneNumberWithoutWhitespace, "DE");
        } catch (NumberParseException e) {
            throw new PhoneNumberFormattingException(String.format("Cannot parse number %s.", phoneNumber), e);
        }

        return parsedNumber;
    }


    public String parseAndFormatToDIN5008(final String phoneNumber) {

        throw new UnsupportedOperationException("not implemented yet");
    }
}
