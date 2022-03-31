package jmccul.config;

import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.jna.MeasurementComputingUniversalLibrary;

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

    public void setNetConnectionCode() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BINETCONNECTCODE,
                0 //new value
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

    public void setNetIoTimeout() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BINETIOTIMEOUT,
                0 //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIHIDELOGINDLG -> BI HIDE LOGIN DLG -> boardInfo hide login dialog
     Readable? yes
     Writabale? yes

    this is not actually a netowrk specific one
     */
    public void setDeviceLogin() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIHIDELOGINDLG,
                0 //new value
        );
    }

}
