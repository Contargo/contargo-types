package net.contargo.types.container;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class ContainerTypeTest {

    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfProvidingInvalidIsoCode() {

        ContainerType.byIsoSize("foo");
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfProvidingNullIsoCode() {

        ContainerType.byIsoSize(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfProvidingEmptyIsoCode() {

        ContainerType.byIsoSize("");
    }


    @Test
    public void ensureReturnsContainerTypeForIsoCode() {

        ContainerType containerType = ContainerType.byIsoSize("40HC");

        Assert.assertNotNull("Should not be null", containerType);
        Assert.assertEquals("Wrong container type", ContainerType.FORTY_HC, containerType);
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfProvidingInvalidInternationalIsoCode() {

        ContainerType.byInternationalIsoSize("foo");
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfProvidingNullInternationalIsoCode() {

        ContainerType.byInternationalIsoSize(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureThrowsIfProvidingEmptyInternationalIsoCode() {

        ContainerType.byInternationalIsoSize("");
    }


    @Test
    public void ensureReturnsContainerTypeForInternationalIsoCode() {

        ContainerType containerType = ContainerType.byInternationalIsoSize("22B0");

        Assert.assertNotNull("Should not be null", containerType);
        Assert.assertEquals("Wrong container type", ContainerType.TWENTY_BO, containerType);
    }
}
