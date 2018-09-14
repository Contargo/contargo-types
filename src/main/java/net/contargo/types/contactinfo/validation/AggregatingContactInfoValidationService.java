package net.contargo.types.contactinfo.validation;

import net.contargo.types.Loggable;
import net.contargo.types.contactinfo.ContactInformation;

import java.util.*;


public class AggregatingContactInfoValidationService implements Loggable {

    private final UniquenessValidator uniquenessValidator;
    private final CompletenessValidator completenessValidator;

    public AggregatingContactInfoValidationService(UniquenessValidator uniquenessValidator,
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
