package net.contargo.types.truck;

import org.junit.Before;
import org.junit.Test;


/**
 * @author  Slaven Travar - slaven.travar@pta.de
 */
public class RomanianLicensePlateHandlerTest extends AbstractLicensePlateHandlerTest {

    @Before
    public void setUp() {

        handler = new RomanianLicensePlateHandler();
    }


    @Test
    public void ensureLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("b 123 abc", "B 123 ABC");
        assertIsNormalizedFromTo.accept(" b 123 abc", "B 123 ABC");
        assertIsNormalizedFromTo.accept("b 123 abc ", "B 123 ABC");
        assertIsNormalizedFromTo.accept("b-123 abc", "B 123 ABC");
        assertIsNormalizedFromTo.accept("b-123-abc", "B 123 ABC");
        assertIsNormalizedFromTo.accept("B-123ABC", "B 123 ABC");
        assertIsNormalizedFromTo.accept("B 123ABC", "B 123 ABC");
        assertIsNormalizedFromTo.accept("B  123ABC", "B 123 ABC");
        assertIsNormalizedFromTo.accept("B  123--ABC", "B 123 ABC");
        assertIsNormalizedFromTo.accept("B123ABC", "B 123 ABC");
        assertIsNormalizedFromTo.accept("b123abc", "B 123 ABC");
    }


    @Test
    public void ensureLicensePlateWithBucharestCountyIsValid() {

        assertIsValid.accept("B 12 ABC");
        assertIsValid.accept("B 123 ABC");

        assertIsValid.accept("b 12 abc");
        assertIsValid.accept("b 123 abc");

        assertIsValid.accept("B12ABC");
        assertIsValid.accept("B123ABC");

        assertIsValid.accept("B-12-ABC");
        assertIsValid.accept("B-123-ABC");
    }


    @Test
    public void ensureLicensePlateWithTwoLetterCountyIsValid() {

        assertIsValid.accept("CT 12 ABC");
        assertIsValid.accept("ct 12 abc");
        assertIsValid.accept("CT12ABC");
        assertIsValid.accept("CT-12-ABC");
        assertIsValid.accept("CT--12--ABC");
    }


    @Test
    public void ensureLicensePlateWithBucharestCountyIsNotValidIfDigitGroupHasNotLengthTwoOrThree() {

        assertIsNotValid.accept("B 1 ABC");
        assertIsNotValid.accept("B 1111 ABC");
    }


    @Test
    public void ensureLicensePlateWithBucharestCountyIsNotValidIfDigitGroupIsMissing() {

        assertIsNotValid.accept("B ABC");
    }


    @Test
    public void ensureLicensePlateWithBucharestCountyIsNotValidIfLetterGroupHasNotLengthThree() {

        assertIsNotValid.accept("B 12 AB");
        assertIsNotValid.accept("B 12 ABCD");
    }


    @Test
    public void ensureLicensePlateWithBucharestCountyIsNotValidIfLetterGroupIsMissing() {

        assertIsNotValid.accept("B 12");
        assertIsNotValid.accept("B 123");
    }


    @Test
    public void ensureLicensePlateWithTwoLetterCountyIsNotValidIfDigitGroupHasNotLengthTwo() {

        assertIsNotValid.accept("CT 123 ABC");
        assertIsNotValid.accept("CT 1 ABC");
    }


    @Test
    public void ensureLicensePlateWithTwoLetterCountyIsNotValidIfDigitGroupIsMissing() {

        assertIsNotValid.accept("CT ABC");
    }


    @Test
    public void ensureLicensePlateWithTwoLetterCountyCountyIsNotValidIfLetterGroupHasNotLengthThree() {

        assertIsNotValid.accept("CT 12 AB");
        assertIsNotValid.accept("CT 12 ABCD");
    }


    @Test
    public void ensureLicensePlateWithTwoLetterCountyIsNotValidIfLetterGroupIsMissing() {

        assertIsNotValid.accept("CT 12");
        assertIsNotValid.accept("CT 123");
    }


    @Test
    public void ensureLicensePlateWithUnknownCountyIsNotValid() {

        assertIsNotValid.accept("Y 12 ABC");
        assertIsNotValid.accept("Y 123 ABC");
        assertIsNotValid.accept("YY 12 ABC");
    }


    @Test
    public void ensureLicensePlateWithWrongLetterOrDigitGroupIsNotValid() {

        assertIsNotValid.accept("CT AA XXX");
        assertIsNotValid.accept("CT 12 123");
    }


    @Test
    public void ensureLicensePlateWithIllegalStartOfLetterGroupIsNotValid() {

        assertIsNotValid.accept("B 12 IC");
        assertIsNotValid.accept("B 123 OBC");
        assertIsNotValid.accept("CT 12 IBC");
        assertIsNotValid.accept("CT 12 OBC");
    }


    @Test
    public void ensureLicensePlateWithSpecialCharactersIsNotValid() {

        assertIsNotValid.accept("B 12 AB*");
        assertIsNotValid.accept("B* 11 ABC");
    }
}
