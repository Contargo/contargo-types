package net.contargo.types.truck;

import org.junit.Before;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class DutchLicensePlateHandlerTest extends AbstractLicensePlateHandlerTest {

    @Before
    public void setUp() {

        handler = new DutchLicensePlateHandler();
    }


    // NORMALIZING -----------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("xx-xx-99", "XX-XX-99");
        assertIsNormalizedFromTo.accept("xx-xx 99", "XX-XX-99");
        assertIsNormalizedFromTo.accept(" xx-xx 99 ", "XX-XX-99");
        assertIsNormalizedFromTo.accept("999 xx 9", "999-XX-9");
        assertIsNormalizedFromTo.accept("999  xx 9", "999-XX-9");
        assertIsNormalizedFromTo.accept("999  xx--9", "999-XX-9");
    }


    // VALIDATION ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateWithThreeGroupsIsValid() {

        assertIsValid.accept("XX-999-X");
        assertIsValid.accept("9-XX-XXX");
        assertIsValid.accept("9-XXX-99");
    }


    @Test
    public void ensureLicensePlateWithMoreThanThreeGroupsIsNotValid() {

        assertIsNotValid.accept("XX-999-X-9");
        assertIsNotValid.accept("XX-999-X-XX");
    }


    @Test
    public void ensureLicensePlateWithLessThanThreeGroupsIsNotValid() {

        assertIsNotValid.accept("XX-999");
        assertIsNotValid.accept("9-XX");
    }


    @Test
    public void ensureLicensePlateWithOneToThreeCharactersPerGroupIsValid() {

        assertIsValid.accept("XX-99-99");
        assertIsValid.accept("99-99-XX");
        assertIsValid.accept("99-XX-99");
        assertIsValid.accept("XX-99-XX");
        assertIsValid.accept("XX-XX-99");
        assertIsValid.accept("99-XX-XX");

        assertIsValid.accept("99-XXX-9");
        assertIsValid.accept("9-XXX-99");
        assertIsValid.accept("XX-999-X");
        assertIsValid.accept("X-999-XX");
        assertIsValid.accept("XXX-99-X");
        assertIsValid.accept("X-99-XXX");
        assertIsValid.accept("9-XX-999");
        assertIsValid.accept("999-XX-9");
    }


    @Test
    public void ensureLicensePlateWithMoreThanThreeCharactersPerGroupIsNotValid() {

        assertIsNotValid.accept("99-XXXX-9");
        assertIsNotValid.accept("XXXX-99-X");
        assertIsNotValid.accept("X-99-XXXX");

        assertIsNotValid.accept("XX-9999-X");
        assertIsNotValid.accept("9999-XX-9");
        assertIsNotValid.accept("9-XX-9999");
    }


    @Test
    public void ensureLicensePlateWithSpecialCharactersIsNotValid() {

        assertIsNotValid.accept("XX-9/9-X");
        assertIsNotValid.accept("XX-99-XX.");
    }


    @Test
    public void ensureLicensePlateWithOnlyLettersAndDigitsInTheGroupsIsValid() {

        assertIsValid.accept("12-DB-16");
        assertIsValid.accept("EF-RP-10");
        assertIsValid.accept("KW-69-68");
        assertIsValid.accept("ABC-12-D");
        assertIsValid.accept("1-ABC-34");
    }


    @Test
    public void ensureLicensePlateWithAMixOfLettersAndDigitsInOneGroupIsValid() {

        assertIsValid.accept("BR-T5-19");
    }


    @Test
    public void ensureLicensePlateWillBeNormalizedBeforeValidation() {

        assertIsValid.accept("XX 99 99");
        assertIsValid.accept("99-99 XX");
        assertIsValid.accept("99  XXX-9");
        assertIsValid.accept("9--XXX--99");
        assertIsValid.accept("XX  999  X");
        assertIsValid.accept("X 999 XX");
    }


    @Test
    public void ensureLicensePlateWithMoreThenEightCharactersIsNotValid() {

        assertIsNotValid.accept("XX-999-XX");
        assertIsNotValid.accept("99-XXX-99");
    }
}
