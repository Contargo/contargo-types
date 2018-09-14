package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInformation;
import net.contargo.types.util.Loggable;

import java.util.*;


public class ContactInfoValidationService implements Loggable {

    private final UniquenessValidator uniquenessValidator;
    private final CompletenessValidator completenessValidator;

    public ContactInfoValidationService(UniquenessValidator uniquenessValidator,
        CompletenessValidator completenessValidator) {

        this.uniquenessValidator = uniquenessValidator;
        this.completenessValidator = completenessValidator;
    }

    public List<ValidationResult> validate(final ContactInformation contactInformation) {

        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults.addAll(completenessValidator.checkCompleteness(contactInformation));
        validationResults.addAll(uniquenessValidator.checkUniqueness(contactInformation));

        return validationResults;
    }
}
