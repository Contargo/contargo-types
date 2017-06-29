package net.contargo.types.truck;

/**
 * Can handle unknown country {@link LicensePlate}s.
 *
 * <p>Any license plate is valid.</p>
 *
 * @author  Stefan Wagner - wagner@synyx.de*
 * @author  Christian Mennerich - mennerich@synyx.de
 * @since  0.6.0
 */
class UnknownCountryLicensePlateHandler implements LicensePlateHandler {

    private static final int MAXIMUM_NUMBER_OF_CHARACTERS = 64;

    /**
     * Normalizes the given {@link LicensePlate} value by trimming and upper casing value.
     *
     * @param  value  to get the normalized value for, never {@code null}
     *
     * @return  the normalized value, never {@code null}
     */
    @Override
    public String normalize(String value) {

        return LicensePlateHandler.trim(value);
    }


    /**
     * Validates the given {@link LicensePlate} value.
     *
     * <p>An unknown license plate consists of maximum 64 characters.</p>
     *
     * @param  value  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(String value) {

        String normalizedValue = normalize(value);

        return normalizedValue.length() <= MAXIMUM_NUMBER_OF_CHARACTERS;
    }
}
