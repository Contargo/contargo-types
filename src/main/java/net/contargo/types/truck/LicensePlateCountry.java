package net.contargo.types.truck;

import net.contargo.types.Assert;


/**
 * Currently supported countries for {@link LicensePlate}s.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
public enum LicensePlateCountry implements Country {

    GERMANY("D", "KA AB 123", new GermanLicensePlateHandler()),
    NETHERLANDS("NL", "2-VDL-52", new DutchLicensePlateHandler()),
    BELGIUM("B", "1-ABC-555", new BelgianLicensePlateHandler()),
    SWITZERLAND("CH", "ZH 445789", new SwissLicensePlateHandler()),
    FRANCE("F", "AA-001-AB", new FrenchLicensePlateHandler()),
    POLAND("PL", "XYZ 12JK", new PolishLicensePlateHandler()),
    CZECH_REPUBLIC("CZ", "2H2 7149", new CzechLicensePlateHandler()),
    ROMANIA("RO", "B 183 CTL", new RomanianLicensePlateHandler()),
    BULGARIA("BG", "CA 7845 XC", new BulgarianLicensePlateHandler()),
    UNKNOWN_COUNTRY("", "", new UnknownCountryLicensePlateHandler());

    private final String countryCode;
    private final String example;
    private final LicensePlateHandler licensePlateHandler;

    LicensePlateCountry(String countryCode, String example, LicensePlateHandler licensePlateHandler) {

        this.countryCode = countryCode;
        this.example = example;
        this.licensePlateHandler = licensePlateHandler;
    }

    /**
     * @see  Country#getCountryCode()
     */
    @Override
    public String getCountryCode() {

        return countryCode;
    }


    public String getExample() {

        return example;
    }


    /**
     * @see  Country#getLicensePlateHandler()
     */
    @Override
    public LicensePlateHandler getLicensePlateHandler() {

        return licensePlateHandler;
    }


    /**
     * Find the matching country for a country code.
     *
     * @param  countryCode  to find a country for
     *
     * @return  the matching country
     *
     * @throws  IllegalArgumentException  if no matching country can be found for the given country code
     */
    public static Country forCountryCode(String countryCode) {

        Assert.notNull(countryCode, "Country code must not be null");

        for (LicensePlateCountry country : LicensePlateCountry.values()) {
            if (country.getCountryCode().equals(countryCode)) {
                return country;
            }
        }

        throw new IllegalArgumentException(String.format("No country with country code '%s' found", countryCode));
    }


    public static String exampleForCountry(String countryCode) {

        Assert.notNull(countryCode, "Country code must not be null");

        for (LicensePlateCountry country : LicensePlateCountry.values()) {
            if (country.getCountryCode().equals(countryCode)) {
                return country.example;
            }
        }

        throw new IllegalArgumentException(String.format("No example with country code '%s' found", countryCode));
    }
}
