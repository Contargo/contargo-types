package net.contargo.types;

/**
 * Utility class providing assertion methods.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
final class Assert {

    private Assert() {

        // Hide utility class constructor
    }

    /**
     * Asserts that the given object is not {@code null}. If it is {@code null}, an {@link IllegalArgumentException} is
     * thrown with the given message.
     *
     * @param  object  the object to be checked
     * @param  message  the exception message to use if the assertion fails
     */
    static void notNull(Object object, String message) {

        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Asserts that the given String value is not blank. If it is blank, an {@link IllegalArgumentException} is thrown
     * with the given message.
     *
     * @param  value  the String value to be checked
     * @param  message  the exception message to use if the assertion fails
     *
     * @throws  IllegalArgumentException  if the value is {@code null} or empty
     */
    static void notBlank(String value, String message) {

        Assert.notNull(value, message);

        String trimmed = value.trim();

        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}
