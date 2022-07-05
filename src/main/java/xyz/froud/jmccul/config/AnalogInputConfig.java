package xyz.froud.jmccul.config;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.enums.AdcSettlingTime;
import xyz.froud.jmccul.enums.AdcTimingMode;
import xyz.froud.jmccul.enums.AdcTransferMode;
import xyz.froud.jmccul.enums.AnalogInputChannelType;
import xyz.froud.jmccul.enums.AnalogRange;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * Analog input = analog to digital = "A2D" or "AD" or "ADC"
 *
 * @author Peter Froud
 */
public class AnalogInputConfig {

    private final int BOARD_NUMBER;

    public AnalogInputConfig(DaqDevice device) {
        BOARD_NUMBER = device.getBoardNumber();
    }


    /* //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIRANGE -> BI RANGE -> boardInfo range
     Readable? yes
     Writable? yes

    TODO what is the difference between BI DAC RANGE and BI RANGE?
     */

    /**
     * Selected voltage range.
     * <p>
     * For switch selectable gains only. If the selected A/D board does not have a programmable gain feature, this
     * argument returns the range as defined by the install settings.
     *
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetRange.htm">BoardConfig.GetRange()</a>
     */
    public AnalogRange getRange() throws JMCCULException {
        return AnalogRange.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIRANGE
                )
        );
    }

    /**
     * Selected voltage range.
     * <p>
     * Refer to board-specific information for the ranges supported by a device.
     *
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetRange.htm">BoardConfig.SetRange()</a>
     */
    public void setRange(AnalogRange range) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRANGE,
                range.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BINUMADCHANS -> BI NUM AD CHANS -> boardInfo number of ADC channels
     Readable? yes
     Writable? yes??
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetNumAdChans.htm">BoardConfig.GetNumAdChans()</a>
     */
    public int getAdcChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMADCHANS
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetNumAdChans.htm">BoardConfig.SetNumAdChans()</a>
     */
    public void setAdcChannelCount(int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMADCHANS,
                n
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADDATARATE -> BI AD DATA RATE -> boardInfo ADC data rate
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdDataRate.htm">BoardConfig.GetAdDataRate()</a>
     */
    public int getAdcDataRate(int channel) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADDATARATE
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAdDataRate.htm">BoardConfig.SetAdDataRate()</a>
     */
    public void setAdcDataRate(int channel, int dataRate) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADDATARATE,
                dataRate
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADXFERMODE -> BI AD XFER MODE -> boardInfo ADC transfer mode
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdXferMode.htm">BoardConfig.GetAdXferMode()</a>
     */
    public AdcTransferMode getAdcDataTransferMode() throws JMCCULException {
        return AdcTransferMode.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIADXFERMODE
                ));
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAdXferMode.htm">BoardConfig.SetAdXferMode()</a>
     */
    public void setDataTransferMode(AdcTransferMode transferMode) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADXFERMODE,
                transferMode.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADRES -> BI AD RES -> boardInfo ADC resolution
     Readable? yes
     Writable? no
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdResolution.htm">BoardConfig.GetAdResolution()</a>
     */
    public int getAdcResolution() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADRES
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADCSETTLETIME -> BI ADC SETTLE TIME -> boardInfo ADC settle  time
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdSettlingTime.htm">BoardConfig.GetAdSettlingTime()</a>
     */
    public AdcSettlingTime getAdcSettlingTime() throws JMCCULException {
        return AdcSettlingTime.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIADCSETTLETIME
                ));
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAdSettlingTime.htm">BoardConfig.SetAdSettlingTime()</a>
     */
    public void setAdcSettlingTime(AdcSettlingTime settleTime) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADCSETTLETIME,
                settleTime.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADTIMINGMODE -> BI AD TIMING MODE -> boardInfo ADC timing mode
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdTimingMode.htm">BoardConfig.GetAdTimingMode()</a>
     */
    public AdcTimingMode getAdcTimingMode() throws JMCCULException {
        return AdcTimingMode.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIADTIMINGMODE
                ));
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAdTimingMode.htm">BoardConfig.SetAdTimingMode()</a>
     */
    public void setAdcTimingMode(AdcTimingMode newTimingMode) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADTIMINGMODE,
                newTimingMode.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADTRIGCOUNT -> BI AD TRIG COUNT -> boardInfo ADC trigger count
     Readable? yes
     Writable? yes
     */

    /**
     * Number of analog input samples to acquire during each trigger event when ScanOptions.RetrigMode is enabled.
     * <p>
     * For use with the cbAInScan()/AInScan() RETRIGMODE option to set up repetitive trigger events.
     *
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdRetrigCount.htm">BoardConfig.GetAdRetrigCount()</a>
     */
    public int getAdcTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADTRIGCOUNT
        );
    }

    /**
     * Number of analog input samples to acquire during each trigger event when ScanOptions.RetrigMode is enabled.
     * <p>
     * For use with the cbAInScan()/AInScan() RETRIGMODE option to set up repetitive trigger events.
     *
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAdRetrigCount.htm">BoardConfig.SetAdRetrigCount()</a>
     */
    public void setAdcTriggerCount(int triggerCount) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADTRIGCOUNT,
                triggerCount
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADTRIGSRC -> BI AD TRIG SRC -> boardInfo ADC trigger count
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAdTrigSource.htm">BoardConfig.GetAdTrigSource()</a>
     */
    public int getAdcTriggerSourceChannel(int channel) throws JMCCULException {
        /*
        Not sure what this is, it just says:
        Use this setting in conjunction with one of these ConfigVal settings:
        0
        1
        2
        3
        According to the dot net docs, the number is the channel to use as the trigger source.
         */
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADTRIGSRC
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAdTrigSource.htm">BoardConfig.SetAdTrigSource()</a>
     */
    public void setAdcTriggerSourceChannel(int channel, int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADTRIGSRC,
                n
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIADCHANTYPE -> BI AD CHAN TYPE -> boardInfo ADC channel type
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAIChanType.htm">BoardConfig.GetAIChanType()</a>
     */
    public AnalogInputChannelType getAnalogInputChannelType(int channel) throws JMCCULException {
        return AnalogInputChannelType.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        channel,
                        MeasurementComputingUniversalLibrary.BIADCHANTYPE
                )
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetAIChanType.htm">BoardConfig.SetAIChanType()</a>
     */
    public void setAnalogInputChannelType(int channel, AnalogInputChannelType channelType) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADCHANTYPE,
                channelType.VALUE
        );
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetChanAIMode.htm">BoardConfig.GetChanAIMode()</a>
     */
    public int getAnalogInputChannelMode(int channel) throws JMCCULException {
        // you set this with cbAChanInputMode(). confirmed in the dot ent docs for GetChanAIMode().
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADCHANAIMODE //BI AD CHAN AI MODE
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetAIMode.htm">BoardConfig.GetAIMode()</a>
     */
    public int getAnalogInputMode() throws JMCCULException {
        // you set this with cbAInputMode(). confirmed in the dot net docs for GetAIMode().
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADAIMODE //BI AD AI MODE
        );
    }

}
