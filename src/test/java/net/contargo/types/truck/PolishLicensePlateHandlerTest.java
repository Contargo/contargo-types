package net.contargo.types.truck;

import org.junit.Before;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class PolishLicensePlateHandlerTest extends AbstractLicensePlateHandlerTest {

    @Before
    public void setUp() {

        handler = new PolishLicensePlateHandler();
    }


    // NORMALIZING -----------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("era 75tm", "ERA 75TM");
        assertIsNormalizedFromTo.accept(" era 75tm", "ERA 75TM");
        assertIsNormalizedFromTo.accept(" era 75tm  ", "ERA 75TM");
        assertIsNormalizedFromTo.accept("era  75tm", "ERA 75TM");
        assertIsNormalizedFromTo.accept("era-75tm", "ERA 75TM");
        assertIsNormalizedFromTo.accept("era--75tm", "ERA 75TM");
        assertIsNormalizedFromTo.accept("era75tm", "ERA75TM");
    }


    // VALIDATION ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateWithTwoLetterCodeIsValid() {

        assertIsValid.accept("DB 75TM");
        assertIsValid.accept("XY 1234J");
        assertIsValid.accept("XY 12345");
    }


    @Test
    public void ensureLicensePlateWithThreeLetterCodeIsValid() {

        assertIsValid.accept("ERA 75TM");
        assertIsValid.accept("RBI A168");
        assertIsValid.accept("XYZ 12345");
    }


    @Test
    public void ensureLicensePlateWithOneLetterCodeIsNotValid() {

        assertIsNotValid.accept("E 75TM");
        assertIsNotValid.accept("E 1234J");
    }


    @Test
    public void ensureLicensePlateWithMoreThanThreeLetterCodeIsNotValid() {

        assertIsNotValid.accept("ERAB 75TM");
        assertIsNotValid.accept("WXYZ 12345");
    }


    @Test
    public void ensureLicensePlateWithFourCharactersAsIdentificationNumberIsValid() {

        assertIsValid.accept("DB 75TM");
        assertIsValid.accept("XY 1234");

        assertIsValid.accept("ERA 75TM");
        assertIsValid.accept("XYZ 1234");
    }


    @Test
    public void ensureLicensePlateWithFiveCharactersAsIdentificationNumberIsValid() {

        assertIsValid.accept("DB 75TMX");
        assertIsValid.accept("XY 12345");

        assertIsValid.accept("ERA 75TMX");
        assertIsValid.accept("XYZ 12345");
    }


    @Test
    public void ensureLicensePlateWithLessThanFourCharactersAsIdentificationNumberIsNotValid() {

        assertIsNotValid.accept("DB 75T");
        assertIsNotValid.accept("XY 123");

        assertIsNotValid.accept("ERA 75T");
        assertIsNotValid.accept("XYZ 123");
    }


    @Test
    public void ensureLicensePlateWithMoreThanFiveCharactersAsIdentificationNumberIsNotValid() {

        assertIsNotValid.accept("DB 75TMXY");
        assertIsNotValid.accept("XY 123456");

        assertIsNotValid.accept("ERA 75TMXY");
        assertIsNotValid.accept("XYZ 123456");
    }


    @Test
    public void ensureCustomLicensePlateIsValid() {

        assertIsValid.accept("F0 MAREK");
        assertIsValid.accept("B4 BMW99");
        assertIsValid.accept("K6 Z17");
        assertIsValid.accept("D8 JA3");
        assertIsValid.accept("Z2 ONA8");
    }


    @Test
    public void ensureCustomLicensePlateWithOneCharacterAsGeographicIdentifierIsNotValid() {

        assertIsNotValid.accept("F MAREK");
        assertIsNotValid.accept("B BMW99");
        assertIsNotValid.accept("6 Z17");
    }


    @Test
    public void ensureCustomLicensePlateWithThreeCharactersAsGeographicIdentifierIsNotValid() {

        assertIsNotValid.accept("F01 MAREK");
        assertIsNotValid.accept("B41 BMW99");
    }


    @Test
    public void ensureCustomLicensePlateWithOnlyDigitsAsGeographicIdentifierIsNotValid() {

        assertIsNotValid.accept("01 MAREK");
        assertIsNotValid.accept("14 BMW99");
        assertIsNotValid.accept("36 Z17");
    }


    @Test
    public void ensureCustomLicensePlateWithDigitAsCharacterInGeographicIdentifierIsNotValid() {

        assertIsNotValid.accept("0F MAREK");
        assertIsNotValid.accept("4B BMW99");
        assertIsNotValid.accept("6K Z17");
    }


    @Test
    public void ensureCustomLicensePlateWithLessThanThreeCharactersAsIdentificationNumberIsNotValid() {

        assertIsNotValid.accept("F0 MA");
        assertIsNotValid.accept("B4 B");
        assertIsNotValid.accept("K6 17");
    }


    @Test
    public void ensureCustomLicensePlateWithMoreThanFiveCharactersAsIdentificationNumberIsNotValid() {

        assertIsNotValid.accept("F0 MAREKY");
        assertIsNotValid.accept("B4 BMWX98");
    }


    @Test
    public void ensureCustomLicensePlateWithDigitsNotAtTheLastTwoPositionsIsNotValid() {

        assertIsNotValid.accept("F0 1AREK");
        assertIsNotValid.accept("B4 99BMW");
    }


    @Test
    public void ensureCustomLicensePlateWithMoreThanTwoDigitsInTheIdentificationNumberIsNotValid() {

        assertIsNotValid.accept("F0 MA456");
        assertIsNotValid.accept("B4 B123");
    }
}
