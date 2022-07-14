package xyz.froud.jmccul.analog;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.enums.AnalogRange;
import xyz.froud.jmccul.enums.DacUpdateMode;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Analog output = digital to analog = "DAC"
 *
 * @author Peter Froud
 */
public class AnalogOutputImpl {

    /*
    https://github.com/mccdaq/mcculw/blob/master/mcculw/device_info/ao_info.py
     */
    private final int BOARD_NUMBER;
    private final DaqDevice DAQ_DEVICE;

    private Integer channelCount;
    private Integer resolution;
    private List<AnalogRange> supportedRanges;
    private Boolean isVoltageOutputSupported;

    public AnalogOutputImpl(DaqDevice device) {
        DAQ_DEVICE = device;
        BOARD_NUMBER = device.getBoardNumber();
    }


    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L37">is_supported
     *         in ao_info.py</a>
     */
    public boolean isAnalogOutputSupported() throws JMCCULException {
        return getDacChannelCount() > 0;
    }


    /*
    void getSupPortedScanOptions() {
        //https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L50
        // Their Python program is querying the DACSCANOPTIONS config item, which is not present in the C header file
    }
    void isScanSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L46
        //todo
    }
     */

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L61">supported_ranges
     *         in ao_info.py</a>
     */
    public List<AnalogRange> getSupportedRanges() throws JMCCULException {
        if (supportedRanges == null) {

            supportedRanges = new ArrayList<>();

            // Check if the range is ignored by passing a bogus range in
            boolean isRangeIgnored = false;
            try {
                // cal cbAOut() with a bogus range.
                // If the D/A board does not have programmable ranges then the range argument will be ignored.
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAOut(
                        DAQ_DEVICE.getBoardNumber(),
                        0,
                        -5,
                        (short) 0
                );
                JMCCULUtils.checkError(errorCode);

                isRangeIgnored = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
            }

            if (isRangeIgnored) {
                // Try and get the range configured in InstaCal
                try {
                    int range = Configuration.getInt(
                            MeasurementComputingUniversalLibrary.BOARDINFO,
                            DAQ_DEVICE.getBoardNumber(),
                            0,
                            MeasurementComputingUniversalLibrary.BIDACRANGE
                    );
                    supportedRanges.add(AnalogRange.parseInt(range));
                } catch (JMCCULException ex) {
                    ex.throwIfErrorIsNetworkDeviceInUse();
                }
            } else {
                // try all possible analog ranges
                for (AnalogRange rangeToCheck : AnalogRange.values()) {
                    try {
                        analogOutput(0, rangeToCheck, (short) 0);
                        supportedRanges.add(rangeToCheck);
                    } catch (JMCCULException ex) {
                        ex.throwIfErrorIsNetworkDeviceInUse();
                    }
                }

            }
        }

        return supportedRanges;

    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L97">supports_v_out
     *         in ao_info.py</a>
     */
    public boolean isVoltageOutputSupported() throws JMCCULException {
        if (isVoltageOutputSupported == null) {
            if (getSupportedRanges().isEmpty()) {
                isVoltageOutputSupported = false;
            } else {
                try {
                    voltageOutput(0, getSupportedRanges().get(0), 0);
                    isVoltageOutputSupported = true;
                } catch (JMCCULException ex) {
                    ex.throwIfErrorIsNetworkDeviceInUse();
                    isVoltageOutputSupported = false;
                }
            }
        }
        return isVoltageOutputSupported;
    }

    /**
     * Sets the value of a D/A channel.
     * <p>
     * Note for "Simultaneous Update" or "Zero Power-Up" boards: If you set the simultaneous update jumper for
     * simultaneous operation, use cbAOutScan() for simultaneous update of multiple channels. cbAOut() always writes the
     * D/A data then reads the D/A, which causes the D/A output to be updated.
     *
     * @param channel D/A channel number. The maximum allowable channel depends on which type of D/A board is
     *         being used.
     * @param range D/A range code. The output range of the D/A channel can be set to any of those supported by
     *         the board. If the D/A board does not have programmable ranges then this argument will be ignored. Refer
     *         to board specific information for a list of the supported A/D ranges.
     * @param value Value to set D/A to. Must be in the range 0 - n where n is the value 2Resolution – 1 of the
     *         converter.
     *         <p>
     *         Exception: using 16-bit boards with Basic range is –32,768 to 32,767. Refer to the discussion on <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/Users_Guide/Overview/UL_Interface.htm#signed%20integers">16-bit
     *         values using a signed integer data type</a> for more information.
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/CBAOut.htm">cbAOut()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/AOut.htm">AOut()</a>
     */

    public void analogOutput(int channel, AnalogRange range, short value) throws JMCCULException {
        // The value must be between zero and 2^(resolution)-1.
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/CBAOut.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAOut(
                DAQ_DEVICE.getBoardNumber(),
                channel,
                range.VALUE,
                value
        );
        JMCCULUtils.checkError(errorCode);
    }

    /**
     * Outputs values to a range of D/A channels. This function can be used for paced analog output on hardware that
     * supports paced output. It can also be used to update all analog outputs at the same time when the SIMULTANEOUS
     * option is used.
     *
     * @param lowChannel First D/A channel of scan.
     * @param highChannel Last D/A channel of scan. The maximum allowable channel depends on which type of D/A
     *         board is being used.
     * @param count Number of D/A values to output. Specifies the total number of D/A values that will be
     *         output. Most D/A boards do not support timed outputs. For these boards, set the count to the number of
     *         channels in the scan.
     * @param rateHz Sample rate in scans per second. For many D/A boards the Rate is ignored and can be set to
     *         NOTUSED. For D/A boards with trigger and transfer methods which allow fast output rates, such as the
     *         CIO-DAC04/12-HS, Rate should be set to the D/A output rate (in scans/sec). This argument returns the
     *         value of the actual rate set. This value may be different from the user specified rate due to pacer
     *         limitations.
     *         <p>
     *         If supported, this is the rate at which scans are triggered. If you are updating 4 channels, 0-3, then
     *         specifying a rate of 10,000 scans per second (10 kHz) will result in the D/A converter rates of 10 kHz
     *         (one D/A per channel). The data transfer rate is 40,000 words per second; 4 channels * 10,000 updates per
     *         scan.
     *         <p>
     *         The maximum update rate depends on the D/A board that is being used. It is also dependent on the sampling
     *         mode options.
     *         <p>
     *         Caution: you will generate an error if you specify a total D/A rate beyond the capability of the board.
     *         The maximum update rate depends on the D/A board that is being used and on the sampling mode options.
     * @param range D/A range code. The output range of the D/A channel can be set to any of those supported by
     *         the board. If the D/A board does not have a programmable gain, this argument is ignored.
     * @param options Bit fields that control various options. This field may contain any combination of
     *         non-contradictory choices from the values listed in the Options argument values section below.
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAOutScan.htm">cbAOutScan()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/AOutScan.htm">AOutScan()</a>
     */
    public void analogOutputScan(int lowChannel, int highChannel, long count, long rateHz, AnalogRange range, short[] values, int options) throws JMCCULException {

        /*
        For 16-bit data, create the buffer with cbWinBufAlloc().
        For data that is >16-bit and ≤32-bit, use cbWinBufAlloc32().
        For data that is >32-bit and ≤64-bit, use cbWinBufAlloc64().
        When the device supports output scanning of scaled data, such as cbAOutScan() using the SCALEDATA option,
        create the buffer with cbScaledWinBufAlloc().

        You can load the data values with cbWinArrayToBuf() or cbScaledWinArrayToBuf() (for scaled data).
         */
        //https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        MeasurementComputingUniversalLibrary.HGLOBAL windowsBuffer
                = MeasurementComputingUniversalLibrary.INSTANCE.cbWinBufAlloc(new NativeLong(count));

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinArrayToBuf.htm
        final int errorCodeWinArrayToBuf = MeasurementComputingUniversalLibrary.INSTANCE.cbWinArrayToBuf(
                ShortBuffer.wrap(values),
                windowsBuffer,
                new NativeLong(0),
                new NativeLong(values.length)
        );
        JMCCULUtils.checkError(errorCodeWinArrayToBuf);

        final NativeLongByReference rateByReference = new NativeLongByReference(new NativeLong(rateHz));

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAOutScan.htm
        final int errorCodeAOutScan = MeasurementComputingUniversalLibrary.INSTANCE.cbAOutScan(
                /* int BoardNum  */DAQ_DEVICE.getBoardNumber(),
                /* int LowChan   */ lowChannel,
                /* int HighChan  */ highChannel,
                /* int NumPoint  */ new NativeLong(count),
                /* long *Rate    */ rateByReference, // returns the value of the actual rate set, which may be different from the requested rate due to pacer limitations.
                /* int Range     */ range.VALUE,
                /* int MemHandle */ windowsBuffer,
                /* int           */ options
        );
        JMCCULUtils.checkError(errorCodeAOutScan);

    }

    /**
     * Sets the voltage value of a D/A channel.
     * <p>
     * This function cannot be used for current output.
     *
     * @param channel The D/A channel number. The maximum allowable channel depends on which type of D/A board
     *         is being used.
     * @param range The D/A range code. If the device has a programmable gain, it is set according to this
     *         argument value. If the range specified isn't supported, the function return a BADRANGE error.
     *         <p>
     *         If the gain is fixed or manually selectable, make sure that this argument matches the gain configured for
     *         the device. If it doesn't, the output voltage will not match the voltage specified in the DataValue
     *         argument.
     * @param value The voltage value to be written.
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVOut.htm">cbVOut()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/VOut.htm">VOut()</a>
     */
    public void voltageOutput(int channel, AnalogRange range, float value) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVOut.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbVOut(
                DAQ_DEVICE.getBoardNumber(),
                channel,
                range.VALUE,
                value,
                0
        );
        JMCCULUtils.checkError(errorCode);

    }


    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACFORCESENSE -> BI DAC FORCE SENSE -> boardInfo
     Readable? yes
     Writable? yes
     */

    /**
     * Remote sensing state of an analog output channel.
     * <p>
     * The remote sensing feature compensates for the voltage drop error that occurs in applications where the analog
     * outputs on a device are connected to its load through a long wire or cable type interconnect.
     * <p>
     * The remote sensing feature utilizes the force and sense output terminals on a supported device. Refer to the
     * device hardware manual for more information.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACForceSense.htm">BoardConfig.GetDACForceSense()</a>
     */
    public boolean getDacForceSense(int channel) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIDACFORCESENSE
        ) == 1;
    }

    /**
     * Enables or disables remote sensing of an analog output channel.
     * <p>
     * The remote sensing feature compensates for the voltage drop error that occurs in applications where the analog
     * outputs on a device are connected to its load through a long wire or cable type interconnect.
     * <p>
     * The remote sensing feature utilizes the force and sense output terminals on a supported device. Refer to the
     * device hardware manual for more information.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDACForceSense.htm">BoardConfig.SetDACForceSense()</a>
     */
    public void setDacForceSense(int channel, boolean sense) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIDACFORCESENSE,
                (sense ? 1 : 0)
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACRANGE  -> BI DAC RANGE -> boardInfo DAC range
     Readable? yes
     Writable? yes

    TODO what is the difference between BI DAC RANGE and BI RANGE?
    TODO why is channel used when setting but ignored when getting?
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACRange.htm">BoardConfig.GetDACRange()</a>
     */
    public AnalogRange getDacRange() throws JMCCULException {
        return AnalogRange.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, // devNum is ignored - probably wrong though
                        MeasurementComputingUniversalLibrary.BIDACRANGE
                )
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDACRange.htm">BoardConfig.SetDACRange()</a>
     */
    public void setDacRange(int channel, AnalogRange range) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIDACRANGE,
                range.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACRES -> BI DAC RES -> boardInfo DAC resolution
     Readable? yes
     Writable? no
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDacResolution.htm">BoardConfig.GetDacResolution()</a>
     */
    public int getDacResolution() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDACRES
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACSTARTUP -> BI DAC STARTUP -> boardInfo DAC startup
     Readable?
     Writable?
     */

    /**
     * Whether values written to the DAC get saved to non-volatile memory on the DAQ board.
     *
     * @see #setDacStartup
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACStartup.htm">BoardConfig.GetDACStartup()</a>
     */
    public boolean getDacStartup(int channel) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel, //devNum is the channel when getting
                MeasurementComputingUniversalLibrary.BIDACSTARTUP
        ) == 1;
    }

    /**
     * Set whether DAC values get saved in non-volatile memory on the DAQ board.
     * <p>
     * Each time a DAC board is powered up, the board reads DAC values stored in onboard non-volatile memory and sets
     * the DAC output to those values.
     * <p>
     * To store the current DAC values as start-up values: first, call {@code setDACStartup(true)}. Then, each time you
     * call AOut() or AOutScan(), the value written for each channel is stored in non-volatile memory on the DAQ board.
     * The last value written to a particular channel when {@code getDacStartup() == true} is the value that channel
     * will be set to at power up. Call {@code setDACStartup(false)} stop storing values in non-volatile memory on the
     * DAQ board.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACStartup.htm">BoardConfig.GetDACStartup()</a>
     */
    public void setDacStartup(boolean enable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored when setting
                MeasurementComputingUniversalLibrary.BIDACSTARTUP,
                (enable ? 1 : 0)
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACTRIGCOUNT -> BI DAC TRIG COUNT -> boardInfo DAC trigger count
     Readable? yes
     Writable? yes
     */

    /**
     * Gets the number of samples to generate during each trigger event when ScanOptions.RetrigMode is enabled.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACRetrigCount.htm">BoardConfig.GetDACRetrigCount()</a>
     */
    public int getDacTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDACTRIGCOUNT
        );
    }

    /**
     * Sets the number of samples to generate during each trigger event when ScanOptions.RetrigMode is enabled.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDACRetrigCount.htm">BoardConfig.SetDACRetrigCount()</a>
     */
    public void setDacTriggerCount(int triggerCount) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDACTRIGCOUNT,
                triggerCount
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACUPDATEMODE -> BI DAC UPDATE MODE  -> boardInfo DAC update mode
     Readable? yes
     Writable? yes
     */

    /**
     * @see #setDacUpdateMode
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACUpdateMode.htm">BoardConfig.GetDACUpdateMode()</a>
     */
    public DacUpdateMode getDacUpdateMode() throws JMCCULException {
        return DacUpdateMode.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIDACUPDATEMODE
                )
        );
    }

    /**
     * <ul>
     *      <li>If the value is {@link DacUpdateMode#IMMEDIATE}, then values written with cbAOut() or cbAOutScan() are automatically output by the DAC channels.</li>
     *      <li>If the value is {@link DacUpdateMode#ON_COMMAND}, then values written with cbAOut() or cbAOutScan() are not output by the DAC channels until you call {@link #updateAnalogOutput()}.</li>
     * </ul>
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACUpdateMode.htm">BoardConfig.GetDACUpdateMode()</a>
     */
    public void setDacUpdateMode(DacUpdateMode updateMode) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDACUPDATEMODE,
                updateMode.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BINUMDACHANS -> BI NUM DA CHANS -> boardInfo number of DA channels
     Readable? yes
     Writable? no
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetNumDaChans.htm">BoardConfig.GetNumDaChans()</a>
     */
    public int getDacChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMDACHANS
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACUPDATECMD -> BI DAC UPDATE CMD -> boardInfo DAC update command

     */

    /**
     * Updates the voltage values on analog output channels. This method is only useful if you first call
     * {@link #setDacUpdateMode} with {@link DacUpdateMode#ON_COMMAND}.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/DACUpdate.htm">BoardConfig.DACUpdate()</a>
     */
    public void updateAnalogOutput() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is not used
                MeasurementComputingUniversalLibrary.BIDACUPDATECMD,
                0 //configVal is not used
        );
    }

    /*
    // BIDACSETTLETIME is not in my JNA thing
    public void setDacSettlingTime() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDACSETTLETIME,
                0 //new value
        );
    }
     */

}
