package xyz.froud.jmccul.enums;


import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum BaseOrExpansionBoard {

    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
    BASE(0),
    EXPANSION(1);

    private static final Map<Integer, BaseOrExpansionBoard> valueMap;

    static {
        final BaseOrExpansionBoard[] allEnumValues = BaseOrExpansionBoard.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (BaseOrExpansionBoard type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static BaseOrExpansionBoard parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    BaseOrExpansionBoard(int value) {
        VALUE = value;
    }

}
