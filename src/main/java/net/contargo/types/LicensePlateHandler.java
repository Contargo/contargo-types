package net.contargo.types;

/**
 * Provides a set of functionalities for {@link LicensePlate} values depending on a {@link Country}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
interface LicensePlateHandler {

    /**
     * Get the normalized value of the given {@link LicensePlate}.
     *
     * @param  value  to get the normalized value of, never {@code null}
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
     * Get the formatted value of the given {@link LicensePlate}.
     *
     * @param  value  to get the formatted value of, never {@code null}
     *
     * @return  the formatted license plate, never {@code null}
     */
    String format(String value);
}
