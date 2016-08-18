package net.contargo.types;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class LicensePlateHandlerFactoryTest {

    @Test
    public void ensureCanHazzDefaultHandlerForNullCountry() {

        LicensePlateHandler handler = LicensePlateHandlerFactory.getForCountry(null);

        Assert.assertNotNull("Missing handler", handler);
        Assert.assertTrue("Wrong handler implementation", handler instanceof DefaultLicensePlateHandler);
    }


    @Test
    public void ensureCanHazzMatchingHandlerForCountry() {

        LicensePlateHandler handler = LicensePlateHandlerFactory.getForCountry(Country.GERMANY);

        Assert.assertNotNull("Missing handler", handler);
        Assert.assertTrue("Wrong handler implementation", handler instanceof GermanLicensePlateHandler);
    }


    @Test
    public void ensureCanHazzMatchingHandlerForCountryWithoutHandlerImplementation() {

        LicensePlateHandler handler = LicensePlateHandlerFactory.getForCountry(Country.BELGIUM);

        Assert.assertNotNull("Missing handler", handler);
        Assert.assertTrue("Wrong handler implementation", handler instanceof DefaultLicensePlateHandler);
    }
}
