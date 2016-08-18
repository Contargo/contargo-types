package net.contargo.types;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class GermanLicensePlateHandlerTest {

    private LicensePlateHandler handler;

    private Consumer<String> assertIsValid = value -> {
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertTrue("Should be valid: " + value, handler.validate(licensePlate));
    };

    private Consumer<String> assertIsNotValid = value -> {
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, handler.validate(licensePlate));
    };

    private BiConsumer<String, String> assertIsFormattedFromTo = (value, expected) -> {
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertEquals("Wrong formatted value", expected, handler.format(licensePlate));
    };

    private BiConsumer<String, String> assertIsNormalizedFromTo = (value, expected) -> {
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertEquals("Wrong formatted value", expected, handler.normalize(licensePlate));
    };

    @Before
    public void setUp() {

        handler = new GermanLicensePlateHandler();
    }


    // VALIDATION ------------------------------------------------------------------------------------------------------

    // GEOGRAPHIC IDENTIFIER

    @Test
    public void ensureLicensePlateWithOneGeographicLetterIsValid() {

        assertIsValid.accept("B XY 123");
    }


    @Test
    public void ensureLicensePlateWithTwoGeographicLettersIsValid() {

        assertIsValid.accept("KA XY 123");
    }


    @Test
    public void ensureLicensePlateWithThreeGeographicLettersIsValid() {

        assertIsValid.accept("GER XY 123");
    }


    @Test
    public void ensureLicensePlateWithMoreThanThreeGeographicLettersIsInvalid() {

        assertIsNotValid.accept("ABCD XY 123");
    }


    @Test
    public void ensureLicensePlateWithGeographicLettersContainingUmlautsIsValid() {

        assertIsValid.accept("LÖ CR 123");
    }


    // IDENTIFICATION LETTERS

    @Test
    public void ensureLicensePlateWithOneIdentificationLetterIsValid() {

        assertIsValid.accept("KA T 123");
    }


    @Test
    public void ensureLicensePlateWithTwoIdentificationLettersIsValid() {

        assertIsValid.accept("KA TI 123");
    }


    @Test
    public void ensureLicensePlateWithThreeIdentificationLettersIsNotValid() {

        assertIsNotValid.accept("KA TIE 123");
    }


    @Test
    public void ensureLicensePlateWithIdentificationLettersContainingUmlautsIsNotValid() {

        assertIsNotValid.accept("KA Ö 123");
    }


    // IDENTIFICATION NUMBERS

    @Test
    public void ensureLicensePlateWithOneIdentificationNumberIsValid() {

        assertIsValid.accept("KA TI 5");
    }


    @Test
    public void ensureLicensePlateWithTwoIdentificationNumbersIsValid() {

        assertIsValid.accept("KA TI 54");
    }


    @Test
    public void ensureLicensePlateWithThreeIdentificationNumbersIsValid() {

        assertIsValid.accept("KA TI 543");
    }


    @Test
    public void ensureLicensePlateWithFourIdentificationNumbersIsValid() {

        assertIsValid.accept("KA TI 5432");
    }


    @Test
    public void ensureLicensePlateWithLeadingZeroInIdentificationNumberIsNotValid() {

        assertIsNotValid.accept("KA TI 043");
    }


    // TOTAL LENGTH

    @Test
    public void ensureLicensePlateWithMoreThanEightCharactersIsNotValid() {

        // all rules are fulfilled, but number of characters is 9
        assertIsValid.accept("GER TI 5432");
    }


    // SEPARATORS

    @Test
    public void ensureLicensePlateWithWhitespaceAsSeparatorIsValid() {

        assertIsValid.accept("KA AB 666");
    }


    @Test
    public void ensureLicensePlateWithMinusAsSeparatorIsValid() {

        assertIsValid.accept("KA-AB-666");
    }


    @Test
    public void ensureLicensePlateWithWhitespaceAndMinusAsSeparatorIsValid() {

        assertIsValid.accept("KA-AB 666");
    }


    @Test
    public void ensureLicensePlateWithOnlyOneMinusAsSeparatorIsValid() {

        assertIsValid.accept("KA-AB666");
    }


    @Test
    public void ensureLicensePlateWithOnlyOneWhitespaceAsSeparatorIsValid() {

        assertIsValid.accept("KA AB666");
    }


    // SPECIAL CHARACTERS

    @Test
    public void ensureLicensePlateWithSpecialCharactersIsNotValid() {

        assertIsNotValid.accept("KA A/ 123");
    }


    // CASE INSENSITIVE

    @Test
    public void ensureLowerCaseLicensePlateIsValid() {

        assertIsValid.accept("ka ab 666");
    }


    // NORMALIZING -----------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("ka ab 123", "KA-AB-123");
        assertIsNormalizedFromTo.accept("ka-ab 123", "KA-AB-123");
        assertIsNormalizedFromTo.accept("KA-AB123", "KA-AB-123");
        assertIsNormalizedFromTo.accept("KA A123", "KA-A-123");
        assertIsNormalizedFromTo.accept("KA  A123", "KA-A-123");
        assertIsNormalizedFromTo.accept("KA  A--123", "KA-A-123");
    }


    // FORMATTING ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsFormattedCorrectly() {

        assertIsFormattedFromTo.accept("ka ab 123", "KA AB 123");
        assertIsFormattedFromTo.accept("ka-ab 123", "KA AB 123");
        assertIsFormattedFromTo.accept("KA-AB123", "KA AB 123");
        assertIsFormattedFromTo.accept("KA A123", "KA A 123");
    }
}
