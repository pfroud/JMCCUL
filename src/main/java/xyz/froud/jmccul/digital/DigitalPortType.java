package xyz.froud.jmccul.digital;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Enumerations/MccDaq.DigitalPortType.htm
 *
 * @author Peter Froud
 */
public enum DigitalPortType {
    AUX_PORT(MeasurementComputingUniversalLibrary.AUXPORT),
    AUX_PORT_0(MeasurementComputingUniversalLibrary.AUXPORT0),
    AUX_PORT_1(MeasurementComputingUniversalLibrary.AUXPORT1),
    AUX_PORT_2(MeasurementComputingUniversalLibrary.AUXPORT2),
    /////////////////////////////////////////////////////////////////////////////
    FIRST_PORT_A(MeasurementComputingUniversalLibrary.FIRSTPORTA),
    FIRST_PORT_B(MeasurementComputingUniversalLibrary.FIRSTPORTB),
    FIRST_PORT_C(MeasurementComputingUniversalLibrary.FIRSTPORTC),
    FIRST_PORT_C_LOW(MeasurementComputingUniversalLibrary.FIRSTPORTCL),
    FIRST_PORT_C_HIGH(MeasurementComputingUniversalLibrary.FIRSTPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    SECOND_PORT_A(MeasurementComputingUniversalLibrary.SECONDPORTA),
    SECOND_PORT_B(MeasurementComputingUniversalLibrary.SECONDPORTB),
    SECOND_PORT_C_LOW(MeasurementComputingUniversalLibrary.SECONDPORTCL),
    SECOND_PORT_C_HIGH(MeasurementComputingUniversalLibrary.SECONDPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    THIRD_PORT_A(MeasurementComputingUniversalLibrary.THIRDPORTA),
    THIRD_PORT_B(MeasurementComputingUniversalLibrary.THIRDPORTB),
    THIRD_PORT_C_LOW(MeasurementComputingUniversalLibrary.THIRDPORTCL),
    THIRD_PORT_C_HIGH(MeasurementComputingUniversalLibrary.THIRDPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    FOURTH_PORT_A(MeasurementComputingUniversalLibrary.FOURTHPORTA),
    FOURTH_PORT_B(MeasurementComputingUniversalLibrary.FOURTHPORTB),
    FOURTH_PORT_C_LOW(MeasurementComputingUniversalLibrary.FOURTHPORTCL),
    FOURTH_PORT_C_HIGH(MeasurementComputingUniversalLibrary.FOURTHPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    FIFTH_PORT_A(MeasurementComputingUniversalLibrary.FIFTHPORTA),
    FIFTH_PORT_B(MeasurementComputingUniversalLibrary.FIFTHPORTB),
    FIFTH_PORT_C_LOW(MeasurementComputingUniversalLibrary.FIFTHPORTCL),
    FIFTH_PORT_C_HIGH(MeasurementComputingUniversalLibrary.FIFTHPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    SIXTH_PORT_A(MeasurementComputingUniversalLibrary.SIXTHPORTA),
    SIXTH_PORT_B(MeasurementComputingUniversalLibrary.SIXTHPORTB),
    SIXTH_PORT_C_LOW(MeasurementComputingUniversalLibrary.SIXTHPORTCL),
    SIXTH_PORT_C_HIGH(MeasurementComputingUniversalLibrary.SIXTHPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    SEVENTH_PORT_A(MeasurementComputingUniversalLibrary.SEVENTHPORTA),
    SEVENTH_PORT_B(MeasurementComputingUniversalLibrary.SEVENTHPORTB),
    SEVENTH_PORT_C_LOW(MeasurementComputingUniversalLibrary.SEVENTHPORTCL),
    SEVENTH_PORT_C_HIGH(MeasurementComputingUniversalLibrary.SEVENTHPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    EIGHTH_PORT_A(MeasurementComputingUniversalLibrary.EIGHTHPORTA),
    EIGHTH_PORT_B(MeasurementComputingUniversalLibrary.EIGHTHPORTB),
    EIGHTH_PORT_C_LOW(MeasurementComputingUniversalLibrary.EIGHTHPORTCL),
    EIGHTH_PORT_C_HIGH(MeasurementComputingUniversalLibrary.EIGHTHPORTCH);

    private static final Map<Integer, DigitalPortType> valueMap;

    static {
        final DigitalPortType[] allTypes = DigitalPortType.values();
        valueMap = new HashMap<>(allTypes.length, 1);
        for (DigitalPortType type : allTypes) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static DigitalPortType parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    DigitalPortType(int value) {
        VALUE = value;

    }
}
