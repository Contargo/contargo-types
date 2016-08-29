package net.contargo.types.truck;

/**
 * Can handle Polish {@link LicensePlate}s.
 *
 * <p>Examples of Polish license plates:</p>
 *
 * <ul>
 * <li>ERA 75TM</li>
 * <li>XY 12345</li>
 * <li>XY 1234J</li>
 * <li>XYZ 12JK</li>
 * <li>XYZ JK34</li>
 * <li>F0 MAREK</li>
 * <li>B4 BMW99</li>
 * <li>X1 JKLMN</li>
 * </ul>
 *
 * <p>Further information: <a href="https://de.wikipedia.org/wiki/Kfz-Kennzeichen_(Polen)">License plates of Poland
 * </a></p>
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
class PolishLicensePlateHandler implements LicensePlateHandler {

    /**
     * Normalizes the given {@link LicensePlate} value by upper casing it and replacing hyphens by whitespaces.
     *
     * @param  value  to get the normalized value for, never {@code null}
     *
     * @return  the normalized value, never {@code null}
     */
    @Override
    public String normalize(String value) {

        return LicensePlateHandler.trim(value).replaceAll("\\-", " ");
    }


    /**
     * Validates the given {@link LicensePlate} value.
     *
     * <p>A Polish license plate consists of two groups separated by a whitespace:</p>
     *
     * <ul>
     * <li>the geographic identifier: two or three letter code, with the first letter denoting the powiat's
     * voivodeship</li>
     * <li>the identification number: four or five letters and digits</li>
     * </ul>
     *
     * <p>Structure: XX 99999, XX XXX99, XXX XXXX9</p>
     *
     * <p>Custom plates have a different format, they consist of two groups separated by a whitespace:</p>
     *
     * <ul>
     * <li>the geographic identifier: letter denoting voivodeship and a single digit</li>
     * <li>the identification number: three to five letters and digits, maximum the last two characters can be
     * digits</li>
     * </ul>
     *
     * <p>There are certain other types of license plates that may deviate from the rules above, for example diplomatic
     * license plates. Note that these special cases are not covered by this validator!</p>
     *
     * @param  value  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(String value) {

        String licensePlateFormat = "[A-Z]{2,3}\\s[A-Z0-9]{4,5}";
        String customLicensePlateFormat = "[A-Z][0-9]\\s[A-Z]{1,3}[A-Z0-9]{2}";

        String normalizedValue = normalize(value);

        return normalizedValue.matches(licensePlateFormat) || normalizedValue.matches(customLicensePlateFormat);
    }
}
