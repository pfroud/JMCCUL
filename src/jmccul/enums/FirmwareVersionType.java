
package jmccul.enums;


import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum FirmwareVersionType {

    MAIN(MeasurementComputingUniversalLibrary.VER_FW_MAIN),
    MEASUREMENT(MeasurementComputingUniversalLibrary.VER_FW_MEASUREMENT),
    RATIO(MeasurementComputingUniversalLibrary.VER_FW_RADIO),
    FPGA(MeasurementComputingUniversalLibrary.VER_FPGA),
    EXPANSION_BOARD(MeasurementComputingUniversalLibrary.VER_FW_MEASUREMENT_EXP);


    private static final Map<Integer, FirmwareVersionType> valueMap;

    static {
        final FirmwareVersionType[] allEnumValues = FirmwareVersionType.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (FirmwareVersionType type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static FirmwareVersionType parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private FirmwareVersionType(int value) {
        VALUE = value;
    }

}
