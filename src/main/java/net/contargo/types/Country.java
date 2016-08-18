package net.contargo.types;

/**
 * Currently supported countries for {@link LicensePlate}s.
 *
 * <p>For further information see the list of <a
 * href="https://en.wikipedia.org/wiki/List_of_international_vehicle_registration_codes">international license plate
 * country codes</a>.</p>
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
