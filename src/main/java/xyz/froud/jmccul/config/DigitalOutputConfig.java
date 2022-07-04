package xyz.froud.jmccul.config;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public class DigitalOutputConfig {

    private final int BOARD_NUMBER;

    public DigitalOutputConfig(DaqDevice device) {
        BOARD_NUMBER = device.getBoardNumber();
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDOTRIGCOUNT -> BI DO TRIG COUNT -> boardInfo digitalOutput trigger count
     Readable? yes
     Writable? yes
     */
    public int getDigitalOutputTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDOTRIGCOUNT
        );
    }

    public void setDigitalOutputTriggerCount(int trigCount) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDOTRIGCOUNT,
                trigCount
        );
    }

}
