package net.contargo.types.truck;

/**
 * Provides a set of functionalities for {@link LicensePlate} values depending on a {@link Country}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
public interface LicensePlateHandler {

    /**
     * Get the normalized value of the given {@link LicensePlate}.
     *
     * @param  value  to get the normalized value for, never {@code null}
     *
     * @return  the normalized license plate, never {@code null}
     */
    String normalize(String value);


    /**
     * Validates the given {@link LicensePlate}.
     *
     * @param  value  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    boolean validate(String value);


    /**
     * Remove all leading and trailing whitespaces, remove separator (whitespace and hyphen) duplications.
     *
     * @param  value  to be trimmed, never {@code null}
     *
     * @return  the trimmed value, never {@code null}
     */
    static String trim(String value) {

        return value.toUpperCase().trim().replaceAll("\\s+", " ").replaceAll("\\-+", "-");
    }
}
