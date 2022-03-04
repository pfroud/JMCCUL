package jmccul;

import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum InterruptClockEdge {

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

    private InterruptClockEdge(int value) {
        VALUE = value;
    }

}
