package net.contargo.types;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class DefaultLicensePlateValidatorTest {

    private DefaultLicensePlateValidator validator;

    private Consumer<String> assertIsValid = value -> {
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertTrue("Should be valid: " + value, validator.isValid(licensePlate));
    };

    private Consumer<String> assertIsNotValid = value -> {
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, validator.isValid(licensePlate));
    };

    @Before
    public void setUp() {

        validator = new DefaultLicensePlateValidator();
    }


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
}
