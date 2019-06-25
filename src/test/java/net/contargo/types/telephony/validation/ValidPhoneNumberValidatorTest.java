package net.contargo.types.telephony.validation;

import com.google.i18n.phonenumbers.NumberParseException;

import net.contargo.types.telephony.PhoneNumber;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.*;


public class ValidPhoneNumberValidatorTest {

    @Test
    public void ensureThatValidatorSucceedsWhenFormatterSucceeds() {

        final ValidPhoneNumberValidator validPhoneNumberValidator = new ValidPhoneNumberValidator();

        assertTrue("failed to validate valid phone number successfully",
            validPhoneNumberValidator.isValid(new PhoneNumber("12344321"), null));
    }


    @Test
    public void ensureThatEmptyStringIsIgnored() {

        ValidPhoneNumberValidator sut = new ValidPhoneNumberValidator();
        assertTrue("Empty phone number (whitespace) must be ignored, as valid",
            sut.isValid(new PhoneNumber(""), null));
    }


    @Test
    public void ensureOnlyWhitespaceStringIsIgnored() {

        ValidPhoneNumberValidator sut = new ValidPhoneNumberValidator();
        assertTrue("Empty phone number must be ignored, as valid", sut.isValid(new PhoneNumber("    "), null));
    }


    @Test
    public void ensureNullStringIsIgnored() {

        final ValidPhoneNumberValidator sut = new ValidPhoneNumberValidator();
        assertTrue("Missing phone number (null) must be ignored, as valid", sut.isValid(null, null));
    }
}
