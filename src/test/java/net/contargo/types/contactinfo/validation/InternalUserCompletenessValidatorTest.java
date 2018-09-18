package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInformation;
import net.contargo.types.contactinfo.normalization.EmailAddressNormalizer;
import net.contargo.types.contactinfo.normalization.PhoneNumberNormalizer;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;


public class InternalUserCompletenessValidatorTest {

    private InternalUserCompletenessValidator internalUserCompletenessValidator;

    @Before
    public void init() {

        this.internalUserCompletenessValidator = new InternalUserCompletenessValidator();
    }


    @Test
    public void ensureThatContactInfoWithoutMailIsInvalid() {

        ContactInformation incompleteContactInformation = new ContactInformation(UUID.randomUUID().toString(),
                "mobile", "phone", "", "communicationemail");
        List<ValidationResult> results = internalUserCompletenessValidator.checkCompleteness(
                incompleteContactInformation);
        assertTrue(results.size() == 1);
        assertTrue(results.get(0).equals(ValidationResult.MISSING_EMAIL));
    }


    @Test
    public void ensureThatCompleteContactInfoIsValid() {

        ContactInformation incompleteContactInformation = new ContactInformation(UUID.randomUUID().toString(),
                "mobile", "phone", "email", "communicationemail");
        List<ValidationResult> results = internalUserCompletenessValidator.checkCompleteness(
                incompleteContactInformation);
        assertTrue(results.isEmpty());
    }


    @Test
    public void ensureThatContactInfoWithJustEMailIsValid() {

        ContactInformation incompleteContactInformation = new ContactInformation(UUID.randomUUID().toString(), "", "",
                "email", "");
        List<ValidationResult> results = internalUserCompletenessValidator.checkCompleteness(
                incompleteContactInformation);
        assertTrue(results.isEmpty());
    }
}
