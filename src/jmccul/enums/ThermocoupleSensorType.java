package jmccul.enums;

import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum ThermocoupleSensorType {

    J(MeasurementComputingUniversalLibrary.TC_TYPE_J),
    K(MeasurementComputingUniversalLibrary.TC_TYPE_K),
    S(MeasurementComputingUniversalLibrary.TC_TYPE_S),
    R(MeasurementComputingUniversalLibrary.TC_TYPE_R),
    B(MeasurementComputingUniversalLibrary.TC_TYPE_B),
    E(MeasurementComputingUniversalLibrary.TC_TYPE_E),
    T(MeasurementComputingUniversalLibrary.TC_TYPE_T),
    N(MeasurementComputingUniversalLibrary.TC_TYPE_N);

    private static final Map<Integer, ThermocoupleSensorType> valueMap;

    static {
        final ThermocoupleSensorType[] allEnumValues = ThermocoupleSensorType.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (ThermocoupleSensorType type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static ThermocoupleSensorType parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private ThermocoupleSensorType(int value) {
        VALUE = value;
    }

}
