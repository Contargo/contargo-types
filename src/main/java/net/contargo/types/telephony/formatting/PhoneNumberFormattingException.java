package net.contargo.types.telephony.formatting;

/**
 * Is thrown when a telephone number could not be formatted.
 *
 * @author  Robin Jayasinghe - jayasinghe@synyx.de
 */
public class PhoneNumberFormattingException extends Exception {

    public PhoneNumberFormattingException(String message) {

        super(message);
    }


    public PhoneNumberFormattingException(String message, Throwable cause) {

        super(message, cause);
    }
}
