package net.contargo.types.contactinfo.validation;

import java.util.Locale;


public class EmailAddressNormalizer {

    public String normalizeEmailAddress(final String emailAddress) {

        if (emailAddress == null) {
            return "";
        }

        String normalized = emailAddress.trim();
        normalized = normalized.toLowerCase(Locale.getDefault());

        return normalized;
    }
}
