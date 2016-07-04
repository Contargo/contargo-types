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
    private final boolean isValid;
    private final boolean isISO6346Valid;

    private final String ownerCode;
    private final Character equipmentCategory;
    private final String serialNumber;
    private final Character checkDigit;

    /**
     * Use {@link #forValue(String)} to build a new {@link ContainerNumber} instance.
     *
     * @param  value  represents a container number
     */
    private ContainerNumber(String value) {

        this.value = value;
        this.normalizedContainerNumber = value.replaceAll("[ -]", "").toUpperCase();

        if (normalizedContainerNumber.length() == VALID_LENGTH) {
            this.ownerCode = getOwnerCodeLetters();
            this.equipmentCategory = getEquipmentCategoryLetter();
            this.serialNumber = getSerialNumberLetter();
            this.checkDigit = getCheckDigitLetter();
        } else {
            // find a sound way to extract as much given information as possible
            // these default settings are not good!
            this.ownerCode = "ZZZ";
            this.equipmentCategory = 'X';
            this.serialNumber = "123456";
            this.checkDigit = '7';
        }

        this.isValid = checkValidity();
        this.isISO6346Valid = checkISO6346Validity();
    }

    public String getOwnerCode() {

        return ownerCode;
    }


    public Character getEquipmentCategory() {

        return equipmentCategory;
    }


    public String getSerialNumber() {

        return serialNumber;
    }


    public Character getCheckDigit() {

        return checkDigit;
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
            return this.ownerCode + this.equipmentCategory + " " + this.serialNumber + "-" + this.checkDigit;
        }

        return value;
    }


    private String getOwnerCodeLetters() {

        return normalizedContainerNumber.substring(0, POSITION_END_OWNER_CODE);
    }


    private char getEquipmentCategoryLetter() {

        return normalizedContainerNumber.charAt(POSITION_END_EQUIPMENT_CATEGORY);
    }


    private String getSerialNumberLetter() {

        return normalizedContainerNumber.substring(POSITION_END_EQUIPMENT_CATEGORY + 1, POSITION_END_SERIAL_NUMBER);
    }


    private char getCheckDigitLetter() {

        return normalizedContainerNumber.charAt(POSITION_END_SERIAL_NUMBER);
    }


    /**
     * Check if the {@link ContainerNumber} is valid.
     *
     * @return  {@code true} if the {@link ContainerNumber} is valid, else {@code false}
     */
    public boolean isValid() {

        return isValid;
    }


    private boolean checkValidity() {

        if (normalizedContainerNumber.length() != VALID_LENGTH) {
            return false;
        }

        boolean validOwnerCode = this.ownerCode.matches("[A-Z]{3}");
        boolean validEquipmentCategory = String.valueOf(this.equipmentCategory).matches("[UJZ]{1}");
        boolean validNumbers = getSerialNumberLetter().matches("[0-9]{6}");
        boolean validCheckDigit = String.valueOf(getCheckDigitLetter()).matches("[0-9]{1}");

        return validOwnerCode && validEquipmentCategory && validNumbers && validCheckDigit;
    }


    /**
     * Check if the {@link ContainerNumber} is ISO6346 valid, i.e. has a valid format and a correct check digit.
     *
     * @return  {@code true} if the {@link ContainerNumber} is ISO6346 valid, else {@code false}
     */

    public boolean isISO6346Valid() {

        return isISO6346Valid;
    }


    public boolean checkISO6346Validity() {

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

        int actualCheckDigit = Character.getNumericValue(this.checkDigit);

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
