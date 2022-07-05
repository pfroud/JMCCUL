package xyz.froud.jmccul.config;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.enums.AnalogRange;
import xyz.froud.jmccul.enums.DacUpdateMode;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * Analog output = digital to analog = "DAC"
 *
 * @author Peter Froud
 */
public class AnalogOutputConfig {

    private final int BOARD_NUMBER;

    public AnalogOutputConfig(DaqDevice device) {
        BOARD_NUMBER = device.getBoardNumber();
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACFORCESENSE -> BI DAC FORCE SENSE -> boardInfo
     Readable? yes
     Writable? yes
     */

    /**
     * Remote sensing state of an analog output channel.
     * <p>
     * The remote sensing feature compensates for the voltage drop error that occurs in applications where the analog
     * outputs on a device are connected to its load through a long wire or cable type interconnect.
     * <p>
     * The remote sensing feature utilizes the force and sense output terminals on a supported device. Refer to the
     * device hardware manual for more information.
     *
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACForceSense.htm">BoardConfig.GetDACForceSense()</a>
     */
    public boolean getDacForceSense(int channel) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIDACFORCESENSE
        ) == 1;
    }

    /**
     * Enables or disables remote sensing of an analog output channel.
     * <p>
     * The remote sensing feature compensates for the voltage drop error that occurs in applications where the analog
     * outputs on a device are connected to its load through a long wire or cable type interconnect.
     * <p>
     * The remote sensing feature utilizes the force and sense output terminals on a supported device. Refer to the
     * device hardware manual for more information.
     *
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDACForceSense.htm">BoardConfig.SetDACForceSense()</a>
     */
    public void setDacForceSense(int channel, boolean sense) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIDACFORCESENSE,
                (sense ? 1 : 0)
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACRANGE  -> BI DAC RANGE -> boardInfo DAC range
     Readable? yes
     Writable? yes

    TODO what is the difference between BI DAC RANGE and BI RANGE?
    TODO why is channel used when setting but ignored when getting?
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACRange.htm">BoardConfig.GetDACRange()</a>
     */
    public AnalogRange getDacRange() throws JMCCULException {
        return AnalogRange.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, // devNum is ignored - probably wrong though
                        MeasurementComputingUniversalLibrary.BIDACRANGE
                )
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDACRange.htm">BoardConfig.SetDACRange()</a>
     */
    public void setDacRange(int channel, AnalogRange range) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIDACRANGE,
                range.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACRES -> BI DAC RES -> boardInfo DAC resolution
     Readable? yes
     Writable? no
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDacResolution.htm">BoardConfig.GetDacResolution()</a>
     */
    public int getDacResolution() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDACRES
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACSTARTUP -> BI DAC STARTUP -> boardInfo DAC startup
     Readable?
     Writable?
     */

    /**
     * Whether values written to the DAC get saved to non-volatile memory on the DAQ board.
     *
     * @see #setDacStartup
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACStartup.htm">BoardConfig.GetDACStartup()</a>
     */
    public boolean getDacStartup(int channel) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel, //devNum is the channel when getting
                MeasurementComputingUniversalLibrary.BIDACSTARTUP
        ) == 1;
    }

    /**
     * Set whether DAC values get saved in non-volatile memory on the DAQ board.
     * <p>
     * Each time a DAC board is powered up, the board reads DAC values stored in onboard non-volatile memory and sets
     * the DAC output to those values.
     * <p>
     * To store the current DAC values as start-up values: first, call {@code setDACStartup(true)}. Then, each time you
     * call AOut() or AOutScan(), the value written for each channel is stored in non-volatile memory on the DAQ board.
     * The last value written to a particular channel when {@code getDacStartup() == true} is the value that channel
     * will be set to at power up. Call {@code setDACStartup(false)} stop storing values in non-volatile memory on the DAQ board.
     *
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACStartup.htm">BoardConfig.GetDACStartup()</a>
     */
    public void setDacStartup(boolean enable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored when setting
                MeasurementComputingUniversalLibrary.BIDACSTARTUP,
                (enable ? 1 : 0)
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACTRIGCOUNT -> BI DAC TRIG COUNT -> boardInfo DAC trigger count
     Readable? yes
     Writable? yes
     */

    /**
     * Gets the number of samples to generate during each trigger event when ScanOptions.RetrigMode is enabled.
     *
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACRetrigCount.htm">BoardConfig.GetDACRetrigCount()</a>
     */
    public int getDacTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDACTRIGCOUNT
        );
    }

    /**
     * Sets the number of samples to generate during each trigger event when ScanOptions.RetrigMode is enabled.
     *
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDACRetrigCount.htm">BoardConfig.SetDACRetrigCount()</a>
     */
    public void setDacTriggerCount(int triggerCount) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDACTRIGCOUNT,
                triggerCount
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACUPDATEMODE -> BI DAC UPDATE MODE  -> boardInfo DAC update mode
     Readable? yes
     Writable? yes
     */

    /**
     * @see #setDacUpdateMode
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACUpdateMode.htm">BoardConfig.GetDACUpdateMode()</a>
     */
    public DacUpdateMode getDacUpdateMode() throws JMCCULException {
        return DacUpdateMode.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIDACUPDATEMODE
                )
        );
    }

    /**
     * <ul>
     *      <li>If the value is {@link DacUpdateMode#IMMEDIATE}, then values written with cbAOut() or cbAOutScan() are automatically output by the DAC channels.</li>
     *      <li>If the value is {@link DacUpdateMode#ON_COMMAND}, then values written with cbAOut() or cbAOutScan() are not output by the DAC channels until you call {@link #updateAnalogOutput()}.</li>
     * </ul>
     *
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDACUpdateMode.htm">BoardConfig.GetDACUpdateMode()</a>
     */
    public void setDacUpdateMode(DacUpdateMode updateMode) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDACUPDATEMODE,
                updateMode.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BINUMDACHANS -> BI NUM DA CHANS -> boardInfo number of DA channels
     Readable? yes
     Writable? no
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetNumDaChans.htm">BoardConfig.GetNumDaChans()</a>
     */
    public int getDacChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMDACHANS
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDACUPDATECMD -> BI DAC UPDATE CMD -> boardInfo DAC update command

    TODO move this out of AnalogOutputConfig.java
     */

    /**
     * Updates the voltage values on analog output channels. This method is only useful if you first call {@link #setDacUpdateMode} with {@link DacUpdateMode#ON_COMMAND}.
     *
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/DACUpdate.htm">BoardConfig.DACUpdate()</a>
     */
    public void updateAnalogOutput() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is not used
                MeasurementComputingUniversalLibrary.BIDACUPDATECMD,
                0 //configVal is not used
        );
    }

    /*
    // BIDACSETTLETIME is not in my JNA thing
    public void setDacSettlingTime() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDACSETTLETIME,
                0 //new value
        );
    }
     */
}
