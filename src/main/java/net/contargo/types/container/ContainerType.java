package net.contargo.types.container;

/**
 * Describes the type of a {@link net.contargo.domain.Container}. The type specifies the
 * {@link net.contargo.domain.Container}'s measure and weight.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @since  0.2.0
 */
public enum ContainerType {

    TWENTY_BO("20BO", "22B0"),
    TWENTY_BU("20BU", "22B0"),
    TWENTY_DV("20DV", "22G0"),
    TWENTY_FL("20FL", "22P1"),
    TWENTY_FS("20FS", "22P3"),
    TWENTY_HC("20HC", "25G0"),
    TWENTY_HO("20HO", "22U6"),
    TWENTY_HR("20HR", "20HR"),
    TWENTY_HT("20HT", "22U6"),
    TWENTY_IS("20IS", "22H5"),
    TWENTY_OT("20OT", "22U0"),
    TWENTY_PH("20PH", "25N0"),
    TWENTY_PW("20PW", "25X0"),
    TWENTY_RF("20RF", "22R0"),
    TWENTY_TK("20TK", "22T0"),
    TWENTY_VE("20VE", "22V0"),
    FORTY_BU("40BU", "40BU"),
    FORTY_CH("40CH", "40CH"),
    FORTY_CS("40CS", "40CS"),
    FORTY_DV("40DV", "42G0"),
    FORTY_FL("40FL", "42P1"),
    FORTY_FS("40FS", "42P3"),
    FORTY_HC("40HC", "45G0"),
    FORTY_HF("40HF", "40HF"),
    FORTY_HR("40HR", "49R0"),
    FORTY_HT("40HT", "42H6"),
    FORTY_IS("40IS", "42H5"),
    FORTY_OH("40OH", "4551"),
    FORTY_OT("40OT", "42U0"),
    FORTY_PH("40PH", "40PH"),
    FORTY_PL("40PL", "40PL"),
    FORTY_PW("40PW", "4NG0"),
    FORTY_RF("40RF", "42R0"),
    FORTY_SC("40SC", "40SC"),
    FORTY_TK("40TK", "42T0"),
    FORTY_VE("40VE", "42V0");

    private final String isoCode;
    private final String internationalIsoCode;

    ContainerType(String isoCode, String internationalIsoCode) {

        this.isoCode = isoCode;
        this.internationalIsoCode = internationalIsoCode;
    }

    /**
     * Get the ISO code as established and used by Contargo.
     *
     * @return  the internal Contargo ISO code string
     */
    public String getIsoCode() {

        return isoCode;
    }


    /**
     * Get the international ISO code (ISO 6346).
     *
     * @return  the international standard ISO 6346 code string
     */
    public String getInternationalIsoCode() {

        return internationalIsoCode;
    }


    /**
     * Find the matching container type for the given ISO code.
     *
     * @param  isoCode  of the container type to find
     *
     * @return  the matching container type
     *
     * @throws  IllegalArgumentException  if no matching container type can be found for the given ISO code
     */
    public static ContainerType byIsoSize(String isoCode) {

        for (ContainerType containerType : ContainerType.values()) {
            if (containerType.getIsoCode().equals(isoCode)) {
                return containerType;
            }
        }

        throw new IllegalArgumentException("Unknown container type ISO code given: " + isoCode);
    }


    /**
     * Find the matching container type for the given international ISO code (ISO 6346).
     *
     * @param  internationalIsoCode  of the container type to find
     *
     * @return  the matching container type
     *
     * @throws  IllegalArgumentException  if no matching container type can be found for the given international ISO
     *                                    code
     */
    public static ContainerType byInternationalIsoSize(String internationalIsoCode) {

        for (ContainerType containerType : ContainerType.values()) {
            if (containerType.getInternationalIsoCode().equals(internationalIsoCode)) {
                return containerType;
            }
        }

        throw new IllegalArgumentException("Unknown international container type ISO code given: "
            + internationalIsoCode);
    }
}
