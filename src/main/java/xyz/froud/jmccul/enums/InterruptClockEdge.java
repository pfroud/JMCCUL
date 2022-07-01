package xyz.froud.jmccul.enums;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum InterruptClockEdge {

    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
    RISING(MeasurementComputingUniversalLibrary.RISING_EDGE),
    FALLING(MeasurementComputingUniversalLibrary.FALLING_EDGE);

    private static final Map<Integer, InterruptClockEdge> valueMap;

    static {
        final InterruptClockEdge[] allEnumValues = InterruptClockEdge.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (InterruptClockEdge type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static InterruptClockEdge parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    InterruptClockEdge(int value) {
        VALUE = value;
    }

}
