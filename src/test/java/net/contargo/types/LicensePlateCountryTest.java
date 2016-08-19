package net.contargo.types;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiConsumer;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class LicensePlateCountryTest {

    private BiConsumer<LicensePlateHandler, Class> assertCorrectHandler = (handler, clazz) -> {
        Assert.assertNotNull("Missing handler", handler);
        Assert.assertEquals("Wrong handler implementation", clazz, handler.getClass());
    };

    @Test
    public void ensureCorrectHandlerMatching() {

        // dedicated handler
        assertCorrectHandler.accept(LicensePlateCountry.GERMANY.getLicensePlateHandler(),
            GermanLicensePlateHandler.class);

        assertCorrectHandler.accept(LicensePlateCountry.NETHERLANDS.getLicensePlateHandler(),
            DutchLicensePlateHandler.class);

        // default handler
        assertCorrectHandler.accept(LicensePlateCountry.BELGIUM.getLicensePlateHandler(),
            DefaultLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.SWITZERLAND.getLicensePlateHandler(),
            DefaultLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.FRANCE.getLicensePlateHandler(),
            DefaultLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.POLAND.getLicensePlateHandler(),
            DefaultLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.CZECH_REPUBLIC.getLicensePlateHandler(),
            DefaultLicensePlateHandler.class);
    }


    @Test
    public void ensureCanHazzCountryCode() {

        Assert.assertEquals("Wrong country code", "D", LicensePlateCountry.GERMANY.getCountryCode());
    }
}
