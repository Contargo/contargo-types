package net.contargo.types;

/**
 * Implementation of {@link net.contargo.domain.ContainerNumber}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.1.0
 */
public class ContainerNumber implements net.contargo.domain.ContainerNumber { // NOSONAR - in this case, it's better to use the same class name

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


    /**
     * Check if the {@link ContainerNumber} is ISO6346 valid, i.e. has a valid format and a correct check digit.
     *
     * @return  {@code true} if the {@link ContainerNumber} is ISO6346 valid, else {@code false}
     */
    public boolean isISO6346Valid() {

        if (!isValid()) {
            return false;
        }

        int correctCheckDigit = 0;
        String charCode = "0123456789A?BCDEFGHIJK?LMNOPQRSTU?VWXYZ";

        for (int i = 0; i < 10; i++) {
            char character = normalizedValue.charAt(i);
            double index = character == '?' ? 1 : charCode.indexOf(character);

            if (index < 0) {
                return false;
            }

            index = index * Math.pow(2, i);
            correctCheckDigit += index;
        }

        correctCheckDigit = (correctCheckDigit % 11) % 10;

        int actualCheckDigit = Character.getNumericValue(getCheckDigit());

        return actualCheckDigit == correctCheckDigit;
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
}
