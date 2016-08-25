package net.contargo.types.truck;

import org.junit.Before;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class DefaultLicensePlateHandlerTest extends AbstractLicensePlateHandlerTest {

    @Before
    public void setUp() {

        handler = new DefaultLicensePlateHandler();
    }


    // VALIDATION ------------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateWithLettersAndNumbersIsValid() {

        assertIsValid.accept("KA XY 123");
    }


    @Test
    public void ensureLicensePlateWithMinusAsSeparatorIsValid() {

        assertIsValid.accept("KA-XY-123");
    }


    @Test
    public void ensureLicensePlateWithSpecialCharactersIsNotValid() {

        assertIsNotValid.accept("KA/XY.123");
    }


    @Test
    public void ensureLicensePlateWithUmlautIsValid() {

        assertIsValid.accept("LÖ-CR-123");
    }


    // NORMALIZING -----------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("ka ab 123", "KA-AB-123");
        assertIsNormalizedFromTo.accept("ka-ab 123", "KA-AB-123");
        assertIsNormalizedFromTo.accept("ka-ab  123", "KA-AB-123");
        assertIsNormalizedFromTo.accept("ka--ab  123", "KA-AB-123");
    }
}
