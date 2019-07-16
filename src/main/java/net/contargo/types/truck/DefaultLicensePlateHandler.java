package net.contargo.types.truck;

/**
 * Default implementation for {@link LicensePlateHandler}s.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
class DefaultLicensePlateHandler implements LicensePlateHandler {

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
     * <p>The given {@link LicensePlate} value is considered valid if it contains no special characters except hyphens
     * or whitespaces.</p>
     *
     * @param  value  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(String value) {

        // allowed: any letter or digit, but no special characters except '-' and ' '
        return normalize(value).matches("[\\p{L}0-9\\- ]{2,15}");
    }
}
