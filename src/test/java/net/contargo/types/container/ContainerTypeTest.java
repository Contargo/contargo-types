package net.contargo.types.container;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Slaven Travar - slaven.travar@pta.de
 */
public class ContainerTypeTest {

    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfProvidingInvalidContargoHandlingCode() {

        ContainerType.byContargoHandlingCode("foo");
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfProvidingNullContargoHandlingCode() {

        ContainerType.byContargoHandlingCode(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfProvidingEmptyContargoHandlingCode() {

        ContainerType.byContargoHandlingCode("");
    }


    @Test
    public void ensureReturnsContainerTypeForContargoHandlingCode() {

        ContainerType containerType = ContainerType.byContargoHandlingCode("40HC");

        Assert.assertNotNull("Should not be null", containerType);
        Assert.assertEquals("Wrong container type", ContainerType.FORTY_HC, containerType);
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfProvidingInvalidIsoCode() {

        ContainerType.byIsoCode("foo");
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfProvidingNullIsoCode() {

        ContainerType.byIsoCode(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfProvidingEmptyIsoCode() {

        ContainerType.byIsoCode("");
    }


    @Test
    public void ensureReturnsContainerTypeForIsoCode() {

        ContainerType containerType = ContainerType.byIsoCode("22B0");

        Assert.assertNotNull("Should not be null", containerType);
        Assert.assertEquals("Wrong container type", ContainerType.TWENTY_BO, containerType);
    }
}
