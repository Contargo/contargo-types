package net.contargo.types;

/**
 * The country of a {@link LicensePlate} describes in which country a car is registered.
 *
 * <p>For further information see the list of <a
 * href="https://en.wikipedia.org/wiki/List_of_international_vehicle_registration_codes">international license plate
 * country codes</a>.</p>
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
public interface LicensePlateCountry {

    /**
     * Get the international license plate country code for this country.
     *
     * <p>Example: 'D' for Germany</p>
     *
     * @return  country code, never {@code null}
     */
    String getCountryCode();


    /**
     * Get a handler that provides country dependent functionalities to handle {@link LicensePlate}.
     *
     * @return  the handler for this country, never {@code null}
     */
    LicensePlateHandler getLicensePlateHandler();
}
