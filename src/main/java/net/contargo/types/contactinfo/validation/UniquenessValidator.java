package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInformation;

import java.util.List;


public interface UniquenessValidator {

    List<ValidationResult> checkUniqueness(ContactInformation contactInformation);


    boolean isEmailUnique(String userUuid, String email);


    boolean isMobileUnique(String userUuid, String mobile);
}
