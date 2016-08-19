package net.contargo.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default implementation for {@link LicensePlateHandler}s.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
class DefaultLicensePlateHandler implements LicensePlateHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultLicensePlateHandler.class);

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
        String normalizedValue = value.replaceAll("\\s+", "-").replaceAll("\\-+", "-").toUpperCase();

        LOG.debug("Normalized '{}' to '{}'", value, normalizedValue);

        return normalizedValue;
    }


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

        // allowed: any letter or digit, but no special characters except '-' and ' '
        return normalize(licensePlate).matches("[\\p{L}0-9\\- ]*");
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

        String normalizedValue = normalize(licensePlate);

        LOG.debug("Formatted '{}' to '{}'", normalizedValue, normalizedValue);

        return normalizedValue;
    }
}
