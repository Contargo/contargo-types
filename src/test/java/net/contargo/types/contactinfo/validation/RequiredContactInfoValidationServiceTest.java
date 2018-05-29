package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInfoConsumer;
import net.contargo.types.contactinfo.ContactInformation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class RequiredContactInfoValidationServiceTest {

    private void consumeColaProfiles(ContactInfoConsumer colaProfileConsumer) {

        List<ContactInformation> allProfiles = new ArrayList<>();

        ContactInformation contactInformation1 = new ContactInformation("uuid1", "1234", "4567", "foo@bar");
        allProfiles.add(contactInformation1);

        ContactInformation contactInformation2 = new ContactInformation("uuid2", "1233", "4567", "foo@bar");
        allProfiles.add(contactInformation2);

        ContactInformation contactInformation3 = new ContactInformation("uuid3", "171789987", "4567", "foo@baz");
        allProfiles.add(contactInformation3);

        ContactInformation contactInformation4 = new ContactInformation("uuid4", "171789987", "4567", "foo@bazl");
        allProfiles.add(contactInformation4);

        colaProfileConsumer.consume(allProfiles);
    }


    private ContactInformation contactInformationWithAllData() {

        return new ContactInformation("uuid1", "171789987", "721321451", "foo@bar");
    }


    private ContactInformation contactInformationWithDuplicateMailAfterNormalization() {

        return new ContactInformation("uuid1", "171789987", "721321451", "foo@BAR");
    }


    private ContactInformation contactInformationWithAllDataAndDistinctMail() {

        return new ContactInformation("uuid3", "171789987", "721321451", "foo@baz");
    }


    private ContactInformation contactInformationWithAllDataAndDistinctMobile() {

        return new ContactInformation("uuid4", "171789988", "721321451", "foo@barz");
    }


    private ContactInformation contactInformationWithDuplicateMobileAfterNormalization() {

        return new ContactInformation("uuid5", "+49171789987", "721321451", null);
    }


    private ContactInformation contactInformationWithDuplicateMobile() {

        return new ContactInformation("uuid5", "171789987", "721321451", null);
    }


    private ContactInformation contactInformationWithoutPhoneNumbers() {

        return new ContactInformation("uuid1", null, null, "foo@bar");
    }


    private ContactInformation contactInformationWithoutEmailAndMobile() {

        return new ContactInformation("uuid1", null, "1728092174", null);
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAfterNormalization() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(requiredContactInfoValidationService);

        List<RequiredContactInfoValidationService.ValidationResult> validationResults = new ArrayList<>();
        ContactInformation contactInformationWithDuplicateMobile =
            contactInformationWithDuplicateMobileAfterNormalization();
        boolean isValid = requiredContactInfoValidationService.validate(contactInformationWithDuplicateMobile,
                validationResults);
        assertFalse(isValid);
        assertEquals(1, validationResults.size());
        assertEquals(RequiredContactInfoValidationService.ValidationResult.NON_UNIQUE_MOBILE,
            validationResults.get(0));
    }


    @Test
    public void ensureThatDuplicateMobileIsDetected() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(requiredContactInfoValidationService);

        List<RequiredContactInfoValidationService.ValidationResult> validationResults = new ArrayList<>();
        ContactInformation contactInformationWithDuplicateMobile = contactInformationWithDuplicateMobile();
        boolean isValid = requiredContactInfoValidationService.validate(contactInformationWithDuplicateMobile,
                validationResults);
        assertFalse(isValid);
        assertEquals(1, validationResults.size());
        assertEquals(RequiredContactInfoValidationService.ValidationResult.NON_UNIQUE_MOBILE,
            validationResults.get(0));
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAfterNormalization() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(requiredContactInfoValidationService);

        List<RequiredContactInfoValidationService.ValidationResult> validationResults = new ArrayList<>();
        ContactInformation contactInfoWithAllData = contactInformationWithDuplicateMailAfterNormalization();
        boolean isValid = requiredContactInfoValidationService.validate(contactInfoWithAllData, validationResults);
        assertFalse(isValid);
        assertEquals(1, validationResults.size());
        assertEquals(RequiredContactInfoValidationService.ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }


    @Test
    public void ensureThatDuplicateMailIsDetected() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(requiredContactInfoValidationService);

        List<RequiredContactInfoValidationService.ValidationResult> validationResults = new ArrayList<>();
        ContactInformation contactInfoWithAllData = contactInformationWithAllData();
        boolean isValid = requiredContactInfoValidationService.validate(contactInfoWithAllData, validationResults);
        assertFalse(isValid);
        assertEquals(1, validationResults.size());
        assertEquals(RequiredContactInfoValidationService.ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }


    @Test
    public void ensureThatUserOwnMobileIsNotHandledAsDuplicate() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(requiredContactInfoValidationService);

        List<RequiredContactInfoValidationService.ValidationResult> validationResults = new ArrayList<>();
        ContactInformation contactInfoWithAllData = contactInformationWithAllDataAndDistinctMobile();
        boolean isValid = requiredContactInfoValidationService.validate(contactInfoWithAllData, validationResults);
        assertTrue(isValid);
        assertEquals(0, validationResults.size());
    }


    @Test
    public void ensureThatUserOwnMailIsNotHandledAsDuplicate() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(requiredContactInfoValidationService);

        List<RequiredContactInfoValidationService.ValidationResult> validationResults = new ArrayList<>();
        ContactInformation contactInfoWithAllData = contactInformationWithAllDataAndDistinctMail();
        boolean isValid = requiredContactInfoValidationService.validate(contactInfoWithAllData, validationResults);
        assertTrue(isValid);
        assertEquals(0, validationResults.size());
    }


    @Test
    public void ensureThatContactInfoWithAllDataIsValid() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());

        List<RequiredContactInfoValidationService.ValidationResult> validationResults = new ArrayList<>();
        ContactInformation contactInfoWithAllData = contactInformationWithAllData();
        assertTrue(requiredContactInfoValidationService.validate(contactInfoWithAllData, validationResults));
    }


    @Test
    public void ensureThatContactInfoWithoutPhoneNumberIsInvalid() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());

        List<RequiredContactInfoValidationService.ValidationResult> validationResults = new ArrayList<>();
        ContactInformation contactInformationWithoutPhoneNumbers = contactInformationWithoutPhoneNumbers();
        assertFalse(requiredContactInfoValidationService.validate(contactInformationWithoutPhoneNumbers,
                validationResults));

        assertEquals(2, validationResults.size());
        assertTrue(validationResults.contains(RequiredContactInfoValidationService.ValidationResult.MISSING_MOBILE));
        assertTrue(validationResults.contains(RequiredContactInfoValidationService.ValidationResult.MISSING_PHONE));
    }


    @Test
    public void ensureThatContactInfoWithoutMailAndMobileIsInvalid() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());

        List<RequiredContactInfoValidationService.ValidationResult> validationResults = new ArrayList<>();

        ContactInformation contactInfoWithoutEmailAndMobile = contactInformationWithoutEmailAndMobile();
        assertFalse(requiredContactInfoValidationService.validate(contactInfoWithoutEmailAndMobile,
                validationResults));

        assertEquals(2, validationResults.size());
        assertTrue(validationResults.contains(RequiredContactInfoValidationService.ValidationResult.MISSING_MOBILE));
        assertTrue(validationResults.contains(RequiredContactInfoValidationService.ValidationResult.MISSING_EMAIL));
    }
}
