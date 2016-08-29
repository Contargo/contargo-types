package net.contargo.types.truck;

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
 * @since  0.2.0
 */
class FrenchLicensePlateHandler implements LicensePlateHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FrenchLicensePlateHandler.class);

    /**
     * Normalizes the given {@link LicensePlate} value by upper casing it and replacing all whitespaces by hyphens. If
     * the value represents a license plate that has been valid between 1950 and 2009, whitespaces instead of hyphens
     * are used.
     *
     * @param  value  to get the normalized value for, never {@code null}
     *
     * @return  the normalized value, never {@code null}
     */
    @Override
    public String normalize(String value) {

        String normalizedValue = LicensePlateHandler.trim(value).replaceAll("\\s", "-");

        if (hasFormerlyValidFormat(normalizedValue)) {
            normalizedValue = normalizedValue.replaceAll("\\-", " ");
        }

        LOG.debug("Normalized '{}' to '{}'", value, normalizedValue);

        return normalizedValue;
    }


    /**
     * Validates the given {@link LicensePlate} value.
     *
     * <p>A French license plate consists of three groups that are separated by a hyphen:</p>
     *
     * <ul>
     * <li>two letters</li>
     * <li>three digits</li>
     * <li>two letters</li>
     * </ul>
     *
     * <p>Structure: XX-999-XX</p>
     *
     * <p>There are certain license plates that may deviate from this rule, for example license plates of military
     * vehicles or moped license plates.</p>
     *
     * <p>Note that these special cases are not covered by this validator!</p>
     *
     * <p>License plates between 1950 and 2009 had a different format and are still in circulation. They consist of
     * three groups that are separated by a whitespace:</p>
     *
     * <ul>
     * <li>up to four digits, minimum two digits</li>
     * <li>up to three letters</li>
     * <li>the department code: up to three digits (usually two digits) or a mix of letters and digits</li>
     * </ul>
     *
     * <p>Structure: 9999 XX 99, 999 XXX 99, 99 XXX 99, 999 XXX 999, 99 XX 9X</p>
     *
     * @param  value  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(String value) {

        String normalizedValue = normalize(value);

        return hasCurrentlyValidFormat(normalizedValue) || hasFormerlyValidFormat(normalizedValue);
    }


    private static boolean hasCurrentlyValidFormat(String normalizedValue) {

        return normalizedValue.matches("[A-Z]{2}\\-[0-9]{3}\\-[A-Z]{2}");
    }


    private static boolean hasFormerlyValidFormat(String normalizedValue) {

        return normalizedValue.matches("[0-9]{2,4}[\\s\\-][A-Z]{1,3}[[\\s\\-]][A-Z0-9]{2,3}");
    }
}
