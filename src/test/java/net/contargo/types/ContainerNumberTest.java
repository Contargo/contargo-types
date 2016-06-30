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
