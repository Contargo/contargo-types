package net.contargo.types;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Consumer;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class ContainerNumberTest {

    // BUILD ---------------------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfBuiltWithNull() {

        ContainerNumber.forValue(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfBuiltWithEmptyString() {

        ContainerNumber.forValue(" ");
    }


    @Test
    public void ensureCanBeBuiltWithString() {

        ContainerNumber containerNumber = ContainerNumber.forValue("foo");

        Assert.assertNotNull("Should not be null", containerNumber);
    }


    // FORMAT --------------------------------------------------------------------------------------

    @Test
    public void ensureTrimmedContainerNumberIsFormattedCorrectly() {

        String value = "hlxu1234567";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertEquals("Wrong String representation for: " + value, "HLXU 123456-7", containerNumber.toString());
    }


    @Test
    public void ensureCompletelyInvalidContainerNumberIsFormattedCorrectly() {

        String value = "foo";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertEquals("Wrong String representation for: " + value, "foo", containerNumber.toString());
    }


    @Test
    public void ensureInvalidContainerNumberIsFormattedCorrectly() {

        // 'W' is not an allowed equipment category
        String value = "HLXW1234567";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertEquals("Wrong String representation for: " + value, "HLXW 123456-7", containerNumber.toString());
    }


    @Test
    public void ensureAlreadyCorrectlyFormattedContainerNumberIsFormattedCorrectly() {

        String value = "HLXU 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertEquals("Wrong String representation for: " + value, "HLXU 123456-7", containerNumber.toString());
    }


    // VALID ---------------------------------------------------------------------------------------

    @Test
    public void ensureTrimmedValidContainerNumberIsValid() {

        String value = "HlXu1234567";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertTrue("Should be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureValidContainerNumberIsValid() {

        String value = "HLXU 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertTrue("Should be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureInvalidContainerNumberIsNotValid() {

        String value = "foo";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithOwnerCodeOfMoreThanFourLettersIsNotValid() {

        String value = "HLXUU 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithOwnerCodeContainingADigitIsNotValid() {

        String value = "H1XU 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithOwnerCodeContainingSpecialCharactersIsNotValid() {

        String value = "HL.U 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithMissingEquipmentCategoryIsNotValid() {

        String value = "HLX 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithAllowedEquipmentCategoryIsValid() {

        Consumer<String> assertValid = (value) -> {
            ContainerNumber containerNumber = ContainerNumber.forValue(value);

            Assert.assertTrue("Should be valid: " + value, containerNumber.isValid());
        };

        // allowed equipment categories: U, J, Z
        assertValid.accept("HLXU 123456-7");
        assertValid.accept("HLXJ 123456-7");
        assertValid.accept("HLXZ 123456-7");
    }


    @Test
    public void ensureContainerNumberWithNotAllowedEquipmentCategoryIsNotValid() {

        String value = "HLXA 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithSerialNumberOfLessThanSixDigitsIsNotValid() {

        String value = "HLX 12345-6";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithSerialNumberOfMoreThanSixDigitsIsNotValid() {

        String value = "HLX 1234567-8";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithSerialNumberContainingLettersIsNotValid() {

        String value = "HLXU 123AB6-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithSerialNumberContainingSpecialCharactersIsNotValid() {

        String value = "HLXU 12/456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithoutCheckDigitIsNotValid() {

        String value = "HLXU 123456-";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithCheckDigitThatIsNotANumberIsNotValid() {

        String value = "HLXU 123456-A";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    // CONTAINER NUMBER SECTIONS -------------------------------------------------------------------

    @Test
    public void ensureOwnerCodeIsReturnedCorrectlyForValidContainerNumber() {

        ContainerNumber containerNumber = ContainerNumber.forValue("HLXU 123456-7");

        Optional<String> optionalOwnerCode = containerNumber.getOwnerCode();

        Assert.assertTrue("Missing owner code", optionalOwnerCode.isPresent());
        Assert.assertEquals("Wrong owner code", "HLX", optionalOwnerCode.get());
    }


    @Test
    public void ensureOwnerCodeIsAbsentForInvalidContainerNumber() {

        ContainerNumber containerNumber = ContainerNumber.forValue("XY");

        Assert.assertFalse("Owner code should be absent", containerNumber.getOwnerCode().isPresent());
    }


    @Test
    public void ensureEquipmentCategoryIsReturnedCorrectlyForValidContainerNumber() {

        ContainerNumber containerNumber = ContainerNumber.forValue("HLXU 123456-7");

        Optional<Character> optionalEquipmentCategory = containerNumber.getEquipmentCategory();

        Assert.assertTrue("Missing equipment category", optionalEquipmentCategory.isPresent());
        Assert.assertEquals("Wrong equipment category", "U", String.valueOf(optionalEquipmentCategory.get()));
    }


    @Test
    public void ensureEquipmentCategoryIsAbsentForInvalidContainerNumber() {

        ContainerNumber containerNumber = ContainerNumber.forValue("Foo");

        Assert.assertFalse("Equipment category should be absent", containerNumber.getEquipmentCategory().isPresent());
    }


    @Test
    public void ensureSerialNumberIsReturnedCorrectlyForValidContainerNumber() {

        ContainerNumber containerNumber = ContainerNumber.forValue("HLXU 123456-7");

        Optional<String> optionalSerialNumber = containerNumber.getSerialNumber();

        Assert.assertTrue("Missing serial number", optionalSerialNumber.isPresent());
        Assert.assertEquals("Wrong serial number", "123456", optionalSerialNumber.get());
    }


    @Test
    public void ensureSerialNumberIsAbsentForInvalidContainerNumber() {

        ContainerNumber containerNumber = ContainerNumber.forValue("Foo");

        Assert.assertFalse("Serial number should be absent", containerNumber.getSerialNumber().isPresent());
    }


    @Test
    public void ensureCheckDigitIsReturnedCorrectlyForValidContainerNumber() {

        ContainerNumber containerNumber = ContainerNumber.forValue("HLXU 123456-7");

        Optional<Character> optionalCheckDigit = containerNumber.getCheckDigit();

        Assert.assertTrue("Missing check digit", optionalCheckDigit.isPresent());
        Assert.assertEquals("Wrong check digit", "7", String.valueOf(optionalCheckDigit.get()));
    }


    @Test
    public void ensureCheckDigitIsAbsentForInvalidContainerNumber() {

        ContainerNumber containerNumber = ContainerNumber.forValue("Foo");

        Assert.assertFalse("Check digit should be absent", containerNumber.getCheckDigit().isPresent());
    }


    // ISO 6346 VALID ------------------------------------------------------------------------------

    @Test
    public void ensureContainerNumberWithCorrectCheckDigitIsISO6346Valid() {

        String value = "HLXU 123456-1";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertTrue("Should be ISO6346 valid: " + value, containerNumber.isISO6346Valid());
    }


    @Test
    public void ensureContainerNumberWithWrongCheckDigitIsNotISO6346Valid() {

        String value = "HLXU 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be ISO6346 valid: " + value, containerNumber.isISO6346Valid());
    }


    // EQUALS --------------------------------------------------------------------------------------

    @Test
    public void ensureTrimmedAndNotTrimmedContainerNumbersAreEqual() {

        String v1 = "hlxu1234567";
        String v2 = "HLXU 123456-7";

        ContainerNumber c1 = ContainerNumber.forValue(v1);
        ContainerNumber c2 = ContainerNumber.forValue(v2);

        Assert.assertTrue(v1 + " should be equal to " + v2, c1.equals(c2));
    }


    @Test
    public void ensureDifferentContainerNumbersAreNotEqual() {

        String v1 = "HLXU1234567";
        String v2 = "HLXU1234568";

        ContainerNumber c1 = ContainerNumber.forValue(v1);
        ContainerNumber c2 = ContainerNumber.forValue(v2);

        Assert.assertFalse(v1 + " should not be equal to " + v2, c1.equals(c2));
    }


    @Test
    public void ensureNotEqualsIfDifferentClassesAreCompared() {

        ContainerNumber containerNumber = ContainerNumber.forValue("HLXU1234567");

        Assert.assertFalse("Different classes should not be equal", containerNumber.equals(new Object()));
    }


    @Test
    public void ensureEqualsDoesNotBreakOnNull() {

        String value = "HLXU1234567";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse(value + " should not be equal to null", containerNumber.equals(null));
    }
}
