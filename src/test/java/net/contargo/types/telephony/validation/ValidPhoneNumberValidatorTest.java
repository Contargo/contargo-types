package net.contargo.types.telephony.validation;

import com.google.i18n.phonenumbers.NumberParseException;

import org.junit.Assert;
import org.junit.Test;


public class ValidPhoneNumberValidatorTest {

    @Test
    public void ensureThatValidatorSucceedsWhenFormatterSuceeds() throws NumberParseException {

        final ValidPhoneNumberValidator validPhoneNumberValidator = new ValidPhoneNumberValidator();

        Assert.assertTrue("failed to validate valid phone number successfully",
            validPhoneNumberValidator.isValid("12344321", null));
    }


    @Test
    public void ensureThatValidatorFailsWhenFormatterThrows() throws NumberParseException {

        final ValidPhoneNumberValidator validPhoneNumberValidator = new ValidPhoneNumberValidator();

        Assert.assertFalse("failed to detect invalid phone number", validPhoneNumberValidator.isValid("&§", null));
    }
}
