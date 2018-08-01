package net.contargo.types.truck;

/**
 * Can handle Bulgarian {@link LicensePlate}s.
 *
 * <p>Examples of Bulgarian license plates:</p>
 *
 * <ul>
 *   <li>A-1234-BB</li>
 *   <li>AAQ-1234-BB</li>
 * </ul>
 *
 * <p>Further information: <a href="https://en.wikipedia.org/wiki/Vehicle_registration_plates_of_Bulgaria#Format">
 * Vehicle registration plates of Bulgaria</a></p>
 *
 * @author  Marius van Herpen - mvanherpen@contargo.net
 * @since  0.11.5
 */
class BulgarianLicensePlateHandler implements LicensePlateHandler {

    /**
     * Normalizes the given {@link LicensePlate} value by upper casing it and replacing all whitespaces by hyphens.
     *
     * @param  value  to get the normalized value for, never {@code null}
     *
     * @return  the normalized value, never {@code null}
     */
    @Override
    public String normalize(String value) {

        return LicensePlateHandler.trim(value).replaceAll("\\s", "-");
    }


    /**
     * Validates the given {@link LicensePlate} value.
     *
     * <p>A Bulgarian license plate consists of a combination of seven or eight digits and letters. It has three groups
     * that are separated by a hyphen:</p>
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
     * <p>Note that these special cases are not covered by this validator! Also this validator considers only the
     * license plate format in general, but not letter combinations in detail.</p>
     *
     * @param  value  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(String value) {

        String normalizedValue = normalize(value);

        // A-1234-AA
        boolean leadingLetter = normalizedValue.matches("[A-Z]\\-[1-9]{4}\\-[A-Z]{2}");

        // AA-1234-AA
        boolean leadingLetters = normalizedValue.matches("[A-Z]{2}\\-[1-9]{4}\\-[A-Z]{2}");

        return leadingLetter || leadingLetters;
    }
}
