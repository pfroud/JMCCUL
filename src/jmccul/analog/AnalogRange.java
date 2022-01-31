package jmccul.analog;

import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/ULStart.htm#Misc/Supported_A_D_Ranges.htm
 *
 *
 * https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/enums.py#L652
 *
 * This enum is used for both digital-to-analog and analog-to-digital operations.
 *
 * @author Peter Froud
 */
public enum AnalogRange {
    BIPOLAR_60_VOLTS(MeasurementComputingUniversalLibrary.BIP60VOLTS, RangeType.BIPOLAR, 60),
    BIPOLAR_20_VOLTS(MeasurementComputingUniversalLibrary.BIP20VOLTS, RangeType.BIPOLAR, 20),
    BIPOLAR_15_VOLTS(MeasurementComputingUniversalLibrary.BIP15VOLTS, RangeType.BIPOLAR, 15),
    BIPOLAR_10_VOLTS(MeasurementComputingUniversalLibrary.BIP10VOLTS, RangeType.BIPOLAR, 10),
    BIPOLAR_5_VOLTS(MeasurementComputingUniversalLibrary.BIP5VOLTS, RangeType.BIPOLAR, 5),
    BIPOLAR_4_VOLTS(MeasurementComputingUniversalLibrary.BIP4VOLTS, RangeType.BIPOLAR, 4),
    BIPOLAR_2_POINT_5_VOLTS(MeasurementComputingUniversalLibrary.BIP2PT5VOLTS, RangeType.BIPOLAR, 2.5),
    BIPOLAR_2_VOLTS(MeasurementComputingUniversalLibrary.BIP2VOLTS, RangeType.BIPOLAR, 2),
    BIPOLAR_1_POINT_67_VOLTS(MeasurementComputingUniversalLibrary.BIP1PT67VOLTS, RangeType.BIPOLAR, 1.67),
    BIPOLAR_1_POINT_25_VOLTS(MeasurementComputingUniversalLibrary.BIP1PT25VOLTS, RangeType.BIPOLAR, 1.25),
    BIPOLAR_1_VOLT(MeasurementComputingUniversalLibrary.BIP1VOLTS, RangeType.BIPOLAR, 1),
    BIPOLAR_ZERO_POINT_625_VOLTS(MeasurementComputingUniversalLibrary.BIPPT625VOLTS, RangeType.BIPOLAR, 0.625),
    BIPOLAR_ZERO_POINT_5_VOLTS(MeasurementComputingUniversalLibrary.BIPPT5VOLTS, RangeType.BIPOLAR, 0.5),
    BIPOLAR_ZERO_POINT_312_VOLTS(MeasurementComputingUniversalLibrary.BIPPT312VOLTS, RangeType.BIPOLAR, 0.312),
    BIPOLAR_ZERO_POINT_25_VOLTS(MeasurementComputingUniversalLibrary.BIPPT25VOLTS, RangeType.BIPOLAR, 0.25),
    BIPOLAR_ZERO_POINT_2_VOLTS(MeasurementComputingUniversalLibrary.BIPPT2VOLTS, RangeType.BIPOLAR, 0.2),
    BIPOLAR_ZERO_POINT_156_VOLTS(MeasurementComputingUniversalLibrary.BIPPT156VOLTS, RangeType.BIPOLAR, 0.156),
    BIPOLAR_ZERO_POINT_125_VOLTS(MeasurementComputingUniversalLibrary.BIPPT125VOLTS, RangeType.BIPOLAR, 0.125),
    BIPOLAR_ZERO_POINT_1_VOLTS(MeasurementComputingUniversalLibrary.BIPPT1VOLTS, RangeType.BIPOLAR, 0.1),
    BIPOLAR_ZERO_POINT_078_VOLTS(MeasurementComputingUniversalLibrary.BIPPT078VOLTS, RangeType.BIPOLAR, 0.078),
    BIPOLAR_ZERO_POINT_05_VOLTS(MeasurementComputingUniversalLibrary.BIPPT05VOLTS, RangeType.BIPOLAR, 0.05),
    BIPOLAR_ZERO_POINT_01_VOLTS(MeasurementComputingUniversalLibrary.BIPPT01VOLTS, RangeType.BIPOLAR, 0.01),
    BIPOLAR_ZERO_POINT_005_VOLTS(MeasurementComputingUniversalLibrary.BIPPT005VOLTS, RangeType.BIPOLAR, 0.005),
    ////////////////////////////////////////////////////////////
    UNIPOLAR_10_VOLTS(MeasurementComputingUniversalLibrary.UNI10VOLTS, RangeType.UNIPOLAR, 10),
    UNIPOLAR_5_VOLTS(MeasurementComputingUniversalLibrary.UNI5VOLTS, RangeType.UNIPOLAR, 5),
    UNIPOLAR_4_VOLTS(MeasurementComputingUniversalLibrary.UNI4VOLTS, RangeType.UNIPOLAR, 4),
    UNIPOLAR_2_POINT_5_VOLTS(MeasurementComputingUniversalLibrary.UNI2PT5VOLTS, RangeType.UNIPOLAR, 2.5),
    UNIPOLAR_2_VOLTS(MeasurementComputingUniversalLibrary.UNI2VOLTS, RangeType.UNIPOLAR, 2),
    UNIPOLAR_1_POINT_67_VOLTS(MeasurementComputingUniversalLibrary.UNI1PT67VOLTS, RangeType.UNIPOLAR, 1.67),
    UNIPOLAR_1_POINT_25_VOLTS(MeasurementComputingUniversalLibrary.UNI1PT25VOLTS, RangeType.UNIPOLAR, 1.25),
    UNIPOLAR_1_VOLT(MeasurementComputingUniversalLibrary.UNI1VOLTS, RangeType.UNIPOLAR, 1),
    UNIPOLAR_ZERO_POINT_5_VOLTS(MeasurementComputingUniversalLibrary.UNIPT5VOLTS, RangeType.UNIPOLAR, 0.5),
    UNIPOLAR_ZERO_POINT_25_VOLTS(MeasurementComputingUniversalLibrary.UNIPT25VOLTS, RangeType.UNIPOLAR, 0.25),
    UNIPOLAR_ZERO_POINT_2_VOLTS(MeasurementComputingUniversalLibrary.UNIPT2VOLTS, RangeType.UNIPOLAR, 0.2),
    UNIPOLAR_ZERO_POINT_1_VOLTS(MeasurementComputingUniversalLibrary.UNIPT1VOLTS, RangeType.UNIPOLAR, 0.1),
    UNIPOLAR_ZERO_POINT_01_VOLTS(MeasurementComputingUniversalLibrary.UNIPT01VOLTS, RangeType.UNIPOLAR, 0.01),
    UNIPOLAR_ZERO_POINT_02_VOLTS(MeasurementComputingUniversalLibrary.UNIPT02VOLTS, RangeType.UNIPOLAR, 0.02),
    UNIPOLAR_ZERO_POINT_05_VOLTS(MeasurementComputingUniversalLibrary.UNIPT05VOLTS, RangeType.UNIPOLAR, 0.05),
    //////////////////////////////////////////////////////////
    MILLIAMPS_4_TO_20(MeasurementComputingUniversalLibrary.MA4TO20, 4, 20),
    MILLIAMPS_2_TO_10(MeasurementComputingUniversalLibrary.MA2TO10, 2, 10),
    MILLIAMPS_1_TO_5(MeasurementComputingUniversalLibrary.MA1TO5, 1, 5),
    MILLIAMPS_ZERO_POINT_5_TO_2_POINT_5(MeasurementComputingUniversalLibrary.MAPT5TO2PT5, 0.5, 2.5),
    MILLIAMPS_0_TO_20(MeasurementComputingUniversalLibrary.MA0TO20, 0, 20),
    BIPOLAR_ZERO_POINT_025_AMPS(MeasurementComputingUniversalLibrary.BIPPT025AMPS, RangeType.BIPOLAR, 0.025),
    ////////////////////////////////////////////////////////
    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED, -1, -1);

    public final int VALUE;
    public final double MINIMUM, MAXIMUM;

    private static final Map<Integer, AnalogRange> valueMap;

    private enum RangeType {
        // this enum is only to make the AnalogRange constructors easier
        BIPOLAR, UNIPOLAR;
    }

    static {
        final AnalogRange[] allRanges = AnalogRange.values();
        valueMap = new HashMap<>(allRanges.length, 1);
        for (AnalogRange range : allRanges) {
            valueMap.put(range.VALUE, range);
        }
    }

    public static AnalogRange parseInt(int value) {
        return valueMap.get(value);
    }

    private AnalogRange(int value, RangeType rangeType, double bound) {
        VALUE = value;
        if (bound <= 0) {
            throw new IllegalArgumentException(String.format(
                    "the bound (%f) should be positive when using a RangeType", bound
            ));
        }
        switch (rangeType) {
            case BIPOLAR:
                MINIMUM = -bound;
                MAXIMUM = bound;
                break;
            case UNIPOLAR:
                MINIMUM = 0;
                MAXIMUM = bound;
                break;
            default:
                throw new IllegalStateException("the RangeType argument is neither BIPOLAR nor UNIPOLAR");
        }
    }

    private AnalogRange(int value, double minimum, double maximum) {
        VALUE = value;

        if (maximum < minimum) {
            throw new IllegalArgumentException(String.format("max is less than min: %f < %f", maximum, minimum));
        }

        MINIMUM = minimum;
        MAXIMUM = maximum;
    }

}
