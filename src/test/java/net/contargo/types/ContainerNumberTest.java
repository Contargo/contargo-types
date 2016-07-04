package net.contargo.types;

import org.junit.Assert;
import org.junit.Test;


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
    public void ensureInvalidContainerNumberIsFormattedCorrectly() {

        String value = "foo";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertEquals("Wrong String representation for: " + value, "foo", containerNumber.toString());
    }


    @Test
    public void ensureAlreadyFormattedContainerNumberIsFormattedCorrectly() {

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
    public void ensureCompletelyInvalidContainerNumberIsNotValid() {

        String value = "foo";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithLessLettersThanFourIsNotValid() {

        String value = "HLX 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithMoreLettersThanFourIsNotValid() {

        String value = "HLXUU 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithLessNumbersThanSixIsNotValid() {

        String value = "HLX 12345-6";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithMoreNumbersThanSixIsNotValid() {

        String value = "HLX 1234567-8";
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


    @Test
    public void ensureContainerNumberWithNumbersOnLetterPositionsIsNotValid() {

        String value = "H1X2 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithLettersOnNumberPositionsIsNotValid() {

        String value = "HLXU 123AB6-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithSpecialCharactersOnLetterPositionsIsNotValid() {

        String value = "HL.U 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    @Test
    public void ensureContainerNumberWithSpecialCharactersOnNumberPositionsIsNotValid() {

        String value = "HLXU 12/456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, containerNumber.isValid());
    }


    // CONTAINER NUMBER SECTIONS -------------------------------------------------------------------

    @Test
    public void ensureOwnerCodeIsReturnedCorrectlyForValidContainerNumber() {

        String value = "HLXU 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        String ownerCode = containerNumber.getOwnerCode();

        Assert.assertEquals("Wrong owner code", "HLX", ownerCode);
    }


    @Test
    public void ensureEquipmentCategoryIsReturnedCorrectlyForValidContainerNumber() {

        String value = "HLXU 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Character equipmentCategory = containerNumber.getEquipmentCategory();

        Assert.assertEquals("Wrong equipment category", "U", String.valueOf(equipmentCategory));
    }


    @Test
    public void ensureSerialNumberIsReturnedCorrectlyForValidContainerNumber() {

        String value = "HLXU 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        String serialNumber = containerNumber.getSerialNumber();

        Assert.assertEquals("Wrong serial number", "123456", serialNumber);
    }


    @Test
    public void ensureCheckDigitIsReturnedCorrectlyForValidContainerNumber() {

        String value = "HLXU 123456-7";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Character checkDigit = containerNumber.getCheckDigit();

        Assert.assertEquals("Wrong check digit", "7", String.valueOf(checkDigit));
    }


    @Test
    public void ensureValidContainerNumbersHaveCorrectEquipmentCategoryRange() {

        String c1 = "HLXU 123456-7";
        ContainerNumber cn1 = ContainerNumber.forValue(c1);

        String c2 = "HLXJ 123456-7";
        ContainerNumber cn2 = ContainerNumber.forValue(c2);

        String c3 = "HLXZ 123456-7";
        ContainerNumber cn3 = ContainerNumber.forValue(c3);

        String c4 = "HLXA 123456-7";
        ContainerNumber invalidContainerNumber = ContainerNumber.forValue(c4);

        Character validEquipmentCategory1 = cn1.getCheckDigit();
        Character validEquipmentCategory2 = cn2.getCheckDigit();
        Character validEquipmentCategory3 = cn3.getCheckDigit();
        Character invalidEquipmentCategory = invalidContainerNumber.getCheckDigit();

        Assert.assertTrue("Should be valid: " + validEquipmentCategory1, cn1.isValid());
        Assert.assertTrue("Should be valid: " + validEquipmentCategory2, cn2.isValid());
        Assert.assertTrue("Should be valid: " + validEquipmentCategory3, cn3.isValid());
        Assert.assertFalse("Should be valid: " + invalidEquipmentCategory, invalidContainerNumber.isValid());
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
    public void ensureTrimmedAndNotTrimmedContainerNumbersAreEquals() {

        String v1 = "hlxu1234567";
        String v2 = "HLXU 123456-7";

        ContainerNumber c1 = ContainerNumber.forValue(v1);
        ContainerNumber c2 = ContainerNumber.forValue(v2);

        Assert.assertTrue(v1 + " should be equals to " + v2, c1.equals(c2));
    }


    @Test
    public void ensureDifferentContainerNumbersAreNotEquals() {

        String v1 = "HLXU1234567";
        String v2 = "HLXU1234568";

        ContainerNumber c1 = ContainerNumber.forValue(v1);
        ContainerNumber c2 = ContainerNumber.forValue(v2);

        Assert.assertFalse(v1 + " should not be equals to " + v2, c1.equals(c2));
    }


    @Test
    public void ensureNotEqualsIfDifferentClassesAreCompared() {

        ContainerNumber containerNumber = ContainerNumber.forValue("HLXU1234567");

        Assert.assertFalse("Different classes should not be equals", containerNumber.equals(new Object()));
    }


    @Test
    public void ensureEqualsDoesNotBreakOnNull() {

        String value = "HLXU1234567";
        ContainerNumber containerNumber = ContainerNumber.forValue(value);

        Assert.assertFalse(value + " should not be equals to null", containerNumber.equals(null));
    }
}
