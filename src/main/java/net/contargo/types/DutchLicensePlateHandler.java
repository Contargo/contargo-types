package net.contargo.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    private static final Logger LOG = LoggerFactory.getLogger(DutchLicensePlateHandler.class);

    /**
     * Normalizes the given {@link LicensePlate} value by upper casing it and replacing all "-" by whitespace.
     *
     * @param  value  to get the normalized value of, never {@code null}
     *
     * @return  the normalized value, never {@code null}
     */
    @Override
    public String normalize(String value) {

        String normalizedValue = value.replaceAll("\\s+", "-").replaceAll("\\-+", "-").toUpperCase();

        LOG.debug("Normalized '{}' to '{}'", value, normalizedValue);

        return normalizedValue;
    }


    /**
     * Validates the given {@link LicensePlate} value.
     *
     * <p>A Dutch license plate consists of three groups of one to three letters or digits that are separated by a
     * hyphen.</p>
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

        return normalizedValue.matches("[A-Z0-9]{1,3}\\-[A-Z0-9]{1,3}\\-[A-Z0-9]{1,3}");
    }


    /**
     * Formats the given {@link LicensePlate} value in a very simple way: just return the normalized value.
     *
     * @param  value  to get the formatted value for, never {@code null}
     *
     * @return  the formatted value, never {@code null}
     */
    @Override
    public String format(String value) {

        String normalizedValue = normalize(value);

        LOG.debug("Formatted '{}' to '{}'", normalizedValue, normalizedValue);

        return normalizedValue;
    }
}
