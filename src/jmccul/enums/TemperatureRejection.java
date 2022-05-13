package jmccul.enums;

import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
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

    private TemperatureRejection(int value) {
        VALUE = value;
    }

}
