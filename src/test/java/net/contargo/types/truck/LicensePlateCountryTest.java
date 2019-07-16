package net.contargo.types.truck;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiConsumer;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class LicensePlateCountryTest {

    private final BiConsumer<LicensePlateHandler, Class> assertCorrectHandler = (handler, clazz) -> {
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
        assertCorrectHandler.accept(LicensePlateCountry.BELGIUM.getLicensePlateHandler(),
            BelgianLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.SWITZERLAND.getLicensePlateHandler(),
            SwissLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.FRANCE.getLicensePlateHandler(),
            FrenchLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.POLAND.getLicensePlateHandler(),
            PolishLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.CZECH_REPUBLIC.getLicensePlateHandler(),
            CzechLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.ROMANIA.getLicensePlateHandler(),
            RomanianLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.BULGARIA.getLicensePlateHandler(),
            BulgarianLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.LITHUANIA.getLicensePlateHandler(),
            LithuanianLicensePlateHandler.class);
    }


    @Test
    public void ensureCanHazzCountryCode() {

        Assert.assertEquals("Wrong country code", "D", LicensePlateCountry.GERMANY.getCountryCode());
    }


    @Test
    public void ensureCanGetCountryByCountryCode() {

        Country country = LicensePlateCountry.forCountryCode("D");

        Assert.assertEquals("Wrong country", LicensePlateCountry.GERMANY, country);
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfTryingToGetCountryByUnknownCountryCode() {

        LicensePlateCountry.forCountryCode("XY");
    }
}
