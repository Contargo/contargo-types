package net.contargo.types.telephony;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import net.contargo.types.Loggable;
import net.contargo.types.telephony.formatting.PhoneNumberFormatter;
import net.contargo.types.telephony.formatting.PhoneNumberFormattingException;

import org.apache.commons.lang.StringUtils;

import java.util.Optional;


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
    private final String rawPhoneNumber;
    private String phoneExtension;

    public PhoneNumber(String rawPhoneNumber) {

        this.rawPhoneNumber = rawPhoneNumber;
    }


    public PhoneNumber(Country country, final String rawPhoneNumber, String phoneExtension) {

        this.country = country;
        this.rawPhoneNumber = rawPhoneNumber;
        this.phoneExtension = phoneExtension;
    }


    public PhoneNumber(Country country, final String rawPhoneNumber) {

        this(country, rawPhoneNumber, null);
    }

    public String getPhoneExtension() {

        return phoneExtension;
    }


    public void setPhoneExtension(String phoneExtension) {

        this.phoneExtension = phoneExtension;
    }


    public Country getCountry() throws PhoneNumberFormattingException {

        return new Country(phoneNumberFormatter.getRegionCode(rawPhoneNumber));
    }


    public void setCountry(Country country) {

        this.country = country;
    }


    public String getNationalSignificantNumber() throws PhoneNumberFormattingException {

        return phoneNumberFormatter.getNationalSignificantNumber(rawPhoneNumber);
    }


    public Optional<String> getInternationalFormatOfPhoneNumber() {

        logger().info("formatting phone number: {} into international phone number.", getPhoneNumber());

        if (StringUtils.isBlank(rawPhoneNumber)) {
            logger().warn("Not able to parse phone number of {}", rawPhoneNumber);

            return Optional.empty();
        }

        try {
            return Optional.of(phoneNumberFormatter.parseAndFormatToDIN5008(getPhoneNumber()));
        } catch (PhoneNumberFormattingException e) {
            logger().warn("Failed to parse and format number {}: {}", rawPhoneNumber, e.getMessage());

            return Optional.empty();
        }
    }


    private String getPhoneNumber() {

        if (StringUtils.isNotBlank(phoneExtension)) {
            return String.format("%s%s", rawPhoneNumber, phoneExtension);
        }

        return rawPhoneNumber;
    }


    public boolean isPhoneNumber() {

        return getInternationalFormatOfPhoneNumber().isPresent();
    }


    @Override
    public String toString() {

        return "PhoneNumber {"
            + "rawPhoneNumber='" + rawPhoneNumber + '\''
            + ", country='" + country + '\''
            + ", phoneExtension='" + phoneExtension + '\'' + '}';
    }
}
