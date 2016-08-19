package net.contargo.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Can handle German {@link LicensePlate}s.
 *
 * <p>Examples of German license plates:</p>
 *
 * <ul>
 * <li>KA AB 123</li>
 * <li>B XY 492</li>
 * <li>GER U 181</li>
 * <li>LÖ U 1048</li>
 * </ul>
 *
 * <p>Further information: <a href="https://de.wikipedia.org/wiki/Kfz-Kennzeichen_(Deutschland)#Aufbau">License plates
 * of Germany</a></p>
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
class GermanLicensePlateHandler implements LicensePlateHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GermanLicensePlateHandler.class);

    /**
     * Normalizes the given {@link LicensePlate} by executing the following steps:
     *
     * <p>1.) Upper case: "ka ab123" to "KA AB123"</p>
     *
     * <p>2.) Use minus instead of whitespace separator: "KA AB123" to "KA-AB123"</p>
     *
     * <p>3.) Ensure identification numbers are separated from identification letters: "KA-AB123" to "KA--AB-123"</p>
     *
     * <p>4.) Remove the duplicated minus separators: "KA--AB-123" to "KA-AB-123"</p>
     *
     * @param  licensePlate  to get the normalized value for, never {@code null}
     *
     * @return  the normalized value, never {@code null}
     */
    @Override
    public String normalize(LicensePlate licensePlate) {

        String value = licensePlate.getValue();
        String normalizedValue = value.toUpperCase()
                .replaceAll("\\s+", "-")
                .replaceAll("(?<=\\D)(?=\\d)", "-")
                .replaceAll("\\-+", "-");

        LOG.debug("Normalized '{}' to '{}'", value, normalizedValue);

        return normalizedValue;
    }


    /**
     * Validates the given {@link LicensePlate}.
     *
     * <p>A German license plate consists of maximum 8 characters and contains two parts:</p>
     *
     * <ul>
     * <li>the geographic identifier: one, two or three letters (may contain umlauts)</li>
     * <li>the identification numbers and/or letters: one or two letters (no umlauts) and one to four digits (without
     * leading zero)</li>
     * </ul>
     *
     * <p>After the geographic identifier there are the round vehicle safety test and registration seal stickers placed
     * above each other.</p>
     *
     * <p>There are certain license plates that may deviate from the rules above:</p>
     *
     * <ul>
     * <li>Seasonal license plates consists of only maximum 7 characters.</li>
     * <li>License plates of official cars can have up to six digits because of missing identification letters.</li>
     * <li>Classic cars can get an H (historic) at the end of the plate, example: ER A 55H</li>
     * </ul>
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

        return normalizedValue.matches("^[A-ZÄÖÜ]{1,3}\\-[A-Z]{0,2}\\-{0,1}[1-9]{1}[0-9]{0,3}");
    }


    /**
     * Formats the given {@link LicensePlate} by replacing "-" by whitespaces of the normalized value.
     *
     * @param  licensePlate  to get the formatted value for, never {@code null}
     *
     * @return  the formatted value, never {@code null}
     */
    @Override
    public String format(LicensePlate licensePlate) {

        String normalizedValue = normalize(licensePlate);
        String formattedValue = normalizedValue.replaceAll("-", " ");

        LOG.debug("Formatted '{}' to '{}'", normalizedValue, formattedValue);

        return formattedValue;
    }
}
