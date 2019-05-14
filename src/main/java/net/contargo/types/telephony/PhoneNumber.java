package net.contargo.types.telephony;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import net.contargo.types.Loggable;
import net.contargo.types.telephony.formatting.PhoneNumberFormatter;
import net.contargo.types.telephony.formatting.PhoneNumberFormattingException;

import org.apache.commons.lang.StringUtils;


/**
 * Phone number to represents a callable number from a user. The number will be formatted in the international format.
 * The phone number will be parsed with the phone number library from google ({@link PhoneNumberUtil}). If the phone
 * number can not be parsed by the library it will throw a {@link PhoneNumberFormattingException}.
 *
 * @author  Julia Dasch - dasch@synyx.de
 */
public final class PhoneNumber implements Loggable {

    private final PhoneNumberFormatter phoneNumberFormatter = PhoneNumberFormatter.getInstance();
    private Country country;
    private String nationalSignificantNumber;
    private String phoneExtension;

    public PhoneNumber(String phoneCallingNumber) throws PhoneNumberFormattingException {

        if (phoneCallingNumber == null) {
            throw new IllegalArgumentException("Phone number must not be null.");
        }

        this.nationalSignificantNumber = phoneNumberFormatter.getNationalSignificantNumber(phoneCallingNumber);
        this.country = new Country(phoneNumberFormatter.getRegionCode(phoneCallingNumber));
    }


    public PhoneNumber(Country country, String nationalSignificantNumber, String phoneExtension) {

        this.country = country;
        this.nationalSignificantNumber = nationalSignificantNumber;
        this.phoneExtension = phoneExtension;
    }


    public PhoneNumber(Country country, String nationalSignificantNumber) {

        this(country, nationalSignificantNumber, null);
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }


    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }


    public Country getCountry() {

        return country;
    }


    public void setCountry(Country country) {

        this.country = country;
    }


    public String getNationalSignificantNumber() {

        return nationalSignificantNumber;
    }


    public void setNationalSignificantNumber(String nationalSignificantNumber) {

        this.nationalSignificantNumber = nationalSignificantNumber;
    }


    public String getInternationalFormattedPhoneNumber() throws PhoneNumberFormattingException {

        logger().info("formatting phone number: {} into international phone number.", getPhoneNumber());

        String format = phoneNumberFormatter.parseAndFormatToDIN5008(getPhoneNumber());

        return format.replaceAll("/", "").replaceAll("-", "");
    }


    public String getPhoneNumberInE164Format() throws PhoneNumberFormattingException {

        return phoneNumberFormatter.parseAndFormatToE164Format(getPhoneNumber());
    }


    private String getPhoneNumber() {

        if (StringUtils.isNotBlank(phoneExtension)) {
            return String.format("%s%s%s", country.getCountryCallingCode(), nationalSignificantNumber, phoneExtension);
        }

        return String.format("%s%s", country.getCountryCallingCode(), nationalSignificantNumber);
    }


    @Override
    public String toString() {

        return "PhoneNumber {"
            + "phoneNumber='" + getPhoneNumber() + '\''
            + ", country='" + country + '\''
            + ", nationalSignificantNumber='" + nationalSignificantNumber + '\''
            + ", phoneExtension='" + phoneExtension + '\'' + '}';
    }
}
