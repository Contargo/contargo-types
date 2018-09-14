package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInformation;

import java.util.List;


public interface CompletenessValidator {

    List<ValidationResult> checkCompleteness(ContactInformation contactInformation);
}
