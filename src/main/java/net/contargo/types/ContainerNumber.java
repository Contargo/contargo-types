package net.contargo.types;

/**
 * Represents a container number identifying a container.
 *
 * <p>Read more about the <a href="https://en.wikipedia.org/wiki/ISO_6346">ISO 6346 standard</a> to learn more about the
 * format of a container number.</p>
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.1.0
 */
public class ContainerNumber {

    private static final int VALID_LENGTH = 11;

    private final String value;

    /**
     * Use {@link #forValue(String)} to build a new {@link ContainerNumber} instance.
     *
     * @param  value  represents a container number
     */
    private ContainerNumber(String value) {

        this.value = value;
    }

    /**
     * Build a new {@link ContainerNumber} with a {@link String} value.
     *
     * @param  value  represents a container number
     *
     * @return  a {@link ContainerNumber}, never {@code null}
     */
    public static ContainerNumber forValue(String value) {

        Assert.notBlank(value, "Value for container number must not be null or empty");

        return new ContainerNumber(value);
    }


    @Override
    public String toString() {

        if (value.length() != VALID_LENGTH) {
            return value;
        }

        String normalizedValue = value.replaceAll("[ -]", "").toUpperCase();

        return normalizedValue.substring(0, 4) + " " + normalizedValue.substring(4, 10) + "-"
            + normalizedValue.charAt(10);
    }
}
