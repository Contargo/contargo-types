package net.contargo.types.telephony.formatting;

import com.google.i18n.phonenumbers.NumberParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class PhoneNumberFormatterTest {

    private PhoneNumberFormatter phoneNumberFormatter;

    @Before
    public void init() {

        phoneNumberFormatter = new PhoneNumberFormatter();
    }


    @Test
    public void ensureThatE164FormatPhoneNumberFromInputStringFormatsValidNumber()
        throws PhoneNumberFormattingException {

        final PhoneNumberFormatter phoneNumberFormatter = new PhoneNumberFormatter();
        Assert.assertEquals("+49123456", phoneNumberFormatter.parseAndFormatToE164Format("123456"));
    }


    @Test(expected = PhoneNumberFormattingException.class)
    public void ensureThatE164FormatPhoneNumberFromInvalidInputThrows() throws PhoneNumberFormattingException {

        final PhoneNumberFormatter phoneNumberFormatter = new PhoneNumberFormatter();
        phoneNumberFormatter.parseAndFormatToE164Format("i321%6&_-?`");
    }


    @Test
    public void ensureThatDIN5008FormatsRussianMobile() throws PhoneNumberFormattingException {

        Assert.assertEquals("+7 922 5551 234", phoneNumberFormatter.parseAndFormatToDIN5008("+7 (922) 555-1234"));
    }


    @Test
    public void ensureThatDIN5008FormatsLongGermanMobileNumber() throws PhoneNumberFormattingException {

        Assert.assertEquals("+49 171 1234 5678 9123",
            phoneNumberFormatter.parseAndFormatToDIN5008("0171123456789123"));
    }


    @Test
    public void ensureThatDIN5008FormatsGermanFixedNumberWithoutCountryCode() throws PhoneNumberFormattingException {

        Assert.assertEquals("+49 6222 1234 56", phoneNumberFormatter.parseAndFormatToDIN5008("06222123456"));
    }


    @Test
    public void ensureThatDIN5008FormatsGermanFixedNumberWithCountryCode() throws PhoneNumberFormattingException {

        Assert.assertEquals("+49 6222 1234 56", phoneNumberFormatter.parseAndFormatToDIN5008("+496222123456"));
        Assert.assertEquals("+49 6222 1234 56", phoneNumberFormatter.parseAndFormatToDIN5008("00496222123456"));
    }


    @Test
    public void ensureThatDIN5008FormatsGermanMobileNumberWithCountryCode() throws PhoneNumberFormattingException {

        Assert.assertEquals("+49 171 1234 56", phoneNumberFormatter.parseAndFormatToDIN5008("+49171123456"));
        Assert.assertEquals("+49 171 1234 56", phoneNumberFormatter.parseAndFormatToDIN5008("0049171123456"));
    }


    @Test
    public void ensureThatDIN5008FormatsGermanMobileNumberWithSlash() throws PhoneNumberFormattingException {

        Assert.assertEquals("+49 171 1234 56", phoneNumberFormatter.parseAndFormatToDIN5008("0171/123456"));
    }
}
