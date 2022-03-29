package jmccul.config;

import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class DigitalOutputConfig {

    private final int BOARD_NUMBER;

    public DigitalOutputConfig(DaqDevice device) {
        BOARD_NUMBER = device.BOARD_NUMBER;
    }

    public int getDigitalOutTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDOTRIGCOUNT
        );
    }

    public void setDigitalOutputTriggerCount() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDOTRIGCOUNT,
                0 //new value
        );
    }

}
