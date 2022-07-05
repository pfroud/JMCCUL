package xyz.froud.jmccul.config;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.enums.BaseOrExpansionBoard;
import xyz.froud.jmccul.enums.RtdSensorType;
import xyz.froud.jmccul.enums.TemperatureRejection;
import xyz.froud.jmccul.enums.TemperatureScale;
import xyz.froud.jmccul.enums.ThermocoupleSensorType;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public class TemperatureConfig {

    private final int BOARD_NUMBER;

    public TemperatureConfig(DaqDevice device) {
        BOARD_NUMBER = device.getBoardNumber();
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BITEMPAVG -> BI TEMP AVG -> boardInfo temperature average
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetTempAvg.htm">BoardConfig.GetTempAvg()</a>
     */
    public int getTemperatureScansToAverage() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPAVG
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetTempAvg.htm">BoardConfig.SetTempAvg()</a>
     */
    public void setTemperatureAverageCount(int count) throws JMCCULException {
        Configuration.setInt(
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
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetTempScale.htm">BoardConfig.GetTempScale()</a>
     */
    public TemperatureScale getTemperatureScale() throws JMCCULException {
        return TemperatureScale.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPSCALE
        ));
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetTempScale.htm">BoardConfig.SetTempScale()</a>
     */
    public void setTemperatureScale(TemperatureScale temperatureScale) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPSCALE,
                temperatureScale.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BITEMPREJFREQ -> BI TEMP REJ FREQ -> boardInfo temperature rejection frequency
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetTempRejFreq.htm">BoardConfig.GetTempRejFreq()</a>
     */
    public TemperatureRejection getRejectionFrequency(BaseOrExpansionBoard baseOrExpansionBoard) throws JMCCULException {
        return TemperatureRejection.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        baseOrExpansionBoard.VALUE,
                        MeasurementComputingUniversalLibrary.BITEMPREJFREQ
                )
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetTempRejFreq.htm">BoardConfig.SetTempRejFreq()</a>
     */
    public void setRejectionFrequency(BaseOrExpansionBoard baseOrExpansionBoard, TemperatureRejection rejection) throws JMCCULException {
        Configuration.setInt(
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
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDetectOpenTC.htm">BoardConfig.GetDetectOpenTc()</a>
     */
    public boolean getOpenThermocoupleDetection(BaseOrExpansionBoard baseOrExpansionBoard) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                baseOrExpansionBoard.VALUE,
                MeasurementComputingUniversalLibrary.BIDETECTOPENTC
        ) == 1;
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDetectOpenTC.htm">BoardConfig.SetDetectOpenTc()</a>
     */
    public void setOpenThermocoupleDetection(BaseOrExpansionBoard baseOrExpansionBoard, boolean enable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                baseOrExpansionBoard.VALUE,
                MeasurementComputingUniversalLibrary.BIDETECTOPENTC,
                (enable ? 1 : 0)
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BINUMTEMPCHANS -> BI NUM TEMP CHANS -> boardInfo number of temperature channels
     Readable? yes
     Writable? NO
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetNumTempChans.htm">BoardConfig.GetNumTempChans()</a>
     */
    public int getTemperatureChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMTEMPCHANS
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BICHANTCTYPE -> BI CHAN TC TYPE  -> boardInfo channel thermocouple type
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetChanTcType.htm">BoardConfig.GetChanTcType()</a>
     */
    public ThermocoupleSensorType getThermocoupleSensorType(int channel) throws JMCCULException {
        return ThermocoupleSensorType.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        channel,
                        MeasurementComputingUniversalLibrary.BICHANTCTYPE
                )
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetChanTcType.htm">BoardConfig.SetChanTcType()</a>
     */
    public void setThermocoupleSensorType(int channel, ThermocoupleSensorType type) throws JMCCULException {
        Configuration.setInt(
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
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetChanRtdType.htm">BoardConfig.GetChanRtdType()</a>
     */
    public RtdSensorType getRtdSensorType(int channel) throws JMCCULException {
        return RtdSensorType.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        channel,
                        MeasurementComputingUniversalLibrary.BICHANRTDTYPE
                )
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetChanRtdType.htm">BoardConfig.SetChanRtdType()</a>
     */
    public void setRtdSensorType(int channel, RtdSensorType rtdSensor) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BICHANRTDTYPE,
                rtdSensor.VALUE
        );
    }

}
