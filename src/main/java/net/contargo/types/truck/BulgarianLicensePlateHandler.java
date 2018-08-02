package net.contargo.types.truck;

import java.util.Arrays;
import java.util.List;


/**
 * Can handle Bulgarian {@link LicensePlate}s.
 *
 * <p>Examples of Bulgarian license plates:</p>
 *
 * <ul>
 *   <li>A-1234-BB</li>
 *   <li>AA-1234-BB</li>
 * </ul>
 *
 * <p>Further information: <a href="https://en.wikipedia.org/wiki/Vehicle_registration_plates_of_Bulgaria#Format">
 * Vehicle registration plates of Bulgaria</a></p>
 *
 * @author  Marius van Herpen - mvanherpen@contargo.net
 * @since  0.11.5
 */
class BulgarianLicensePlateHandler implements LicensePlateHandler {

    private static final String WHITESPACE = " ";

    /**
     * The provinces of Bulgaria.
     *
     * <p>For further information see the <a
     * href="https://en.wikipedia.org/wiki/Vehicle_registration_plates_of_Bulgaria#Provincial_codes">list of
     * provinces</a></p>
     */
    private static final List<String> PROVINCES = Arrays.asList("A", "B", "BH", "BP", "BT", "E", "EB", "EH", "K", "KH",
            "M", "H", "OB", "P", "PA", "PB", "PK", "PP", "C", "CA", "CB", "CH", "CM", "CO", "CC", "CT", "T", "TX", "Y",
            "X");

    private static final List<String> ACCEPTED_LETTERS = Arrays.asList("A", "B", "M", "H", "P", "C", "T", "Y", "X");

    /**
     * Normalizes the given {@link LicensePlate} value by upper casing it and replacing all hyphens by whitespaces, and
     * ensure numbers are seperated from letters.
     *
     * @param  value  to get the normalized value for, never {@code null}
     *
     * @return  the normalized value, never {@code null}
     */
    @Override
    public String normalize(String value) {

        return LicensePlateHandler.trim(value)
            .replaceAll("\\-", WHITESPACE)
            .replaceAll("(?<=\\d)(?=\\p{L})|(?<=\\p{L})(?=\\d)", WHITESPACE);
    }


    /**
     * Validates the given {@link LicensePlate} value.
     *
     * <p>A Bulgarian license plate consists of a combination of seven or eight digits and letters. It has three groups
     * that are separated by a whitespace:</p>
     *
     * <ul>
     *   <li>one leading letter</li>
     *   <li>four digits</li>
     *   <li>two letters</li>
     * </ul>
     *
     * <p>or</p>
     *
     * <ul>
     *   <li>two leading letters</li>
     *   <li>four digits</li>
     *   <li>two letters</li>
     * </ul>
     *
     * <p>Structure: A-1234-AA, AA-1234-AA</p>
     *
     * <p>There are certain license plates that may deviate from this rule, for example military of police license
     * plates.</p>
     *
     * <p>Note that these special cases are not covered by this validator!</p>
     *
     * <p>The first group depicts the Province where the licens plate is issued, and is validated against the known
     * list.</p>
     *
     * <p>The third group can only consist of certain letters, which are validated.</p>
     *
     * @param  value  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(String value) {

        String normalizedValue = normalize(value);

        if (!isValidFormat(normalizedValue)) {
            return false;
        }

        String[] parts = normalizedValue.split(WHITESPACE);

        if (!isLetterGroupValid(parts[2])) {
            return false;
        }

        return isProvinceCode(parts[0]);
    }


    private static boolean isValidFormat(String normalizedValue) {

        // A-1234-AA
        return normalizedValue.matches("[A-Z]\\s[0-9]{4}\\s[A-Z]{2}")
            // AA-1234-AA
            || normalizedValue.matches("[A-Z]{2}\\s[0-9]{4}\\s[A-Z]{2}");
    }


    private static boolean isProvinceCode(String countyCode) {

        return PROVINCES.contains(countyCode);
    }


    private boolean isLetterGroupValid(String letterGroup) {

        return ACCEPTED_LETTERS.contains(letterGroup.substring(0, 1))
            && ACCEPTED_LETTERS.contains(letterGroup.substring(1, 2));
    }
}
