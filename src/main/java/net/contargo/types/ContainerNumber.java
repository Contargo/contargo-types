package net.contargo.types;

/**
 * Implementation of {@link net.contargo.domain.ContainerNumber}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.1.0
 */
public class ContainerNumber implements net.contargo.domain.ContainerNumber {

    private static final int VALID_LENGTH = 11;
    private static final int POSITION_END_LETTERS = 4;
    private static final int POSITION_END_NUMBERS = 10;

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

        return normalizedValue.substring(0, POSITION_END_LETTERS) + " "
            + normalizedValue.substring(POSITION_END_LETTERS, POSITION_END_NUMBERS) + "-"
            + normalizedValue.charAt(POSITION_END_NUMBERS);
    }
}
