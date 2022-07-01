package xyz.froud.jmccul.enums;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum ExternalPacerClockEdge {

    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
    RISING(MeasurementComputingUniversalLibrary.EXT_PACER_EDGE_RISING),
    FALLING(MeasurementComputingUniversalLibrary.EXT_PACER_EDGE_FALLING);

    private static final Map<Integer, ExternalPacerClockEdge> valueMap;

    static {
        final ExternalPacerClockEdge[] allEnumValues = ExternalPacerClockEdge.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (ExternalPacerClockEdge type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static ExternalPacerClockEdge parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    ExternalPacerClockEdge(int value) {
        VALUE = value;
    }

}
