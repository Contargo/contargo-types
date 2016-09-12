package net.contargo.types.truck;

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

    private static final int MAXIMUM_NUMBER_OF_CHARACTERS = 8;

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
     * <p>A Dutch license plate consists of three groups that are separated by a hyphen. Each group has one to three
     * letters or digits.</p>
     *
     * <p>Structure: 999-XX-9, XXX-99-X, 99-XX-XX etc.</p>
     *
     * <p>There are certain license plates that may deviate from this rule, for example royal cars containing only two
     * groups.</p>
     *
     * <p>Note that these special cases are not covered by this validator!</p>
     *
     * @param  value  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(String value) {

        String normalizedValue = normalize(value);

        if (normalizedValue.length() > MAXIMUM_NUMBER_OF_CHARACTERS) {
            return false;
        }

        return normalizedValue.matches("[A-Z0-9]{1,3}\\-[A-Z0-9]{1,3}\\-[A-Z0-9]{1,3}");
    }
}
