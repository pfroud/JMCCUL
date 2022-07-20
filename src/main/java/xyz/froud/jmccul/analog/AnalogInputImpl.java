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
import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

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
public class AnalogInputImpl {

    private final DaqDevice DAQ_DEVICE;
    private final int BOARD_NUMBER;

    private Integer channelCount;
    private Integer resolution;
    private Integer packetSize;
    private Integer triggerResolution;

    private List<AnalogRange> supportedRanges;
    private AnalogRange analogTriggerRange;

    private Boolean isVoltageInputSupported;
    private Boolean isAnalogTriggerSupported;
    private Boolean isGainQueueSupported;
    private Boolean isScanSupported;

    public AnalogInputImpl(DaqDevice device) {
        DAQ_DEVICE = device;
        BOARD_NUMBER = device.getBoardNumber();
    }


    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L41">is_supported
     *         in ai_info.py</a>
     */
    public boolean isAnalogInputSupported() throws JMCCULException {
        return getAdcChannelCount() > 0;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdResolution.htm">BoardConfig.GetAdResolution()</a>
     */
    public int getResolution() throws JMCCULException {
        if (resolution == null) {
            resolution = Configuration.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    BOARD_NUMBER,
                    0,
                    MeasurementComputingUniversalLibrary.BIADRES
            );
        }
        return resolution;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L59">supports_scan
     *         in ai_info.py</a>
     */
    public boolean isScanSupported() throws JMCCULException {
        if (isScanSupported == null) {
            try {
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetIOStatus(
                        BOARD_NUMBER,
                        ShortBuffer.allocate(1),
                        new NativeLongByReference(new NativeLong(0)),
                        new NativeLongByReference(new NativeLong(0)),
                        MeasurementComputingUniversalLibrary.AIFUNCTION
                );
                JMCCULUtils.checkError(errorCode);
                isScanSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                isScanSupported = false;
            }
        }
        return isScanSupported;

    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L68">supported_rangees
     *         in ai_info.py</a>
     */
    public List<AnalogRange> getSupportedRanges() throws JMCCULException {
        if (supportedRanges == null) {
            supportedRanges = new ArrayList<>();

            // Check if the board has a switch-selectable, or only one, range.
            // I do not really understand this so I am not repalcing this call with getRange().
            final int hardRange = Configuration.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    BOARD_NUMBER,
                    0,
                    MeasurementComputingUniversalLibrary.BIRANGE
            );

            if (hardRange >= 0) {
                supportedRanges.add(AnalogRange.parseInt(hardRange));
            } else {
                // try all the ranges
                for (AnalogRange rangeToCheck : AnalogRange.values()) {
                    try {
                        if (getResolution() <= 16) {
                            analogInput(0, rangeToCheck);
                        } else {
                            analogInput32(0, rangeToCheck);
                        }
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
     * The hardware in the following table will return a packet size. This hardware must use an integer multiple of the
     * packet size as the total_count for a_in_scan when using the CONTINUOUS option in BLOCKIO mode.
     * <p>
     * For all other hardware, this method will return 1.
     *
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
        if (packetSize == null) {
            packetSize = switch (DAQ_DEVICE.getProductID()) {
                case 122 -> 64;
                case 130, 161, 240 -> 31;
                default -> 1;
            };
        }
        return packetSize;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L121">supports_v_in
     *         in ai_info.py</a>
     */
    public boolean isVoltageInputSupported() throws JMCCULException {
        if (isVoltageInputSupported == null) {
            final List<AnalogRange> supportedRanges = getSupportedRanges();
            if (supportedRanges.isEmpty()) {
                isVoltageInputSupported = false;
            } else {
                try {
                    voltageInput(0, supportedRanges.get(0));
                    isVoltageInputSupported = true;
                } catch (JMCCULException ex) {
                    ex.throwIfErrorIsNetworkDeviceInUse();
                    isVoltageInputSupported = false;
                }
            }
        }
        return isVoltageInputSupported;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L134">analog_trig_resolution
     *         in ai_info.py</a>
     */
    public int getAnalogTriggerResolution() {
        if (triggerResolution == null) {

            triggerResolution = switch (DAQ_DEVICE.getProductID()) {
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
        return triggerResolution;

    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L155">analog_trig_range
     *         in ai_info.py</a>
     */
    public AnalogRange getAnalogTriggerRange() throws JMCCULException {
        if (analogTriggerRange == null) {
            int trigSource;
            try {
                trigSource = getAdcTriggerSourceChannel(0);
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                trigSource = 0;
            }

            if (getAnalogTriggerResolution() > 0 && trigSource <= 0) {
                analogTriggerRange = AnalogRange.BIPOLAR_10_VOLTS;
            } else {
                analogTriggerRange = AnalogRange.UNKNOWN;
            }
        }
        return analogTriggerRange;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L">supports_analog_trid
     *         in ai_info.py</a>
     */
    public boolean isAnalogTriggerSupported() throws JMCCULException {
        if (isAnalogTriggerSupported == null) {
            try {
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbSetTrigger(
                        BOARD_NUMBER,
                        MeasurementComputingUniversalLibrary.TRIGABOVE,
                        (short) 0,
                        (short) 0
                );
                JMCCULUtils.checkError(errorCode);
                isAnalogTriggerSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                isAnalogTriggerSupported = false;
            }
        }
        return isAnalogTriggerSupported;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L190">supports_gain_queue
     *         in ai_info.py</a>
     */
    public boolean isGainQueueSupported() throws JMCCULException {
        if (isGainQueueSupported == null) {
            try {
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbALoadQueue(
                        BOARD_NUMBER,
                        ShortBuffer.allocate(0),
                        ShortBuffer.allocate(0),
                        0
                );
                JMCCULUtils.checkError(errorCode);
                isGainQueueSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                isGainQueueSupported = false;
            }
        }
        return isGainQueueSupported;

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
    public short analogInput(int channel, AnalogRange range) throws JMCCULException {
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
    public long analogInput32(int channel, AnalogRange range) throws JMCCULException {

        final NativeLongByReference nlbr = new NativeLongByReference(new NativeLong());

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn32.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAIn32(
                BOARD_NUMBER,
                channel,
                range.VALUE,
                nlbr,
                0
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
    public float voltageInput(int channel, AnalogRange range) throws JMCCULException {

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
    public double voltageInput32(int channel, AnalogRange range) throws JMCCULException {

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
    public AnalogRange getAdcRange() throws JMCCULException {
        return AnalogRange.parseInt(
                Configuration.getInt(
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
    public void setAdcRange(AnalogRange range) throws JMCCULException {
        Configuration.setInt(
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
    public int getAdcChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMADCHANS
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetNumAdChans.htm">BoardConfig.SetNumAdChans()</a>
     */
    public void setAdcChannelCount(int n) throws JMCCULException {
        Configuration.setInt(
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
    public int getAdcDataRate(int channel) throws JMCCULException {
        return Configuration.getInt(
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
    public void setAdcDataRate(int channel, int dataRate) throws JMCCULException {
        Configuration.setInt(
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
    public AdcTransferMode getAdcDataTransferMode() throws JMCCULException {
        return AdcTransferMode.parseInt(
                Configuration.getInt(
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
    public void setAdcDataTransferMode(AdcTransferMode transferMode) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADXFERMODE,
                transferMode.VALUE
        );
    }


    public int getAdcResolution() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADRES
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
    public AdcSettlingTime getAdcSettlingTime() throws JMCCULException {
        return AdcSettlingTime.parseInt(
                Configuration.getInt(
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
    public void setAdcSettlingTime(AdcSettlingTime settleTime) throws JMCCULException {
        Configuration.setInt(
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
    public AdcTimingMode getAdcTimingMode() throws JMCCULException {
        return AdcTimingMode.parseInt(
                Configuration.getInt(
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
    public void setAdcTimingMode(AdcTimingMode newTimingMode) throws JMCCULException {
        Configuration.setInt(
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
    public int getAdcTriggerCount() throws JMCCULException {
        return Configuration.getInt(
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
    public void setAdcTriggerCount(int triggerCount) throws JMCCULException {
        Configuration.setInt(
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
    public int getAdcTriggerSourceChannel(int channel) throws JMCCULException {
        /*
        Not sure what this is, it just says:
        Use this setting in conjunction with one of these ConfigVal settings:
        0
        1
        2
        3
        According to the dot net docs, the number is the channel to use as the trigger source.
         */
        return Configuration.getInt(
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
    public void setAdcTriggerSourceChannel(int channel, int n) throws JMCCULException {
        Configuration.setInt(
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
    public AnalogInputChannelType getAnalogInputChannelType(int channel) throws JMCCULException {
        return AnalogInputChannelType.parseInt(
                Configuration.getInt(
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
    public void setAnalogInputChannelType(int channel, AnalogInputChannelType channelType) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADCHANTYPE,
                channelType.VALUE
        );
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetChanAIMode.htm">BoardConfig.GetChanAIMode()</a>
     */
    public int getAnalogInputChannelMode(int channel) throws JMCCULException {
        // you set this with cbAChanInputMode(). confirmed in the dot ent docs for GetChanAIMode().
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADCHANAIMODE //BI AD CHAN AI MODE
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAIMode.htm">BoardConfig.GetAIMode()</a>
     */
    public int getAnalogInputMode() throws JMCCULException {
        // you set this with cbAInputMode(). confirmed in the dot net docs for GetAIMode().
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADAIMODE //BI AD AI MODE
        );
    }


}
