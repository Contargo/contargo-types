package net.contargo.types;

/**
 * Can handle Dutch {@link LicensePlate}s.
 *
 * <p>Examples of Dutch license plates:</p>
 *
 * <ul>
 * <li>01-GBB-1</li>
 * <li>XK-50-HF</li>
 * <li>01-BB-DB</li>
 * <li>9-KXX-99</li>
 * </ul>
 *
 * <p>Further information: <a href="https://de.wikipedia.org/wiki/Kfz-Kennzeichen_(Niederlande)#Aktuelles_System">
 * License plates of the Netherlands</a></p>
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
class DutchLicensePlateHandler implements LicensePlateHandler {

    /**
     * Normalizes the given {@link LicensePlate} by upper casing it and replacing all "-" by whitespace.
     *
     * @param  licensePlate  to get the normalized value of, never {@code null}
     *
     * @return  the normalized value, never {@code null}
     */
    @Override
    public String normalize(LicensePlate licensePlate) {

        String value = licensePlate.getValue();

        return value.replaceAll("\\s+", "-").replaceAll("\\-+", "-").toUpperCase();
    }


    /**
     * Validates the given {@link LicensePlate}.
     *
     * <p>A Dutch license plate consists of three groups of one to three letters or digits that are separated by a
     * hyphen.</p>
     *
     * <p>There are certain license plates that may deviate from this rule, for example royal cars containing only two
     * groups.</p>
     *
     * <p>Note that these special cases are not covered by this validator!</p>
     *
     * @param  licensePlate  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(LicensePlate licensePlate) {

        String normalizedValue = normalize(licensePlate);

        return normalizedValue.matches("[A-Z0-9]{1,3}\\-[A-Z0-9]{1,3}\\-[A-Z0-9]{1,3}");
    }


    /**
     * Formats the given {@link LicensePlate} in a very simple way: just return the normalized value.
     *
     * @param  licensePlate  to get the formatted value for, never {@code null}
     *
     * @return  the formatted value, never {@code null}
     */
    @Override
    public String format(LicensePlate licensePlate) {

        return normalize(licensePlate);
    }
}
