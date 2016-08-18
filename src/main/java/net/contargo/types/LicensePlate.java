package net.contargo.types;

import java.util.Optional;


/**
 * Each {@link net.contargo.domain.Truck} can be identified by its license plate.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.1.0
 */
public final class LicensePlate {

    private final String value;
    private String country;

    /**
     * Use {@link #forValue(String)} to build a new {@link LicensePlate} instance.
     *
     * @param  value  represents a license plate
     */
    private LicensePlate(String value) {

        this.value = value;
    }

    /**
     * Build a new {@link LicensePlate} with a {@link String} value.
     *
     * @param  value  represents a license plate
     *
     * @return  a {@link LicensePlate}, never {@code null}
     */
    public static LicensePlate forValue(String value) {

        Assert.notBlank(value, "Value for license plate must not be null or empty");

        return new LicensePlate(value);
    }


    /**
     * Set the country of this {@link LicensePlate}.
     *
     * @param  countryCode  represents a country, for example "D", never {@code null}
     *
     * @return  {@link LicensePlate}, never {@code null}
     */
    public LicensePlate withCountryCode(String countryCode) {

        Assert.notBlank(countryCode, "Country code must not be null or empty");

        this.country = countryCode;

        return this;
    }


    /**
     * Return the {@link LicensePlate} in a formatted way.
     *
     * @return  formatted {@link LicensePlate}, never {@code null}
     */
    @Override
    public String toString() {

        return getNormalizedValue();
    }


    private String getNormalizedValue() {

        return value.replaceAll("-", " ").toUpperCase();
    }


    /**
     * Check if the {@link LicensePlate} is valid.
     *
     * @return  {@code true} if the {@link LicensePlate} is valid, else {@code false}
     */
    public boolean isValid() {

        String normalizedValue = getNormalizedValue();

        String trimmedValue = normalizedValue.replaceAll("\\s", "");

        // allowed: any letter or digit, but no special characters
        return trimmedValue.matches("[\\p{L}0-9]*");
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        String o1 = this.toString();
        String o2 = obj.toString();

        return o1.equals(o2);
    }


    @Override
    public int hashCode() {

        return this.toString().hashCode();
    }


    public Optional<String> getCountry() {

        return Optional.ofNullable(country);
    }
}
