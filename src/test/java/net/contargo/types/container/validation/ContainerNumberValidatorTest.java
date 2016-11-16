package net.contargo.types.container.validation;

import org.junit.Test;


/**
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class ContainerNumberValidatorTest extends ValidationTest {

    @Test
    public void validateUnitNumberIsValid() {

        TestValidationObject validationObject = new TestValidationObject();
        validationObject.containerNumber = "MSKU 180651-0";
        assertPropertyHasNoErrors(validationObject, "containerNumber");
    }


    @Test
    public void validateUnitNumberIsValidFailsWithWrongCheckDigit() {

        TestValidationObject validationObject = new TestValidationObject();
        validationObject.containerNumber = "MSKU 180651-9";
        assertPropertyHasErrors(validationObject, "containerNumber", "{validation.unit.number.error.message}");
    }


    @Test
    public void validateUnitNumberIsValidFails() {

        TestValidationObject validationObject = new TestValidationObject();
        validationObject.containerNumber = "123";
        assertPropertyHasErrors(validationObject, "containerNumber", "{validation.unit.number.error.message}");
    }


    @Test
    public void validateUnitNumberIsEmpty() {

        TestValidationObject validationObject = new TestValidationObject();
        validationObject.containerNumber = " ";

        assertPropertyHasErrors(validationObject, "containerNumber", "{validation.unit.number.error.message}");
    }


    @Test
    public void validateUnitNumberIsNull() {

        TestValidationObject validationObject = new TestValidationObject();
        validationObject.containerNumber = null;

        assertPropertyHasNoErrors(validationObject, "containerNumber");
    }

    public static class TestValidationObject {

        @ContainerNumber
        public String containerNumber;
    }
}
