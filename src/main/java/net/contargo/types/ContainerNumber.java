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
    private final String normalizedValue;

    /**
     * Use {@link #forValue(String)} to build a new {@link ContainerNumber} instance.
     *
     * @param  value  represents a container number
     */
    private ContainerNumber(String value) {

        this.value = value;
        this.normalizedValue = value.replaceAll("[ -]", "").toUpperCase();
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


    /**
     * Return the {@link ContainerNumber} in a formatted way.
     *
     * @return  formatted {@link ContainerNumber}, never {@code null}
     */
    @Override
    public String toString() {

        if (isValid()) {
            return getLetters() + " " + getNumbers() + "-" + getCheckDigit();
        }

        return value;
    }


    private String getLetters() {

        return normalizedValue.substring(0, POSITION_END_LETTERS);
    }


    private String getNumbers() {

        return normalizedValue.substring(POSITION_END_LETTERS, POSITION_END_NUMBERS);
    }


    private char getCheckDigit() {

        return normalizedValue.charAt(POSITION_END_NUMBERS);
    }


    /**
     * Check if the {@link ContainerNumber} is valid.
     *
     * @return  {@code true} if the {@link ContainerNumber} is valid, else {@code false}
     */
    public boolean isValid() {

        if (normalizedValue.length() != VALID_LENGTH) {
            return false;
        }

        boolean validLetters = getLetters().matches("[A-Z]*");
        boolean validNumbers = getNumbers().matches("[0-9]*");
        boolean validCheckDigit = String.valueOf(getCheckDigit()).matches("[0-9]*");

        return validLetters && validNumbers && validCheckDigit;
    }


    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        String c1 = this.toString();
        String c2 = obj.toString();

        return c1.equals(c2);
    }
}
