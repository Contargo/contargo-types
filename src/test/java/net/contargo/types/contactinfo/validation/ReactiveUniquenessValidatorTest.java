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

    private void consumeColaProfiles(ContactInfoConsumer colaProfileConsumer) {

        List<ContactInformation> allProfiles = new ArrayList<>();

        ContactInformation contactInformation1 = new ContactInformation("uuid1", "1234", "4567", "foo1@bar"
        );
        allProfiles.add(contactInformation1);

        ContactInformation contactInformation2 = new ContactInformation("uuid2", "1233", "4567", "foo@bar");
        allProfiles.add(contactInformation2);

        ContactInformation contactInformation3 = new ContactInformation("uuid3", "171789987", "4567", "foo@baz"
        );
        allProfiles.add(contactInformation3);

        ContactInformation contactInformation4 = new ContactInformation("uuid4", "171789987", "4567", "foo@bazl"
        );
        allProfiles.add(contactInformation4);

        colaProfileConsumer.consume(allProfiles);
    }


    private ContactInformation contactInformationWithAllData() {

        return new ContactInformation("uuid1", "171789987", "721321451", "foo@bar");
    }


    private ContactInformation contactInformationWithAllDataAndDuplicateMailOfOtherUser() {

        return new ContactInformation("uuid1", "", "721321451", "foo@baz");
    }


    private ContactInformation contactInformationWithDuplicateMailAfterNormalization() {

        return new ContactInformation("uuid1-1", "", "721321451", "foo1@BAR");
    }


    private ContactInformation contactInformationWithAllDataAndDistinctMail() {

        return new ContactInformation("uuid3", "", "721321451", "foo@baz");
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

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());

        consumeColaProfiles(reactiveUniquenessValidator);

        ContactInformation contactInformationWithDuplicateMobile =
            contactInformationWithDuplicateMobileAfterNormalization();
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

        ContactInformation contactInfoWithNullValue1 = new ContactInformation("uuid1", "1234", null, null);
        ContactInformation contactInfoWithNullValue2 = new ContactInformation("uuid2", "1234", null, null);

        reactiveUniquenessValidator.consume(contactInfoWithNullValue1);

        assertDetectionOfDuplicateMobile(reactiveUniquenessValidator, contactInfoWithNullValue2);

        reactiveUniquenessValidator.remove(contactInfoWithNullValue1);

        assertThatNoValidationErrorIsReported(reactiveUniquenessValidator, contactInfoWithNullValue2);
    }


    @Test
    public void ensureThatDuplicateMobileIsDetected() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(reactiveUniquenessValidator);

        assertDetectionOfDuplicateMobile(reactiveUniquenessValidator, contactInformationWithDuplicateMobile());
    }

    @Test
    public void ensureThatDuplicateMobileIsDetectedAndNotDetectedAfterConflictingUserRemovedMobile() {

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid2", "", "4567",
                "foo-uuid2@bar");
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233", "4567",
                "foo-uuid8@bar");

        ensureDuplicateMobileIsDetectedAndNotDetected(contactInfoToBeChanged, contactInfoToBeValidated);
    }

    @Test
    public void ensureThatDuplicateMobileIsDetectedAndNotDetectedAfterConflictingUserChangedMobile() {

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid2", "12345", "4567",
                "foo-uuid2@bar");
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233", "4567",
                "foo-uuid8@bar");


        ensureDuplicateMobileIsDetectedAndNotDetected(contactInfoToBeChanged, contactInfoToBeValidated);
    }

    private void ensureDuplicateMobileIsDetectedAndNotDetected(ContactInformation contactInfoToBeChanged, ContactInformation contactInfoToBeValidated) {
        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(reactiveUniquenessValidator);

        assertDetectionOfDuplicateMobile(reactiveUniquenessValidator, contactInformationWithDuplicateMobile());

        reactiveUniquenessValidator.consume(contactInfoToBeChanged);
        assertThatNoValidationErrorIsReported(reactiveUniquenessValidator, contactInfoToBeValidated);
    }

    @Test
    public void ensureThatDuplicateMailIsDetectedAndNotDetectedAfterConflictingUserRemovedMail() {

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid4", "12345", "4567", ""
        );
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "", "4567", "foo@bazl"
        );

        ensureDuplicateMailIsDetectedAndNotDected(contactInfoToBeChanged, contactInfoToBeValidated);
    }

    @Test
    public void ensureThatDuplicateMailIsDetectedAndNotDetectedAfterConflictingUserChangedMail() {

        final ContactInformation contactInfoToBeChanged = new ContactInformation("uuid4", "12345", "4567", "foo@bazk"
        );
        final ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "", "4567", "foo@bazl"
        );

        ensureDuplicateMailIsDetectedAndNotDected(contactInfoToBeChanged, contactInfoToBeValidated);
    }

    private void ensureDuplicateMailIsDetectedAndNotDected(ContactInformation contactInfoToBeChanged, ContactInformation contactInfoToBeValidated) {
        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());

        consumeColaProfiles(reactiveUniquenessValidator);

        assertDetectionOfDuplicateMail(reactiveUniquenessValidator, contactInfoToBeValidated);

        reactiveUniquenessValidator.consume(contactInfoToBeChanged);
        assertThatNoValidationErrorIsReported(reactiveUniquenessValidator, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMobileIsDetectedAndNotDetectedAfterRemoval() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(reactiveUniquenessValidator);

        assertDetectionOfDuplicateMobile(reactiveUniquenessValidator, contactInformationWithDuplicateMobile());

        ContactInformation contactInfoToBeRemoved = new ContactInformation("uuid2", "1233", "4567", "foo@bar"
        );
        ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233", "4567", "foo@bar"
        );
        reactiveUniquenessValidator.remove(contactInfoToBeRemoved);
        assertThatNoValidationErrorIsReported(reactiveUniquenessValidator, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMailIsDetectedAndNotDetectedAfterRemoval() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(reactiveUniquenessValidator);

        assertDetectionOfDuplicateMail(reactiveUniquenessValidator,
            contactInformationWithDuplicateMailAfterNormalization());

        ContactInformation contactInfoToBeRemoved = new ContactInformation("uuid3", "1233", "4567", "foo@baz"
        );
        ContactInformation contactInfoToBeValidated = new ContactInformation("uuid8", "1233123", "4567", "foo@baz"
        );
        reactiveUniquenessValidator.remove(contactInfoToBeRemoved);
        assertThatNoValidationErrorIsReported(reactiveUniquenessValidator, contactInfoToBeValidated);
    }


    @Test
    public void ensureThatDuplicateMailIsDetected() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(reactiveUniquenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithAllDataAndDuplicateMailOfOtherUser();
        List<ValidationResult> validationResults = reactiveUniquenessValidator.checkUniqueness(contactInfoWithAllData);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }


    @Test
    public void ensureThatUserOwnMobileIsNotHandledAsDuplicate() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(reactiveUniquenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithAllDataAndDistinctMobile();
        List<ValidationResult> validationResults = reactiveUniquenessValidator.checkUniqueness(contactInfoWithAllData);
        assertEquals(0, validationResults.size());
    }


    @Test
    public void ensureThatUserOwnMailIsNotHandledAsDuplicate() {

        ReactiveUniquenessValidator reactiveUniquenessValidator = new ReactiveUniquenessValidator(
                new PhoneNumberNormalizer(), new EmailAddressNormalizer());
        consumeColaProfiles(reactiveUniquenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithAllDataAndDistinctMail();
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
        consumeColaProfiles(reactiveUniquenessValidator);

        ContactInformation contactInfoWithAllData = contactInformationWithDuplicateMailAfterNormalization();
        List<ValidationResult> validationResults = reactiveUniquenessValidator.checkUniqueness(contactInfoWithAllData);
        assertEquals(1, validationResults.size());
        assertEquals(ValidationResult.NON_UNIQUE_EMAIL, validationResults.get(0));
    }
}
