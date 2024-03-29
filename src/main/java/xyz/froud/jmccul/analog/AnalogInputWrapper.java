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

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.config.ConfigurationWrapper;
import xyz.froud.jmccul.MeasurementComputingUniversalLibrary;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Analog input = analog to digital = "A2D" or "AD" or "ADC"
 *
 * @author Peter Froud
 * @see <a href="https://github.com/mccdaq/mcculw/blob/master/mcculw/device_info/ai_info.py">ai_info.py</a>
 */
public class AnalogInputWrapper {

    private final DaqDevice DAQ_DEVICE;
    private final int BOARD_NUMBER;

    /*
    Underscore prefix means the field is lazy-loaded in the getter method.
    It is a reminder to call the getter method instead of reading the field directly.
     */
    private Integer _channelCount;
    private Integer _resolution;
    private Integer _packetSize;
    private Integer _triggerResolution;

    private List<AnalogRange> _supportedRanges;
    private AnalogRange _analogTriggerRange;

    private Boolean _isVoltageInputSupported;
    private Boolean _isAnalogTriggerSupported;
    private Boolean _isGainQueueSupported;
    private Boolean _isScanSupported;

    public AnalogInputWrapper(DaqDevice device) {
        DAQ_DEVICE = device;
        BOARD_NUMBER = device.getBoardNumber();
    }


    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L41">is_supported
     *         in ai_info.py</a>
     */
    public boolean isSupported() throws JMCCULException {
        return getChannelCount() > 0;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdResolution.htm">BoardConfig.GetAdResolution()</a>
     */
    public int getResolution() throws JMCCULException {
        if (_resolution == null) {
            _resolution = ConfigurationWrapper.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    BOARD_NUMBER,
                    0,
                    MeasurementComputingUniversalLibrary.BIADRES
            );
        }
        return _resolution;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L59">supports_scan
     *         in ai_info.py</a>
     */
    public boolean isScanSupported() throws JMCCULException {
        if (_isScanSupported == null) {
            try {
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetIOStatus(
                        BOARD_NUMBER,
                        ShortBuffer.allocate(1),
                        new NativeLongByReference(new NativeLong(0)),
                        new NativeLongByReference(new NativeLong(0)),
                        MeasurementComputingUniversalLibrary.AIFUNCTION
                );
                JMCCULUtils.checkError(errorCode);
                _isScanSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                _isScanSupported = false;
            }
        }
        return _isScanSupported;

    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L68">supported_rangees
     *         in ai_info.py</a>
     */
    public List<AnalogRange> getSupportedRanges() throws JMCCULException {
        if (_supportedRanges == null) {
            _supportedRanges = new ArrayList<>();

            // Check if the board has a switch-selectable, or only one, range.
            // I do not really understand this so I am not repalcing this call with getRange().
            final int hardRange = ConfigurationWrapper.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    BOARD_NUMBER,
                    0,
                    MeasurementComputingUniversalLibrary.BIRANGE
            );

            if (hardRange >= 0) {
                _supportedRanges.add(AnalogRange.parseInt(hardRange));
            } else {
                // try all the ranges
                for (AnalogRange rangeToCheck : AnalogRange.values()) {
                    try {
                        if (getResolution() <= 16) {
                            read(0, rangeToCheck);
                        } else {
                            read32(0, rangeToCheck);
                        }
                        _supportedRanges.add(rangeToCheck);
                    } catch (JMCCULException ex) {
                        ex.throwIfErrorIsNetworkDeviceInUse();
                    }
                }
            }
        }
        return _supportedRanges;
    }

    /**
     * The hardware in the following table will return a packet size. This hardware must use an integer multiple of the
     * packet size as the total_count for a_in_scan when using the CONTINUOUS option in BLOCKIO mode.
     * <p>
     * For all other hardware, this method will return 1.
     * <table summary="Packet sizes by product">
     *      <thead>
     *          <tr><th>Hardware</th> <th>Product ID</th> <th>Packet size</th></tr>
     *      </thead>
     *      <tbody>
     *        <tr>
     *          <td>USB-1208LS</td> <td>122</td> <td>64</td>
     *        </tr> <tr>
     *          <td>USB-1208FS</td> <td>130</td> <td>31</td>
     *        </tr> <tr>
     *          <td>USB-1408FS</td> <td>161</td> <td>31</td>
     *        </tr> <tr>
     *          <td>USB-7204</td> <td>240</td> <td>31</td>
     *        </tr>
     *      </tbody>
     * </table>
     *
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L93">packet_size
     *         in ai_info.py</a>
     */
    public int getPacketSize() {
        if (_packetSize == null) {
            _packetSize = switch (DAQ_DEVICE.getProductID()) {
                case 122 -> 64;
                case 130, 161, 240 -> 31;
                default -> 1;
            };
        }
        return _packetSize;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L121">supports_v_in
     *         in ai_info.py</a>
     */
    public boolean isVoltageInputSupported() throws JMCCULException {
        if (_isVoltageInputSupported == null) {
            final List<AnalogRange> supportedRanges = getSupportedRanges();
            if (supportedRanges.isEmpty()) {
                _isVoltageInputSupported = false;
            } else {
                try {
                    readVoltage(0, supportedRanges.get(0));
                    _isVoltageInputSupported = true;
                } catch (JMCCULException ex) {
                    ex.throwIfErrorIsNetworkDeviceInUse();
                    _isVoltageInputSupported = false;
                }
            }
        }
        return _isVoltageInputSupported;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L134">analog_trig_resolution
     *         in ai_info.py</a>
     */
    public int getTriggerResolution() {
        if (_triggerResolution == null) {

            _triggerResolution = switch (DAQ_DEVICE.getProductID()) {
                /*
                Twelve-bit boards:
                    PCI-DAS6030, 6031, 6032, 6033, 6052
                    USB-1602HS, 1602HS-2AO, 1604HS, 1604HS-2AO
                    PCI-2511, 2513, 2515, 2517, USB-2523, 2527, 2533, 2537
                    USB-1616HS, 1616HS-2, 1616HS-4, 1616HS-BNC
                */
                case 95, 96, 97, 98, 102, 165, 166, 167, 168, 177, 178, 179, 180, 203, 204, 205, 213, 214, 215, 216, 217 ->
                        12;

                // Eight-bit boards: PCI-DAS6040, 6070, 6071
                case 101, 103, 104 -> 8;
                default -> 0;
            };
        }
        return _triggerResolution;

    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L155">analog_trig_range
     *         in ai_info.py</a>
     */
    public AnalogRange getTriggerRange() throws JMCCULException {
        if (_analogTriggerRange == null) {
            int trigSource;
            try {
                trigSource = getTriggerSourceChannel(0);
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                trigSource = 0;
            }

            if (getTriggerResolution() > 0 && trigSource <= 0) {
                _analogTriggerRange = AnalogRange.BIPOLAR_10_VOLTS;
            } else {
                _analogTriggerRange = AnalogRange.UNKNOWN;
            }
        }
        return _analogTriggerRange;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L">supports_analog_trid
     *         in ai_info.py</a>
     */
    public boolean isTriggerSupported() throws JMCCULException {
        if (_isAnalogTriggerSupported == null) {
            try {
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbSetTrigger(
                        BOARD_NUMBER,
                        MeasurementComputingUniversalLibrary.TRIGABOVE,
                        (short) 0,
                        (short) 0
                );
                JMCCULUtils.checkError(errorCode);
                _isAnalogTriggerSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                _isAnalogTriggerSupported = false;
            }
        }
        return _isAnalogTriggerSupported;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L190">supports_gain_queue
     *         in ai_info.py</a>
     */
    public boolean isGainQueueSupported() throws JMCCULException {
        if (_isGainQueueSupported == null) {
            try {
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbALoadQueue(
                        BOARD_NUMBER,
                        ShortBuffer.allocate(0),
                        ShortBuffer.allocate(0),
                        0
                );
                JMCCULUtils.checkError(errorCode);
                _isGainQueueSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                _isGainQueueSupported = false;
            }
        }
        return _isGainQueueSupported;

    }

    /**
     * Reads an A/D input channel from the specified board, and returns a 16-bit unsigned integer value.
     * <p>
     * If the specified A/D board has programmable gain then it sets the gain to the specified range. The raw A/D value
     * is converted to an A/D value and returned.
     *
     * @param channel A/D channel number. The maximum allowable channel depends on which type of A/D board is
     *         being used. For boards with both single ended and differential inputs, the maximum allowable channel
     *         number also depends on how the board is configured. For example, a USB-1608GX device has 8 differential
     *         or 16 single-ended analog input channels. Expansion boards also support this function, so this argument
     *         can contain values up to 272. See board specific information for EXP boards if you are using an expansion
     *         board.
     * @param range A/D range. If the selected A/D board does not have a programmable gain feature, this
     *         argument is ignored. If the A/D board does have programmable gain, set the Range argument to the desired
     *         A/D range. Refer to board specific information for a list of the supported A/D ranges of each board.
     *
     * @return the value of the A/D sample.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn.htm">cbAIn()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/AIn.htm">AIn()</a>
     */
    public short read(int channel, AnalogRange range) throws JMCCULException {
        final ShortBuffer buf = ShortBuffer.allocate(1);
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAIn(
                BOARD_NUMBER,
                channel,
                range.VALUE,
                buf
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get();
    }

    /**
     * Reads an A/D input channel from the specified board, and returns a 32-bit unsigned integer value.
     * <p>
     * If the specified A/D board has programmable gain then it sets the gain to the specified range. The raw A/D value
     * is converted to an A/D value and returned. In general, this function should be used with devices with a
     * resolution higher than 16-bits.
     *
     * @param channel A/D channel number. The maximum allowable channel depends on which type of A/D board is
     *         being used. For boards with both single ended and differential inputs, the maximum allowable channel
     *         number also depends on how the board is configured. For example, a USB-1608GX device has 8 differential
     *         or 16 single-ended analog input channels. Expansion boards also support this function, so this argument
     *         can contain values up to 272. See board specific information for EXP boards if you are using an expansion
     *         board.
     * @param range A/D Range. If the selected A/D board does not have a programmable gain feature, this
     *         argument is ignored. If the A/D board does have programmable gain, set the Range argument to the desired
     *         A/D range. Refer to board specific information for a list of the supported A/D ranges of each board.
     *
     * @return the value of the A/D sample.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn32.htm">cbAIn32()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/AIn32.htm">AIn32()</a>
     */
    public long read32(int channel, AnalogRange range) throws JMCCULException {

        final NativeLongByReference nlbr = new NativeLongByReference(new NativeLong());

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn32.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAIn32(
                BOARD_NUMBER,
                channel,
                range.VALUE,
                nlbr,
                0 //reserved for future use
        );
        JMCCULUtils.checkError(errorCode);
        return nlbr.getValue().longValue();
    }

    /**
     * Reads an A/D input channel, and returns a voltage value.
     * <p>
     * If the specified A/D board has programmable gain, then this function sets the gain to the specified range. The
     * voltage value is returned to DataValue.
     *
     * @param channel A/D channel number. The maximum allowable channel depends on which type of A/D board is
     *         being used. For boards with both single-ended and differential inputs, the maximum allowable channel
     *         number also depends on how the board is configured.
     * @param range A/D range code. If the board has a programmable gain, it will be set according to this
     *         argument value. Keep in mind that some A/D boards have a programmable gain feature, and others set the
     *         gain via switches on the board. In either case, the range that the board is configured for must be passed
     *         to this function. Refer to board specific information for a list of the supported A/D ranges of each
     *         board.
     *
     * @return the value in volts of the A/D sample.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVIn.htm">cbVIn()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/VIn.htm">VIn()</a>
     */
    public float readVoltage(int channel, AnalogRange range) throws JMCCULException {

        final FloatBuffer buf = FloatBuffer.allocate(1);

        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbVIn(
                BOARD_NUMBER,
                channel,
                range.VALUE,
                buf,
                0
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get();
    }

    /**
     * Reads an A/D input channel, and returns a voltage value. This function is similar to cbVIn(), but returns a
     * double precision float value instead of a single precision float value. If the specified A/D board has
     * programmable gain, then this function sets the gain to the specified range. The voltage value is returned to
     * DataValue.
     *
     * @param channel A/D channel number. The maximum allowable channel depends on which type of A/D board is
     *         being used. For boards with both single-ended and differential inputs, the maximum allowable channel
     *         number also depends on how the board is configured.
     * @param range A/D range code. If the board has a programmable gain, it will be set according to this
     *         argument value. Keep in mind that some A/D boards have a programmable gain feature, and others set the
     *         gain via switches on the board. In either case, the range that the board is configured for must be passed
     *         to this function. Refer to board specific information for a list of the supported A/D ranges of each
     *         board.
     *
     * @return the value in volts of the A/D sample.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVIn32.htm">cbVIn32()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/VIn32.htm">VIn32()</a>
     */
    public double readVoltage32(int channel, AnalogRange range) throws JMCCULException {

        final DoubleBuffer buf = DoubleBuffer.allocate(1);

        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbVIn32(
                BOARD_NUMBER,
                channel,
                range.VALUE,
                buf,
                0
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get();
    }

    /**
     * Scans a range of A/D channels and stores the samples in an array. cbAInScan() reads the specified number of A/D
     * samples at the specified sampling rate from the specified range of A/D channels from the specified board. If the
     * A/D board has programmable gain, then it sets the gain to the specified range. The collected data is returned to
     * the data array.
     * <p>
     * <b>Important:</b> In order to understand the functions, you must read the board-specific information found in
     * the Universal Library User's Guide. The example programs should be examined and run prior to attempting any
     * programming of your own. Following this advice will save you hours of frustration, and possibly time wasted
     * holding for technical support.
     * <p>
     * This note, which appears elsewhere, is especially applicable to this function. Now is the time to read the board
     * specific information for your board that is contained in the Universal Library User's Guide. We suggest that you
     * make a copy of this information for reference as you read this manual and examine the example programs.
     *
     * @param lowChan The first A/D channel in the scan. When cbALoadQueue() is used, the channel count is
     *         determined by the total number of entries in the channel gain queue, and LowChan is ignored.
     * @param highChan The last A/D channel in the scan. When cbALoadQueue() is used, the channel count is
     *         determined by the total number of entries in the channel gain queue, and HighChan is ignored.
     *         <p>
     *         Low / High Channel number: The maximum allowable channel depends on which type of A/D board is being
     *         used. For boards that have both single ended and differential inputs the maximum allowable channel number
     *         also depends on how the board is configured. For example, a CIO-DAS1600 has 8 channels for differential,
     *         16 for single ended.
     * @param count The number of A/D samples to collect. Specifies the total number of A/D samples that will be
     *         collected. If more than one channel is being sampled, the number of samples collected per channel is
     *         equal to Count / (HighChan – LowChan + 1).
     * @param rateHz The rate at which samples are acquired, in samples per second per channel.
     *         <p>
     *         For example, if you sample four channels, 0 to 3, at a rate of 10,000 scans per second (10 kHz), the
     *         resulting A/D converter rate is 40 kHz: four channels at 10,000 samples per channel per second. This is
     *         different from some software where you specify the total A/D chip rate. In those systems, the per channel
     *         rate is equal to the A/D rate divided by the number of channels in a scan.
     *         <p>
     *         The channel count is determined by the LowChan and HighChan parameters. Channel Count = (HighChan -
     *         LowChan + 1).
     *         <p>
     *         When cbALoadQueue is used, the channel count is determined by the total number of entries in the channel
     *         gain queue. LowChan and HighChan are ignored.
     *         <p>
     *         Rate also returns the value of the actual rate set, which may be different from the requested rate
     *         because of pacer limitations.
     *         <p>
     *         Caution! You will generate an error if you specify a total A/D rate beyond the capability of the board.
     *         For example, if you specify LowChan = 0, HighChan = 7 (8 channels total), and Rate = 20,000, and you are
     *         using a CIO-DAS16/JR, you will get an error – you have specified a total rate of 8*20,000 = 160,000, but
     *         the CIO-DAS16/JR is capable of converting only 120,000 samples per second.
     *         <p>
     *         The maximum sampling rate depends on the A/D board that is being used. It is also dependent on the
     *         sampling mode options.
     * @param range A/D range code. If the selected A/D board does not have a programmable range feature, this
     *         argument is ignored. Otherwise, set the Range argument to any range that is supported by the selected A/D
     *         board. Refer to board specific information for a list of the supported A/D ranges of each board.
     * @param options Bit fields that control various options. This field may contain any combination of
     *         non-contradictory choices from the values listed in the Options argument values section below.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAInScan.htm">cbAInScan()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/AInScan.htm">AInScan()</a>
     */
    private void scan(int lowChan, int highChan, long count, long rateHz, AnalogRange range, AnalogInputScanOptions... options) throws JMCCULException {

        final NativeLongByReference rateByReference = new NativeLongByReference(new NativeLong(rateHz));

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        final MeasurementComputingUniversalLibrary.HGLOBAL windowsBuffer = MeasurementComputingUniversalLibrary.INSTANCE.cbWinBufAlloc(new NativeLong(count));

        // TODO how do I put data into the buffer?????

        final int errorCodeCbAInScan = MeasurementComputingUniversalLibrary.INSTANCE.cbAInScan(
                /* int boardNum  */ BOARD_NUMBER,
                /* int LowChan   */ lowChan,
                /* int highChan  */ highChan,
                /* long Count    */ new NativeLong(count),
                /* long *Rate    */ rateByReference,
                /* int Range     */ range.VALUE,
                /* int MemHandle */ windowsBuffer,
                /* int Options   */ AnalogInputScanOptions.bitwiseOr(options)

        );
        JMCCULUtils.checkError(errorCodeCbAInScan);

    }


    /* //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIRANGE -> BI RANGE -> boardInfo range
     Readable? yes
     Writable? yes

    TODO what is the difference between BI DAC RANGE and BI RANGE?
     */

    /**
     * Selected voltage range.
     * <p>
     * For switch selectable gains only. If the selected A/D board does not have a programmable gain feature, this
     * argument returns the range as defined by the install settings.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetRange.htm">BoardConfig.GetRange()</a>
     */
    public AnalogRange getRange() throws JMCCULException {
        return AnalogRange.parseInt(
                ConfigurationWrapper.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIRANGE
                )
        );
    }

    /**
     * Selected voltage range.
     * <p>
     * Refer to board-specific information for the ranges supported by a device.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetRange.htm">BoardConfig.SetRange()</a>
     */
    public void setRange(AnalogRange range) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRANGE,
                range.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BINUMADCHANS -> BI NUM AD CHANS -> boardInfo number of ADC channels
     Readable? yes
     Writable? yes??
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetNumAdChans.htm">BoardConfig.GetNumAdChans()</a>
     */
    public int getChannelCount() throws JMCCULException {
        if (_channelCount == null) {
            _channelCount = ConfigurationWrapper.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    BOARD_NUMBER,
                    0, //devNum is ignored
                    MeasurementComputingUniversalLibrary.BINUMADCHANS
            );
        }
        return _channelCount;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetNumAdChans.htm">BoardConfig.SetNumAdChans()</a>
     */
    public void setChannelCount(int n) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMADCHANS,
                n
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADDATARATE -> BI AD DATA RATE -> boardInfo ADC data rate
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdDataRate.htm">BoardConfig.GetAdDataRate()</a>
     */
    public int getDataRate(int channel) throws JMCCULException {
        return ConfigurationWrapper.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADDATARATE
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAdDataRate.htm">BoardConfig.SetAdDataRate()</a>
     */
    public void setDataRate(int channel, int dataRate) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADDATARATE,
                dataRate
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADXFERMODE -> BI AD XFER MODE -> boardInfo ADC transfer mode
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdXferMode.htm">BoardConfig.GetAdXferMode()</a>
     */
    public AdcTransferMode getDataTransferMode() throws JMCCULException {
        return AdcTransferMode.parseInt(
                ConfigurationWrapper.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIADXFERMODE
                ));
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAdXferMode.htm">BoardConfig.SetAdXferMode()</a>
     */
    public void setDataTransferMode(AdcTransferMode transferMode) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADXFERMODE,
                transferMode.VALUE
        );
    }



    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADCSETTLETIME -> BI ADC SETTLE TIME -> boardInfo ADC settle  time
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdSettlingTime.htm">BoardConfig.GetAdSettlingTime()</a>
     */
    public AdcSettlingTime getSettlingTime() throws JMCCULException {
        return AdcSettlingTime.parseInt(
                ConfigurationWrapper.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIADCSETTLETIME
                ));
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAdSettlingTime.htm">BoardConfig.SetAdSettlingTime()</a>
     */
    public void setSettlingTime(AdcSettlingTime settleTime) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADCSETTLETIME,
                settleTime.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADTIMINGMODE -> BI AD TIMING MODE -> boardInfo ADC timing mode
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdTimingMode.htm">BoardConfig.GetAdTimingMode()</a>
     */
    public AdcTimingMode getTimingMode() throws JMCCULException {
        return AdcTimingMode.parseInt(
                ConfigurationWrapper.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIADTIMINGMODE
                ));
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAdTimingMode.htm">BoardConfig.SetAdTimingMode()</a>
     */
    public void setTimingMode(AdcTimingMode newTimingMode) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADTIMINGMODE,
                newTimingMode.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADTRIGCOUNT -> BI AD TRIG COUNT -> boardInfo ADC trigger count
     Readable? yes
     Writable? yes
     */

    /**
     * Number of analog input samples to acquire during each trigger event when ScanOptions.RetrigMode is enabled.
     * <p>
     * For use with the cbAInScan()/AInScan() RETRIGMODE option to set up repetitive trigger events.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdRetrigCount.htm">BoardConfig.GetAdRetrigCount()</a>
     */
    public int getTriggerCount() throws JMCCULException {
        return ConfigurationWrapper.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADTRIGCOUNT
        );
    }

    /**
     * Number of analog input samples to acquire during each trigger event when ScanOptions.RetrigMode is enabled.
     * <p>
     * For use with the cbAInScan()/AInScan() RETRIGMODE option to set up repetitive trigger events.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAdRetrigCount.htm">BoardConfig.SetAdRetrigCount()</a>
     */
    public void setTriggerCount(int triggerCount) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADTRIGCOUNT,
                triggerCount
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADTRIGSRC -> BI AD TRIG SRC -> boardInfo ADC trigger count
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdTrigSource.htm">BoardConfig.GetAdTrigSource()</a>
     */
    public int getTriggerSourceChannel(int channel) throws JMCCULException {
        /*
        Not sure what this is, it just says:
        Use this setting in conjunction with one of these ConfigVal settings:
        0
        1
        2
        3
        According to the dot net docs, the number is the channel to use as the trigger source.
         */
        return ConfigurationWrapper.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADTRIGSRC
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAdTrigSource.htm">BoardConfig.SetAdTrigSource()</a>
     */
    public void setTriggerSourceChannel(int channel, int n) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADTRIGSRC,
                n
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADCHANTYPE -> BI AD CHAN TYPE -> boardInfo ADC channel type
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAIChanType.htm">BoardConfig.GetAIChanType()</a>
     */
    public AnalogInputChannelType getChannelType(int channel) throws JMCCULException {
        return AnalogInputChannelType.parseInt(
                ConfigurationWrapper.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        channel,
                        MeasurementComputingUniversalLibrary.BIADCHANTYPE
                )
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAIChanType.htm">BoardConfig.SetAIChanType()</a>
     */
    public void setChannelType(int channel, AnalogInputChannelType channelType) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADCHANTYPE,
                channelType.VALUE
        );
    }

     /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADCHANAIMODE -> BI AD CHAN AI MODE -> bordInfo ADC channel analogInput mode
     Readable? Yes, using the BIADCHANAIMODE config item.
     Writable? Yes, using the dedicated cbAChanInputMode() function.

     Confirmed in the dot-net docs for BoardConfig.GetChanAIMode()
     The only other thing to be get and set in different ways is the analog input mode for the whole board.
     */

    /**
     * Gets the analog input mode for a specific channel.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetChanAIMode.htm">BoardConfig.GetChanAIMode()</a>
     */
    public AnalogInputMode getModeForChannel(int channel) throws JMCCULException {
        return AnalogInputMode.parseInt(
                ConfigurationWrapper.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        channel,
                        MeasurementComputingUniversalLibrary.BIADCHANAIMODE //BI AD CHAN AI MODE
                )
        );
    }

    /**
     * Sets the analog input mode for a specific channel.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetChanAIMode.htm">BoardConfig.GetChanAIMode()</a>
     */
    public void setModeForChannel(int channel, AnalogInputMode mode) throws JMCCULException {
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAChanInputMode(BOARD_NUMBER, channel, mode.VALUE);
        JMCCULUtils.checkError(errorCode);

    }



    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADAIMODE -> BI AD AI MODE -> bordInfo ADC analogInput mode
     Readable? Yes, using the BIADAIMODE config item.
     Writable? Yes, using the dedicated cbAInputMode() function.

     Confirmed in the dot-net docs for BoardConfig.GetAIMode()
     The only other thing to be get and set in different ways is the analog input mode for a specific channel.
     */

    /**
     * Gets the analog input mode for the whole board.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAIMode.htm">BoardConfig.GetAIMode()</a>
     */
    public AnalogInputMode getModeForBoard() throws JMCCULException {
        return AnalogInputMode.parseInt(
                ConfigurationWrapper.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIADAIMODE //BI AD AI MODE
                )
        );
    }

    /**
     * Sets the analog input mode for the whole board.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbAInputMode.htm">cbAInputMode()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/AInputMode.htm">AInputMode()</a>
     */
    public void setModeForBoard(AnalogInputMode mode) throws JMCCULException {
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAInputMode(BOARD_NUMBER, mode.VALUE);
        JMCCULUtils.checkError(errorCode);
    }


}
