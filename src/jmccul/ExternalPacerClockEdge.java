package jmccul;

import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum ExternalPacerClockEdge {

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

    private ExternalPacerClockEdge(int value) {
        VALUE = value;
    }

}
