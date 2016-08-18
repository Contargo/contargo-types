package net.contargo.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Factory providing {@link LicensePlateHandler}s.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
class LicensePlateHandlerFactory {

    private static final Map<Country, LicensePlateHandler> COUNTRY_HANDLERS;
    private static final LicensePlateHandler FALLBACK_HANDLER = new DefaultLicensePlateHandler();

    static {
        Map<Country, LicensePlateHandler> map = new HashMap<>();
        map.put(Country.GERMANY, new GermanLicensePlateHandler());

        COUNTRY_HANDLERS = Collections.unmodifiableMap(map);
    }

    /**
     * Get the matching {@link LicensePlateHandler} for the given {@link Country}.
     *
     * @param  country  to get the correct handler for, may be {@code null}
     *
     * @return  the matching handler for the given country or a fallback handler, never {@code null}
     */
    static LicensePlateHandler getForCountry(Country country) {

        if (country != null && COUNTRY_HANDLERS.containsKey(country)) {
            return COUNTRY_HANDLERS.get(country);
        }

        return FALLBACK_HANDLER;
    }
}
