package net.contargo.types;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class DefaultLicensePlateHandlerTest {

    private static final Country COUNTRY = LicensePlateCountry.BELGIUM;

    private LicensePlateHandler handler;

    private Consumer<String> assertIsValid = value -> {
        LicensePlate licensePlate = LicensePlate.forValue(value).withCountry(COUNTRY);

        Assert.assertTrue("Should be valid: " + value, handler.validate(licensePlate));
    };

    private BiConsumer<String, String> assertIsFormattedFromTo = (value, expected) -> {
        LicensePlate licensePlate = LicensePlate.forValue(value).withCountry(COUNTRY);

        Assert.assertEquals("Wrong formatted value", expected, handler.format(licensePlate));
    };

    private BiConsumer<String, String> assertIsNormalizedFromTo = (value, expected) -> {
        LicensePlate licensePlate = LicensePlate.forValue(value).withCountry(COUNTRY);

        Assert.assertEquals("Wrong formatted value", expected, handler.normalize(licensePlate));
    };

    private Consumer<String> assertIsNotValid = value -> {
        LicensePlate licensePlate = LicensePlate.forValue(value).withCountry(COUNTRY);

        Assert.assertFalse("Should not be valid: " + value, handler.validate(licensePlate));
    };

    @Before
    public void setUp() {

        handler = new DefaultLicensePlateHandler();
    }


    // VALIDATION ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateWithLettersAndNumbersIsValid() {

        assertIsValid.accept("KA XY 123");
    }


    @Test
    public void ensureLicensePlateWithMinusAsSeparatorIsValid() {

        assertIsValid.accept("KA-XY-123");
    }


    @Test
    public void ensureLicensePlateWithSpecialCharactersIsNotValid() {

        assertIsNotValid.accept("KA/XY.123");
    }


    @Test
    public void ensureLicensePlateWithUmlautIsValid() {

        assertIsValid.accept("LÖ-CR-123");
    }


    // NORMALIZING -----------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("ka ab 123", "KA-AB-123");
        assertIsNormalizedFromTo.accept("ka-ab 123", "KA-AB-123");
        assertIsNormalizedFromTo.accept("ka-ab  123", "KA-AB-123");
        assertIsNormalizedFromTo.accept("ka--ab  123", "KA-AB-123");
    }


    // FORMATTING ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsFormattedCorrectly() {

        assertIsFormattedFromTo.accept("ka ab 123", "KA-AB-123");
        assertIsFormattedFromTo.accept("ka-ab 123", "KA-AB-123");
    }
}
