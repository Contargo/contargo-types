package net.contargo.types.contactinfo.validation;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;


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

        if (value == null || value.isEmpty() || !value.contains("@") || !value.contains(".")) {
            return false;
        }

        int endIndex = value.lastIndexOf('.');
        int beginIndex = value.lastIndexOf('@') + 1;

        if (beginIndex > endIndex) {
            return false;
        }

        String domain = value.substring(beginIndex, endIndex);
        domain = StringUtils.lowerCase(domain);

        return domains.stream().noneMatch(domain::contains);
    }
}
