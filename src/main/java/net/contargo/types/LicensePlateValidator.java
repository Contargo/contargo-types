package net.contargo.types;

/**
 * Checks if a {@link LicensePlate} is valid.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
interface LicensePlateValidator {

    /**
     * Validates the given {@link LicensePlate}.
     *
     * @param  licensePlate  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    boolean isValid(LicensePlate licensePlate);
}
