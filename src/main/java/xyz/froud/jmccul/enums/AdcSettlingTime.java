package xyz.froud.jmccul.enums;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum AdcSettlingTime {

    /*
    BoardInfo
    BIADCSETTLETIME
     */
    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
    DEFAULT(MeasurementComputingUniversalLibrary.SETTLE_DEFAULT),
    ONE_MICROSECOND(MeasurementComputingUniversalLibrary.SETTLE_1us),
    FIVE_MICROSECONDS(MeasurementComputingUniversalLibrary.SETTLE_5us),
    TEN_MICROSECONDS(MeasurementComputingUniversalLibrary.SETTLE_10us),
    ONE_MILLISECOND(MeasurementComputingUniversalLibrary.SETTLE_1ms);

    private static final Map<Integer, AdcSettlingTime> valueMap;

    static {
        final AdcSettlingTime[] allEnumValues = AdcSettlingTime.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (AdcSettlingTime type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static AdcSettlingTime parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    AdcSettlingTime(int value) {
        VALUE = value;
    }

}
