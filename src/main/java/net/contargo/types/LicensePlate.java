package net.contargo.types;

/**
 * Implementation of {@link net.contargo.domain.LicensePlate}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.1.0
 */
public class LicensePlate implements net.contargo.domain.LicensePlate {

    private final String value;

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

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        String o1 = this.toString();
        String o2 = obj.toString();

        return o1.equals(o2);
    }
}
