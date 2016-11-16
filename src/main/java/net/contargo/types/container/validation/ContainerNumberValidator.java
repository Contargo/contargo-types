package net.contargo.types.container.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Container number validation based on the ISO 6346 specification.
 *
 * <p>Use this validator with the supported annotation {@link ContainerNumber}</p>
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class ContainerNumberValidator implements ConstraintValidator<ContainerNumber, String> {

    @Override
    public void initialize(ContainerNumber constraint) {

        // Annotation does not provide any attributes
    }


    @Override
    public boolean isValid(final String unitNumber, final ConstraintValidatorContext context) {

        if (unitNumber == null) {
            // default validator will check that for us
            return true;
        }

        boolean isValid;

        try {
            isValid = net.contargo.types.container.ContainerNumber.forValue(unitNumber).isISO6346Valid();
        } catch (IllegalArgumentException e) {
            isValid = false;
        }

        return isValid;
    }
}
