package net.contargo.types;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class DefaultLicensePlateValidatorTest {

    private DefaultLicensePlateValidator validator;

    @Before
    public void setUp() {

        validator = new DefaultLicensePlateValidator();
    }


    @Test
    public void ensureLicensePlateWithLettersAndNumbersIsValid() {

        String value = "KA XY 123";
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertTrue("Should be valid: " + value, validator.isValid(licensePlate));
    }


    @Test
    public void ensureLicensePlateWithMinusAsSeparatorIsValid() {

        String value = "KA-XY-123";
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertTrue("Should be valid: " + value, validator.isValid(licensePlate));
    }


    @Test
    public void ensureLicensePlateWithSpecialCharactersIsNotValid() {

        String value = "KA/XY.123";
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, validator.isValid(licensePlate));
    }


    @Test
    public void ensureLicensePlateWithUmlautIsValid() {

        String value = "LÖ-CR-123";
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertTrue("Should be valid: " + value, validator.isValid(licensePlate));
    }
}
