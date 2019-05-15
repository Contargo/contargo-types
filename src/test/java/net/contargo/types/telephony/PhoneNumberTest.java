package net.contargo.types.telephony;

import net.contargo.types.telephony.formatting.PhoneNumberFormattingException;

import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;


/**
 * @author  Julia Dasch - dasch@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class PhoneNumberTest {

    @Test
    public void ensureInternationalFormattingPhoneNumberFromValid() throws PhoneNumberFormattingException {

        final PhoneNumber phoneNumber = new PhoneNumber("00492342323523423");

        String formattedPhoneNumber = phoneNumber.getInternationalFormattedPhoneNumber();

        assertEquals("+49 234 2323 5234 23", formattedPhoneNumber);
    }


    @Test(expected = PhoneNumberFormattingException.class)
    public void ensureInternationalFormattingPhoneNumberFromInvalidInputThrows()
        throws PhoneNumberFormattingException {

        final PhoneNumber phoneNumber = new PhoneNumber("i321%6&_-?`");
        phoneNumber.getInternationalFormattedPhoneNumber();
    }


    @Test
    public void ensureFormatsRussianMobile() throws PhoneNumberFormattingException {

        final PhoneNumber phoneNumber = new PhoneNumber("+7 (922) 555-1234");

        Assert.assertEquals("+7 922 5551 234", phoneNumber.getInternationalFormattedPhoneNumber());
    }


    @Test
    public void ensureFormatsLongGermanMobileNumber() throws PhoneNumberFormattingException {

        final PhoneNumber phoneNumber = new PhoneNumber("0171123456789123");

        Assert.assertEquals("+49 171 1234 5678 9123", phoneNumber.getInternationalFormattedPhoneNumber());
    }


    @Test
    public void ensureGettingCorrectCountryForPhoneNumber() throws PhoneNumberFormattingException {

        final PhoneNumber phoneNumber = new PhoneNumber("00124278364736");

        Assert.assertEquals("+1", phoneNumber.getCountry().getCountryCallingCode());
        Assert.assertEquals("BS", phoneNumber.getCountry().getCountryCode());
    }


    @Test
    public void ensureGettingCorrectRegionCodeForPhoneNumber() throws PhoneNumberFormattingException {

        final PhoneNumber phoneNumber = new PhoneNumber("00124278364736");

        Assert.assertEquals("24278364736", phoneNumber.getNationalSignificantNumber());
    }

    @Test
    public void ensureGermanNumberToInternationalPhoneNumber() throws PhoneNumberFormattingException {

        final PhoneNumber phoneNumber = new PhoneNumber("02342323523423");

        Assert.assertEquals("+49 234 2323 5234 23", phoneNumber.getInternationalFormattedPhoneNumber());
    }


    @Test
    public void ensureGettingCorrectNumberWithPhoneExtension() throws PhoneNumberFormattingException {

        final PhoneNumber phoneNumber = new PhoneNumber("02342323523423");
        phoneNumber.setPhoneExtension("56");

        Assert.assertEquals("+49 234 2323 5234 2356", phoneNumber.getInternationalFormattedPhoneNumber());
    }
}
