package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInfoConsumer;
import net.contargo.types.contactinfo.ContactInformation;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ContactInfoValidationServiceTest {

    private MobileAndEmailUniquenessValidator uniquenessValidator;
    private CompletenessValidator completenessValidator;

    @Before
    public void initValidators() {

        uniquenessValidator = new MobileAndEmailUniquenessValidator(new PhoneNumberNormalizer(),
                new EmailAddressNormalizer());
        completenessValidator = new ExternalUserRequiredFieldsValidator();
    }


    private void consumeColaProfiles(ContactInfoConsumer colaProfileConsumer) {

        List<ContactInformation> allProfiles = new ArrayList<>();

        ContactInformation contactInformation1 = new ContactInformation("uuid1", "1234", "4567", "foo1@bar",
                "bar@foo");
        allProfiles.add(contactInformation1);

        ContactInformation contactInformation2 = new ContactInformation("uuid2", "1233", "4567", "foo@bar", "bar@foo");
        allProfiles.add(contactInformation2);

        ContactInformation contactInformation3 = new ContactInformation("uuid3", "171789987", "4567", "foo@baz",
                "bar@foo");
        allProfiles.add(contactInformation3);

        ContactInformation contactInformation4 = new ContactInformation("uuid4", "171789987", "4567", "foo@bazl",
                "bar@foo");
        allProfiles.add(contactInformation4);

        colaProfileConsumer.consume(allProfiles);
    }


    private ContactInformation contactInformationWithAllData() {

        return new ContactInformation("uuid1", "171789987", "721321451", "foo@bar", "bar@foo");
    }


    private ContactInformation contactInformationWithAllDataAndDuplicateMailOfOtherUser() {

        return new ContactInformation("uuid1", "", "721321451", "foo@baz", "bar@foo");
    }


    private ContactInformation contactInformationWithDuplicateMailAfterNormalization() {

        return new ContactInformation("uuid1-1", "", "721321451", "foo1@BAR", "bar@foo");
    }


    private ContactInformation contactInformationWithAllDataAndDistinctMail() {

        return new ContactInformation("uuid3", "", "721321451", "foo@baz", "bar@foo");
    }


    private ContactInformation contactInformationWithAllDataAndDistinctMobile() {

        return new ContactInformation("uuid4", "171789988", "721321451", "foo@barz", "bar@foo");
    }


    private ContactInformation contactInformationWithDuplicateMobileAfterNormalization() {

        return new ContactInformation("uuid5", "+49171789987", "721321451", null, "bar@foo");
    }


    private ContactInformation contactInformationWithDuplicateMobile() {

        return new ContactInformation("uuid5", "171789987", "721321451", null, "bar@foo");
    }


    private ContactInformation contactInformationWithoutPhoneNumbers() {

        return new ContactInformation("uuid1", null, null, "foo@bar", "bar@foo");
    }


    private ContactInformation contactInformationWithoutEmailAndMobile() {

        return new ContactInformation("uuid1", null, "1728092174", null, "bar@foo");
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAfterNormalization() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        ContactInformation contactInformationWithDuplicateMobile =
            contactInformationWithDuplicateMobileAfterNormalization();
        List<ValidationResult> validationResults = contactInfoValidationService.validate(
                contactInformationWithDuplicateMobile);

        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_MOBILE, validationResults.get(0));
    }


    @Test
    public void ensureThatContactInfoWithNullValuesCanBeHandled() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);

        ContactInformation contactInfoWithNullValue1 = new ContactInformation("uuid1", "1234", null, null, "");
        ContactInformation contactInfoWithNullValue2 = new ContactInformation("uuid2", "1234", null, null, "");

        uniquenessValidator.consume(contactInfoWithNullValue1);

        assertDetectionOfDuplicateMobile(contactInfoValidationService, contactInfoWithNullValue2);

        uniquenessValidator.remove(contactInfoWithNullValue1);

        assertThatNoValidationErrorIsReported(contactInfoValidationService, contactInfoWithNullValue2);
    }


    @Test
    public void ensureThatDuplicateMobileIsDetected() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        assertDetectionOfDuplicateMobile(contactInfoValidationService, contactInformationWithDuplicateMobile());
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAndNotDetectedAfterConflictingUserChangedMobile() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        assertDetectionOfDuplicateMobile(contactInfoValidationService, contactInformationWithDuplicateMobile());

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid2", "12345", "4567",
                "foo-uuid2@bar", "bar@foo");
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233", "4567",
                "foo-uuid8@bar", "bar@foo");

        uniquenessValidator.consume(contactInfoToBeChanged);
        assertThatNoValidationErrorIsReported(contactInfoValidationService, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAndNotDetectedAfterConflictingUserChangedMail() {

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid4", "12345", "4567", "foo@bazk",
                "bar@foo");
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "", "4567", "foo@bazl",
                "bar@foo");

        final ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);

        consumeColaProfiles(uniquenessValidator);

        assertDetectionOfDuplicateMail(contactInfoValidationService, contactInfoToBeValidated);

        uniquenessValidator.consume(contactInfoToBeChanged);
        assertThatNoValidationErrorIsReported(contactInfoValidationService, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAndNotDetectedAfterRemoval() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        assertDetectionOfDuplicateMobile(contactInfoValidationService, contactInformationWithDuplicateMobile());

        ContactInformation contactInfoToBeRemoved = new ContactInformation("uuid2", "1233", "4567", "foo@bar",
                "bar@foo");
        ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233", "4567", "foo@bar",
                "bar@foo");
        uniquenessValidator.remove(contactInfoToBeRemoved);
        assertThatNoValidationErrorIsReported(contactInfoValidationService, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAndNotDetectedAfterRemoval() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        assertDetectionOfDuplicateMail(contactInfoValidationService,
            contactInformationWithDuplicateMailAfterNormalization());

        ContactInformation contactInfoToBeRemoved = new ContactInformation("uuid3", "1233", "4567", "foo@baz",
                "bar@foo");
        ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233123", "4567", "foo@baz",
                "bar@foo");
        uniquenessValidator.remove(contactInfoToBeRemoved);
        assertThatNoValidationErrorIsReported(contactInfoValidationService, contactInfoToBeValidated);
    }


    private void assertDetectionOfDuplicateMail(final ContactInfoValidationService contactInfoValidationService,
        final ContactInformation contactInformationWithDuplicateMail) {

        List<ValidationResult> validationResults = contactInfoValidationService.validate(
                contactInformationWithDuplicateMail);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }


    private void assertDetectionOfDuplicateMobile(final ContactInfoValidationService contactInfoValidationService,
        final ContactInformation contactInformationWithDuplicateMobile) {

        List<ValidationResult> validationResults = contactInfoValidationService.validate(
                contactInformationWithDuplicateMobile);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_MOBILE, validationResults.get(0));
    }


    private void assertThatNoValidationErrorIsReported(ContactInfoValidationService contactInfoValidationService,
        final ContactInformation contactInfoToBeCheckedForDuplicate) {

        List<ValidationResult> validationResults = contactInfoValidationService.validate(
                contactInfoToBeCheckedForDuplicate);
        assertTrue("after the conflicting entry has been removed, no duplicate shall be detected",
            validationResults.size() == 0);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAfterNormalization() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithDuplicateMailAfterNormalization();
        List<ValidationResult> validationResults = contactInfoValidationService.validate(contactInfoWithAllData);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }


    @Test
    public void ensureThatDuplicateMailIsDetected() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithAllDataAndDuplicateMailOfOtherUser();
        List<ValidationResult> validationResults = contactInfoValidationService.validate(contactInfoWithAllData);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }


    @Test
    public void ensureThatUserOwnMobileIsNotHandledAsDuplicate() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithAllDataAndDistinctMobile();
        List<ValidationResult> validationResults = contactInfoValidationService.validate(contactInfoWithAllData);
        assertEquals(0, validationResults.size());
    }


    @Test
    public void ensureThatUserOwnMailIsNotHandledAsDuplicate() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithAllDataAndDistinctMail();
        List<ValidationResult> validationResults = contactInfoValidationService.validate(contactInfoWithAllData);
        assertTrue(validationResults.isEmpty());
    }


    @Test
    public void ensureThatContactInfoWithAllDataIsValid() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithAllData();
        List<ValidationResult> validationResults = contactInfoValidationService.validate(contactInfoWithAllData);
        assertTrue(validationResults.isEmpty());
    }


    @Test
    public void ensureThatContactInfoWithoutPhoneNumberIsInvalid() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);

        ContactInformation contactInformationWithoutPhoneNumbers = contactInformationWithoutPhoneNumbers();
        List<ValidationResult> validationResults = contactInfoValidationService.validate(
                contactInformationWithoutPhoneNumbers);

        assertEquals(2, validationResults.size());
        assertTrue(validationResults.contains(ValidationResult.MISSING_MOBILE));
        assertTrue(validationResults.contains(ValidationResult.MISSING_PHONE));
    }


    @Test
    public void ensureThatContactInfoWithoutMailAndMobileIsInvalid() {

        ContactInfoValidationService contactInfoValidationService = new ContactInfoValidationService(
                uniquenessValidator, completenessValidator);

        ContactInformation contactInfoWithoutEmailAndMobile = contactInformationWithoutEmailAndMobile();
        List<ValidationResult> validationResults = contactInfoValidationService.validate(
                contactInfoWithoutEmailAndMobile);

        assertEquals(2, validationResults.size());
        assertTrue(validationResults.contains(ValidationResult.MISSING_MOBILE));
        assertTrue(validationResults.contains(ValidationResult.MISSING_EMAIL));
    }


    @Test
    public void ensureThatConsumesRegistrationsIsSetAndReportedCorrectly() {

        ContactInfoConsumer uniquenessValidator = new MobileAndEmailUniquenessValidator(new PhoneNumberNormalizer(),
                new EmailAddressNormalizer());
        assertTrue(uniquenessValidator.consumesRegistrations());

        ContactInfoConsumer uniquenessValidatorWithRegistrations = new MobileAndEmailUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer(), false);
        assertFalse(uniquenessValidatorWithRegistrations.consumesRegistrations());
    }
}
