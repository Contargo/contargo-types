package net.contargo.types;

import org.junit.Before;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class FrenchLicensePlateHandlerTest extends AbstractLicensePlateHandlerTest {

    @Before
    public void setUp() {

        handler = new FrenchLicensePlateHandler();
    }


    // NORMALIZING -----------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("aa 001 ab", "AA-001-AB");
        assertIsNormalizedFromTo.accept("aa  001-ab", "AA-001-AB");
        assertIsNormalizedFromTo.accept("aa--001-ab", "AA-001-AB");
    }


    @Test
    public void ensureOldLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("2928 tw 74", "2928 TW 74");
        assertIsNormalizedFromTo.accept("324  ebs-91", "324 EBS 91");
        assertIsNormalizedFromTo.accept("56-abm-13", "56 ABM 13");
    }


    // VALIDATION FOR LICENSE PLATES AFTER 2009 ------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateWithThreeGroupsIsValid() {

        assertIsValid.accept("AA-001-AB");
    }


    @Test
    public void ensureLicensePlateWithMoreOrLessThanThreeGroupsIsNotValid() {

        assertIsNotValid.accept("AA-001");
        assertIsNotValid.accept("AA-001-AB-CD");
    }


    @Test
    public void ensureLicensePlateWithMoreOrLessThanTwoLettersInTheFirstGroupIsNotValid() {

        assertIsNotValid.accept("A-001-AB");
        assertIsNotValid.accept("AAA-001-AB");
    }


    @Test
    public void ensureLicensePlateWithDigitsInTheFirstGroupIsNotValid() {

        assertIsNotValid.accept("11-001-AB");
        assertIsNotValid.accept("A1-001-AB");
    }


    @Test
    public void ensureLicensePlateWithMoreOrLessThanThreeDigitsInTheSecondGroupIsNotValid() {

        assertIsNotValid.accept("AA-01-AB");
        assertIsNotValid.accept("AA-0100-AB");
    }


    @Test
    public void ensureLicensePlateWithLettersInTheSecondGroupIsNotValid() {

        assertIsNotValid.accept("AA-XXX-AB");
        assertIsNotValid.accept("AA-0X1-AB");
    }


    @Test
    public void ensureLicensePlateWithMoreOrLessThanTwoLettersInTheLastGroupIsNotValid() {

        assertIsNotValid.accept("AA-001-A");
        assertIsNotValid.accept("AA-001-ABC");
    }


    @Test
    public void ensureLicensePlateWithDigitsInTheLastGroupIsNotValid() {

        assertIsNotValid.accept("AA-001-A1");
        assertIsNotValid.accept("AA-001-99");
    }


    // VALIDATION FOR LICENSE PLATES BETWEEN 1950 and 2009 -------------------------------------------------------------

    @Test
    public void ensureOldLicensePlateWithThreeGroupsIsValid() {

        assertIsValid.accept("2928 TW 74");
        assertIsValid.accept("324 EBS 91");
        assertIsValid.accept("56 ABM 13");
        assertIsValid.accept("654 ANY 971");
    }


    @Test
    public void ensureOldLicensePlateWithMoreOrLessThanThreeGroupsIsNotValid() {

        assertIsNotValid.accept("2928 TW");
        assertIsNotValid.accept("2928 TW 74 AB");
    }


    @Test
    public void ensureOldLicensePlateWithMoreThanFourDigitsInTheFirstGroupIsNotValid() {

        assertIsNotValid.accept("29281 TW 74");
    }


    @Test
    public void ensureOldLicensePlateWithLessThanTwoDigitsInTheFirstGroupIsNotValid() {

        assertIsNotValid.accept("2 TW 74");
    }


    @Test
    public void ensureOldLicensePlateWithLettersInTheFirstGroupIsNotValid() {

        assertIsNotValid.accept("2X28 TW 74");
        assertIsNotValid.accept("ABC EBS 91");
    }


    @Test
    public void ensureOldLicensePlateWithMoreThanThreeLettersInTheSecondGroupIsNotValid() {

        assertIsNotValid.accept("2928 ABCD 74");
        assertIsNotValid.accept("324 ABCD 91");
        assertIsNotValid.accept("56 ABCD 13");
    }


    @Test
    public void ensureOldLicensePlateWithDigitsInTheSecondGroupIsNotValid() {

        assertIsNotValid.accept("2928 AB1 74");
        assertIsNotValid.accept("324 999 91");
    }


    @Test
    public void ensureOldLicensePlateWithLessThanTwoCharactersInTheLastGroupIsNotValid() {

        assertIsNotValid.accept("2928 TW 7");
    }


    @Test
    public void ensureOldLicensePlateWithMoreThanThreeCharactersInTheLastGroupIsNotValid() {

        assertIsNotValid.accept("654 ANY 9711");
    }


    @Test
    public void ensureOldLicensePlateWithAMixOfLettersAndDigitsInTheLastGroupIsValid() {

        assertIsValid.accept("11 GY 2A");
    }
}
