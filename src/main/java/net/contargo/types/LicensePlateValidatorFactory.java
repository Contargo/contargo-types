package net.contargo.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Factory providing a {@link LicensePlateValidator}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
class LicensePlateValidatorFactory {

    private static final Map<Country, LicensePlateValidator> VALIDATORS;
    private static final LicensePlateValidator FALLBACK_VALIDATOR = new DefaultLicensePlateValidator();

    static {
        Map<Country, LicensePlateValidator> map = new HashMap<>();
        map.put(Country.GERMANY, new GermanLicensePlateValidator());

        VALIDATORS = Collections.unmodifiableMap(map);
    }

    /**
     * Get the matching {@link LicensePlateValidator} for the given {@link Country}.
     *
     * @param  country  to get the correct validator for, may be {@code null}
     *
     * @return  the matching validator for the given country or a fallback validator, never {@code null}
     */
    static LicensePlateValidator getForCountry(Country country) {

        if (country != null && VALIDATORS.containsKey(country)) {
            return VALIDATORS.get(country);
        }

        return FALLBACK_VALIDATOR;
    }
}
