package jmccul.enums;

import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum CalibrationTableType {

    FACTORY(MeasurementComputingUniversalLibrary.CAL_TABLE_FACTORY),
    FIELD(MeasurementComputingUniversalLibrary.CAL_TABLE_FIELD);

    private static final Map<Integer, CalibrationTableType> valueMap;

    static {
        final CalibrationTableType[] allEnumValues = CalibrationTableType.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (CalibrationTableType type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static CalibrationTableType parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private CalibrationTableType(int value) {
        VALUE = value;
    }

}
