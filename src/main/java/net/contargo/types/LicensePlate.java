package net.contargo.types;

/**
 * Each {@link net.contargo.domain.Truck} can be identified by its license plate.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.1.0
 */
public final class LicensePlate {

    private final String value;
    private LicensePlateCountry country;

    /**
     * Use {@link #forValue(String)} to build a new {@link LicensePlate} instance.
     *
     * @param  value  represents a license plate
     */
    private LicensePlate(String value) {

        this.value = value;
    }

    /**
     * Build a new {@link LicensePlate} with a {@link String} value.
     *
     * @param  value  represents a license plate
     *
     * @return  a {@link LicensePlate}, never {@code null}
     */
    public static LicensePlateBuilder forValue(String value) {

        Assert.notBlank(value, "Value for license plate must not be null or empty");

        return new LicensePlateBuilder(value);
    }


    /**
     * Return the {@link LicensePlate} in a formatted way.
     *
     * @return  formatted {@link LicensePlate}, never {@code null}
     */
    @Override
    public String toString() {

        return country.getLicensePlateHandler().format(value);
    }


    /**
     * Check if the {@link LicensePlate} is valid.
     *
     * @return  {@code true} if the {@link LicensePlate} is valid, else {@code false}
     */
    public boolean isValid() {

        return country.getLicensePlateHandler().validate(value);
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        String o1 = this.toString();
        String o2 = obj.toString();

        return o1.equals(o2);
    }


    @Override
    public int hashCode() {

        return this.toString().hashCode();
    }


    /**
     * Get the {@link LicensePlateCountry} of this {@link LicensePlate}.
     *
     * @return  country, may be empty
     */
    public LicensePlateCountry getCountry() {

        return country;
    }

    public static class LicensePlateBuilder {

        private LicensePlate licensePlate;

        private LicensePlateBuilder(String value) {

            this.licensePlate = new LicensePlate(value);
        }

        /**
         * Set the {@link LicensePlateCountry} for this {@link LicensePlate}.
         *
         * @param  country  never {@code null}
         *
         * @return  {@link LicensePlate}, never {@code null}
         */
        public LicensePlate withCountry(LicensePlateCountry country) {

            Assert.notNull(country, "Country must not be null");

            licensePlate.country = country;

            return licensePlate;
        }
    }
}
