package net.contargo.types.container;

import java.util.Arrays;


/**
 * Describes the type of a {@link net.contargo.domain.Container}. The type specifies the
 * {@link net.contargo.domain.Container}'s measure and weight.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Slaven Travar - slaven.travar@pta.de
 * @see  net.contargo.domain.ContainerType
 * @since  0.2.0
 */
public enum ContainerType implements net.contargo.domain.ContainerType {

    TEN_DV("10DV", "10G0"),
    TWENTY_BH("20BH", "25B1"),
    TWENTY_BO("20BO", "22B0"),
    TWENTY_BU("20BU", "22B0"),
    TWENTY_DL("20DL", "20DL"),
    TWENTY_DV("20DV", "22G0"),
    TWENTY_FL("20FL", "22P1"),
    TWENTY_FS("20FS", "22P3"),
    TWENTY_HC("20HC", "25G0"),
    TWENTY_HO("20HO", "22U6"),
    TWENTY_HR("20HR", "20HR"),
    TWENTY_HT("20HT", "22U6"),
    TWENTY_IS("20IS", "22H5"),
    TWENTY_OS("20OS", "22G2"),
    TWENTY_OT("20OT", "22U0"),
    TWENTY_PH("20PH", "25N0"),
    TWENTY_PL("20PL", "29P0"),
    TWENTY_PW("20PW", "25X0"),
    TWENTY_RF("20RF", "22R0"),
    TWENTY_TK("20TK", "22T0"),
    TWENTY_VE("20VE", "22V0"),
    TWENTY_OH("20OH", "25UT"),
    TWENTYTWO_TK("22TK", "22T0"),
    TWENTYTHREE_TK("23TK", "22T0"),
    TWENTYFOUR_TK("24TK", "22T0"),
    TWENTYFIVE_DV("25DV", "C2G0"),
    TWENTYSIX_TK("26TK", "22T0"),
    THIRTY_BU("30BU", "30BU"),
    THIRTY_DV("30DV", "32G0"),
    THIRTY_FL("30FL", "32P1"),
    THIRTY_FS("30FS", "32P3"),
    THIRTY_TK("30TK", "32T0"),
    THIRTYTHREE_TK("33TK", "32T0"),
    THIRTYFIVE_TK("35TK", "32T0"),
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
    FORTY_OS("40OS", "42U0"),
    FORTY_OT("40OT", "42U0"),
    FORTY_PH("40PH", "40PH"),
    FORTY_PL("40PL", "40PL"),
    FORTY_PW("40PW", "4NG0"),
    FORTY_RF("40RF", "42R0"),
    FORTYFIVE_RF("45RF", "L2R0"),
    FORTYFIVE_HR("45HR", "L5R0"),
    FORTY_SC("40SC", "40SC"),
    FORTY_TK("40TK", "42T0"),
    FORTY_VE("40VE", "42V0"),
    FORTYFIVE_DV("45DV", "L2G0"),
    FORTYFIVE_HC("45HC", "L5G0"),
    FORTYFIVE_PH("45PH", "45PH"),
    FORTYFIVE_PW("45PW", "LNG0");

    private final String contargoHandlingCode;
    private final String isoCode;

    ContainerType(String contargoHandlingCode, String isoCode) {

        this.contargoHandlingCode = contargoHandlingCode;
        this.isoCode = isoCode;
    }

    /**
     * Get the handling code as established and used by Contargo.
     *
     * @return  the internal Contargo handling code string
     */
    public String getContargoHandlingCode() {

        return contargoHandlingCode;
    }


    /**
     * Get the ISO code (ISO 6346).
     *
     * @return  the standard ISO 6346 code string
     */
    public String getIsoCode() {

        return isoCode;
    }


    /**
     * Find the matching container type for the given Contargo handling code.
     *
     * @param  contargoHandlingCode  of the container type to find
     *
     * @return  the matching container type
     *
     * @throws  IllegalArgumentException  if no matching container type can be found for the given Contargo handling
     *                                    code
     */
    public static ContainerType byContargoHandlingCode(String contargoHandlingCode) {

        return Arrays.stream(ContainerType.values()).filter(containerType ->
                        containerType.getContargoHandlingCode().equals(contargoHandlingCode))
            .findFirst()
            .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Unknown container type Contargo handling code given: "
                        + contargoHandlingCode));
    }


    /**
     * Find the first matching container type for the given ISO code (ISO 6346) (matching from ISO code to Contargo
     * handling code is unambiguous!).
     *
     * @param  isoCode  of the container type to find
     *
     * @return  the matching container type
     *
     * @throws  IllegalArgumentException  if no matching container type can be found for the given ISO code
     */
    public static ContainerType byIsoCode(String isoCode) {

        return Arrays.stream(ContainerType.values()).filter(containerType ->
                        containerType.getIsoCode()
                        .equals(isoCode)).findFirst().orElseThrow(() ->
                    new IllegalArgumentException("Unknown container type ISO code given: "
                        + isoCode));
    }
}
