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

package xyz.froud.jmccul.temperature;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.config.ConfigurationWrapper;
import xyz.froud.jmccul.enums.BaseOrExpansionBoard;
import xyz.froud.jmccul.MeasurementComputingUniversalLibrary;

import java.nio.FloatBuffer;

/**
 * @author Peter Froud
 */
public class TemperatureWrapper {

    private final DaqDevice DAQ_DEVICE;
    private final int BOARD_NUMBER;

    /*
    Underscore prefix means the field is lazy-loaded in the getter method.
    It is a reminder to call the getter method instead of reading the field directly.
    */
    private Integer _channelCount;

    public TemperatureWrapper(DaqDevice device) {
        DAQ_DEVICE = device;
        BOARD_NUMBER = device.getBoardNumber();
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetNumTempChans.htm">BoardConfig.GetNumTempChans()</a>
     */
    public int getChannelCount() throws JMCCULException {
        if (_channelCount == null) {
            // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L45
            _channelCount = ConfigurationWrapper.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    DAQ_DEVICE.getBoardNumber(),
                    0, //device number
                    MeasurementComputingUniversalLibrary.BINUMTEMPCHANS
            );
        }
        return _channelCount;

    }

    public boolean isSupported() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L50
        return getChannelCount() > 0;
    }


    /**
     * Reads an analog input channel, linearizes it according to the selected temperature sensor type, if required, and
     * returns the temperature in units determined by the Scale argument.
     * <p>
     * Important! For an EXP board connected to an A/D board that does not have programmable gain (DAS08, DAS16,
     * DAS16F), the A/D board range is read from the configuration file (cb.cfg). In most cases, set hardware-selectable
     * ranges to ±5 V for thermocouples, and to 0 to 10 V for RTDs. Refer to the board-specific information in the
     * Universal Library User's Guide or in the user manual for your board. If the board has programmable gains, the
     * cbTIn() function sets the appropriate A/D range.
     * <p>
     * <b>Using CIO-EXP boards</b>
     * <p>
     * For CIO-EXP boards, the channel number is calculated using the formula<br> Chan = (ADChan × 16) + (16 +
     * MuxChan)<br> where:
     * <ul>
     *    <li>ADChan is the A/D channel that is connected to the multiplexer.</li>
     *    <li>MuxChan is a number ranging from 0 to 15 that specifies the channel number on a particular bank of the multiplexer board.</li>
     * </ul>
     * For example, you have an EXP16 connected to a CIO-DAS08 via the CIO-DAS08 channel 0.
     * (Remember that DAS08 channels are numbered 0, 1, 2, 3, 4, 5, 6 and 7).
     * If you connect a thermocouple to channel 5 of the EXP16, the value for Chan would be (0 × 16) + (16 + 5)= 0 + 21 = 21.
     *
     * <p>
     *     <b>CJC channel</b>
     * <p>
     *     The CJC channel is set in the InstaCal installation and configuration program. If you have multiple EXP boards, the Universal Library will apply the CJC reading to the linearization formula in the following manner:
     * <ul>
     *
     * <li>If you have chosen a CJC channel for the EXP board that the channel you are reading is on, it will use the CJC temp reading from that channel.</li>
     * <li>If you left the CJC channel for the EXP board that the channel you are reading is on to NOT SET, the library will use the CJC reading from the next lower EXP board with a CJC channel selected.</li>
     * </ul>
     * For example: You have four CIO-EXP16 boards connected to a CIO-DAS08 on channel 0, 1, 2 and 3. You choose
     * CIO-EXP16 #1 (connected to CIO-DAS08 channel 0) to have its CJC read on CIO-DAS08 channel 7, AND, you leave the
     * CIO-EXP16's 2, 3 and 4 CJC channels to NOT SET. Result: The CIO-EXP boards will all use the CJC reading from
     * CIO-EXP16 #1, connected to channel 7 for linearization. <i>It is important to keep the CIO-EXP boards in the same
     * case and out of any breezes to ensure a clean CJC reading.</i>
     *
     * @param scale Specifies the temperature scale that the input will be converted to.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions/cbTIn.htm">cbTIn()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions_for_NET/TIn.htm">TIn()</a>
     */
    public float read(int channel, TemperatureUnit scale, TemperatureInputOptions... options) throws JMCCULException {
        final FloatBuffer temperatureFloat = FloatBuffer.allocate(1);

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions/cbTIn.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbTIn(
                DAQ_DEVICE.getBoardNumber(),
                channel,
                scale.VALUE,
                temperatureFloat,
                TemperatureInputOptions.bitwiseOr(options)
        );

        JMCCULUtils.checkError(errorCode);
        return temperatureFloat.get();
    }


    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BITEMPAVG -> BI TEMP AVG -> boardInfo temperature average
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetTempAvg.htm">BoardConfig.GetTempAvg()</a>
     */
    public int getScansToAverage() throws JMCCULException {
        return ConfigurationWrapper.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPAVG
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetTempAvg.htm">BoardConfig.SetTempAvg()</a>
     */
    public void setScansToAverage(int count) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //it does not say what devNum does
                MeasurementComputingUniversalLibrary.BITEMPAVG,
                count
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BITEMPSCALE -> BI TEMP SCALE -> boardInfo temperature scale
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetTempScale.htm">BoardConfig.GetTempScale()</a>
     */
    public TemperatureUnit getUnits() throws JMCCULException {
        return TemperatureUnit.parseInt(ConfigurationWrapper.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPSCALE
        ));
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetTempScale.htm">BoardConfig.SetTempScale()</a>
     */
    public void setUnits(TemperatureUnit temperatureUnit) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPSCALE,
                temperatureUnit.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BITEMPREJFREQ -> BI TEMP REJ FREQ -> boardInfo temperature rejection frequency
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetTempRejFreq.htm">BoardConfig.GetTempRejFreq()</a>
     */
    public TemperatureRejectionFrequency getRejectionFrequency(BaseOrExpansionBoard baseOrExpansionBoard) throws JMCCULException {
        return TemperatureRejectionFrequency.parseInt(
                ConfigurationWrapper.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        baseOrExpansionBoard.VALUE,
                        MeasurementComputingUniversalLibrary.BITEMPREJFREQ
                )
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetTempRejFreq.htm">BoardConfig.SetTempRejFreq()</a>
     */
    public void setRejectionFrequency(BaseOrExpansionBoard baseOrExpansionBoard, TemperatureRejectionFrequency rejection) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                baseOrExpansionBoard.VALUE,
                MeasurementComputingUniversalLibrary.BITEMPREJFREQ,
                rejection.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDETECTOPENTC  -> BI DETECT OPEN TC -> boardInfo detect open thermocouple
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDetectOpenTC.htm">BoardConfig.GetDetectOpenTc()</a>
     */
    public boolean isOpenThermocoupleDetectionEnabled(BaseOrExpansionBoard baseOrExpansionBoard) throws JMCCULException {
        return ConfigurationWrapper.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                baseOrExpansionBoard.VALUE,
                MeasurementComputingUniversalLibrary.BIDETECTOPENTC
        ) == 1;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDetectOpenTC.htm">BoardConfig.SetDetectOpenTc()</a>
     */
    public void setOpenThermocoupleDetectionEnabled(BaseOrExpansionBoard baseOrExpansionBoard, boolean enable) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                baseOrExpansionBoard.VALUE,
                MeasurementComputingUniversalLibrary.BIDETECTOPENTC,
                (enable ? 1 : 0)
        );
    }


    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BICHANTCTYPE -> BI CHAN TC TYPE  -> boardInfo channel thermocouple type
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetChanTcType.htm">BoardConfig.GetChanTcType()</a>
     */
    public ThermocoupleType getThermocoupleType(int channel) throws JMCCULException {
        return ThermocoupleType.parseInt(
                ConfigurationWrapper.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        channel,
                        MeasurementComputingUniversalLibrary.BICHANTCTYPE
                )
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetChanTcType.htm">BoardConfig.SetChanTcType()</a>
     */
    public void setThermocoupleType(int channel, ThermocoupleType type) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BICHANTCTYPE,
                type.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BICHANRTDTYPE -> BI CHAN RTD TYPE  -> boardInfo channel resistanceTemperatureDetector type
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetChanRtdType.htm">BoardConfig.GetChanRtdType()</a>
     */
    public RtdSensorType getRtdSensorType(int channel) throws JMCCULException {
        return RtdSensorType.parseInt(
                ConfigurationWrapper.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        channel,
                        MeasurementComputingUniversalLibrary.BICHANRTDTYPE
                )
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetChanRtdType.htm">BoardConfig.SetChanRtdType()</a>
     */
    public void setRtdSensorType(int channel, RtdSensorType rtdSensor) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BICHANRTDTYPE,
                rtdSensor.VALUE
        );
    }

}
