package jmccul.config;

import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.enums.BaseOrExpansionBoard;
import jmccul.enums.RtdSensorType;
import jmccul.enums.TemperatureRejection;
import jmccul.enums.TemperatureScale;
import jmccul.enums.ThermocoupleSensorType;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class TemperatureConfig {

    private final int BOARD_NUMBER;

    public TemperatureConfig(DaqDevice device) {
        BOARD_NUMBER = device.BOARD_NUMBER;
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BITEMPAVG -> BI TEMP AVG -> boardInfo temperature average
     Readable? yes
     Writabale? yes
     */
    public int getTemperatureScansToAverage() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPAVG
        );
    }

    public void setTemperatureAverageCount(int count) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //it does not say what devNum does
                MeasurementComputingUniversalLibrary.BITEMPAVG,
                count
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BITEMPSCALE -> BI TEMP SCALE -> boardInfo temperature scale
     Readable? yes
     Writabale? yes
     */
    public TemperatureScale getTemperatureScale() throws JMCCULException {
        return TemperatureScale.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPSCALE
        ));
    }

    public void setTemperatureScale(TemperatureScale temperatureScale) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPSCALE,
                temperatureScale.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BITEMPREJFREQ -> BI TEMP REJ FREQ -> boardInfo temperature rejection frequency
     Readable? yes
     Writabale? yes
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

    public void setRejectionFrequency(BaseOrExpansionBoard baseOrExpansionBoard, TemperatureRejection rejection) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                baseOrExpansionBoard.VALUE,
                MeasurementComputingUniversalLibrary.BITEMPREJFREQ,
                rejection.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDETECTOPENTC  -> BI DETECT OPEN TC -> boardInfo detect open thermocouople
     Readable? yes
     Writabale? yes
     */
    public boolean getOpenThermocoupleDetection(BaseOrExpansionBoard baseOrExpansionBoard) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                baseOrExpansionBoard.VALUE,
                MeasurementComputingUniversalLibrary.BIDETECTOPENTC
        ) == 1;
    }

    public void setOpenThermocoupleDetection(BaseOrExpansionBoard baseOrExpansionBoard, boolean enable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                baseOrExpansionBoard.VALUE,
                MeasurementComputingUniversalLibrary.BIDETECTOPENTC,
                (enable ? 1 : 0)
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BINUMTEMPCHANS -> BI NUM TEMP CHANS -> boardInfo number of temperature channels
     Readable? yes
     Writabale? NO
     */
    public int getTemperatureChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMTEMPCHANS
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BICHANTCTYPE -> BI CHAN TC TYPE  -> boardInfo channel thermocouple type
     Readable? yes
     Writabale? yes
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

    public void setThermocoupleSensorType(int channel, ThermocoupleSensorType type) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BICHANTCTYPE,
                type.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BICHANRTDTYPE -> BI CHAN RTD TYPE  -> boardInfo channel resistanceTemperatureDetector type
     Readable? yes
     Writabale? yes
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