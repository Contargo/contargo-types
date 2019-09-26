package net.contargo.types.contactinfo.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


/**
 * @author  Marius van Herpen - mvanherpen@contargo.net
 * @author  Julia Dasch - dasch@synyx.com
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SupportedDomainValidator.class)
public @interface SupportedDomain {

    String message() default "{constraint.violation.invaliddomain}";


    String[] domains() default { "contargo", "dit-duisburg" };


    Class<?>[] groups() default {};


    Class<? extends Payload>[] payload() default {};
}
