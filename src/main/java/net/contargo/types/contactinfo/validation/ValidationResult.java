package net.contargo.types.contactinfo.validation;

public enum ValidationResult {

    MISSING_PHONE,
    MISSING_MOBILE,
    MISSING_EMAIL,
    NON_UNIQUE_PHONE, // TODO: implement this case
    NON_UNIQUE_MOBILE,
    NON_UNIQUE_EMAIL
}
