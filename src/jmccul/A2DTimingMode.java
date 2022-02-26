package jmccul;

import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum A2DTimingMode {

    /*
    BoardInfo
    BIADTIMINGMODE
     */
    HIGH_SPEED(1),
    SIXTY_HERTZ_REJECTION(8),
    FIFTY_HERTZ_REJECTION(9),
    HIGH_RESOLUTION(15);

    private static final Map<Integer, A2DTimingMode> valueMap;

    static {
        final A2DTimingMode[] allEnumValues = A2DTimingMode.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (A2DTimingMode type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static A2DTimingMode parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private A2DTimingMode(int value) {
        VALUE = value;
    }

}
