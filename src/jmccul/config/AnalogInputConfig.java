package jmccul.config;

import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.enums.AdcTimingMode;
import jmccul.enums.AdcTransferMode;
import jmccul.enums.AdcSettlingTime;
import jmccul.enums.AnalogInputChannelType;
import jmccul.jna.MeasurementComputingUniversalLibrary;

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
     BINUMADCHANS -> BI NUM AD CHANS -> boardInfo number of ADC channels
     Readable? yes
     Writabale? NO
     */
    public int getAdcChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMADCHANS
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

    public void setAdcDataRate() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIADDATARATE,
                0 //new value
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

    public void setDataTransferMode() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIADXFERMODE,
                0 //new value
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
     Writabale? yse
     */
    public AdcSettlingTime getAdcSettlingTime() throws JMCCULException {
        return AdcSettlingTime.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIADCSETTLETIME //
                ));
    }

    public void setAdcSettlingTime() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIADCSETTLETIME,
                0 //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIADTIMINGMODE -> BI AD TIMING MODE -> boardInfo ADC timing mode
     Readable? yes
     Writabale? yes

    TODO why is devNum ignored when getting but used when setting??
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
                0, //devNum
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

    public void setAdcTriggerCount() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIADTRIGCOUNT,
                0 //new value
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

    public void setAdcTriggerSource() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIADTRIGSRC,
                0 //new value
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

    public void setAnalogInputChannelType() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIADCHANTYPE,
                0 //new value
        );
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public int getAnalogInputChannelMode() throws JMCCULException {
        // you set this with cbAChanInputMode() I think
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0,
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
