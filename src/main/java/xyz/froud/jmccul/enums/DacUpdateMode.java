package xyz.froud.jmccul.enums;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum DacUpdateMode {

    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),

    /** Values written with cbAOut() or cbAOutScan() are automatically output by the DAC channels. */
    IMMEDIATE(MeasurementComputingUniversalLibrary.UPDATEIMMEDIATE),

    /** Values written with cbAOut() or cbAOutScan() are not output by the DAC channels until you call {@link xyz.froud.jmccul.config.AnalogOutputConfig#updateAnalogOutput()}. */
    ON_COMMAND(MeasurementComputingUniversalLibrary.UPDATEONCOMMAND);

    private static final Map<Integer, DacUpdateMode> valueMap;

    static {
        final DacUpdateMode[] allEnumValues = DacUpdateMode.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (DacUpdateMode type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static DacUpdateMode parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    DacUpdateMode(int value) {
        VALUE = value;
    }

}
