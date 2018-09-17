package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInformation;

import java.util.List;


/**
 * Implementations of this interface check the completeness of a
 * {@link net.contargo.types.contactinfo.ContactInfoConsumer}.
 */
public interface CompletenessValidator {

    List<ValidationResult> checkCompleteness(ContactInformation contactInformation);
}
