package net.contargo.types.truck;

import org.junit.Before;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class CzechLicensePlateHandlerTest extends AbstractLicensePlateHandlerTest {

    @Before
    public void setUp() {

        handler = new CzechLicensePlateHandler();
    }


    // NORMALIZING -----------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("1so 3690", "1SO 3690");
        assertIsNormalizedFromTo.accept("1so  3690", "1SO 3690");
        assertIsNormalizedFromTo.accept("5a6-3240", "5A6 3240");
        assertIsNormalizedFromTo.accept("5a6--3240", "5A6 3240");
        assertIsNormalizedFromTo.accept("5a63240", "5A6 3240");
        assertIsNormalizedFromTo.accept("1so 36 90", "1SO 3690");
    }


    @Test
    public void ensureOldLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("ala 40 11", "ALA 40-11");
        assertIsNormalizedFromTo.accept("ala  40--11", "ALA 40-11");
        assertIsNormalizedFromTo.accept("akx-1377", "AKX 13-77");
        assertIsNormalizedFromTo.accept("akx1377", "AKX 13-77");
        assertIsNormalizedFromTo.accept("tp-65-51", "TP 65-51");
    }


    @Test
    public void ensureCustomLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("trabant1", "TRABANT1");
    }


    @Test
    public void ensureInvalidLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("foo", "FOO");
        assertIsNormalizedFromTo.accept("a", "A");
        assertIsNormalizedFromTo.accept("1234", "1234");
    }


    // VALIDATION FOR LICENSE PLATES SINCE 2001 ------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateWithTwoGroupsIsValid() {

        assertIsValid.accept("1AA 4303");
        assertIsValid.accept("5A6 3240");
    }


    @Test
    public void ensureLicensePlateWithoutDistrictCodeAtTheSecondPositionIsNotValid() {

        assertIsNotValid.accept("11A 4303");
        assertIsNotValid.accept("111 4303");
    }


    @Test
    public void ensureLicensePlateWithLetterAtTheFirstPositionIsNotValid() {

        assertIsNotValid.accept("AA1 4303");
    }


    @Test
    public void ensureLicensePlateWithMoreThanThreeCharactersInTheFirstGroupIsNotValid() {

        assertIsNotValid.accept("1AAB 4303");
        assertIsNotValid.accept("1AA5 4303");
    }


    @Test
    public void ensureLicensePlateWithMoreOrLessThanFourDigitsIsNotValid() {

        assertIsNotValid.accept("1AA 123");
        assertIsNotValid.accept("1AA 12345");
    }


    @Test
    public void ensureLicensePlateWithLettersInTheSecondGroupIsNotValid() {

        assertIsNotValid.accept("1AA A234");
        assertIsNotValid.accept("1AA 1B34");
        assertIsNotValid.accept("1AA 12C3");
        assertIsNotValid.accept("1AA 123D");
    }


    @Test
    public void ensureCustomLicensePlateWithEightCharactersIsValid() {

        assertIsValid.accept("TRABANT1");
        assertIsValid.accept("ABCDEFGH");
    }


    @Test
    public void ensureCustomLicensePlateWithMoreOrLessThanEightCharactersIsNotValid() {

        assertIsNotValid.accept("TRABANT");
        assertIsNotValid.accept("ABCDEFGHI");
    }


    // VALIDATION FOR LICENSE PLATES BETWEEN 1960 AND 2001 -------------------------------------------------------------

    @Test
    public void ensureOldLicensePlateWithThreeLettersInTheFirstGroupIsValid() {

        assertIsValid.accept("AKX 13-77");
    }


    @Test
    public void ensureOldLicensePlateWithTwoLettersInTheFirstGroupIsValid() {

        assertIsValid.accept("TP 65-51");
    }


    @Test
    public void ensureOldLicensePlateWithMixOfLettersAndDigitsInTheFirstIsNotValid() {

        assertIsNotValid.accept("T1 65-51");
    }


    @Test
    public void ensureOldLicensePlateWithMoreOrLessThanFourDigitsIsNotValid() {

        assertIsNotValid.accept("AKX 1-77");
        assertIsNotValid.accept("AKX 13-778");
    }


    @Test
    public void ensureOldLicensePlateWithLettersInTheSecondGroupIsNotValid() {

        assertIsNotValid.accept("AKX A2-34");
        assertIsNotValid.accept("AKX 1B-34");
        assertIsNotValid.accept("TP 12-C3");
        assertIsNotValid.accept("TP 12-3D");
    }
}
