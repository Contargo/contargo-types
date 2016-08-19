package net.contargo.types;

import org.junit.Assert;
import org.junit.Test;

import org.mockito.Mockito;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class LicensePlateTest {

    private static final LicensePlateCountry COUNTRY = LicensePlateCountry.GERMANY;

    // BUILD ---------------------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfBuiltWithNull() {

        LicensePlate.forValue(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfBuiltWithEmptyString() {

        LicensePlate.forValue(" ");
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfBuiltWithNullCountry() {

        LicensePlate.forValue("abc").withCountry(null);
    }


    @Test
    public void ensureCanBeBuiltWithStringValueAndCountry() {

        LicensePlate licensePlate = LicensePlate.forValue("foo").withCountry(COUNTRY);

        Assert.assertNotNull("Should not be null", licensePlate);
        Assert.assertEquals("Wrong country", COUNTRY, licensePlate.getCountry());
    }


    // FORMAT --------------------------------------------------------------------------------------

    // NOTE: Dedicated tests for formatting license plates can be found in the specialized handler tests

    @Test
    public void ensureHandlerForCountryIsCalledOnToString() {

        LicensePlateHandler handlerMock = Mockito.mock(LicensePlateHandler.class);
        Country country = new DummyCountry(handlerMock);

        String value = "foo";
        String formattedValue = "formatted";

        Mockito.when(handlerMock.format(Mockito.anyString())).thenReturn(formattedValue);

        LicensePlate licensePlate = LicensePlate.forValue(value).withCountry(country);

        Assert.assertEquals("Wrong String representation", formattedValue, licensePlate.toString());
        Mockito.verify(handlerMock).format(value);
    }


    // VALID ---------------------------------------------------------------------------------------

    // NOTE: Dedicated tests for validation of license plates can be found in the specialized handler tests
    @Test
    public void ensureHandlerForCountryIsCalledOnIsValid() {

        LicensePlateHandler handlerMock = Mockito.mock(LicensePlateHandler.class);
        Country country = new DummyCountry(handlerMock);
        String value = "foo";

        Mockito.when(handlerMock.validate(Mockito.anyString())).thenReturn(true);

        LicensePlate licensePlate = LicensePlate.forValue(value).withCountry(country);

        Assert.assertTrue("Should be valid", licensePlate.isValid());
        Mockito.verify(handlerMock).validate(value);
    }


    // EQUALS --------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlatesWithDifferentSeparatorsAreEquals() {

        String v1 = "KA-AB-123";
        String v2 = "KA AB 123";

        LicensePlate l1 = LicensePlate.forValue(v1).withCountry(COUNTRY);
        LicensePlate l2 = LicensePlate.forValue(v2).withCountry(COUNTRY);

        Assert.assertTrue(v1 + " should be equals to " + v2, l1.equals(l2));
    }


    @Test
    public void ensureLicensePlatesWithDifferentCasesAreEquals() {

        String v1 = "KA-AB-123";
        String v2 = "ka ab 123";

        LicensePlate l1 = LicensePlate.forValue(v1).withCountry(COUNTRY);
        LicensePlate l2 = LicensePlate.forValue(v2).withCountry(COUNTRY);

        Assert.assertTrue(v1 + " should be equals to " + v2, l1.equals(l2));
    }


    @Test
    public void ensureDifferentLicensePlatesAreNotEquals() {

        String v1 = "KA AB 123";
        String v2 = "B XY 456";

        LicensePlate l1 = LicensePlate.forValue(v1).withCountry(COUNTRY);
        LicensePlate l2 = LicensePlate.forValue(v2).withCountry(COUNTRY);

        Assert.assertFalse(v1 + " should not be equals to " + v2, l1.equals(l2));
    }


    @Test
    public void ensureNotEqualsIfDifferentClassesAreCompared() {

        LicensePlate licensePlate = LicensePlate.forValue("KA AB 123").withCountry(COUNTRY);

        Assert.assertFalse("Different classes should not be equals", licensePlate.equals(new Object()));
    }


    @Test
    public void ensureEqualsDoesNotBreakOnNull() {

        String value = "KA AB 123";
        LicensePlate licensePlate = LicensePlate.forValue(value).withCountry(COUNTRY);

        Assert.assertFalse(value + " should not be equals to null", licensePlate.equals(null));
    }

    private class DummyCountry implements Country {

        private final LicensePlateHandler handler;

        private DummyCountry(LicensePlateHandler handler) {

            this.handler = handler;
        }

        @Override
        public String getCountryCode() {

            return "DUMMY";
        }


        @Override
        public LicensePlateHandler getLicensePlateHandler() {

            return handler;
        }
    }
}
