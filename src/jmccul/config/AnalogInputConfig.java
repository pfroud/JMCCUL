package jmccul.config;

import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.enums.A2DTimingMode;
import jmccul.enums.A2DTransferMode;
import jmccul.enums.AdcSettlingTime;
import jmccul.enums.AnalogInputChannelType;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class AnalogInputConfig {

    private final int BOARD_NUMBER;

    /*
    Analog input = analog to digital = "A2D" or "AD" or "ADC"
     */
    public AnalogInputConfig(DaqDevice device) {
        BOARD_NUMBER = device.BOARD_NUMBER;
    }

    public int getAnalogInputMode() throws JMCCULException {
        // you set this with cbAInputMode() I think
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADAIMODE
        );
    }

    public int getAnalogInputChannelMode() throws JMCCULException {
        // you set this with cbAChanInputMode() I think
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0,
                MeasurementComputingUniversalLibrary.BIADCHANAIMODE
        );
    }

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

    public AdcSettlingTime getAdcSettlingTime() throws JMCCULException {
        return AdcSettlingTime.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIADCSETTLETIME
                ));
    }

    public int getA2DDataRate(int channel) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIADDATARATE
        );
    }

    public int getA2DResolution() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADRES
        );
    }

    public A2DTransferMode getA2DDataTransferMode() throws JMCCULException {
        return A2DTransferMode.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignores
                        MeasurementComputingUniversalLibrary.BIADXFERMODE
                ));
    }

    public A2DTimingMode getA2DTimingMode() throws JMCCULException {
        return A2DTimingMode.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIADTIMINGMODE
                ));
    }

    public int getA2DTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIADTRIGCOUNT
        );
    }

    public int getA2DTriggerSource(int channel) throws JMCCULException {
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

    public int getA2DChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMADCHANS
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

    public void setAdcSettlingTime() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIADCSETTLETIME,
                0 //new value
        );
    }

    public void setA2DDataRate() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIADDATARATE,
                0 //new value
        );
    }

    public void setTimingMode() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIADTIMINGMODE,
                0 //new value
        );
    }

    public void setA2DTriggerCount() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIADTRIGCOUNT,
                0 //new value
        );
    }

    public void setA2DTriggerSource() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIADTRIGSRC,
                0 //new value
        );
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

}
