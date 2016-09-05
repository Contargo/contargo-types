package net.contargo.types.truck;

/**
 * Can handle German {@link LicensePlate}s.
 *
 * <p>Examples of German license plates:</p>
 *
 * <ul>
 * <li>KA AB 123</li>
 * <li>B XY 492</li>
 * <li>GER U 181</li>
 * <li>LÖ U 1048</li>
 * </ul>
 *
 * <p>Further information: <a href="https://de.wikipedia.org/wiki/Kfz-Kennzeichen_(Deutschland)#Aufbau">License plates
 * of Germany</a></p>
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
class GermanLicensePlateHandler implements LicensePlateHandler {

    private static final String WHITESPACE = " ";
    private static final int MAXIMUM_NUMBER_OF_CHARACTERS = 8;
    private static final int NUMBER_OF_WHITESPACES = 2;

    /**
     * Normalizes the given {@link LicensePlate} value by executing the following steps:
     *
     * <p>1.) Trim and upper case: " ka-ab123 " to "KA-AB123"</p>
     *
     * <p>2.) Use whitespace as separator, remove hyphens: "KA-AB123" to "KA AB123"</p>
     *
     * <p>3.) Ensure identification numbers are separated from identification letters: "KA AB123" to "KA AB 123"</p>
     *
     * @param  value  to get the normalized value for, never {@code null}
     *
     * @return  the normalized value, never {@code null}
     */
    @Override
    public String normalize(String value) {

        return LicensePlateHandler.trim(value)
            .replaceAll("\\-", WHITESPACE)
            .replaceAll("(?<=[A-ZÄÖÜ])(?=[0-9])", WHITESPACE);
    }


    /**
     * Validates the given {@link LicensePlate} value.
     *
     * <p>A German license plate consists of maximum 8 characters and contains two parts:</p>
     *
     * <ul>
     * <li>the geographic identifier: one, two or three letters (may contain umlauts)</li>
     * <li>the identification numbers and/or letters: one or two letters (no umlauts) and one to four digits (without
     * leading zero)</li>
     * </ul>
     *
     * <p>Structure: GGG XX 9999</p>
     *
     * <p>After the geographic identifier there are the round vehicle safety test and registration seal stickers placed
     * above each other.</p>
     *
     * <p>There are certain license plates that may deviate from the rules above:</p>
     *
     * <ul>
     * <li>Seasonal license plates consists of only maximum 7 characters.</li>
     * <li>License plates of official cars can have up to six digits because of missing identification letters.</li>
     * <li>Classic cars can get an H (historic) at the end of the plate, example: ER A 55H</li>
     * </ul>
     *
     * <p>Note that these special cases are not covered by this validator!</p>
     *
     * @param  value  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(String value) {

        String normalizedValue = normalize(value);

        return normalizedValue.length() <= MAXIMUM_NUMBER_OF_CHARACTERS + NUMBER_OF_WHITESPACES
            && normalizedValue.matches("^[A-ZÄÖÜ]{1,3}\\s[A-Z]{0,2}\\s[1-9][0-9]{0,3}");
    }
}
