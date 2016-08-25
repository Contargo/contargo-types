package net.contargo.types.truck;

import org.junit.Before;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class BelgianLicensePlateHandlerTest extends AbstractLicensePlateHandlerTest {

    @Before
    public void setUp() {

        handler = new BelgianLicensePlateHandler();
    }


    // NORMALIZING -----------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("1-abc-123", "1-ABC-123");
        assertIsNormalizedFromTo.accept("9-xyz-456", "9-XYZ-456");
        assertIsNormalizedFromTo.accept("1 abc 123", "1-ABC-123");
        assertIsNormalizedFromTo.accept("1  abc 123", "1-ABC-123");
        assertIsNormalizedFromTo.accept("9--xyz--456", "9-XYZ-456");
    }


    // VALIDATION ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateWithThreeGroupsIsValid() {

        assertIsValid.accept("1-AMS-147");
        assertIsValid.accept("9-123-XYZ");
    }


    @Test
    public void ensureLicensePlateWithMoreOrLessThanSevenCharactersIsNotValid() {

        assertIsNotValid.accept("1-AMSX-147");
        assertIsNotValid.accept("1-AMS-1475");
        assertIsNotValid.accept("1-AM-147");
        assertIsNotValid.accept("1-AMS-14");

        assertIsNotValid.accept("1-1234-ABC");
        assertIsNotValid.accept("1-123-ABCD");
        assertIsNotValid.accept("1-12-ABC");
        assertIsNotValid.accept("1-123-AB");

        assertIsNotValid.accept("G-0421-ABC");
        assertIsNotValid.accept("G-042-ABCD");

        assertIsNotValid.accept("WX-042-ABC");
        assertIsNotValid.accept("WX-42-ABCD");
    }


    @Test
    public void ensureLicensePlateWithLeadingDigitIsValid() {

        assertIsValid.accept("1-AMS-147");
        assertIsValid.accept("9-123-ABC");
    }


    @Test
    public void ensureLicensePlateWithLeadingLetterIsValid() {

        assertIsValid.accept("G-ABC-042");
    }


    @Test
    public void ensureLicensePlateWithLeadingLetterFollowedByDigitsIsNotValid() {

        assertIsNotValid.accept("G-042-ABC");
    }


    @Test
    public void ensureLicensePlateWithTwoLeadingLettersIsValid() {

        assertIsValid.accept("WX-42-ABC");
    }


    @Test
    public void ensureLicensePlateWithTwoLeadingLettersFollowedByLettersIsNotValid() {

        assertIsNotValid.accept("WX-AB-042");
    }


    @Test
    public void ensureLicensePlateWithOnlyDigitsIsNotValid() {

        assertIsNotValid.accept("1-234-567");
    }


    @Test
    public void ensureLicensePlateWithOnlyLettersIsNotValid() {

        assertIsNotValid.accept("A-BCD-EFG");
        assertIsNotValid.accept("1-BCD-EFG");
    }


    @Test
    public void ensureLicensePlateWithMixedUpGroupsIsNotValid() {

        assertIsNotValid.accept("1-A2S-147");
        assertIsNotValid.accept("9-123-X9Z");
        assertIsNotValid.accept("G-AB3-042");
        assertIsNotValid.accept("G-ABC-04A");
    }
}
