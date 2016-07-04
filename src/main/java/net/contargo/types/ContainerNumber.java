package net.contargo.types;

/**
 * Each {@link net.contargo.domain.Container} has a worldwide unique container number.
 *
 * <p>Example: HLXU 123456-7</p>
 *
 * <p>A container number is valid, if it consists of four letters, of which the first three indicate the worldwide
 * unique owner code according to 'Bureau International des Conteneurs' in Paris, and the forth the equipment category
 * (U for all freight containers, J for detachable freight container-related equipment, Z for trailers and chassis).</p>
 *
 * <p>Further information: <a href="https://en.wikipedia.org/wiki/ISO_6346">ISO 6346 standard</a></p>
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.1.0
 */
public final class ContainerNumber {

    private static final int VALID_LENGTH = 11;
    private static final int POSITION_END_OWNER_CODE = 3;
    private static final int POSITION_END_EQUIPMENT_CATEGORY = 3;
    private static final int POSITION_END_SERIAL_NUMBER = 10;

    private final String value;
    private final String normalizedContainerNumber;

    /**
     * Use {@link #forValue(String)} to build a new {@link ContainerNumber} instance.
     *
     * @param  value  represents a container number
     */
    private ContainerNumber(String value) {

        this.value = value;
        this.normalizedContainerNumber = value.replaceAll("[ -]", "").toUpperCase();
    }

    /**
     * Get the owner code of this {@link ContainerNumber}.
     *
     * <p>The owner code is worldwide unique and indicates the principal operator of the
     * {@link net.contargo.domain.Container}.</p>
     *
     * @return  owner code consisting of three capital letters
     */
    public String getOwnerCode() {

        return normalizedContainerNumber.substring(0, POSITION_END_OWNER_CODE);
    }


    /**
     * Get the equipment category of this {@link ContainerNumber}.
     *
     * <p>'U' for all freight containers</p>
     *
     * <p>'J' for detachable freight container-related equipment</p>
     *
     * <p>'Z' for trailers and chassis</p>
     *
     * @return  equipment category consisting of one capital letter
     */
    public Character getEquipmentCategory() {

        return normalizedContainerNumber.charAt(POSITION_END_EQUIPMENT_CATEGORY);
    }


    /**
     * Get the serial number of this {@link ContainerNumber}.
     *
     * <p>The serial number is assigned by the owner or operator and identifies the
     * {@link net.contargo.domain.Container} within that owner's/operator's {@link net.contargo.domain.Fleet}</p>
     *
     * @return  serial number consisting of 6 numeric digits
     */
    public String getSerialNumber() {

        return normalizedContainerNumber.substring(POSITION_END_EQUIPMENT_CATEGORY + 1, POSITION_END_SERIAL_NUMBER);
    }


    /**
     * Get the check digit of this {@link ContainerNumber}.
     *
     * <p>The check digit provides a means of validating the recording and transmission accuracies of the owner code and
     * serial number.</p>
     *
     * @return  check digit consisting of one numeric digit
     */
    public Character getCheckDigit() {

        return normalizedContainerNumber.charAt(POSITION_END_SERIAL_NUMBER);
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
            return getOwnerCode() + getEquipmentCategory() + " " + getSerialNumber() + "-" + getCheckDigit();
        }

        return value;
    }


    /**
     * Check if the {@link ContainerNumber} is valid.
     *
     * @return  {@code true} if the {@link ContainerNumber} is valid, else {@code false}
     */
    public boolean isValid() {

        if (normalizedContainerNumber.length() != VALID_LENGTH) {
            return false;
        }

        boolean validOwnerCode = getOwnerCode().matches("[A-Z]{3}");
        boolean validEquipmentCategory = String.valueOf(getEquipmentCategory()).matches("[UJZ]{1}");
        boolean validNumbers = getSerialNumber().matches("[0-9]{6}");
        boolean validCheckDigit = String.valueOf(getCheckDigit()).matches("[0-9]{1}");

        return validOwnerCode && validEquipmentCategory && validNumbers && validCheckDigit;
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
            char character = normalizedContainerNumber.charAt(i);
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
