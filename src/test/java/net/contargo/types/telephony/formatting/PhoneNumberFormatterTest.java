package net.contargo.types.telephony.formatting;

import com.google.i18n.phonenumbers.NumberParseException;

import org.junit.Assert;
import org.junit.Test;


public class PhoneNumberFormatterTest {

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
}
