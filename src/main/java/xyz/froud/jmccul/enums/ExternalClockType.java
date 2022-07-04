package xyz.froud.jmccul.enums;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum ExternalClockType {

    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
    CONTINUOUS(MeasurementComputingUniversalLibrary.CONTINUOUS_CLK),
    GATED(MeasurementComputingUniversalLibrary.GATED_CLK);

    private static final Map<Integer, ExternalClockType> valueMap;

    static {
        final ExternalClockType[] allEnumValues = ExternalClockType.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (ExternalClockType type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static ExternalClockType parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    ExternalClockType(int value) {
        VALUE = value;
    }

}
