package net.contargo.types.telephony.formatting;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;


/**
 * Handles the formatting of telephone numbers.
 *
 * @author  Robin Jayasinghe - jayasinghe@synyx.de
 */
public class PhoneNumberFormatter {

    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    /**
     * Formats a given phoneNumber according to the rules defined in the E164 standard:
     * http://www.itu.int/rec/T-REC-E.164/en
     *
     * @param  phoneNumber  the phone number to be formatted
     *
     * @return  the formatted phone number
     *
     * @throws  PhoneNumberFormattingException  if the phoneNumber could not be parsed successfully
     */
    public String parseAndFormatToE164Format(final String phoneNumber) throws PhoneNumberFormattingException {

        final String phoneNumberWithoutWhitespace = phoneNumber.replaceAll("\\s+", "");

        final Phonenumber.PhoneNumber parsedNumber;

        parsedNumber = parsePhoneNumber(phoneNumber, phoneNumberWithoutWhitespace);

        final String formattedNumber = phoneNumberUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

        return formattedNumber;
    }


    /**
     * Formats a given phoneNumber to the following rules:
     *
     * <p>If not present it adds the german country code as '+49'. If a country code is present it is ensured that it
     * is formatted with '+' and not '00'.</p>
     *
     * <p>For mobile numbers it assumes that the areacode is built from the first three numbers after the country code.
     * For fixed numbers the area code extracted by libphonenumber is taken.</p>
     *
     * <p>The rest of the number is chunked into blocks of 4 digits.</p>
     *
     * @param  phoneNumber  the phone number to be formatted
     *
     * @return  the phone number formatted according to the formatting rules
     *
     * @throws  PhoneNumberFormattingException  if the phoneNumber could not be parsed successfully
     */
    public String parseAndFormatToDIN5008(final String phoneNumber) throws PhoneNumberFormattingException {

        // strip all whitespace from input
        final String phoneNumberWithoutWhitespace = phoneNumber.replaceAll("\\s+", "");

        // pre-validation and formatting with libphonenumber
        final Phonenumber.PhoneNumber parsedNumber = parsePhoneNumber(phoneNumber, phoneNumberWithoutWhitespace);
        final String formattedNumber = phoneNumberUtil.format(parsedNumber,
                PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);

        // extract primitives from libphonenumber parse result
        final String nationalNumber = String.valueOf(parsedNumber.getNationalNumber());
        final int countryCode = parsedNumber.getCountryCode();
        final PhoneNumberUtil.PhoneNumberType phoneNumberType = PhoneNumberUtil.getInstance()
                .getNumberType(parsedNumber);

        // trigger the actual formatting
        return formatDIN5008(countryCode, nationalNumber, formattedNumber,
                phoneNumberType.equals(PhoneNumberUtil.PhoneNumberType.FIXED_LINE));
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


    private String formatDIN5008(final int countryCode, final String nationalNumber, final String preFormattedNumber,
        final boolean isFixedLine) throws PhoneNumberFormattingException {

        final AreaCodeAndConnectionNumber areaCodeAndConnectionNumber;

        if (isFixedLine) {
            areaCodeAndConnectionNumber = getAreaCodeAndConnectionNumberFromFixedNumber(preFormattedNumber);
        } else {
            areaCodeAndConnectionNumber = getAreaCodeAndConnectionNumberFromMobileNumber(nationalNumber);
        }

        return chunkAndFormatResult(countryCode, areaCodeAndConnectionNumber);
    }


    private AreaCodeAndConnectionNumber getAreaCodeAndConnectionNumberFromMobileNumber(String nationalNumber) {

        AreaCodeAndConnectionNumber areaCodeAndConnectionNumber;

        if (nationalNumber.length() > 4) { // only for numbers longer than 4 digits
            areaCodeAndConnectionNumber = new AreaCodeAndConnectionNumber(nationalNumber.substring(0, 3),
                    nationalNumber.substring(3, nationalNumber.length()).replaceAll("\\D", ""));
        } else { // for the shorter numbers
            areaCodeAndConnectionNumber = new AreaCodeAndConnectionNumber("", nationalNumber);
        }

        return areaCodeAndConnectionNumber;
    }


    private AreaCodeAndConnectionNumber getAreaCodeAndConnectionNumberFromFixedNumber(String preFormattedNumber)
        throws PhoneNumberFormattingException {

        AreaCodeAndConnectionNumber areaCodeAndConnectionNumber;
        final String[] splittedPreformattedNumber = preFormattedNumber.split(" ");

        if (splittedPreformattedNumber.length == 0) { // no elements -> must not happen in theory ;)
            throw new PhoneNumberFormattingException(String.format("Could not extract anything from input number: %s",
                    preFormattedNumber));
        } else if (splittedPreformattedNumber.length == 1) { // one element -> take the whole number as connection
            areaCodeAndConnectionNumber = new AreaCodeAndConnectionNumber("", preFormattedNumber);
        } else { // more than one element -> take the second as area code and the third as connection number (first is country code)
            areaCodeAndConnectionNumber = new AreaCodeAndConnectionNumber(preFormattedNumber.split(" ")[1],
                    preFormattedNumber.split(" ")[2].replaceAll("\\D", ""));
        }

        return areaCodeAndConnectionNumber;
    }


    private String chunkAndFormatResult(int countryCode, AreaCodeAndConnectionNumber areaCodeAndConnectionNumber) {

        final String numberFormat = "+%d %s %s";

        // chunk the number into blocks of 4 digits. the last block can be 1-4 digits
        final String chunkedConnectionNumber = String.join(" ",
                areaCodeAndConnectionNumber.connectionNumber.split("(?<=\\G.{4})"));

        // target format
        return String.format(numberFormat, countryCode, areaCodeAndConnectionNumber.areaCode, chunkedConnectionNumber);
    }

    private class AreaCodeAndConnectionNumber {

        public final String areaCode;
        public final String connectionNumber;

        private AreaCodeAndConnectionNumber(String areaCode, String connectionNumber) {

            this.areaCode = areaCode;
            this.connectionNumber = connectionNumber;
        }
    }
}
