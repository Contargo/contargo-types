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

        assertIsNormalizedFromTo.accept("a-1234-mm", "A-1234-MM");
        assertIsNormalizedFromTo.accept("b-1234-fa", "B-1234-FA");
        assertIsNormalizedFromTo.accept("a 1234 mm", "A-1234-MM");
        assertIsNormalizedFromTo.accept("a 1234 mm  ", "A-1234-MM");
        assertIsNormalizedFromTo.accept("a  1234 mm", "A-1234-MM");
        assertIsNormalizedFromTo.accept("a--1234--mm", "A-1234-MM");
    }


    // VALIDATION ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateWithThreeGroupsIsValid() {

        assertIsValid.accept("X-1234-AA");
        assertIsValid.accept("AB-5678-XZ");
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

        assertIsNotValid.accept("1-2345-67");
    }


    @Test
    public void ensureLicensePlateWithOnlyLettersIsNotValid() {

        assertIsNotValid.accept("A-BCDE-FG");
        assertIsNotValid.accept("AB-CDEF-GH");
    }


    @Test
    public void ensureLicensePlateWithMixedUpGroupsIsNotValid() {

        assertIsNotValid.accept("1-A2SV-14");
        assertIsNotValid.accept("9-123B-9Z");
        assertIsNotValid.accept("G-AB3D-42");
        assertIsNotValid.accept("G-ABCD-4A");
    }
}
