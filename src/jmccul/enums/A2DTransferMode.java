package jmccul.enums;

import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum A2DTransferMode {

    /*
    BoardInfo
    BIADXFERMODE
     */
    KERNEL(MeasurementComputingUniversalLibrary.XFER_KERNEL),
    USER(MeasurementComputingUniversalLibrary.XFER_USER);

    private static final Map<Integer, A2DTransferMode> valueMap;

    static {
        final A2DTransferMode[] allEnumValues = A2DTransferMode.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (A2DTransferMode type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static A2DTransferMode parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private A2DTransferMode(int value) {
        VALUE = value;
    }

}
