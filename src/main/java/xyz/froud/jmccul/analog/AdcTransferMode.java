package xyz.froud.jmccul.analog;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum AdcTransferMode {

    /*
    BoardInfo
    BIADXFERMODE
     */
    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
    KERNEL(MeasurementComputingUniversalLibrary.XFER_KERNEL),
    USER(MeasurementComputingUniversalLibrary.XFER_USER);

    private static final Map<Integer, AdcTransferMode> valueMap;

    static {
        final AdcTransferMode[] allEnumValues = AdcTransferMode.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (AdcTransferMode type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static AdcTransferMode parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    AdcTransferMode(int value) {
        VALUE = value;
    }

}
