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
    public void ensureInternationalFormattingPhoneNumberFromInvalidInput() {

        final PhoneNumber phoneNumber = new PhoneNumber("i321%6&_-?`");
        assertFalse(phoneNumber.canBeFormatted());
    }


    @Test
    public void ensureFormatsRussianMobile() {

        final PhoneNumber phoneNumber = new PhoneNumber("+7 (922) 555-1234");

        assertEquals("+7 922 5551 234", phoneNumber.getInternationalFormatOfPhoneNumber().get());
    }


    @Test
    public void ensureFormatsAlgeriaMobile() {

        final PhoneNumber phoneNumber = new PhoneNumber("+213 5 934583");

        assertEquals("+213 593 4583", phoneNumber.getInternationalFormatOfPhoneNumber().get());
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
        assertEquals("+1 242 7836 4736", phoneNumber.getInternationalFormatOfPhoneNumber().get());
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


    @Test
    public void ensureGettingCorrectFormattedGermanNumber() {

        final PhoneNumber phoneNumber = new PhoneNumber("072183478374");

        assertEquals("+49 721 8347 8374", phoneNumber.getInternationalFormatOfPhoneNumber().get());
    }


    @Test
    public void ensureGetFormattingPhoneNumberFromMixedPhoneNumber() {

        /*
         * currently phoneNumber lib converts mixed numbers to correct phoneNumbers
         * falsehoods of phone numbers says that 'numbers' can contain letters
         * if there are too many problems think about excluding letters in a custom contargo phone number validator
         */
        final PhoneNumber phoneNumber = new PhoneNumber("a234svljshdf034");

        assertEquals("+49 234 7855 7433 034", phoneNumber.getInternationalFormatOfPhoneNumber().get());
        assertTrue(phoneNumber.canBeFormatted());
    }


    @Test
    public void ensureGettingCorrectFormattedPhoneNumber() {

        final PhoneNumber phoneNumber = new PhoneNumber("+41 713111301");

        assertEquals("+41 71 3111 301", phoneNumber.getInternationalFormatOfPhoneNumber().get());
    }
}
