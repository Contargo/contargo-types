package net.contargo.types.telephony.formatting;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber;

import net.contargo.types.Loggable;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;


/**
 * Handles the formatting of telephone numbers.
 *
 * @author  Robin Jayasinghe - jayasinghe@synyx.de
 * @author  Julia Dasch - dasch@synyx.de
 */
public class PhoneNumberFormatter implements Loggable {

    private static PhoneNumberFormatter instance = null;

    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public static PhoneNumberFormatter getInstance() {

        if (instance == null) {
            return new PhoneNumberFormatter();
        }

        return instance;
    }


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

        final Phonenumber.PhoneNumber parsedNumber = parsePhoneNumber(phoneNumber, trimPhoneNumber(phoneNumber));

        return phoneNumberUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
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

        // strip all whitespace and line breaks from input
        final String trimPhoneNumber = trimPhoneNumber(phoneNumber);

        // pre-validation and formatting with libphonenumber
        final Phonenumber.PhoneNumber parsedNumber = parsePhoneNumber(phoneNumber, trimPhoneNumber);
        final String formattedNumber = phoneNumberUtil.format(parsedNumber,
                PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);

        // extract primitives from libphonenumber parse result
        final String nationalNumber = String.valueOf(parsedNumber.getNationalNumber());
        final int countryCode = parsedNumber.getCountryCode();
        final PhoneNumberType phoneNumberType = getPhoneNumberType(phoneNumber);

        // trigger the actual formatting
        String formatDIN5008 = formatDIN5008(countryCode, nationalNumber, formattedNumber,
                phoneNumberType.equals(PhoneNumberType.FIXED_LINE));

        if (formatDIN5008.length() >= trimPhoneNumber.length()) {
            return formatDIN5008;
        } else {
            throw new PhoneNumberFormattingException(String.format(
                    "can not format number. the formatted phone number('%s') has less digits than the incoming phone number('%s')",
                    formatDIN5008, phoneNumber));
        }
    }


    /**
     * Formats a given phoneNumber to the rules mentiond for parseAndFormatToDIN5008(final String phoneNumber).
     *
     * <p>The formatted result is appended with a dash and the supplied extension</p>
     *
     * @param  basePhoneNumber  the base phoneNumber to be formatted
     * @param  extension  the extension to be added to the base number
     *
     * @return  the formatted base number appended with the extension
     *
     * @throws  PhoneNumberFormattingException  if the phoneNumber cannot be parsed
     */
    public String parseAndFormatToDIN5008(final String basePhoneNumber, final String extension)
        throws PhoneNumberFormattingException {

        final String formattedBaseNumber = parseAndFormatToDIN5008(basePhoneNumber);
        final String sanitizedExtension = extension.replaceAll("\\s+", "").replaceAll("\\D+", "");

        return String.format("%s %s", formattedBaseNumber, sanitizedExtension);
    }


    /**
     * Returns the region code of a number. It is needed to check the whole number to return the region/country code.
     * It will not only check the country calling code. example for +1 242 = 'BS'
     *
     * @param  phoneNumber  the phoneNumber to get the region code/country code from
     *
     * @return  the country code from formatted number
     *
     * @throws  PhoneNumberFormattingException  if the phoneNumber cannot be parsed
     */
    public String getRegionCode(final String phoneNumber) throws PhoneNumberFormattingException {

        Phonenumber.PhoneNumber parsedNumber = parsePhoneNumber(phoneNumber, trimPhoneNumber(phoneNumber));

        return phoneNumberUtil.getRegionCodeForNumber(parsedNumber);
    }


    /**
     * Returns the national significant number of a phone number.
     *
     * @param  phoneNumber  the phoneNumber to get the national significant number from
     *
     * @return  the national significant number
     *
     * @throws  PhoneNumberFormattingException  if the phoneNumber cannot be parsed
     */
    public String getNationalSignificantNumber(final String phoneNumber) throws PhoneNumberFormattingException {

        Phonenumber.PhoneNumber parsedNumber = parsePhoneNumber(phoneNumber, trimPhoneNumber(phoneNumber));

        return phoneNumberUtil.getNationalSignificantNumber(parsedNumber);
    }


    /**
     * Returns the type of a number.
     *
     * @param  phoneNumber  the phoneNumber to get the type from
     *
     * @return  phone number type
     *
     * @throws  PhoneNumberFormattingException  if the phoneNumber cannot be parsed
     */
    public PhoneNumberType getPhoneNumberType(final String phoneNumber) throws PhoneNumberFormattingException {

        Phonenumber.PhoneNumber parsedNumber = parsePhoneNumber(phoneNumber, trimPhoneNumber(phoneNumber));

        return phoneNumberUtil.getNumberType(parsedNumber);
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

        final NationalNumber areaCodeAndConnectionNumber;

        if (isFixedLine) {
            areaCodeAndConnectionNumber = getNationalNumberFromFixedNumber(preFormattedNumber);
        } else {
            areaCodeAndConnectionNumber = getNationalNumberFromMobileNumber(nationalNumber);
        }

        return chunkAndFormatResult(countryCode, areaCodeAndConnectionNumber);
    }


    private NationalNumber getNationalNumberFromMobileNumber(String nationalNumber) {

        if (nationalNumber.length() > 4) { // only for numbers longer than 4 digits

            String connectionNumber = nationalNumber.substring(3).replaceAll("\\D", "");

            return new NationalNumber(nationalNumber.substring(0, 3), connectionNumber);
        } else { // for the shorter numbers
            return new NationalNumber("", nationalNumber);
        }
    }


    private NationalNumber getNationalNumberFromFixedNumber(String preFormattedNumber)
        throws PhoneNumberFormattingException {

        // fist element should be the country code
        final String[] splittedPreformattedNumber = preFormattedNumber.split(" ");

        if (splittedPreformattedNumber.length <= 1) { // no or one elements -> must not happen in theory ;)
            throw new PhoneNumberFormattingException(String.format("Could not extract anything from input number: %s",
                    preFormattedNumber));
        } else if (splittedPreformattedNumber.length <= 2) { // two elements -> take the second element as connection
            return new NationalNumber("", splittedPreformattedNumber[1].replaceAll("\\D+", ""));
        } else { // more than one element -> take the second as area code and the third as connection number (first is country code)

            String[] connectionNumbers = Arrays.copyOfRange(splittedPreformattedNumber, 2,
                    splittedPreformattedNumber.length);
            String connectionNumber = StringUtils.join(connectionNumbers, "");

            return new NationalNumber(splittedPreformattedNumber[1], connectionNumber.replaceAll("\\D+", ""));
        }
    }


    private String chunkAndFormatResult(int countryCode, NationalNumber nationalNumber) {

        logger().debug("chunk the number:'{} {} {}' into blocks of 4 digits. the last block can be 1-4 digits",
            countryCode, nationalNumber.areaCode, nationalNumber.connectionNumber);

        final String chunkedConnectionNumber = String.join(" ", nationalNumber.connectionNumber.split("(?<=\\G.{4})"));

        if (StringUtils.isBlank(nationalNumber.areaCode)) {
            return String.format("+%d %s", countryCode, chunkedConnectionNumber);
        } else {
            return String.format("+%d %s %s", countryCode, nationalNumber.areaCode, chunkedConnectionNumber);
        }
    }


    /**
     * removes all whitespaces and line breaks.
     *
     * @param  phoneNumber  the number to be trimed
     *
     * @return  the trimed number
     */
    private String trimPhoneNumber(String phoneNumber) {

        logger().debug("trim number:'{}' remove spaces, new Lines, '/', '-' and '()'.", phoneNumber);

        String phoneNumberWithoutNewLine = phoneNumber.replaceAll("\\s+", "").replaceAll("\n", "");
        String phoneNumberWithoutBraces = phoneNumberWithoutNewLine.replaceAll("\\(", "").replaceAll("\\)", "");

        return phoneNumberWithoutBraces.replaceAll("/", "").replaceAll("-", "");
    }

    // National number contains areaCode and connectionNumber
    private class NationalNumber {

        public final String areaCode;
        public final String connectionNumber;

        private NationalNumber(String areaCode, String connectionNumber) {

            this.areaCode = areaCode;
            this.connectionNumber = connectionNumber;
        }
    }
}
