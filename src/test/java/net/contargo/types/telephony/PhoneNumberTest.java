package net.contargo.types.telephony;

import net.contargo.types.telephony.formatting.PhoneNumberFormattingException;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;


/**
 * @author  Julia Dasch - dasch@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class PhoneNumberTest {

    @Test
    public void ensureInternationalFormattingPhoneNumberFromValid() {

        final PhoneNumber phoneNumber = new PhoneNumber("00492342323523423");

        Optional<String> formattedPhoneNumber = phoneNumber.getInternationalFormatOfPhoneNumber();

        assertTrue(formattedPhoneNumber.isPresent());
        assertEquals("+49 234 2323 5234 23", formattedPhoneNumber.get());
    }


    @Test
    public void ensureInternationalFormattingPhoneNumberFromInvalidInputThrows()
        throws PhoneNumberFormattingException {

        final PhoneNumber phoneNumber = new PhoneNumber("i321%6&_-?`");
        assertFalse(phoneNumber.getInternationalFormatOfPhoneNumber().isPresent());
    }


    @Test
    public void ensureFormatsRussianMobile() {

        final PhoneNumber phoneNumber = new PhoneNumber("+7 (922) 555-1234");

        assertEquals("+7 922 5551 234", phoneNumber.getInternationalFormatOfPhoneNumber().get());
    }


    @Test
    public void ensureFormatsLongGermanMobileNumber() {

        final PhoneNumber phoneNumber = new PhoneNumber("0171123456789123");

        assertEquals("+49 171 1234 5678 9123", phoneNumber.getInternationalFormatOfPhoneNumber().get());
    }


    @Test
    public void ensureGettingCorrectCountryForPhoneNumber() throws PhoneNumberFormattingException {

        final PhoneNumber phoneNumber = new PhoneNumber("00124278364736");

        assertEquals("+1", phoneNumber.getCountry().getCountryCallingCode());
        assertEquals("BS", phoneNumber.getCountry().getCountryCode());
    }


    @Test
    public void ensureGettingCorrectRegionCodeForPhoneNumber() throws PhoneNumberFormattingException {

        final PhoneNumber phoneNumber = new PhoneNumber("00124278364736");

        assertEquals("24278364736", phoneNumber.getNationalSignificantNumber());
    }


    @Test
    public void ensureGermanNumberToInternationalPhoneNumber() {

        final PhoneNumber phoneNumber = new PhoneNumber("02342323523423");

        assertEquals("+49 234 2323 5234 23", phoneNumber.getInternationalFormatOfPhoneNumber().get());
    }


    @Test
    public void ensureGettingCorrectNumberWithPhoneExtension() {

        final PhoneNumber phoneNumber = new PhoneNumber("02342323523423");
        phoneNumber.setPhoneExtension("56");

        assertEquals("+49 234 2323 5234 2356", phoneNumber.getInternationalFormatOfPhoneNumber().get());
    }
}
