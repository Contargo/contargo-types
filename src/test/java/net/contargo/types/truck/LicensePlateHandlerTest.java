package net.contargo.types.truck;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiConsumer;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class LicensePlateHandlerTest {

    private static final BiConsumer<String, String> ASSERT_IS_TRIMMED_FROM_TO = (value, expected) -> {
        Assert.assertEquals("Wrong trimmed value", expected, LicensePlateHandler.trim(value));
    };

    @Test
    public void ensureTrimmingUpperCasesValue() {

        ASSERT_IS_TRIMMED_FROM_TO.accept("foo", "FOO");
    }


    @Test
    public void ensureTrimmingRemovesAllLeadingAndTrailingWhitespaces() {

        ASSERT_IS_TRIMMED_FROM_TO.accept(" foo bar", "FOO BAR");
        ASSERT_IS_TRIMMED_FROM_TO.accept("foo bar ", "FOO BAR");
    }


    @Test
    public void ensureTrimmingRemovesAllDuplicatedWhitespaces() {

        ASSERT_IS_TRIMMED_FROM_TO.accept("foo bar", "FOO BAR");
        ASSERT_IS_TRIMMED_FROM_TO.accept("foo  bar", "FOO BAR");
        ASSERT_IS_TRIMMED_FROM_TO.accept("foo   bar", "FOO BAR");
    }


    @Test
    public void ensureTrimmingRemovesAllDuplicatedHyphens() {

        ASSERT_IS_TRIMMED_FROM_TO.accept("foo-bar", "FOO-BAR");
        ASSERT_IS_TRIMMED_FROM_TO.accept("foo--bar", "FOO-BAR");
        ASSERT_IS_TRIMMED_FROM_TO.accept("foo---bar", "FOO-BAR");
    }
}
