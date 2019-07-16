package net.contargo.types.truck;

import org.junit.Before;
import org.junit.Test;


/**
 * @author  Marius van Herpen - mvanherpen@contargo.net
 */
public class LithuanianLicensePlateHandlerTest extends AbstractLicensePlateHandlerTest {

    @Before
    public void setUp() {

        handler = new LithuanianLicensePlateHandler();
    }


    // NORMALIZING -----------------------------------------------------------------------------------------------------

    @Test
    public void ensureLicensePlateIsNormalizedCorrectly() {

        assertIsNormalizedFromTo.accept("abc-123", "ABC 123");
        assertIsNormalizedFromTo.accept("abc 123", "ABC 123");
        assertIsNormalizedFromTo.accept("abc--123", "ABC 123");
    }


    // VALIDATION ------------------------------------------------------------------------------------------------------

    @Test
    public void assertCorrectlyGroupedIsValid() {

        assertIsValid.accept("ABC 123");
        assertIsValid.accept("KRK 365");
    }


    @Test
    public void assertIncorrectGroupsInvalid() {

        assertIsNotValid.accept("645 564");
        assertIsNotValid.accept("645 JKZ");
        assertIsNotValid.accept("FDS JKR");
        assertIsNotValid.accept("JKR 65");
        assertIsNotValid.accept("JKR 6435");
        assertIsNotValid.accept("JK 645");
    }
}
