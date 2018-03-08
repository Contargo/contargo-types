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


    public String parseAndFormatToDIN5008(final String phoneNumber) throws PhoneNumberFormattingException {

        final String phoneNumberWithoutWhitespace = phoneNumber.replaceAll("\\s+", "");

        final Phonenumber.PhoneNumber parsedNumber;

        parsedNumber = parsePhoneNumber(phoneNumber, phoneNumberWithoutWhitespace);

        final String formattedNumber = phoneNumberUtil.format(parsedNumber,
                PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);

        final String nationalNumber = String.valueOf(parsedNumber.getNationalNumber());
        final int countryCode = parsedNumber.getCountryCode();
        final PhoneNumberUtil.PhoneNumberType phoneNumberType = PhoneNumberUtil.getInstance()
                .getNumberType(parsedNumber);

        return formatDIN5008(countryCode, nationalNumber, formattedNumber,
                phoneNumberType.equals(PhoneNumberUtil.PhoneNumberType.FIXED_LINE));
    }


    private String formatDIN5008(final int countryCode, final String nationalNumber, final String preFormattedNumber,
        final boolean isFixedLine) {

        final String numberFormat = "+%d %s %s";

        final String areaCode;
        final String connectionNumber;
        final String chunkedConnectionNumber;

        if (isFixedLine) { // if the number is a fixed number take the area code from the preformatted number

            areaCode = preFormattedNumber.split(" ")[1];
            connectionNumber = preFormattedNumber.split(" ")[2].replaceAll("\\D", "");
        } else {
            areaCode = nationalNumber.substring(0, 3); // take the first three digits
            connectionNumber = nationalNumber.substring(3, nationalNumber.length()).replaceAll("\\D", "");
        }

        chunkedConnectionNumber = String.join(" ", connectionNumber.split("(?<=\\G.{4})"));

        return String.format(numberFormat, countryCode, areaCode, chunkedConnectionNumber);
    }
}
