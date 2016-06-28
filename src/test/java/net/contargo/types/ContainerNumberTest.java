package net.contargo.types;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class ContainerNumberTest {

    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfBuiltWithNull() {

        ContainerNumber.forValue(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfBuiltWithEmptyString() {

        ContainerNumber.forValue(" ");
    }


    @Test
    public void ensureCanBeBuiltWithString() {

        ContainerNumber containerNumber = ContainerNumber.forValue("foo");

        Assert.assertNotNull("Should not be null", containerNumber);
        Assert.assertEquals("Wrong String representation", "foo", containerNumber.toString());
    }


    @Test
    public void ensureTrimmedContainerNumberIsFormattedCorrectly() {

        ContainerNumber containerNumber = ContainerNumber.forValue("hlxu1234567");

        Assert.assertEquals("Wrong String representation", "HLXU 123456-7", containerNumber.toString());
    }


    @Test
    public void ensureInvalidContainerNumberIsFormattedCorrectly() {

        ContainerNumber containerNumber = ContainerNumber.forValue("foo");

        Assert.assertEquals("Wrong String representation", "foo", containerNumber.toString());
    }


    @Test
    public void ensureAlreadyFormattedContainerNumberIsFormattedCorrectly() {

        ContainerNumber containerNumber = ContainerNumber.forValue("HLXU 123456-7");

        Assert.assertEquals("Wrong String representation", "HLXU 123456-7", containerNumber.toString());
    }
}
