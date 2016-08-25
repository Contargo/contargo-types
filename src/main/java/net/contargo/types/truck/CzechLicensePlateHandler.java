package net.contargo.types.truck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Can handle Czech {@link LicensePlate}s.
 *
 * <p>Examples of Czech license plates since 2001:</p>
 *
 * <ul>
 * <li>1SO 3690</li>
 * <li>1AA 4303</li>
 * <li>5A6 3240</li>
 * <li>4A2 3000</li>
 * <li>2H2 7149</li>
 * <li>TRABANT1 (custom license plate)</li>
 * </ul>
 *
 * <p>Example of Czech license plates between 1960 and 2001:</p>
 *
 * <ul>
 * <li>ALA 40-11</li>
 * <li>ALC 31-20</li>
 * <li>AKX 13-77</li>
 * <li>TP 65-51</li>
 * </ul>
 *
 * <p>Further information: <a href="https://de.wikipedia.org/wiki/Kfz-Kennzeichen_(Tschechien)">License plates of the
 * Czech Republic</a></p>
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
class CzechLicensePlateHandler implements LicensePlateHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CzechLicensePlateHandler.class);

    private static final int NUMBER_OF_DIGITS = 4;

    /**
     * <p>Normalizes the given {@link LicensePlate} value by upper casing it and using whitespace as separator. If the
     * value represents a license plate that has been valid between 1960 and 2001, the sequence of numbers is separated
     * by a hyphen.</p>
     *
     * @param  value  to get the normalized value for, never {@code null}
     *
     * @return  the normalized value, never {@code null}
     */
    @Override
    public String normalize(String value) {

        String trimmedValue = value.replaceAll("\\s+", "").replaceAll("\\-+", "").toUpperCase();
        String normalizedValue = trimmedValue;

        if (trimmedValue.length() > NUMBER_OF_DIGITS) {
            int numbersIndex = trimmedValue.length() - NUMBER_OF_DIGITS;

            String numbers = trimmedValue.substring(numbersIndex, trimmedValue.length());
            String letters = trimmedValue.substring(0, numbersIndex);

            // if the last four characters are digits, consider that it is a currently or formerly valid license plate
            if (numbers.matches("[0-9]{4}")) {
                normalizedValue = letters + " " + numbers;
            }

            // if the first two or three characters are letters, consider that it is a formerly valid license plate
            if (letters.matches("[A-Z]{2,3}")) {
                int numbersLength = numbers.length();
                normalizedValue = letters + " " + numbers.substring(0, numbersLength / 2) + "-"
                    + numbers.substring(numbersLength / 2, numbersLength);
            }
        }

        LOG.debug("Normalized '{}' to '{}'", value, normalizedValue);

        return normalizedValue;
    }


    /**
     * Validates the given {@link LicensePlate} value.
     *
     * <p>A Czech license plate consists of two groups separated by a whitespace:</p>
     *
     * <ul>
     * <li>a combination of letters and digits: one digit followed by one letter representing the district and then one
     * digit or letter</li>
     * <li>four digits</li>
     * </ul>
     *
     * <p>Structure: 9X9 9999, 9XX 9999</p>
     *
     * <p>A custom license plate consists of eight characters (letters or digits).</p>
     *
     * <p>License plates between 1960 and 2001 had a different format and are still in circulation:</p>
     *
     * <ul>
     * <li>two or three letters - the two first letters represent the district</li>
     * <li>two by two digits separated by a hyphen</li>
     * </ul>
     *
     * <p>Structure: XX 99-99 or XXX 99-99</p>
     *
     * <p>There are certain license plates that deviate from the rules above, for example diplomatic license plates.</p>
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

        String currentlyValidFormat = "[0-9][A-Z][A-Z0-9]\\s[0-9]{4}";
        String formerlyValidFormat = "[A-Z]{2,3}\\s[0-9]{2}-[0-9]{2}";
        String customFormat = "[A-Z0-9]{8}";

        return normalizedValue.matches(currentlyValidFormat) || normalizedValue.matches(formerlyValidFormat)
            || normalizedValue.matches(customFormat);
    }
}
