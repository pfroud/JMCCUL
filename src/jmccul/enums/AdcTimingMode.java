package jmccul.enums;

import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum AdcTimingMode {

    /*
    BoardInfo
    BIADTIMINGMODE
     */
    HIGH_SPEED(1),
    SIXTY_HERTZ_REJECTION(8),
    FIFTY_HERTZ_REJECTION(9),
    HIGH_RESOLUTION(15);

    private static final Map<Integer, AdcTimingMode> valueMap;

    static {
        final AdcTimingMode[] allEnumValues = AdcTimingMode.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (AdcTimingMode type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static AdcTimingMode parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private AdcTimingMode(int value) {
        VALUE = value;
    }

}
