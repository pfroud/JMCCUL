package xyz.froud.jmccul.config;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public class NetworkConfig {

    private final int BOARD_NUMBER;

    public NetworkConfig(DaqDevice device) {
        BOARD_NUMBER = device.getBoardNumber();
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BINETCONNECTCODE -> BI NET CONNECT CODE -> boardInfo network connection code
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetNetConnectionCode.htm">BoardConfig.GetNetConnectionCode()</a>
     */
    public int getNetworkConnectionCode() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINETCONNECTCODE
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetNetConnectionCode.htm">BoardConfig.SetNetConnectionCode()</a>
     */
    public void setNetworkConnectionCode(int code) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINETCONNECTCODE,
                code
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BINETIOTIMEOUT -> BI NET IO TIMEOUT -> boardInfo network I/O timeout
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public int getNetworkIoTimeoutMillisec() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINETIOTIMEOUT
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     */
    public void setNetworkIoTimeoutMillisec(int timeoutMillisec) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINETIOTIMEOUT,
                timeoutMillisec
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDEVMACADDR -> BI DEV MAC ADDR -> boardInfo device MAC address
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDeviceMacAddress.htm">BoardConfig.GetDeviceMacAddress()</a>
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
