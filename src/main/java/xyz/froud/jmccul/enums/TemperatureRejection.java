package xyz.froud.jmccul.enums;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum TemperatureRejection {

    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
    OFF(0),
    FIFTY_HERTZ(50),
    SIXTY_HERTZ(60);

    private static final Map<Integer, TemperatureRejection> valueMap;

    static {
        final TemperatureRejection[] allEnumValues = TemperatureRejection.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (TemperatureRejection type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static TemperatureRejection parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    TemperatureRejection(int value) {
        VALUE = value;
    }

}