package net.contargo.types;

/**
 * Currently supported countries for {@link LicensePlate}s.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public enum Country {

    GERMANY("D"),
    NETHERLANDS("NL"),
    BELGIUM("B"),
    SWITZERLAND("CH"),
    FRANCE("F"),
    POLAND("PL"),
    CZECH_REPUBLIC("CZ");

    private String countryCode;

    Country(String countryCode) {

        this.countryCode = countryCode;
    }

    public String getCountryCode() {

        return countryCode;
    }
}
