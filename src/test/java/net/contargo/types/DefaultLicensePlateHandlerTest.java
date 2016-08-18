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

    private LicensePlateHandler handler;

    private Consumer<String> assertIsValid = value -> {
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertTrue("Should be valid: " + value, handler.validate(licensePlate));
    };

    private BiConsumer<String, String> assertIsFormattedFromTo = (value, expected) -> {
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertEquals("Wrong formatted value", expected, handler.format(licensePlate));
    };

    private Consumer<String> assertIsNotValid = value -> {
        LicensePlate licensePlate = LicensePlate.forValue(value);

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


    // FORMATTING ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsJustUpperCased() {

        assertIsFormattedFromTo.accept("ka ab 123", "KA AB 123");
        assertIsFormattedFromTo.accept("ka-ab 123", "KA-AB 123");
    }
}
