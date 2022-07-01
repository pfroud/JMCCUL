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
        BOARD_NUMBER = device.BOARD_NUMBER;
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIRANGE -> BI RANGE -> boardInfo range
     Readable? yes
     Writabale? yes

    TODO what is the difference betweeb BI DAC RANGE and BI RANGE?
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

    public void setRange(AnalogRange range) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRANGE,
                range.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BINUMADCHANS -> BI NUM AD CHANS -> boardInfo number of ADC channels
     Readable? yes
     Writabale? yes??
     */
    public int getAdcChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMADCHANS
        );
    }

    public void setAdcChannelCount(int n) throws JMCCULException {
        // this probably does not work
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMADCHANS,
                n
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIADDATARATE -> BI AD DATA RATE -> boardInfo ADC data rate
     Readable? yes
     Writabale? yes
     */
    public int getAdcDataRate(int channel) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADDATARATE
        );
    }

    public void setAdcDataRate(int channel, int dataRate) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADDATARATE,
                dataRate
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIADXFERMODE -> BI AD XFER MODE -> boardInfo ADC transfer mode
     Readable? yes
     Writabale? yes
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

    public void setDataTransferMode(AdcTransferMode transferMode) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADXFERMODE,
                transferMode.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIADRES -> BI AD RES -> boardInfo ADC resolution
     Readable? yes
     Writabale? no
     */
    public int getAdcResolution() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADRES
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIADCSETTLETIME -> BI ADC SETTLE TIME -> boardInfo ADC settle  time
     Readable? yes
     Writabale? yes
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

    public void setAdcSettlingTime(AdcSettlingTime settleTime) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADCSETTLETIME,
                settleTime.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIADTIMINGMODE -> BI AD TIMING MODE -> boardInfo ADC timing mode
     Readable? yes
     Writabale? yes
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

    public void setAdcTimingMode(AdcTimingMode newTimingMode) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADTIMINGMODE,
                newTimingMode.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIADTRIGCOUNT -> BI AD TRIG COUNT -> boardInfo ADC triger count
     Readable? yes
     Writabale? yes
     */
    public int getAdcTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADTRIGCOUNT
        );
    }

    public void setAdcTriggerCount(int triggerCount) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADTRIGCOUNT,
                triggerCount
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIADTRIGSRC -> BI AD TRIG SRC -> boardInfo ADC triger count
     Readable? yes
     Writabale? yes
     */
    public int getAdcTriggerSource(int channel) throws JMCCULException {
        /*
        Not sure what this is, it just says:
        Use this setting in conjunction with one of these ConfigVal settings:
        0
        1
        2
        3
         */
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADTRIGSRC
        );
    }

    public void setAdcTriggerSource(int channel, int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADTRIGSRC,
                n
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIADCHANTYPE -> BI AD CHAN TYPE -> boardInfo ADC channel type
     Readable? yes
     Writabale? yes
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

    public int getAnalogInputChannelMode(int channel) throws JMCCULException {
        // you set this with cbAChanInputMode() I think
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADCHANAIMODE //BI AD CHAN AI MODE
        );
    }

    public int getAnalogInputMode() throws JMCCULException {
        // you set this with cbAInputMode() I think
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADAIMODE //BI AD AI MODE
        );
    }

}
