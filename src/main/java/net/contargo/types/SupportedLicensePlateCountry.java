package net.contargo.types;

/**
 * Currently supported countries for {@link LicensePlate}s.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
public enum SupportedLicensePlateCountry implements LicensePlateCountry {

    GERMANY("D", new GermanLicensePlateHandler()),
    NETHERLANDS("NL", new DutchLicensePlateHandler()),
    BELGIUM("B", new BelgianLicensePlateHandler()),
    SWITZERLAND("CH", new SwissLicensePlateHandler()),
    FRANCE("F", new FrenchLicensePlateHandler()),
    POLAND("PL", new PolishLicensePlateHandler()),
    CZECH_REPUBLIC("CZ", new DefaultLicensePlateHandler());

    private String countryCode;
    private LicensePlateHandler licensePlateHandler;

    SupportedLicensePlateCountry(String countryCode, LicensePlateHandler licensePlateHandler) {

        this.countryCode = countryCode;
        this.licensePlateHandler = licensePlateHandler;
    }

    /**
     * @see  LicensePlateCountry#getCountryCode()
     */
    @Override
    public String getCountryCode() {

        return countryCode;
    }


    /**
     * @see  LicensePlateCountry#getLicensePlateHandler()
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
    public static LicensePlateCountry forCountryCode(String countryCode) {

        Assert.notBlank(countryCode, "Country code must not be null or empty");

        for (SupportedLicensePlateCountry country : SupportedLicensePlateCountry.values()) {
            if (country.getCountryCode().equals(countryCode)) {
                return country;
            }
        }

        throw new IllegalArgumentException(String.format("No country with country code '%s' found", countryCode));
    }
}
