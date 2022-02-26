package jmccul;

import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum DacUpdateMode {

    IMMEDIATE(MeasurementComputingUniversalLibrary.UPDATEIMMEDIATE),
    ON_COMMAND(MeasurementComputingUniversalLibrary.UPDATEONCOMMAND);

    private static final Map<Integer, DacUpdateMode> valueMap;

    static {
        final DacUpdateMode[] allEnumValues = DacUpdateMode.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (DacUpdateMode type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static DacUpdateMode parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private DacUpdateMode(int value) {
        VALUE = value;
    }

}
