package net.contargo.types.truck;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;


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
        assertCorrectHandler.accept(LicensePlateCountry.BELGIUM.getLicensePlateHandler(),
            BelgianLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.SWITZERLAND.getLicensePlateHandler(),
            SwissLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.FRANCE.getLicensePlateHandler(),
            FrenchLicensePlateHandler.class);
        assertCorrectHandler.accept(LicensePlateCountry.POLAND.getLicensePlateHandler(),
            PolishLicensePlateHandler.class);

        // default handler
        assertCorrectHandler.accept(LicensePlateCountry.CZECH_REPUBLIC.getLicensePlateHandler(),
            DefaultLicensePlateHandler.class);
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


    @Test
    public void ensureThrowsIfTryingToGetCountryByEmptyCountryCode() {

        Consumer<String> assertNotEmpty = countryCode -> {
            try {
                LicensePlateCountry.forCountryCode(countryCode);
                Assert.fail(String.format("Should throw if trying to get country by country code: '%s'", countryCode));
            } catch (IllegalArgumentException ex) {
                // Expected
            }
        };

        assertNotEmpty.accept(null);
        assertNotEmpty.accept("");
        assertNotEmpty.accept(" ");
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfTryingToGetCountryByUnknownCountryCode() {

        LicensePlateCountry.forCountryCode("XY");
    }
}
