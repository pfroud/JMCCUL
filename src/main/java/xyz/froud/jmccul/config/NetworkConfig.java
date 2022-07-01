package xyz.froud.jmccul.config;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class NetworkConfig {

    private final int BOARD_NUMBER;

    public NetworkConfig(DaqDevice device) {
        BOARD_NUMBER = device.BOARD_NUMBER;
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BINETCONNECTCODE -> BI NET CONNECT CODE -> boardInfo network connection code
     Readable? yes
     Writabale? yes
     */
    public int getNetworkConnectionCode() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINETCONNECTCODE
        );
    }

    public void setNetworkConnectionCode(int code) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINETCONNECTCODE,
                code
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BINETIOTIMEOUT -> BI NET IO TIMEOUT -> boardInfo network I/O timeout
     Readable? yes
     Writabale? yes
     */
    public int getNetworkIoTimeoutMillisec() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINETIOTIMEOUT
        );
    }

    public void setNetworkIoTimeoutMillisec(int timeoutMillisec) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINETIOTIMEOUT,
                timeoutMillisec
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDEVMACADDR -> BI DEV MAC ADDR -> boardInfo device MAC address
     Readable? yes
     Writabale? yes
     */
    public String getMacAddress() throws JMCCULException {
        return Configuration.getString(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDEVMACADDR,
                1024
        );
    }

}
