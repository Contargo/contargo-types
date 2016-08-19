package net.contargo.types;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiConsumer;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class CountryTest {

    private BiConsumer<LicensePlateHandler, Class> assertCorrectHandler = (handler, clazz) -> {
        Assert.assertNotNull("Missing handler", handler);
        Assert.assertEquals("Wrong handler implementation", clazz, handler.getClass());
    };

    @Test
    public void ensureCorrectHandlerMatching() {

        // dedicated handler
        assertCorrectHandler.accept(Country.GERMANY.getLicensePlateHandler(), GermanLicensePlateHandler.class);

        assertCorrectHandler.accept(Country.NETHERLANDS.getLicensePlateHandler(), DutchLicensePlateHandler.class);

        // default handler
        assertCorrectHandler.accept(Country.BELGIUM.getLicensePlateHandler(), DefaultLicensePlateHandler.class);
        assertCorrectHandler.accept(Country.SWITZERLAND.getLicensePlateHandler(), DefaultLicensePlateHandler.class);
        assertCorrectHandler.accept(Country.FRANCE.getLicensePlateHandler(), DefaultLicensePlateHandler.class);
        assertCorrectHandler.accept(Country.POLAND.getLicensePlateHandler(), DefaultLicensePlateHandler.class);
        assertCorrectHandler.accept(Country.CZECH_REPUBLIC.getLicensePlateHandler(), DefaultLicensePlateHandler.class);
    }


    @Test
    public void ensureCanHazzCountryCode() {

        Assert.assertEquals("Wrong country code", "D", Country.GERMANY.getCountryCode());
    }
}
