package net.contargo.types.truck;

import org.junit.Before;
import org.junit.Test;


/**
 * @author  Marius van Herpen - mvanherpen@contargo.net
 */
public class BulgarianLicensePlateHandlerTest extends AbstractLicensePlateHandlerTest {

    @Before
    public void setUp() {

        handler = new BulgarianLicensePlateHandler();
    }


    // NORMALIZING -----------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("a-1234-mm", "A 1234 MM");
        assertIsNormalizedFromTo.accept("b-1234-fa", "B 1234 FA");
        assertIsNormalizedFromTo.accept("a 1234 mm", "A 1234 MM");
        assertIsNormalizedFromTo.accept("a 1234 mm  ", "A 1234 MM");
        assertIsNormalizedFromTo.accept("a  1234 mm", "A 1234 MM");
        assertIsNormalizedFromTo.accept("a--1234--mm", "A 1234 MM");
    }


    // VALIDATION ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateWithThreeGroupsIsValid() {

        assertIsValid.accept("X 1234 CH");
        assertIsValid.accept("CH 5678 TM");
    }


    @Test
    public void ensureLicensePlateWithMoreOrLessCharactersPerGroupIsInvalid() {

        assertIsNotValid.accept("ABC-1234-YZ");
        assertIsNotValid.accept("AB-123-YZ");
        assertIsNotValid.accept("AB-1234-XYZ");

        assertIsNotValid.accept("ABC-34-XYZ");
        assertIsNotValid.accept("AB-234-XYZ");
        assertIsNotValid.accept("AB-234-XYZ");
    }


    @Test
    public void ensureLicensePlateWithLeadingDigitIsInvalid() {

        assertIsNotValid.accept("1-1234-AB");
    }


    @Test
    public void ensureLicensePlateWithOnlyDigitsIsNotValid() {

        assertIsNotValid.accept("1 2345 67");
    }


    @Test
    public void ensureLicensePlateWithOnlyLettersIsNotValid() {

        assertIsNotValid.accept("A BCDE FG");
        assertIsNotValid.accept("AB CDEF GH");
    }


    @Test
    public void ensureLicensePlateWithMixedUpGroupsIsNotValid() {

        assertIsNotValid.accept("1 A2SV 14");
        assertIsNotValid.accept("9 123B 9Z");
        assertIsNotValid.accept("G AB3D 42");
        assertIsNotValid.accept("G-ABCD 4A");
    }


    @Test
    public void ensureLicensePlateWithAllDigitsIsValid() {

        assertIsValid.accept("X 0123 CH");
        assertIsValid.accept("X 1234 CH");
        assertIsValid.accept("X 2345 CH");
        assertIsValid.accept("X 3456 CH");
        assertIsValid.accept("X 4567 CH");
        assertIsValid.accept("X 5678 CH");
        assertIsValid.accept("X 6789 CH");
        assertIsValid.accept("X 7890 CH");
        assertIsValid.accept("X 8901 CH");
        assertIsValid.accept("X 9012 CH");
    }


    @Test
    public void ensureOnlyGivenCodesAreValid() {

        assertIsValid.accept("A 9012 CH");
        assertIsNotValid.accept("AB 9012 CH");
        assertIsValid.accept("B 9012 CH");
        assertIsNotValid.accept("BA 9012 CH");
        assertIsValid.accept("BP 9012 CH");
        assertIsValid.accept("C 9012 CH");
        assertIsNotValid.accept("D 9012 CH");
        assertIsValid.accept("E 9012 CH");
        assertIsNotValid.accept("ED 9012 CH");
        assertIsNotValid.accept("XY 9012 CH");
    }


    @Test
    public void ensureOnlyCertainLettersAreValid() {

        assertIsValid.accept("A 9012 AB");
        assertIsNotValid.accept("A 9012 CD");
        assertIsNotValid.accept("A 9012 FG");
        assertIsValid.accept("A 9012 MH");
    }
}
