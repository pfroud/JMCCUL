package xyz.froud.jmccul.analog;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum AdcTimingMode {

    /*
    BoardInfo
    BIADTIMINGMODE
     */
    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
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

    AdcTimingMode(int value) {
        VALUE = value;
    }

}
