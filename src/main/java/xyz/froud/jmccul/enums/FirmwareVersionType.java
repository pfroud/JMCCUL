package xyz.froud.jmccul.enums;


import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum FirmwareVersionType {

    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
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

    FirmwareVersionType(int value) {
        VALUE = value;
    }

}