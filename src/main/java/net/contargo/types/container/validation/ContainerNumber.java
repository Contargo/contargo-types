package net.contargo.types.container.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Annotation to validate if the {@link net.contargo.types.container.ContainerNumber ContainerNumber} is ISO6346 valid,
 * i.e. has a valid format and a correct check digit.
 *
 * <pre>
 Example:
 public class Container {

    &#064;ContainerNumber
    public String containerNumber;
    ...
 }
 * </pre>
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Documented
@Constraint(validatedBy = ContainerNumberValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface ContainerNumber {

    String message() default "{validation.unit.number.error.message}";


    Class<?>[] groups() default {};


    Class<? extends Payload>[] payload() default {};
}
