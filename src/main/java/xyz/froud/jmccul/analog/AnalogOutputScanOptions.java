package xyz.froud.jmccul.analog;


import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 * @see xyz.froud.jmccul.analog.AnalogOutputWrapper#scan(int, int, long, long, AnalogRange, short[],
 *         java.util.EnumSet)
 * @see <a
 *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAOutScan.htm">cbAOutScan()</a>
 */
public enum AnalogOutputScanOptions {
    /**
     * Paces the data output operation using the ADC clock.
     */
    ADC_CLOCK(MeasurementComputingUniversalLibrary.ADCCLOCK),
    /**
     * Triggers a data output operation when the ADC clock starts.
     */
    ADC_CLOCK_TRIGGER(MeasurementComputingUniversalLibrary.ADCCLOCKTRIG),
    /**
     * This option may only be used with boards which support interrupt, DMA or REP-INSW transfer methods. When this
     * option is used the D/A operations will begin running in the background and control will immediately return to the
     * next line of your program. Use cbGetStatus() with AOFUNCTION to check the status of background operation.
     * Alternatively, some boards support cbEnableEvent() for event notification of changes in status of BACKGROUND
     * scans. Use cbStopBackground() with AOFUNCTION to terminate background operations before they are completed.
     * cbStopBackground() should be executed after normal termination of all background functions in order to clear
     * variables and flags.
     */
    BACKGROUND(MeasurementComputingUniversalLibrary.BACKGROUND),
    /**
     * This option may only be used with boards which support interrupt, DMA or REP INSW transfer methods. This option
     * puts the method in an endless loop.
     * <p>
     * Once it outputs the specified number (NumPoints) of D/A values, it resets to the start of the buffer and begins
     * again. The only way to stop this operation is by calling cbStopBackground() with AOFUNCTION. This option should
     * only be used in combination with BACKGROUND so that your program can regain control.
     */
    CONTINUOUS(MeasurementComputingUniversalLibrary.CONTINUOUS),
    /**
     * If this option is specified, conversions will be paced by the signal on the external clock input rather than by
     * the internal pacer clock. Each conversion will be triggered on the appropriate edge of the clock input signal
     * (refer to board-specific information contained in the Universal Library Users Guide).
     * <p>
     * When this option is used the Rate argument is ignored. The sampling rate is dependent on the clock signal.
     * Options for the board default to transfer types that allow the maximum conversion rate to be attained unless
     * otherwise specified.
     */
    EXTERNAL_CLOCK(MeasurementComputingUniversalLibrary.EXTCLOCK),
    /**
     * If this option is specified the sampling will not begin until the trigger condition is met. On many boards, this
     * trigger condition is programmable (refer to the cbSetTrigger() function and board-specific information contained
     * in the Universal Library Users Guide for details).
     */
    EXTERNAL_TRIGGER(MeasurementComputingUniversalLibrary.EXTTRIGGER),
    /**
     * Acquires data at a high resolution rate. When specified, the rate at which samples are acquired is in "samples
     * per 1000 seconds per channel". When this option is not specified, the rate at which samples are acquired is in
     * "samples per second per channel" (refer to the Rate argument above).
     */
    HIGH_RESOLUTION_RATE(MeasurementComputingUniversalLibrary.HIGHRESRATE),
    /**
     * When this option is used, you can output non-streamed data to a specific DAC output channel. The aggregate size
     * of the data output buffer must be less than or equal to the size of the internal data output FIFO in the device.
     * This allows the data output buffer to be loaded into the device's internal output FIFO. Once the sample updates
     * are transferred or downloaded to the device, the device is responsible for outputting the data. You can't make
     * any changes to the output buffer once the output begins.
     * <p>
     * With NONSTREAMEDIO mode, you do not have to periodically feed output data through the program to the device for
     * the data output to continue. However, the size of the buffer is limited.
     * <p>
     * NONSTREAMEDIO can only be used with the number of samples (Count) set equal to the size of the FIFO or less.
     */
    NON_STREAMED_IO(MeasurementComputingUniversalLibrary.NONSTREAMEDIO),
    /**
     * Re-arms the trigger after a trigger event is performed. With this mode, the scan begins when a trigger event
     * occurs. When the scan completes, the trigger is re-armed to generate the next the batch of data. You can specify
     * the number of samples to generate for each trigger event (described below). The RETRIGMODE option can be used
     * with the CONTINUOUS option to continue arming the trigger until cbStopBackground() is called.
     * <p>
     * You can specify the number of samples to generate with each trigger event. This is the trigger count. Use the
     * cbSetConfig() ConfigItem option BIDACTRIGCOUNT to set the trigger count. If you specify a trigger count that is
     * either zero or greater than the value of the NumPoints argument, the trigger count will be set to the value of
     * NumPoints.
     */
    RETRIGGER_MODE(MeasurementComputingUniversalLibrary.RETRIGMODE),
    /**
     * Gets scaled data, such as voltage, temperature, and so on, from the user buffer, and converts it to raw data. The
     * user buffer should have been allocated with cbScaledWinBufAlloc().
     * <p>
     * Results using SCALEDATA may be slightly different from results using cbFromEngUnits() near range limits, due to
     * the nature of the calibration being applied and the internal calculation using floating count values. If this is
     * undesirable use cbFromEngUnits().
     */
    SCALE_DATA(MeasurementComputingUniversalLibrary.SCALEDATA),
    /**
     * When this option is used (if the board supports it and the appropriate switches are set on the board) all of the
     * D/A voltages will be updated simultaneously when the last D/A in the scan is updated. This generally means that
     * all the D/A values will be written to the board, then a read of a D/A address causes all D/As to be updated with
     * new values simultaneously.
     */
    SIMULTANEOUS(MeasurementComputingUniversalLibrary.SIMULTANEOUS);


    private static final Map<Integer, AnalogOutputScanOptions> valueMap;

    static {
        final AnalogOutputScanOptions[] allEnumValues = AnalogOutputScanOptions.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (AnalogOutputScanOptions type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static AnalogOutputScanOptions parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private AnalogOutputScanOptions(int value) {
        VALUE = value;
    }

    public static int arrayToInt(AnalogOutputScanOptions[] optionsArray) {
        return Arrays.stream(optionsArray).map(e -> e.VALUE).reduce(0, (i1, i2) -> i1 | i2);
    }
}
