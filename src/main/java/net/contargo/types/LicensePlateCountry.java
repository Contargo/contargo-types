package net.contargo.types;

/**
 * Currently supported countries for {@link LicensePlate}s.
 *
 * <p>For further information see the list of <a
 * href="https://en.wikipedia.org/wiki/List_of_international_vehicle_registration_codes">international license plate
 * country codes</a>.</p>
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
public enum LicensePlateCountry {

    GERMANY("D", new GermanLicensePlateHandler()),
    NETHERLANDS("NL", new DutchLicensePlateHandler()),
    BELGIUM("B", new DefaultLicensePlateHandler()),
    SWITZERLAND("CH", new DefaultLicensePlateHandler()),
    FRANCE("F", new DefaultLicensePlateHandler()),
    POLAND("PL", new DefaultLicensePlateHandler()),
    CZECH_REPUBLIC("CZ", new DefaultLicensePlateHandler());

    private String countryCode;
    private LicensePlateHandler licensePlateHandler;

    LicensePlateCountry(String countryCode, LicensePlateHandler licensePlateHandler) {

        this.countryCode = countryCode;
        this.licensePlateHandler = licensePlateHandler;
    }

    public String getCountryCode() {

        return countryCode;
    }


    public LicensePlateHandler getLicensePlateHandler() {

        return licensePlateHandler;
    }


    public static LicensePlateCountry forCountryCode(String countryCode) {

        Assert.notBlank(countryCode, "Country code must not be null or empty");

        for (LicensePlateCountry country : LicensePlateCountry.values()) {
            if (country.getCountryCode().equals(countryCode)) {
                return country;
            }
        }

        throw new IllegalArgumentException(String.format("No country with country code '%s' found", countryCode));
    }
}
