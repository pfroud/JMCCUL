package xyz.froud.jmccul.config;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public class WirelessConfig {

    private final int BOARD_NUMBER;

    public WirelessConfig(DaqDevice device) {
        BOARD_NUMBER = device.BOARD_NUMBER;
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIRFCHANNEL -> BI RF channel
     Readable? yes
     Writabale? yes
     */
    public int getWirelessRfChannel() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRFCHANNEL
        );
    }

    public void setWirelessRfChannel(int channel) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRFCHANNEL,
                channel
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIRSS -> BI RF receivedSignalStrength
     Readable? yes
     Writabale? yes but that is probably a mistake in the docs
     */
    public int getWirelessSignalStrength() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRSS
        );
    }

    public void setWirelessSignalStrength(int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRSS,
                n
        );
    }

}
