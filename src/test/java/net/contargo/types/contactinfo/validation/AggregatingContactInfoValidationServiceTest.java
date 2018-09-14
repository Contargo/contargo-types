package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInfoConsumer;
import net.contargo.types.contactinfo.ContactInformation;
import net.contargo.types.contactinfo.normalization.EmailAddressNormalizer;
import net.contargo.types.contactinfo.normalization.PhoneNumberNormalizer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class AggregatingContactInfoValidationServiceTest {

    private MobileAndEmailUniquenessValidator uniquenessValidator;
    private CompletenessValidator completenessValidator;

    @Before
    public void initValidators() {

        uniquenessValidator = new MobileAndEmailUniquenessValidator(new PhoneNumberNormalizer(),
                new EmailAddressNormalizer());
        completenessValidator = new ExternalUserCompletenessValidator();
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

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        ContactInformation contactInformationWithDuplicateMobile =
            contactInformationWithDuplicateMobileAfterNormalization();
        List<ValidationResult> validationResults = aggregatingContactInfoValidationService.validate(
                contactInformationWithDuplicateMobile);

        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_MOBILE, validationResults.get(0));
    }


    @Test
    public void ensureThatContactInfoWithNullValuesCanBeHandled() {

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);

        ContactInformation contactInfoWithNullValue1 = new ContactInformation("uuid1", "1234", null, null, "");
        ContactInformation contactInfoWithNullValue2 = new ContactInformation("uuid2", "1234", null, null, "");

        uniquenessValidator.consume(contactInfoWithNullValue1);

        assertDetectionOfDuplicateMobile(aggregatingContactInfoValidationService, contactInfoWithNullValue2);

        uniquenessValidator.remove(contactInfoWithNullValue1);

        assertThatNoValidationErrorIsReported(aggregatingContactInfoValidationService, contactInfoWithNullValue2);
    }


    @Test
    public void ensureThatDuplicateMobileIsDetected() {

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        assertDetectionOfDuplicateMobile(aggregatingContactInfoValidationService,
            contactInformationWithDuplicateMobile());
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAndNotDetectedAfterConflictingUserChangedMobile() {

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        assertDetectionOfDuplicateMobile(aggregatingContactInfoValidationService,
            contactInformationWithDuplicateMobile());

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid2", "12345", "4567",
                "foo-uuid2@bar", "bar@foo");
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233", "4567",
                "foo-uuid8@bar", "bar@foo");

        uniquenessValidator.consume(contactInfoToBeChanged);
        assertThatNoValidationErrorIsReported(aggregatingContactInfoValidationService, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAndNotDetectedAfterConflictingUserChangedMail() {

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid4", "12345", "4567", "foo@bazk",
                "bar@foo");
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "", "4567", "foo@bazl",
                "bar@foo");

        final AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);

        consumeColaProfiles(uniquenessValidator);

        assertDetectionOfDuplicateMail(aggregatingContactInfoValidationService, contactInfoToBeValidated);

        uniquenessValidator.consume(contactInfoToBeChanged);
        assertThatNoValidationErrorIsReported(aggregatingContactInfoValidationService, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAndNotDetectedAfterRemoval() {

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        assertDetectionOfDuplicateMobile(aggregatingContactInfoValidationService,
            contactInformationWithDuplicateMobile());

        ContactInformation contactInfoToBeRemoved = new ContactInformation("uuid2", "1233", "4567", "foo@bar",
                "bar@foo");
        ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233", "4567", "foo@bar",
                "bar@foo");
        uniquenessValidator.remove(contactInfoToBeRemoved);
        assertThatNoValidationErrorIsReported(aggregatingContactInfoValidationService, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAndNotDetectedAfterRemoval() {

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        assertDetectionOfDuplicateMail(aggregatingContactInfoValidationService,
            contactInformationWithDuplicateMailAfterNormalization());

        ContactInformation contactInfoToBeRemoved = new ContactInformation("uuid3", "1233", "4567", "foo@baz",
                "bar@foo");
        ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233123", "4567", "foo@baz",
                "bar@foo");
        uniquenessValidator.remove(contactInfoToBeRemoved);
        assertThatNoValidationErrorIsReported(aggregatingContactInfoValidationService, contactInfoToBeValidated);
    }


    private void assertDetectionOfDuplicateMail(
        final AggregatingContactInfoValidationService aggregatingContactInfoValidationService,
        final ContactInformation contactInformationWithDuplicateMail) {

        List<ValidationResult> validationResults = aggregatingContactInfoValidationService.validate(
                contactInformationWithDuplicateMail);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }


    private void assertDetectionOfDuplicateMobile(
        final AggregatingContactInfoValidationService aggregatingContactInfoValidationService,
        final ContactInformation contactInformationWithDuplicateMobile) {

        List<ValidationResult> validationResults = aggregatingContactInfoValidationService.validate(
                contactInformationWithDuplicateMobile);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_MOBILE, validationResults.get(0));
    }


    private void assertThatNoValidationErrorIsReported(
        AggregatingContactInfoValidationService aggregatingContactInfoValidationService,
        final ContactInformation contactInfoToBeCheckedForDuplicate) {

        List<ValidationResult> validationResults = aggregatingContactInfoValidationService.validate(
                contactInfoToBeCheckedForDuplicate);
        assertTrue("after the conflicting entry has been removed, no duplicate shall be detected",
            validationResults.size() == 0);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAfterNormalization() {

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithDuplicateMailAfterNormalization();
        List<ValidationResult> validationResults = aggregatingContactInfoValidationService.validate(
                contactInfoWithAllData);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }


    @Test
    public void ensureThatDuplicateMailIsDetected() {

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithAllDataAndDuplicateMailOfOtherUser();
        List<ValidationResult> validationResults = aggregatingContactInfoValidationService.validate(
                contactInfoWithAllData);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }


    @Test
    public void ensureThatUserOwnMobileIsNotHandledAsDuplicate() {

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithAllDataAndDistinctMobile();
        List<ValidationResult> validationResults = aggregatingContactInfoValidationService.validate(
                contactInfoWithAllData);
        assertEquals(0, validationResults.size());
    }


    @Test
    public void ensureThatUserOwnMailIsNotHandledAsDuplicate() {

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);
        consumeColaProfiles(uniquenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithAllDataAndDistinctMail();
        List<ValidationResult> validationResults = aggregatingContactInfoValidationService.validate(
                contactInfoWithAllData);
        assertTrue(validationResults.isEmpty());
    }


    @Test
    public void ensureThatContactInfoWithAllDataIsValid() {

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithAllData();
        List<ValidationResult> validationResults = aggregatingContactInfoValidationService.validate(
                contactInfoWithAllData);
        assertTrue(validationResults.isEmpty());
    }


    @Test
    public void ensureThatContactInfoWithoutPhoneNumberIsInvalid() {

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);

        ContactInformation contactInformationWithoutPhoneNumbers = contactInformationWithoutPhoneNumbers();
        List<ValidationResult> validationResults = aggregatingContactInfoValidationService.validate(
                contactInformationWithoutPhoneNumbers);

        assertEquals(2, validationResults.size());
        assertTrue(validationResults.contains(ValidationResult.MISSING_MOBILE));
        assertTrue(validationResults.contains(ValidationResult.MISSING_PHONE));
    }


    @Test
    public void ensureThatContactInfoWithoutMailAndMobileIsInvalid() {

        AggregatingContactInfoValidationService aggregatingContactInfoValidationService =
            new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);

        ContactInformation contactInfoWithoutEmailAndMobile = contactInformationWithoutEmailAndMobile();
        List<ValidationResult> validationResults = aggregatingContactInfoValidationService.validate(
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
