package net.contargo.types.util;

import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class AssertTest {

    // NOT NULL ----------------------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void ensureNotNullThrowsForNull() {

        Assert.notNull(null, "message");
    }


    @Test
    public void ensureNotNullUsesGivenMessageOnThrow() {

        String message = "message";

        try {
            Assert.notNull(null, message);
            org.junit.Assert.fail("Should throw for null value");
        } catch (IllegalArgumentException ex) {
            org.junit.Assert.assertEquals("Wrong message", message, ex.getMessage());
        }
    }


    @Test
    public void ensureNotNullDoesNotThrowForObject() {

        try {
            Assert.notNull(new Object(), "message");
        } catch (IllegalArgumentException ex) {
            org.junit.Assert.fail("Should not throw for not null object");
        }
    }


    // NOT BLANK ---------------------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void ensureNotBlankThrowsForNullString() {

        Assert.notBlank(null, "message");
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureNotBlankThrowsForEmptyString() {

        Assert.notBlank("", "message");
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureNotBlankThrowsForWhitespaceString() {

        Assert.notBlank("  ", "message");
    }


    @Test
    public void ensureNotBlankUsesGivenMessageOnThrow() {

        String message = "message";

        try {
            Assert.notBlank("", message);
            org.junit.Assert.fail("Should throw for null value");
        } catch (IllegalArgumentException ex) {
            org.junit.Assert.assertEquals("Wrong message", message, ex.getMessage());
        }
    }


    @Test
    public void ensureNotBlankDoesNotThrowForValidString() {

        try {
            Assert.notBlank("foo", "message");
        } catch (IllegalArgumentException ex) {
            org.junit.Assert.fail("Should not throw for valid String");
        }
    }
}
