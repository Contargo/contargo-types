package net.contargo.types.telephony;

import com.google.i18n.phonenumbers.PhoneNumberUtil;


/**
 * The country of a {@link PhoneNumber} describes in which country a phone number is registered. Holds the country code
 * in ISO 3166 a2 format and the country calling code for the country.
 *
 * @author  Julia Dasch - dasch@synyx.de
 */
public final class Country {

    private PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    /**
     * Country code in ISO 3166 a2 format. example: Germany = DE
     */
    private String countryCode;

    /**
     * country calling code which includes '+' and 1-3 digits.
     */
    private String countryCallingCode;

    public Country(String countryCode, String countryCallingCode) {

        this.countryCode = countryCode;
        this.countryCallingCode = countryCallingCode;
    }


    public Country(String countryCode) {

        this.countryCode = countryCode;
        this.countryCallingCode = getCallingCodeFromCountryCode(countryCode);
    }

    public String getCountryCode() {

        return countryCode;
    }


    public void setCountryCode(String countryCode) {

        this.countryCode = countryCode;
    }


    public String getCountryCallingCode() {

        return countryCallingCode;
    }


    public void setCountryCallingCode(String countryCallingCode) {

        this.countryCallingCode = countryCallingCode;
    }


    private String getCallingCodeFromCountryCode(String countryCode) {

        int countryCodeForRegion = phoneNumberUtil.getCountryCodeForRegion(countryCode);

        return addPlusToCountryCallingCode(countryCodeForRegion);
    }


    private String addPlusToCountryCallingCode(int countryCallingCode) {

        return String.format("+%d", countryCallingCode);
    }


    @Override
    public String toString() {

        return "Country{"
            + "countryCode='" + countryCode + '\''
            + ", countryCallingCode='" + countryCallingCode + '\'' + '}';
    }
}
