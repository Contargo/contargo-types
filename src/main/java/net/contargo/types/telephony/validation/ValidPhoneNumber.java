package net.contargo.types.telephony.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


/**
 * @author  Robin Jayasinghe - jayasinghe@synyx.de
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPhoneNumberValidator.class)
public @interface ValidPhoneNumber {

    String message() default "{constraint.violation.invalidphonenumber}";


    Class<?>[] groups() default {};


    Class<? extends Payload>[] payload() default {};
}
