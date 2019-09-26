package net.contargo.types.contactinfo.validation;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * This validator checks the domain of the email address against a configured list of domain which aren't allowed. The
 * TLD is ignored as we basically want to prevent external users to switch their email address to one that belongs to
 * our own company (contargo, dit-duisburg).
 *
 * @author  Marius van Herpen - mvanherpen@contargo.net
 * @author  Julia Dasch - dasch@synyx.com
 */
public class SupportedDomainValidator implements ConstraintValidator<SupportedDomain, String> {

    private List<String> domains;

    @Override
    public void initialize(SupportedDomain constraintAnnotation) {

        this.domains = Arrays.asList(constraintAnnotation.domains());
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean isValid = true;

        if (value == null || value.isEmpty()) {
            // is checked else where in the separate user completeness validators
            return true;
        }

        if (!value.contains("@") || !value.contains(".")) {
            return false;
        }

        int endIndex = value.lastIndexOf('.');
        int beginIndex = value.lastIndexOf('@') + 1;

        if (beginIndex > endIndex) {
            isValid = false;
        }

        String domain = value.substring(beginIndex, endIndex);
        domain = StringUtils.lowerCase(domain);

        if (domains.stream().anyMatch(domain::contains)) {
            isValid = false;
        }

        return isValid;
    }
}
