package net.contargo.types;

import org.junit.Before;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class SwissLicensePlateHandlerTest extends AbstractLicensePlateHandlerTest {

    @Before
    public void setUp() {

        handler = new SwissLicensePlateHandler();
    }


    // NORMALIZING -----------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("ag 123456", "AG123456");
        assertIsNormalizedFromTo.accept("ag  123456", "AG123456");
        assertIsNormalizedFromTo.accept("sh-75936", "SH75936");
        assertIsNormalizedFromTo.accept("sh--75936", "SH75936");
        assertIsNormalizedFromTo.accept("zh 123 456", "ZH123456");
    }


    // FORMATTING ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsFormattedCorrectly() {

        assertIsFormattedFromTo.accept("ag 123456", "AG 123456");
        assertIsFormattedFromTo.accept("ag  123456", "AG 123456");
        assertIsFormattedFromTo.accept("sh-75936", "SH 75936");
        assertIsFormattedFromTo.accept("sh--75936", "SH 75936");
        assertIsFormattedFromTo.accept("zh 123 456", "ZH 123456");
    }


    @Test
    public void ensureLicensePlateIsNotFormattedIfItHasNoValidFormat() {

        assertIsFormattedFromTo.accept("agx 123456", "agx 123456");
        assertIsFormattedFromTo.accept("a123", "a123");
    }


    // VALIDATION ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateWithTwoLetterCodeIsValid() {

        assertIsValid.accept("AG 123456");
        assertIsValid.accept("SH 423");
        assertIsValid.accept("BL 91827");
    }


    @Test
    public void ensureLicensePlateWithTwoLetterCodeThatIsNoCantonIsNotValid() {

        assertIsNotValid.accept("XY 123456");
        assertIsNotValid.accept("AB 43242");
    }


    @Test
    public void ensureLicensePlateWithOneLetterCodeIsNotValid() {

        assertIsNotValid.accept("A 123456");
        assertIsNotValid.accept("S 423");
        assertIsNotValid.accept("B 91827");
    }


    @Test
    public void ensureLicensePlateWithThreeLetterCodeIsNotValid() {

        assertIsNotValid.accept("AGA 123456");
        assertIsNotValid.accept("SHB 423");
        assertIsNotValid.accept("BLC 91827");
    }


    @Test
    public void ensureLicensePlateWithoutLetterCodeIsNotValid() {

        assertIsNotValid.accept("123456");
        assertIsNotValid.accept("423");
        assertIsNotValid.accept("91827");
    }


    @Test
    public void ensureLicensePlateWithoutDigitsIsNotValid() {

        assertIsNotValid.accept("AG");
        assertIsNotValid.accept("SH");
        assertIsNotValid.accept("BL");
    }


    @Test
    public void ensureLicensePlateWithUpToSixDigitsIsValid() {

        assertIsValid.accept("AG 1");
        assertIsValid.accept("SH 12");
        assertIsValid.accept("BL 123");
        assertIsValid.accept("SG 1234");
        assertIsValid.accept("VD 12345");
        assertIsValid.accept("BL 123456");
    }


    @Test
    public void ensureLicensePlateWithMoreThanSixDigitsIsNotValid() {

        assertIsNotValid.accept("AG 1234567");
    }
}
