package net.contargo.types.truck;

/**
 * Can handle Lithuanian {@link LicensePlate}s.
 *
 * <p>Examples of Lithuanian license plates:</p>
 *
 * <ul>
 *   <li>KRK 365</li>
 *   <li>DFZ 289</li>
 * </ul>
 *
 * <p>For more more information: <a href="https://en.wikipedia.org/wiki/Vehicle_registration_plates_of_Lithuania" ></a>
 * </p>
 *
 * <p>Lithiuania also allows so called vanity plates. Those are not correctly validated, as they don't follow the
 * structure of a normal license plate.</p>
 *
 * @author  Marius van Herpen - mvanherpen@contargo.net
 */
public class LithuanianLicensePlateHandler implements LicensePlateHandler {

    private static final String WHITESPACE = " ";

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
     * <p>A Lithuan license plate consists of a group of 3 letters and 3 digits. There is also the possibility of a
     * vanity plate, which allow any combination of letters and digits, and has no minimum length. Those will not be
     * validated.</p>
     *
     * @param  value  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(String value) {

        // KRK 565
        return normalize(value).matches("[A-Z]{3}\\s[0-9]{3}");
    }
}
