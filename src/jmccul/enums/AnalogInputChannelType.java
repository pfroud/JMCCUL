package jmccul.enums;

import java.util.HashMap;
import java.util.Map;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum AnalogInputChannelType {

    /*
    InfoType: BoardInfo
    ConfigItem: BIADCHANTYPE
     */
    VOLTAGE(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_VOLTAGE),
    CURRENT(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_CURRENT),
    RESISTANCE_4WIRE_10KOHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RESISTANCE_10K4W),
    RESISTANCE_4WIRE_1KOHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RESISTANCE_1K4W),
    RESISTANCE_2WIRE_10KOHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RESISTANCE_10K2W),
    RESISTANCE_2WIRE_1KOHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RESISTANCE_1K2W),
    THERMOCOUPLE(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_TC),
    RTD_4WIRE_1KOHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RTD_1000OHM_4W),
    RTD_4WIRE_100OHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RTD_100OHM_4W),
    RTD_3WIRE_1KOHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RTD_1000OHM_3W),
    RTD_3WIRE_100OHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RTD_100OHM_3W),
    QUARTER_BRIDGE_350OHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_QUART_BRIDGE_350OHM),
    QUARTER_BRIDGE_120OHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_QUART_BRIDGE_120OHM),
    HALF_BRIDGE(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_HALF_BRIDGE),
    FULL_BRIDGE_62_POINT_5_MILLIVOLTS_PER_VOLT(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_FULL_BRIDGE_62PT5mVV),
    FULL_BRIDGE_7_POINT_8_MILLIVOLTS_PER_VOLT(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_FULL_BRIDGE_7PT8mVV);

    private static final Map<Integer, AnalogInputChannelType> valueMap;

    static {
        final AnalogInputChannelType[] allEnumValues = AnalogInputChannelType.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (AnalogInputChannelType type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static AnalogInputChannelType parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private AnalogInputChannelType(int value) {
        VALUE = value;
    }

}
