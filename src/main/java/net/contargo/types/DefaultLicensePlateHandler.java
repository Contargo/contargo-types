package net.contargo.types;

/**
 * Default implementation for {@link LicensePlateHandler}s.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
class DefaultLicensePlateHandler implements LicensePlateHandler {

    /**
     * Validates the given {@link LicensePlate}.
     *
     * <p>The given {@link LicensePlate} is considered valid if it contains no special characters except '-'.</p>
     *
     * @param  licensePlate  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(LicensePlate licensePlate) {

        String formattedValue = format(licensePlate);

        // allowed: any letter or digit, but no special characters except '-' and ' '
        return formattedValue.matches("[\\p{L}0-9\\- ]*");
    }


    /**
     * Formats the given {@link LicensePlate} in a very simple way: just upper case it.
     *
     * @param  licensePlate  to get the formatted value for, never {@code null}
     *
     * @return  the formatted value, never {@code null}
     */
    @Override
    public String format(LicensePlate licensePlate) {

        String value = licensePlate.getValue();

        return value.toUpperCase();
    }
}
