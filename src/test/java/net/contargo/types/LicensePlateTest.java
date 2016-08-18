package net.contargo.types;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class LicensePlateTest {

    // BUILD ---------------------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfBuiltWithNull() {

        LicensePlate.forValue(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfBuiltWithEmptyString() {

        LicensePlate.forValue(" ");
    }


    @Test
    public void ensureCanBeBuiltWithString() {

        LicensePlate licensePlate = LicensePlate.forValue("foo");

        Assert.assertNotNull("Should not be null", licensePlate);
    }


    // FORMAT --------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsUpperCased() {

        String value = "ka ab 123";
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertEquals("Wrong String representation for: " + value, "KA AB 123", licensePlate.toString());
    }


    @Test
    public void ensureMinusSeparatorIsReplacedByWhiteSpace() {

        String value = "ka-ab-123";
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertEquals("Wrong String representation for: " + value, "KA AB 123", licensePlate.toString());
    }


    // EQUALS --------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlatesWithDifferentSeparatorsAreEquals() {

        String v1 = "KA-AB-123";
        String v2 = "KA AB 123";

        LicensePlate l1 = LicensePlate.forValue(v1);
        LicensePlate l2 = LicensePlate.forValue(v2);

        Assert.assertTrue(v1 + " should be equals to " + v2, l1.equals(l2));
    }


    @Test
    public void ensureDifferentLicensePlatesAreNotEquals() {

        String v1 = "KA AB 123";
        String v2 = "B XY 456";

        LicensePlate l1 = LicensePlate.forValue(v1);
        LicensePlate l2 = LicensePlate.forValue(v2);

        Assert.assertFalse(v1 + " should not be equals to " + v2, l1.equals(l2));
    }


    @Test
    public void ensureNotEqualsIfDifferentClassesAreCompared() {

        LicensePlate licensePlate = LicensePlate.forValue("KA AB 123");

        Assert.assertFalse("Different classes should not be equals", licensePlate.equals(new Object()));
    }


    @Test
    public void ensureEqualsDoesNotBreakOnNull() {

        String value = "KA AB 123";
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertFalse(value + " should not be equals to null", licensePlate.equals(null));
    }


    // COUNTRY -------------------------------------------------------------------------------------

    @Test
    public void ensureCountryCanBeEmpty() {

        Assert.assertFalse("Country should not be set", LicensePlate.forValue("abc").getCountry().isPresent());
    }


    @Test
    public void ensureCanBeBuiltWithCountry() {

        LicensePlate licensePlate = LicensePlate.forValue("abc").withCountry(Country.GERMANY);

        Assert.assertNotNull("Should not be null", licensePlate);

        Optional<Country> optionalCountry = licensePlate.getCountry();
        Assert.assertTrue("Missing country", optionalCountry.isPresent());
        Assert.assertEquals("Wrong country", Country.GERMANY, optionalCountry.get());
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfBuiltWithNullCountry() {

        LicensePlate.forValue("abc").withCountry(null);
    }


    // VALID ---------------------------------------------------------------------------------------

    @Test
    public void ensureValidLicensePlateWithoutCountryIsValid() {

        String value = "KA XY 123";
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertTrue("Should be valid: " + value, licensePlate.isValid());
    }


    @Test
    public void ensureInvalidLicensePlateWithoutCountryIsNotValid() {

        String value = "KA/XY.123";
        LicensePlate licensePlate = LicensePlate.forValue(value);

        Assert.assertFalse("Should not be valid: " + value, licensePlate.isValid());
    }
}
