package xyz.froud.jmccul.temperature;


import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 * @see <a
 *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions/cbTIn.htm">cbTIn()</a>
 * @see <a
 *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions/cbTInScan.htm">cbTInScan()</a>
 */
public enum TemperatureInputOptions {

    /**
     * When selected, a smoothing function is applied to temperature readings, very much like the electrical smoothing
     * inherent in all hand held temperature sensor instruments. This is the default. When selected, 10 samples are read
     * from the specified channel and averaged. The average is the reading returned. Averaging removes normally
     * distributed signal line noise.
     */
    FILTER(MeasurementComputingUniversalLibrary.FILTER),
    /**
     * If you use the NOFILTER option then the readings will not be smoothed and you will see a scattering of readings
     * around a mean.
     */
    NO_FILTER(MeasurementComputingUniversalLibrary.NOFILTER),
    /**
     * Waits for new data to become available.
     */
    WAIT_FOR_NEW_DATA(MeasurementComputingUniversalLibrary.WAITFORNEWDATA);


    private static final Map<Integer, TemperatureInputOptions> valueMap;

    static {
        final TemperatureInputOptions[] allEnumValues = TemperatureInputOptions.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (TemperatureInputOptions type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static TemperatureInputOptions parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private TemperatureInputOptions(int value) {
        VALUE = value;
    }

    public static int bitwiseOr(TemperatureInputOptions[] optionsArray) {
        return Arrays.stream(optionsArray).map(e -> e.VALUE).reduce(0, (i1, i2) -> i1 | i2);
    }

}
