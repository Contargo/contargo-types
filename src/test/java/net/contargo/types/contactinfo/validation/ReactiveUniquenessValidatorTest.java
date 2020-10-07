package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInfoConsumer;
import net.contargo.types.contactinfo.ContactInformation;
import net.contargo.types.contactinfo.normalization.EmailAddressNormalizer;
import net.contargo.types.contactinfo.normalization.PhoneNumberNormalizer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ReactiveUniquenessValidatorTest {

    private void consumeColaProfilesFixtures(ContactInfoConsumer colaProfileConsumer) {

        List<ContactInformation> allProfiles = new ArrayList<>();

        allProfiles.add(new ContactInformation("uuid1", "01234", "04567", "foo1@bar"));
        allProfiles.add(new ContactInformation("uuid2", "01233", "04567", "foo@bar"));
        allProfiles.add(new ContactInformation("uuid3", "0171789987", "04567", "foo@baz"));
        allProfiles.add(new ContactInformation("uuid4", "0171789987", "04567", "foo@bazl"));

        colaProfileConsumer.consume(allProfiles);
    }


    @Test
    public void ensureThatResetClearsAllData() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());

        consumeColaProfilesFixtures(reactiveUniquenessValidator);

        reactiveUniquenessValidator.reset();

        ContactInformation contactInformationWithDuplicateMobile = new ContactInformation("uuid5", "+49171789987",
                "0721321451", null);
        List<ValidationResult> validationResults = reactiveUniquenessValidator.checkUniqueness(
                contactInformationWithDuplicateMobile);

        assertEquals(0, validationResults.size());
        // no validation errors since data of validator should be empty
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAfterNormalization() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());

        consumeColaProfilesFixtures(reactiveUniquenessValidator);

        ContactInformation contactInformationWithDuplicateMobile = new ContactInformation("uuid5", "+49171789987",
                "0721321451", null);
        List<ValidationResult> validationResults = reactiveUniquenessValidator.checkUniqueness(
                contactInformationWithDuplicateMobile);

        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_MOBILE, validationResults.get(0));
    }


    @Test
    public void ensureThatConsumingNullProfileDoesNotThrow() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        reactiveUniquenessValidator.consume((ContactInformation) null);
        reactiveUniquenessValidator.consume((List<ContactInformation>) null);
    }


    @Test
    public void ensureThatContactInfoWithNullValuesCanBeHandled() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());

        ContactInformation contactInfoWithNullValue1 = new ContactInformation("uuid1", "001234", null, null);
        ContactInformation contactInfoWithNullValue2 = new ContactInformation("uuid2", "001234", null, null);

        reactiveUniquenessValidator.consume(contactInfoWithNullValue1);

        assertDetectionOfDuplicateMobile(reactiveUniquenessValidator, contactInfoWithNullValue2);

        reactiveUniquenessValidator.remove(contactInfoWithNullValue1);

        assertThatNoValidationErrorIsReported(reactiveUniquenessValidator, contactInfoWithNullValue2);
    }


    @Test
    public void ensureThatDuplicateMobileIsDetected() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfilesFixtures(reactiveUniquenessValidator);

        assertDetectionOfDuplicateMobile(reactiveUniquenessValidator,
            new ContactInformation("uuid5", "0171789987", "0721321451", null));
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAndNotDetectedAfterConflictingUserRemovedMobile() {

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid2", "", "004567",
                "foo-uuid2@bar");
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "001233", "004567",
                "foo-uuid8@bar");

        ensureDuplicateMobileIsDetectedAndNotDetected(contactInfoToBeChanged, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAndNotDetectedAfterConflictingUserChangedMobile() {

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid2", "012345", "04567",
                "foo-uuid2@bar");
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "012335", "04567",
                "foo-uuid8@bar");

        ensureDuplicateMobileIsDetectedAndNotDetected(contactInfoToBeChanged, contactInfoToBeValidated);
    }


    private void ensureDuplicateMobileIsDetectedAndNotDetected(ContactInformation contactInfoToBeChanged,
        ContactInformation contactInfoToBeValidated) {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfilesFixtures(reactiveUniquenessValidator);

        assertDetectionOfDuplicateMobile(reactiveUniquenessValidator,
            new ContactInformation("uuid5", "0171789987", "0721321451", null));

        reactiveUniquenessValidator.consume(contactInfoToBeChanged);
        assertThatNoValidationErrorIsReported(reactiveUniquenessValidator, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAndNotDetectedAfterConflictingUserRemovedMail() {

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid4", "012345", "04567", "");
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "", "04567", "foo@bazl");

        ensureDuplicateMailIsDetectedAndNotDected(contactInfoToBeChanged, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAndNotDetectedAfterConflictingUserChangedMail() {

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid4", "012345", "04567",
                "foo@bazk");
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "", "04567", "foo@bazl");

        ensureDuplicateMailIsDetectedAndNotDected(contactInfoToBeChanged, contactInfoToBeValidated);
    }


    private void ensureDuplicateMailIsDetectedAndNotDected(ContactInformation contactInfoToBeChanged,
        ContactInformation contactInfoToBeValidated) {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());

        consumeColaProfilesFixtures(reactiveUniquenessValidator);

        assertDetectionOfDuplicateMail(reactiveUniquenessValidator, contactInfoToBeValidated);

        reactiveUniquenessValidator.consume(contactInfoToBeChanged);
        assertThatNoValidationErrorIsReported(reactiveUniquenessValidator, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAndNotDetectedAfterRemoval() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfilesFixtures(reactiveUniquenessValidator);

        assertDetectionOfDuplicateMobile(reactiveUniquenessValidator,
            new ContactInformation("uuid5", "0171789987", "0721321451", null));

        ContactInformation contactInfoToBeRemoved = new ContactInformation("uuid2", "01233", "04567", "foo@bar");
        ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "01233", "04567", "foo@bar");
        reactiveUniquenessValidator.remove(contactInfoToBeRemoved);
        assertThatNoValidationErrorIsReported(reactiveUniquenessValidator, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAndNotDetectedAfterRemoval() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfilesFixtures(reactiveUniquenessValidator);

        assertDetectionOfDuplicateMail(reactiveUniquenessValidator,
            new ContactInformation("uuid1-1", "", "0721321451", "foo1@BAR"));

        ContactInformation contactInfoToBeRemoved = new ContactInformation("uuid3", "01233", "04567", "foo@baz");
        ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "01233123", "04567", "foo@baz");
        reactiveUniquenessValidator.remove(contactInfoToBeRemoved);
        assertThatNoValidationErrorIsReported(reactiveUniquenessValidator, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMailIsDetected() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfilesFixtures(reactiveUniquenessValidator);

        ContactInformation contactInfoWithAllData = new ContactInformation("uuid1", "", "0721321451", "foo@baz");
        List<ValidationResult> validationResults = reactiveUniquenessValidator.checkUniqueness(contactInfoWithAllData);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }


    @Test
    public void ensureThatUserOwnMobileIsNotHandledAsDuplicate() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfilesFixtures(reactiveUniquenessValidator);

        ContactInformation contactInfoWithAllData = new ContactInformation("uuid4", "0171789988", "0721321451",
                "foo@barz");
        List<ValidationResult> validationResults = reactiveUniquenessValidator.checkUniqueness(contactInfoWithAllData);
        assertEquals(0, validationResults.size());
    }


    @Test
    public void ensureThatUserOwnMailIsNotHandledAsDuplicate() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfilesFixtures(reactiveUniquenessValidator);

        ContactInformation contactInfoWithAllData = new ContactInformation("uuid3", "", "0721321451", "foo@baz");
        List<ValidationResult> validationResults = reactiveUniquenessValidator.checkUniqueness(contactInfoWithAllData);
        assertTrue(validationResults.isEmpty());
    }


    @Test
    public void ensureThatConsumesRegistrationsIsSetAndReportedCorrectly() {

        ContactInfoConsumer uniquenessValidator = new ReactiveUniquenessValidator(new PhoneNumberNormalizer(),
                new EmailAddressNormalizer());
        assertTrue(uniquenessValidator.consumesRegistrations());

        ContactInfoConsumer uniquenessValidatorWithRegistrations = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer(), false);
        assertFalse(uniquenessValidatorWithRegistrations.consumesRegistrations());
    }


    private void assertDetectionOfDuplicateMail(final UniquenessValidator uniquenessValidator,
        final ContactInformation contactInformationWithDuplicateMail) {

        List<ValidationResult> validationResults = uniquenessValidator.checkUniqueness(
                contactInformationWithDuplicateMail);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }


    private void assertDetectionOfDuplicateMobile(final UniquenessValidator uniquenessValidator,
        final ContactInformation contactInformationWithDuplicateMobile) {

        List<ValidationResult> validationResults = uniquenessValidator.checkUniqueness(
                contactInformationWithDuplicateMobile);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_MOBILE, validationResults.get(0));
    }


    private void assertThatNoValidationErrorIsReported(UniquenessValidator uniquenessValidator,
        final ContactInformation contactInfoToBeCheckedForDuplicate) {

        List<ValidationResult> validationResults = uniquenessValidator.checkUniqueness(
                contactInfoToBeCheckedForDuplicate);
        assertTrue("after the conflicting entry has been removed, no duplicate shall be detected",
            validationResults.size() == 0);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAfterNormalization() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfilesFixtures(reactiveUniquenessValidator);

        ContactInformation contactInfoWithAllData = new ContactInformation("uuid1-1", "", "0721321451", "foo1@BAR");
        List<ValidationResult> validationResults = reactiveUniquenessValidator.checkUniqueness(contactInfoWithAllData);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }
}
