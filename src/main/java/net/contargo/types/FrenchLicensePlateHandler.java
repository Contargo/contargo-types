package net.contargo.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Can handle French {@link LicensePlate}s.
 *
 * <p>Examples of French license plates since 2009:</p>
 *
 * <ul>
 * <li>AA-001-AB</li>
 * <li>AB-123-BC</li>
 * </ul>
 *
 * <p>Further information: <a href="https://de.wikipedia.org/wiki/Kfz-Kennzeichen_(Frankreich)">License plates of
 * France</a></p>
 *
 * <p>Examples of French license plates between 1950 and 2009:</p>
 *
 * <ul>
 * <li>2928 TW 74</li>
 * <li>324 EBS 91</li>
 * <li>56 ABM 13</li>
 * <li>11 GY 2A</li>
 * <li>654 ANY 971</li>
 * </ul>
 *
 * <p>Further information: <a href="https://de.wikipedia.org/wiki/Kfz-Kennzeichen_(Frankreich)_(1950%E2%80%932009)">
 * License plates of France between 1950 and 2009</a></p>
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
class FrenchLicensePlateHandler implements LicensePlateHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FrenchLicensePlateHandler.class);

    /**
     * Normalizes the given {@link LicensePlate} value by upper casing it and replacing all whitespaces by hyphens.
     *
     * @param  value  to get the normalized value for, never {@code null}
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
     * <p>A French license plate consists of two letters, three digits and two letters - these three groups are
     * separated by a hyphen.</p>
     *
     * <p>Structure: XX-999-XX</p>
     *
     * <p>There are certain license plates that may deviate from this rule, for example license plates of military
     * vehicles or moped license plates.</p>
     *
     * <p>Note that these special cases are not covered by this validator!</p>
     *
     * <p>License plates between 1950 and 2009 had a different format and are still in circulation. They consist of up
     * to four digits, up to three letters and up to three digits - these three groups are separated by a hyphen.</p>
     *
     * <p>Structure: 9999 XX 99, 999 XXX 99, 99 XXX 99, 999 XXX 999</p>
     *
     * @param  value  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(String value) {

        return normalize(value).matches("[A-Z]{2}-[0-9]{3}-[A-Z]{2}");
    }
}
