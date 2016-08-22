package net.contargo.types;

import org.junit.Assert;

import java.util.function.BiConsumer;
import java.util.function.Consumer;


/**
 * Abstract test class for testing implementations of {@link LicensePlateHandler}s providing different assertions.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
class AbstractLicensePlateHandlerTest {

    LicensePlateHandler handler;

    Consumer<String> assertIsValid = value -> {
        Assert.assertTrue("Should be valid: " + value, handler.validate(value));
    };

    Consumer<String> assertIsNotValid = value -> {
        Assert.assertFalse("Should not be valid: " + value, handler.validate(value));
    };

    BiConsumer<String, String> assertIsFormattedFromTo = (value, expected) -> {
        Assert.assertEquals("Wrong formatted value", expected, handler.format(value));
    };

    BiConsumer<String, String> assertIsNormalizedFromTo = (value, expected) -> {
        Assert.assertEquals("Wrong formatted value", expected, handler.normalize(value));
    };
}
