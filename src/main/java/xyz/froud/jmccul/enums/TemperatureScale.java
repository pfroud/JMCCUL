package xyz.froud.jmccul.enums;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum TemperatureScale {

    // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Enumerations/MccDaq.TempScale.htm
    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
    CELSIUS(MeasurementComputingUniversalLibrary.CELSIUS),
    FAHRENHEIT(MeasurementComputingUniversalLibrary.FAHRENHEIT),
    KELVIN(MeasurementComputingUniversalLibrary.KELVIN),
    /** Specify the VOLTS option to read the voltage input of a thermocouple. */
    VOLTS(MeasurementComputingUniversalLibrary.VOLTS),
    /**
     * Specify the NOSCALE option to retrieve raw data from the device. When NOSCALE is specified, calibrated data is
     * returned, although a cold junction compensation (CJC) correction factor is not applied to the returned values.
     */
    NO_SCALE(MeasurementComputingUniversalLibrary.NOSCALE);

    private static final Map<Integer, TemperatureScale> valueMap;

    static {
        final TemperatureScale[] allEnumValues = TemperatureScale.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (TemperatureScale type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static TemperatureScale parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    TemperatureScale(int value) {
        VALUE = value;
    }

}
