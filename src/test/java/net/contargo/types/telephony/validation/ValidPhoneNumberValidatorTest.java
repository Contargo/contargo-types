package net.contargo.types.telephony.validation;

import com.google.i18n.phonenumbers.NumberParseException;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ValidPhoneNumberValidatorTest {

    @Test
    public void ensureThatValidatorSucceedsWhenFormatterSuceeds() throws NumberParseException {

        final ValidPhoneNumberValidator validPhoneNumberValidator = new ValidPhoneNumberValidator();

        assertTrue("failed to validate valid phone number successfully",
            validPhoneNumberValidator.isValid("12344321", null));
    }


    @Test
    public void ensureThatEmptyStringIsIgnored() {

        ValidPhoneNumberValidator sut = new ValidPhoneNumberValidator();
        assertTrue("Empty phone number (whitespace) must be ignored, as valid", sut.isValid("", null));
    }


    @Test
    public void ensureOnlyWhitespaceStringIsIgnored() throws Exception {

        ValidPhoneNumberValidator sut = new ValidPhoneNumberValidator();
        assertTrue("Empty phone number must be ignored, as valid", sut.isValid("    ", null));
    }


    @Test
    public void ensureNullStringIsIgnored() {

        final ValidPhoneNumberValidator sut = new ValidPhoneNumberValidator();
        assertTrue("Missing phone number (null) must be ignored, as valid", sut.isValid(null, null));
    }


    @Test
    public void ensureThatValidatorFailsWhenFormatterThrows() throws NumberParseException {

        final ValidPhoneNumberValidator validPhoneNumberValidator = new ValidPhoneNumberValidator();

        assertFalse("failed to detect invalid phone number", validPhoneNumberValidator.isValid("&§", null));
    }
}
