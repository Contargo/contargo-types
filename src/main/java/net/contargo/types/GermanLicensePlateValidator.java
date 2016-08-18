package net.contargo.types;

/**
 * Validates German {@link LicensePlate}s.
 *
 * <p>Example for a valid German license plate: KA PA 777</p>
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
class GermanLicensePlateValidator implements LicensePlateValidator {

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
     * <p>Further information: <a href="https://de.wikipedia.org/wiki/Kfz-Kennzeichen_(Deutschland)#Aufbau">License
     * plates of Germany</a></p>
     *
     * @param  licensePlate  to be validated, never {@code null}
     *
     * @return  {@code true} if the given {@link LicensePlate} is valid, else {@code false}
     */
    @Override
    public boolean isValid(LicensePlate licensePlate) {

        throw new UnsupportedOperationException();
    }
}
