package net.contargo.types;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class LicensePlateValidatorFactoryTest {

    @Test
    public void ensureCanHazzDefaultValidatorForNullCountry() {

        LicensePlateValidator validator = LicensePlateValidatorFactory.getForCountry(null);

        Assert.assertNotNull("Missing validator", validator);
        Assert.assertTrue("Wrong validator implementation", validator instanceof DefaultLicensePlateValidator);
    }


    @Test
    public void ensureCanHazzMatchingValidatorForCountry() {

        LicensePlateValidator validator = LicensePlateValidatorFactory.getForCountry(Country.GERMANY);

        Assert.assertNotNull("Missing validator", validator);
        Assert.assertTrue("Wrong validator implementation", validator instanceof GermanLicensePlateValidator);
    }


    @Test
    public void ensureCanHazzMatchingValidatorForCountryWithoutValidatorImplementation() {

        LicensePlateValidator validator = LicensePlateValidatorFactory.getForCountry(Country.BELGIUM);

        Assert.assertNotNull("Missing validator", validator);
        Assert.assertTrue("Wrong validator implementation", validator instanceof DefaultLicensePlateValidator);
    }
}
