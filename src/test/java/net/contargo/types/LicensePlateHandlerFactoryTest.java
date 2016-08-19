package net.contargo.types;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiConsumer;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class LicensePlateHandlerFactoryTest {

    private BiConsumer<LicensePlateHandler, Class> assertCorrectHandler = (handler, clazz) -> {
        Assert.assertNotNull("Missing handler", handler);
        Assert.assertEquals("Wrong handler implementation", clazz, handler.getClass());
    };

    @Test
    public void ensureCanHazzDefaultHandlerForNullCountry() {

        assertCorrectHandler.accept(LicensePlateHandlerFactory.getForCountry(null), DefaultLicensePlateHandler.class);
    }


    @Test
    public void ensureCanHazzMatchingHandlerForCountry() {

        assertCorrectHandler.accept(LicensePlateHandlerFactory.getForCountry(Country.GERMANY),
            GermanLicensePlateHandler.class);

        assertCorrectHandler.accept(LicensePlateHandlerFactory.getForCountry(Country.NETHERLANDS),
            DutchLicensePlateHandler.class);
    }


    @Test
    public void ensureCanHazzMatchingHandlerForCountryWithoutHandlerImplementation() {

        assertCorrectHandler.accept(LicensePlateHandlerFactory.getForCountry(Country.BELGIUM),
            DefaultLicensePlateHandler.class);

        assertCorrectHandler.accept(LicensePlateHandlerFactory.getForCountry(Country.SWITZERLAND),
            DefaultLicensePlateHandler.class);

        assertCorrectHandler.accept(LicensePlateHandlerFactory.getForCountry(Country.FRANCE),
            DefaultLicensePlateHandler.class);

        assertCorrectHandler.accept(LicensePlateHandlerFactory.getForCountry(Country.POLAND),
            DefaultLicensePlateHandler.class);

        assertCorrectHandler.accept(LicensePlateHandlerFactory.getForCountry(Country.CZECH_REPUBLIC),
            DefaultLicensePlateHandler.class);
    }
}
