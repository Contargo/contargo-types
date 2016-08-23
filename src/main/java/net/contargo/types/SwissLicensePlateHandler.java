package net.contargo.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;


/**
 * Can handle Swiss {@link LicensePlate}s.
 *
 * <p>Examples of Swiss license plates:</p>
 *
 * <ul>
 * <li>FR 24539</li>
 * <li>SZ 65726</li>
 * <li>ZH 445789</li>
 * <li>GR 123</li>
 * </ul>
 *
 * <p>Further information: <a href="https://de.wikipedia.org/wiki/Kontrollschild_(Schweiz)">License plates of
 * Switzerland</a></p>
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
class SwissLicensePlateHandler implements LicensePlateHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SwissLicensePlateHandler.class);

    /**
     * The cantons of Switzerland.
     *
     * <p>For further information see the <a
     * href="https://de.wikipedia.org/wiki/Kanton_(Schweiz)#Liste_der_Schweizer_Kantone_mit_ihren_Eckdaten">list of
     * cantons</a></p>
     */
    private static final List<String> CANTONS = Arrays.asList("AG", "AR", "AI", "BL", "BS", "BE", "FR", "GE", "GL",
            "GR", "JU", "LU", "NE", "NW", "OW", "SH", "SZ", "SO", "SG", "TI", "TG", "UR", "VD", "VS", "ZG", "ZH");

    /**
     * Normalizes the given {@link LicensePlate} value by upper casing it and removing separators.
     *
     * @param  value  to get the normalized value for, never {@code null}
     *
     * @return  the normalized value, never {@code null}
     */
    @Override
    public String normalize(String value) {

        // remove whitespaces and hyphens
        String normalizedValue = value.replaceAll("\\s+", "").replaceAll("\\-+", "").toUpperCase();

        LOG.debug("Normalized '{}' to '{}'", value, normalizedValue);

        return normalizedValue;
    }


    /**
     * Validates the given {@link LicensePlate} value.
     *
     * <p>A Swiss license plate consists of a two letter code for the canton followed by up to six numerical digits.</p>
     *
     * <p>Structure: XX 123456</p>
     *
     * <p>There are license plates that can have one or two letters after the number. These license plates are for
     * special uses, e.g. dealer license plate. Note that these special cases are not covered by this validator! Also
     * this validator does not consider license plates for official use, such as for military vehicles.</p>
     *
     * @param  value  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean validate(String value) {

        String normalizedValue = normalize(value);

        if (!hasValidFormat(normalizedValue)) {
            return false;
        }

        String cantonCode = normalizedValue.substring(0, 2);

        return CANTONS.contains(cantonCode);
    }


    private boolean hasValidFormat(String normalizedValue) {

        return normalizedValue.matches("[A-Z]{2}[0-9]{1,6}");
    }


    /**
     * Formats the given {@link LicensePlate} value: separate canton code from number.
     *
     * @param  value  to get the formatted value for, never {@code null}
     *
     * @return  the formatted value, never {@code null}
     */
    @Override
    public String format(String value) {

        String normalizedValue = normalize(value);

        if (!hasValidFormat(normalizedValue)) {
            return value;
        }

        String formattedValue = normalizedValue.substring(0, 2) + " "
            + normalizedValue.substring(2, normalizedValue.length());

        LOG.debug("Formatted '{}' to '{}'", normalizedValue, formattedValue);

        return formattedValue;
    }
}
