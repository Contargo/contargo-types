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
                        containerType.getContargoHandlingCode()
                        .equals(contargoHandlingCode)).findFirst().orElseThrow(() ->
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

        return Arrays.stream(ContainerType.values())
            .filter(containerType -> containerType.getIsoCode().equals(isoCode))
            .findFirst()
            .orElseThrow(() ->
                    new IllegalArgumentException("Unknown container type ISO code given: "
                        + isoCode));
    }
}
