package jmccul.config;

import jmccul.DaqDevice;
import jmccul.JMCCULException;
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

    public int getTemperatureScansToAverage() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPAVG
        );
    }

    public TemperatureScale getTemperatureScale() throws JMCCULException {
        return TemperatureScale.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPSCALE
        ));
    }

    public int getTemperatureRejectionFrequency() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //DevNum is either ignored or specifies a base or expansion board.
                MeasurementComputingUniversalLibrary.BITEMPREJFREQ
        );
    }

    public boolean getOpenThermocoupleDetectEnable() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //DevNum is either ignored or specifies a base or expansion board; refer to device-specific information.
                MeasurementComputingUniversalLibrary.BIDETECTOPENTC
        ) == 1;
    }

    public int getTemperatureChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMTEMPCHANS
        );
    }

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

    public void setOpenThermocoupleDetection() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDETECTOPENTC,
                0 //new value
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

    public void setTemperatureScale() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BITEMPSCALE,
                0 //new value
        );
    }

}
