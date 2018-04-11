package net.contargo.types.truck;

import java.util.Arrays;
import java.util.List;


/**
 * Can handle Romanian {@link LicensePlate}s.
 *
 * <p>Examples of Romanian license plates:</p>
 *
 * <ul>
 *   <li>B 12 CTL</li>
 *   <li>B 567 DHG</li>
 *   <li>CT 12 BNG</li>
 * </ul>
 *
 * <p>Further information: <a href="https://de.wikipedia.org/wiki/Kfz-Kennzeichen_(Rumänien)#Aufbau">License plates of
 * Romania</a></p>
 *
 * @author  Slaven Travar - slaven.travar@pta.de
 * @since  0.9.0
 */
class RomanianLicensePlateHandler implements LicensePlateHandler {

    private static final String WHITESPACE = " ";

    /**
     * The counties of Romania.
     *
     * <p>For further information see the <a
     * href="https://en.wikipedia.org/wiki/Vehicle_registration_plates_of_Romania#County_codes">list of
     * counties</a></p>
     */
    private static final List<String> COUNTIES = Arrays.asList("AB", "AG", "AR", "B", "BC", "BH", "BN", "BR", "BT",
            "BV", "BZ", "CJ", "CL", "CS", "CT", "CV", "DB", "DJ", "GJ", "GL", "GR", "HD", "HR", "IF", "IL", "IS", "MH",
            "MM", "MS", "NT", "OT", "PH", "SB", "SJ", "SM", "SV", "TL", "TM", "TR", "VL", "VN", "VS");

    /**
     * Normalizes the given {@link LicensePlate} value by executing the following steps:
     *
     * <p>1.) Trim and upper case: " b-183dkg " to "B-183DKG"</p>
     *
     * <p>2.) Use whitespace as separator, remove hyphens: "B-183DKG" to "B 183DKG"</p>
     *
     * <p>3.) Ensure identification numbers are separated from identification letters: "B 183DKG" to "B 183 DKG"</p>
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
     * <p>A Romanian license plate consists of maximum 7 characters and contains three parts:</p>
     *
     * <ul>
     *   <li>the county code: one ore two letters</li>
     *   <li>the identification digit group: two or three digits if county code is B (Bucharest), two digits for all
     *     other county codes</li>
     *   <li>the identification letter group: three letters, never starting with "I" or "O"</li>
     * </ul>
     *
     * <p>Structure: AA 11 XXX or B 11 XXX or B 111 XXX</p>
     *
     * <p>There are certain license plates that may deviate from the rules above:</p>
     *
     * <ul>
     *   <li>Short-term temporary plates</li>
     *   <li>Long-term temporary plates</li>
     *   <li>Diplomatic plates</li>
     *   <li>Plates for special organizations</li>
     *   <li>Local administration plates</li>
     *   <li>Diplomatic plates</li>
     * </ul>
     *
     * <p>Note that these special cases are not covered by this validator!</p>
     *
     * <p>License plates issued before 1992 are also not covered!</p>
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

        if (!isCountyCode(parts[0])) {
            return false;
        }

        return isLetterGroupValid(parts[2]);
    }


    private static boolean isValidFormat(String normalizedValue) {

        return normalizedValue.matches("^[A-Z]{1}\\s\\d{2,3}\\s[A-Z]{3}$")
            || normalizedValue.matches("^[A-Z]{2}\\s\\d{2}\\s[A-Z]{3}$");
    }


    private static boolean isCountyCode(String countyCode) {

        return COUNTIES.contains(countyCode);
    }


    private static boolean isLetterGroupValid(String letterGroup) {

        return !letterGroup.startsWith("I") && !letterGroup.startsWith("O");
    }
}
