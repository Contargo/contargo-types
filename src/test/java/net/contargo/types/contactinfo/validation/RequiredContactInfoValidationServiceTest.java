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

        return new ContactInformation("uuid1", "171789987", "721321451", "foo@baz", "bar@foo");
    }


    private ContactInformation contactInformationWithDuplicateMailAfterNormalization() {

        return new ContactInformation("uuid1-1", "171789987", "721321451", "foo1@BAR", "bar@foo");
    }


    private ContactInformation contactInformationWithAllDataAndDistinctMail() {

        return new ContactInformation("uuid3", "171789987", "721321451", "foo@baz", "bar@foo");
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
    public void ensureThatContactInfoWithNullValuesCanBeHandled() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());

        ContactInformation contactInfoWithNullValue1 = new ContactInformation("uuid1", "1234", null, null, "");
        ContactInformation contactInfoWithNullValue2 = new ContactInformation("uuid2", "1234", null, null, "");

        requiredContactInfoValidationService.consume(contactInfoWithNullValue1);

        assertDetectionOfDuplicateMobile(requiredContactInfoValidationService, contactInfoWithNullValue2);

        requiredContactInfoValidationService.remove(contactInfoWithNullValue1);

        assertThatNoValidationErrorIsReported(requiredContactInfoValidationService, contactInfoWithNullValue2);
    }


    @Test
    public void ensureThatDuplicateMobileIsDetected() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(requiredContactInfoValidationService);

        assertDetectionOfDuplicateMobile(requiredContactInfoValidationService,
            contactInformationWithDuplicateMobile());
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAndNotDetectedAfterConflictingUserChangedMobile() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(requiredContactInfoValidationService);

        assertDetectionOfDuplicateMobile(requiredContactInfoValidationService,
            contactInformationWithDuplicateMobile());

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid2", "12345", "4567",
                "foo-uuid2@bar", "bar@foo");
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233", "4567",
                "foo-uuid8@bar", "bar@foo");

        requiredContactInfoValidationService.consume(contactInfoToBeChanged);
        assertThatNoValidationErrorIsReported(requiredContactInfoValidationService, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAndNotDetectedAfterConflictingUserChangedMail() {

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid4", "12345", "4567", "foo@bazk",
                "bar@foo");
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233", "4567", "foo@bazl",
                "bar@foo");

        final RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());

        consumeColaProfiles(requiredContactInfoValidationService);

        assertDetectionOfDuplicateMail(requiredContactInfoValidationService, contactInfoToBeValidated);

        requiredContactInfoValidationService.consume(contactInfoToBeChanged);
        assertThatNoValidationErrorIsReported(requiredContactInfoValidationService, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAndNotDetectedAfterRemoval() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(requiredContactInfoValidationService);

        assertDetectionOfDuplicateMobile(requiredContactInfoValidationService,
            contactInformationWithDuplicateMobile());

        ContactInformation contactInfoToBeRemoved = new ContactInformation("uuid2", "1233", "4567", "foo@bar",
                "bar@foo");
        ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233", "4567", "foo@bar",
                "bar@foo");
        requiredContactInfoValidationService.remove(contactInfoToBeRemoved);
        assertThatNoValidationErrorIsReported(requiredContactInfoValidationService, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAndNotDetectedAfterRemoval() {

        RequiredContactInfoValidationService requiredContactInfoValidationService =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(requiredContactInfoValidationService);

        assertDetectionOfDuplicateMail(requiredContactInfoValidationService,
            contactInformationWithDuplicateMailAfterNormalization());

        ContactInformation contactInfoToBeRemoved = new ContactInformation("uuid3", "1233", "4567", "foo@baz",
                "bar@foo");
        ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233123", "4567", "foo@baz",
                "bar@foo");
        requiredContactInfoValidationService.remove(contactInfoToBeRemoved);
        assertThatNoValidationErrorIsReported(requiredContactInfoValidationService, contactInfoToBeValidated);
    }


    private void assertDetectionOfDuplicateMail(
        final RequiredContactInfoValidationService requiredContactInfoValidationService,
        final ContactInformation contactInformationWithDuplicateMail) {

        List<RequiredContactInfoValidationService.ValidationResult> validationResults = new ArrayList<>();
        boolean isValid = requiredContactInfoValidationService.validate(contactInformationWithDuplicateMail,
                validationResults);
        assertFalse(isValid);
        assertEquals(1, validationResults.size());
        assertEquals(RequiredContactInfoValidationService.ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }


    private void assertDetectionOfDuplicateMobile(
        final RequiredContactInfoValidationService requiredContactInfoValidationService,
        final ContactInformation contactInformationWithDuplicateMobile) {

        List<RequiredContactInfoValidationService.ValidationResult> validationResults = new ArrayList<>();
        boolean isValid = requiredContactInfoValidationService.validate(contactInformationWithDuplicateMobile,
                validationResults);
        assertFalse(isValid);
        assertEquals(1, validationResults.size());
        assertEquals(RequiredContactInfoValidationService.ValidationResult.NON_UNIQUE_MOBILE,
            validationResults.get(0));
    }


    private void assertThatNoValidationErrorIsReported(
        RequiredContactInfoValidationService requiredContactInfoValidationService,
        final ContactInformation contactInfoToBeCheckedForDuplicate) {

        List<RequiredContactInfoValidationService.ValidationResult> validationResults = new ArrayList<>();
        requiredContactInfoValidationService.validate(contactInfoToBeCheckedForDuplicate, validationResults);
        assertTrue("after the conflicting entry has been removed, no duplicate shall be detected",
            validationResults.size() == 0);
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
        ContactInformation contactInfoWithAllData = contactInformationWithAllDataAndDuplicateMailOfOtherUser();
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


    @Test
    public void ensureThatConsumesRegistrationsIsSetAndReportedCorrectly() {

        RequiredContactInfoValidationService requiredContactInfoValidationService1 =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        assertTrue(requiredContactInfoValidationService1.consumesRegistrations());

        RequiredContactInfoValidationService requiredContactInfoValidationService2 =
            new RequiredContactInfoValidationService(new PhoneNumberNormalizer(), new EmailAddressNormalizer(), false);
        assertFalse(requiredContactInfoValidationService2.consumesRegistrations());
    }
}
