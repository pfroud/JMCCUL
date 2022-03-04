package jmccul;

import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum ExternalClockType {

    CONTINOUS(MeasurementComputingUniversalLibrary.CONTINUOUS_CLK),
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

    private ExternalClockType(int value) {
        VALUE = value;
    }

}
