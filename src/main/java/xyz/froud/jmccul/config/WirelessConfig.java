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
        BOARD_NUMBER = device.getBoardNumber();
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIRFCHANNEL -> BI RF channel
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetRFChannel.htm">BoardConfig.GetRFChannel()</a>
     */
    public int getWirelessRfChannel() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRFCHANNEL
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetRFChannel.htm">BoardConfig.SetRFChannel()</a>
     */
    public void setWirelessRfChannel(int channel) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRFCHANNEL,
                channel
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIRSS -> BI RF receivedSignalStrength
     Readable? yes
     Writable? yes but that is probably a mistake in the docs
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetRSS.htm">BoardConfig.GetRSS()</a>
     */
    public int getWirelessSignalStrength() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRSS
        );
    }


      /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIPANID  -> BI PAN ID -> boardInfo PersonalAreaNetwork ID
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetPanID.htm">BoardConfig.GetPanID()</a>
     */
    public int getPanId() throws JMCCULException {
        // Personal Area Network
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIPANID
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     *         https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetPanID.htm
     */
    public void setPanId(int panID) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIPANID,
                panID
        );
    }

}