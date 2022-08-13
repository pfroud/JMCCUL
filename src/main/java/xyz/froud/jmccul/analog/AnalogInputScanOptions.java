/*
 * The MIT License.
 *
 * Copyright (c) 2022 Peter Froud.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.froud.jmccul.analog;


import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 * @see xyz.froud.jmccul.analog.AnalogInputWrapper#scan(int, int, long, long, AnalogRange,
 *         AnalogInputScanOptions...)
 * @see <a
 *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAInScan.htm">cbAInScan()</a>
 */
public enum AnalogInputScanOptions {

    /**
     * A/D data is transferred to memory one sample at a time. Rates attainable using SINGLEIO are PC-dependent and
     * generally less than 4 kHz.
     * <p>
     * The TRANSFER_METHOD options determine how data is transferred from the board to PC memory. If no TRANSFER_METHOD
     * options are specified (recommended), the optimum sampling mode is automatically chosen based on board type and
     * sampling speed. Use the default method unless you have a reason to select a specific transfer method.
     */
    TRANSFER_METHOD_SINGLE_IO(MeasurementComputingUniversalLibrary.SINGLEIO),
    /**
     * A/D transfers are initiated by a DMA request.
     * <p>
     * The TRANSFER_METHOD options determine how data is transferred from the board to PC memory. If no TRANSFER_METHOD
     * options are specified (recommended), the optimum sampling mode is automatically chosen based on board type and
     * sampling speed. Use the default method unless you have a reason to select a specific transfer method.
     */
    TRANSFER_METHOD_DMA_IO(MeasurementComputingUniversalLibrary.DMAIO),
    /**
     * A/D transfers are handled in blocks (by REP-INSW for example).
     * <p>
     * <b>BLOCKIO is not recommended for slow acquisition rates.</b> If the rate of acquisition is very slow (for
     * example less than 200 Hz) BLOCKIO is probably not the best choice for transfer mode. The reason for this is that
     * status for the operation is not available until one packet of data has been collected (typically 512 samples).
     * The implication is that if acquiring 100 samples at 100 Hz using BLOCKIO, the operation will not complete until
     * 5.12 seconds has elapsed.
     * <p>
     * The TRANSFER_METHOD options determine how data is transferred from the board to PC memory. If no TRANSFER_METHOD
     * options are specified (recommended), the optimum sampling mode is automatically chosen based on board type and
     * sampling speed. Use the default method unless you have a reason to select a specific transfer method.
     */
    TRANSFER_METHOD_BLOCK_IO(MeasurementComputingUniversalLibrary.BLOCKIO),
    /**
     * Allows higher sampling rates for sample counts up to full FIFO. Data is collected into the local FIFO. Data
     * transfers to the PC are held off until after the scan is complete. For BACKGROUND scans, the count and index
     * returned by cbGetStatus() remain 0 and the status equals RUNNING until the scan finishes. When the scan is
     * complete and the data is retrieved, the count and index are updated and the status equals IDLE.
     * <p>
     * BURSTIO is the default mode for non-Continuous fast scans (aggregate sample rates above 1000 Hz) with sample
     * counts up to full FIFO. To avoid the BURSTIO default, specify BLOCKIO.
     * <p>
     * BURSTIO is not a valid option for most boards. It is used mainly for USB products.
     * <p>
     * The TRANSFER_METHOD options determine how data is transferred from the board to PC memory. If no TRANSFER_METHOD
     * options are specified (recommended), the optimum sampling mode is automatically chosen based on board type and
     * sampling speed. Use the default method unless you have a reason to select a specific transfer method.
     */
    TRANSFER_METHOD_BURST_IO(MeasurementComputingUniversalLibrary.BURSTIO),
    /**
     * If the BACKGROUND option is not used then the cbAInScan() function will not return to your program until all of
     * the requested data has been collected and returned to the buffer. When the BACKGROUND option is used, control
     * will return immediately to the next line in your program and the data collection from the A/D into the buffer
     * will continue in the background. Use cbGetStatus() with AIFUNCTION to check on the status of the background
     * operation. Alternatively, some boards support cbEnableEvent() for event notification of changes in status of
     * BACKGROUND scans. Use cbStopBackground() with AIFUNCTION to terminate the background process before it has
     * completed. cbStopBackground() should be executed after normal termination of all background functions in order to
     * clear variables and flags.
     */
    BACKGROUND(MeasurementComputingUniversalLibrary.BACKGROUND),
    /**
     * Enables burst mode sampling. Scans from LowChan to HighChan are clocked at the maximum A/D rate in order to
     * minimize channel to channel skew. Scans are initiated at the rate specified by the Rate argument.
     * <p>
     * BURSTMODE is not recommended for use with the SINGLEIO option. If this combination is used, the Count value
     * should be set as low as possible, preferably to the number of channels in the scan. Otherwise, overruns may
     * occur.
     */
    BURST_MODE(MeasurementComputingUniversalLibrary.BURSTMODE),
    /**
     * This option is used to align data, either within each byte (in the case of some 12-bit devices) or within the
     * buffer (see the cbAPreTrig() function). Only the former case applies for cbAInScan(). The data stored on some
     * 12-bit devices is offset in the devices data register. For these devices, the CONVERTDATA option converts the
     * data to 12-bit A/D values by shifting the data to the first 12 bits within the byte. For devices that store the
     * data without an offset and for all 16-bit devices, this option is ignored.
     * <p>
     * Use of CONVERTDATA is recommended unless one of the following two conditions exist: 1) On some devices,
     * CONVERTDATA may not be specified if you are using the BACKGROUND option and DMA transfers. In this case, if data
     * conversion is required, use cbAConvertData() to re-align the data. 2) Some 12-bit boards store the data as a
     * 12-bit A/D value and a 4-bit channel number. Using CONVERTDATA will strip out the channel number from the data.
     * If you prefer to store the channel number as well as the data, call cbAConvertData() to retrieve the data and the
     * channel number from the buffer after the data acquisition to the buffer is complete.
     */
    CONVERT_DATA(MeasurementComputingUniversalLibrary.CONVERTDATA),
    /**
     * This option puts the function in an endless loop. Once it collects the required number of samples, it resets to
     * the start of the buffer and begins again. The only way to stop this operation is with cbStopBackground().
     * Normally this option should be used in combination with BACKGROUND so that your program will regain control.
     * <p>
     * Count argument settings in CONTINUOUS mode: For some DAQ hardware, Count must be an integer multiple of the
     * packet size. Packet size is the amount of data that a DAQ device transmits back to the PC's memory buffer during
     * each data transfer. Packet size can differ among DAQ hardware, and can even differ on the same DAQ product
     * depending on the transfer method.
     * <p>
     * In some cases, the minimum value for the Count argument may change when the CONTINUOUS option is used. This can
     * occur for several reasons; the most common is that in order to trigger an interrupt on boards with FIFOs, the
     * circular buffer must occupy at least half the FIFO. Typical half-FIFO sizes are 256, 512 and 1,024.
     * <p>
     * Another reason for a minimum Count value is that the buffer in memory must be periodically transferred to the
     * user buffer. If the buffer is too small, data will be overwritten during the transfer resulting in garbled data.
     * <p>
     * Refer to board-specific information in the Universal Library User's Guide for packet size information for your
     * particular DAQ hardware.
     */
    CONTINUOUS(MeasurementComputingUniversalLibrary.CONTINUOUS),
    /**
     * All A/D values will be sent to the A/D board's DT-Connect port. This option is incorporated into the EXTMEMORY
     * option. Use DTCONNECT only if the external board is not supported by Universal Library.
     */
    DT_CONNECT(MeasurementComputingUniversalLibrary.DTCONNECT),
    /**
     * If this option is used, conversions will be controlled by the signal on the external clock input rather than by
     * the internal pacer clock. Each conversion will be triggered on the appropriate edge of the clock input signal
     * (refer to the board-specific information in the Universal Library User's Guide). In most cases, when this option
     * is used the Rate argument is ignored. The sampling rate is dependent on the clock signal. Options for the board
     * will default to a transfer mode that will allow the maximum conversion rate to be attained unless otherwise
     * specified.
     * <p>
     * In some cases, such as with the PCI-DAS4020/12, an approximation of the rate is used to determine the size of the
     * packets to transfer from the board. Set the Rate argument to an approximate maximum value.
     * <p>
     * <b>SINGLEIO is recommended for slow external clock rates:</b> If the rate of the external clock is very slow
     * (for example less than 200 Hz) and the board you are using supports BLOCKIO, you may want to include the SINGLEIO
     * option. The reason for this is that the status for the operation is not available until one packet of data has
     * been collected (typically 512 samples). The implication is that, if acquiring 100 samples at 100 Hz using BLOCKIO
     * (the default for boards that support it if EXTCLOCK is used), the operation will not complete until 5.12 seconds
     * has elapsed.
     */
    EXTERNAL_CLOCK(MeasurementComputingUniversalLibrary.EXTCLOCK),
    /**
     * Causes the command to send the data to a connected memory board via the DT Connect interface rather than
     * returning the data to the buffer. Data for each call to this function will be appended unless cbMemReset() is
     * called. The data should be unloaded with the cbMemRead() function before collecting new data. When EXTMEMORY
     * option is used, the MemHandle argument can be set to null or 0. CONTINUOUS option cannot be used with EXTMEMORY.
     * Do not use EXTMEMORY and DTCONNECT together. The transfer modes DMAIO, SINGLEIO, BLOCKIO and BURSTIO have no
     * meaning when used with this option.
     */
    EXTERNAL_MEMORY(MeasurementComputingUniversalLibrary.EXTMEMORY),
    /**
     * If this option is specified, the sampling will not begin until the trigger condition is met. On many boards, this
     * trigger condition is programmable (refer to the cbSetTrigger() function and board-specific information for
     * details) and can be programmed for rising or falling edge or an analog level.
     * <p>
     * On other boards, only 'polled gate' triggering is supported. In this case, assuming active high operation, data
     * acquisition will commence immediately if the trigger input is high. If the trigger input is low, acquisition will
     * be held off unit it goes high. Acquisition continues until Count samples are taken, regardless of the state of
     * the trigger input. For "polled gate" triggering, this option is most useful if the signal is a pulse with a very
     * low duty cycle (trigger signal in TTL low state most of the time) so that triggering will be held off until the
     * occurrence of the pulse.
     */
    EXTERNAL_TRIGGER(MeasurementComputingUniversalLibrary.EXTTRIGGER),
    /**
     * Acquires data at a high resolution rate. When specified, the rate at which samples are acquired is in "samples
     * per 1000 seconds per channel". When this option is not specified, the rate at which samples are acquired is in
     * "samples per second per channel" (refer to the Rate argument above).
     */
    HIGH_RESOLUTION_RATE(MeasurementComputingUniversalLibrary.HIGHRESRATE),
    /**
     * Turns off real-time software calibration for boards which are software calibrated. This is done by applying
     * calibration factors to the data on a sample by sample basis as it is acquired. Examples are the PCM-DAS16/330 and
     * PCM-DAS16x/12. Turning off software calibration saves CPU time during a high speed acquisition run. This may be
     * required if your processor is less than a 150 MHz Pentium and you desire an acquisition speed in excess of 200
     * kHz. These numbers may not apply to your system. Only trial will tell for sure. DO NOT use this option if you do
     * not have to. If this option is used, the data must be calibrated after the acquisition run with the
     * cbACalibrateData() function.
     */
    DISABLE_CALIBRATION(MeasurementComputingUniversalLibrary.NOCALIBRATEDATA),
    /**
     * If this option is specified, the system's time-of-day interrupts are disabled for the duration of the scan. These
     * interrupts are used to update the systems real time clock and are also used by various other programs. These
     * interrupts can limit the maximum sampling speed of some boards - particularly the PCM-DAS08. If the interrupts
     * are turned off using this option, the real-time clock will fall behind by the length of time that the scan
     * takes.
     */
    DISABLE_TIME_OF_DAY_INTERRUPTS(MeasurementComputingUniversalLibrary.NOTODINTS),
    /**
     * Re-arms the trigger after a trigger event is performed. With this mode, the scan begins when a trigger event
     * occurs. When the scan completes, the trigger is re-armed to acquire the next the batch of data. You can specify
     * the number of samples in the scan for each trigger event (described below). The RETRIGMODE option can be used
     * with the CONTINUOUS option to continue arming the trigger until cbStopBackground() is called.
     * <p>
     * You can specify the number of samples to acquire with each trigger event. This is the trigger count. Use the
     * cbSetConfig() ConfigItem option BIADTRIGCOUNT to set the trigger count. If you specify a trigger count that is
     * either zero or greater than the value of the cbAInScan() Count argument, the trigger count will be set to the
     * value of the Count argument.
     * <p>
     * Specify the CONTINUOUS option with the trigger count set to zero to fill the buffer with Count samples, re-arm
     * the trigger, and refill the buffer upon the next trigger.
     */
    RETRIGGER_MODE(MeasurementComputingUniversalLibrary.RETRIGMODE),
    /**
     * Converts raw scan data — to voltage, temperature, and so on, depending upon the selected channel sensor category
     * — during the analog input scan, and puts the scaled data directly into the user buffer. The user buffer should
     * have been allocated with cbScaledWinBufAlloc(). Results using SCALEDATA may be slightly different from results
     * using cbToEngUnits() near range limits, due to the nature of the calibration being applied and the internal
     * calculation using floating count values. If this is undesirable use cbToEngUnits().
     */
    SCALE_DATA(MeasurementComputingUniversalLibrary.SCALEDATA);


    private static final Map<Integer, AnalogInputScanOptions> valueMap;

    static {
        final AnalogInputScanOptions[] allEnumValues = AnalogInputScanOptions.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (AnalogInputScanOptions type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static AnalogInputScanOptions parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private AnalogInputScanOptions(int value) {
        VALUE = value;
    }

    public static int bitwiseOr(AnalogInputScanOptions[] optionsArray) {
        return Arrays.stream(optionsArray).map(e -> e.VALUE).reduce(0, (i1, i2) -> i1 | i2);
    }

}
