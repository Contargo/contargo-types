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


    // FORMATTING ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsFormattedCorrectly() {

        assertIsFormattedFromTo.accept("aa 001 ab", "AA-001-AB");
        assertIsFormattedFromTo.accept("aa  001-ab", "AA-001-AB");
        assertIsFormattedFromTo.accept("aa--001-ab", "AA-001-AB");
    }


    // VALIDATION ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateWithThreeGroupsIsValid() {

        assertIsValid.accept("AA-001-AB");
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
}
