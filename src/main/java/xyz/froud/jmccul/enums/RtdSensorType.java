package xyz.froud.jmccul.enums;

import java.util.HashMap;
import java.util.Map;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum RtdSensorType {

    // RTD = Resistance Temperature Detector
    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
    PLATINUM_3750(MeasurementComputingUniversalLibrary.RTD_PT_3750),
    PLATINUM_3850(MeasurementComputingUniversalLibrary.RTD_PT_3850),
    PLATINUM_3851(MeasurementComputingUniversalLibrary.RTD_PT_3851),
    PLATINUM_3911(MeasurementComputingUniversalLibrary.RTD_PT_3911),
    PLATINUM_3916(MeasurementComputingUniversalLibrary.RTD_PT_3916),
    PLATINUM_3920(MeasurementComputingUniversalLibrary.RTD_PT_3920),
    PLATINUM_3928(MeasurementComputingUniversalLibrary.RTD_PT_3928),
    CUSTOM(MeasurementComputingUniversalLibrary.RTD_CUSTOM);

    private static final Map<Integer, RtdSensorType> valueMap;

    static {
        final RtdSensorType[] allEnumValues = RtdSensorType.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (RtdSensorType type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static RtdSensorType parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private RtdSensorType(int value) {
        VALUE = value;
    }

}
