package jmccul.config;

import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.enums.RtdSensorType;
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

    public void setTemperatureAverageCount() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BITEMPAVG,
                0 //new value
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

    public void setTemperatureScale() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BITEMPSCALE,
                0 //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BITEMPREJFREQ -> BI TEMP REJ FREQ -> boardInfo temperature rejection frequency
     Readable? yes
     Writabale? yes
     */
    public int getTemperatureRejectionFrequency() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //DevNum is either ignored or specifies a base or expansion board.
                MeasurementComputingUniversalLibrary.BITEMPREJFREQ
        );
    }

    public void setRejectionFrequency() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BITEMPREJFREQ,
                0 //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDETECTOPENTC  -> BI DETECT OPEN TC -> boardInfo detect open thermocouople
     Readable? yes
     Writabale? yes
     */
    public boolean getOpenThermocoupleDetectEnable() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //DevNum is either ignored or specifies a base or expansion board; refer to device-specific information.
                MeasurementComputingUniversalLibrary.BIDETECTOPENTC
        ) == 1;
    }

    public void setOpenThermocoupleDetection() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDETECTOPENTC,
                0 //new value
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

    public void setThermocuopleSensorType() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BICHANTCTYPE,
                0 //new value
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

    public void setRtdSensorType(RtdSensorType rtdSensor) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BICHANRTDTYPE,
                rtdSensor.VALUE
        );
    }

}
