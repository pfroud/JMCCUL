package jmccul;

import jmccul.analog.AnalogRange;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class BoardConfig {

    private final int BOARD_NUMBER;

    public BoardConfig(DaqDevice device) {
        BOARD_NUMBER = device.BOARD_NUMBER;
    }

    //<editor-fold defaultstate="collapsed" desc="cbGetConfig() which is wrapped in Configuration.getInt()">
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

    public int getBaseAddress() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIBASEADR
        );
    }

    public int getBoardType() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIBOARDTYPE
        );
    }

    public CalibrationTableType getCalibrationTableType() throws JMCCULException {
        return CalibrationTableType.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //DevNum is either ignored or specifies a base or expansion board.
                        MeasurementComputingUniversalLibrary.BICALTABLETYPE
                )
        );
    }

    public RtdSensorType getRtdSensorType(int channel) throws JMCCULException {
        return RtdSensorType.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        channel,
                        MeasurementComputingUniversalLibrary.BICHANRTDTYPE
                )
        );
    }

    public ThermocoupleSensorType getThermocoupleSensorType(int channel) throws JMCCULException {
        return ThermocoupleSensorType.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        channel,
                        MeasurementComputingUniversalLibrary.BICHANTCTYPE
                )
        );
    }

    public int getCounterDeviceCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BICINUMDEVS
        );
    }

    public int getClockFrequenceMegahertz(int channel) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BICLOCK
        );
    }

    public int getCoutnerTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BICTRTRIGCOUNT
        );
    }

    public boolean isDacForceSense(int channel) throws JMCCULException {
        /*
        The remote sensing feature compensates for the voltage
        drop error that occurs in applications where the analog
        outputs on a device are connected to its load through a
        long wire or cable type interconnect.

        The remote sensing feature utilizes the force and sense
        output terminals on a supported device. Refer to the
        device hardware manual for more information.
         */
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIDACFORCESENSE
        ) == 1;
    }

    public AnalogRange getDacRange(int channel) throws JMCCULException {
        return AnalogRange.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        channel,
                        MeasurementComputingUniversalLibrary.BIDACRANGE
                )
        );
    }

    public int getD2AResolution() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignores
                MeasurementComputingUniversalLibrary.BIDACRES
        );
    }

    public int getDacStartup(int channel) throws JMCCULException {
        /*
        Use the BIDACSTARTUP option to determine whether a board's
        DAC values before the last power down are stored.

        With ConfigItem set to BIDACSTARTUP, Configval returns 0 (zero)
        when the startup bit is DISabled. Current DAC settings are stored
        as startup values.

        ConfigVal returns 1 (one) when the startup bit is ENabled. The
        last DAC values are stored as startup values.

        Refer to the cbSetConfig() Notes section for information about how to store the current or last DAC values as start-up values.
         */
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIDACSTARTUP
        );
    }

    public int getDacTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDACTRIGCOUNT
        );
    }

    public DacUpdateMode getDacUpdateMode() throws JMCCULException {
        return DacUpdateMode.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIDACUPDATEMODE
                )
        );
        /*
        Use the BIDACUPDATEMODE option to check the update mode for a DAC board.

        With ConfigItem set to BIDACUPDATEMODE, ConfigVal returns 0 when the
        DAC update mode is immediate. Values written with cbAOut() are
        automatically output by the DAC channels.
        ConfigVal returns 1 when the DAC update mode is set to on command.
        Values written with cbAOut() are not output by the DAC channels
        until a cbSetConfig() call is made with its ConfigItem argument
        set to BIDACUPDATECMD.

        #define UPDATEIMMEDIATE  0
        #define UPDATEONCOMMAND  1
         */
    }

    public boolean getOpenThermocoupleDetectEnable() throws JMCCULException {
        ///////////////////// resume here //////////////////////
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //DevNum is either ignored or specifies a base or expansion board; refer to device-specific information.
                MeasurementComputingUniversalLibrary.BIDETECTOPENTC
        ) == 1;
    }

    public int getDigitalDeviceCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDINUMDEVS
        );
    }

    public boolean getACFilter(int bitNumber) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                bitNumber,
                MeasurementComputingUniversalLibrary.BIDISOFILTER
        ) == 1;
    }

    public int getDigitalInputTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDITRIGCOUNT
        );
    }

    public int getDmaChannel() throws JMCCULException {
        // direct memory access
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDMACHAN
        );
    }

    public int getDigitalOutTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDOTRIGCOUNT
        );
    }

    public int getDtBoardNumber() throws JMCCULException {
        // Data Translation, acquired by Measurement Computing in 2015
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0,//devNum is ignored
                MeasurementComputingUniversalLibrary.BIDTBOARD
        );
    }

    public ExternalClockType getExternalClockType() throws JMCCULException {
        return ExternalClockType.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devnum is ignored`
                MeasurementComputingUniversalLibrary.BIEXTCLKTYPE
        ));
    }

    public ExternalPacerClockEdge getInputPacerClockEdge() throws JMCCULException {
        return ExternalPacerClockEdge.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0,
                MeasurementComputingUniversalLibrary.BIEXTINPACEREDGE
        ));
    }

    public ExternalPacerClockEdge getOutputPacerClockEdge() throws JMCCULException {
        return ExternalPacerClockEdge.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0,
                MeasurementComputingUniversalLibrary.BIEXTOUTPACEREDGE
        ));
    }

    public boolean getInputPacerClockState() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINPUTPACEROUT
        ) == 1;
    }

    public InterruptClockEdge getInterruptEdge() throws JMCCULException {
        return InterruptClockEdge.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINTEDGE
        ));
    }

    public int getInterruptLevel() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINTLEVEL
        );
    }

    public int getNetworkConnectionCode() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINETCONNECTCODE
        );
    }

    public int getNetworkIoTimeoutMillisec() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINETIOTIMEOUT
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

    public int getD2AChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMDACHANS
        );
    }

    public int getIoPortCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMIOPORTS
        );
    }

    public int getTemperatureChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMTEMPCHANS
        );
    }

    public int getPanId() throws JMCCULException {
        // Personal Area Network
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIPANID
        );
    }

    public int getPatternTriggerPort() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIPATTERNTRIGPORT
        );
    }

    public int getRange() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRANGE
        );
    }

    public int getRfChannel() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRFCHANNEL
        );
    }

    public int getWirelessSignalStrengthDbm() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRSS
        );
    }

    public int getUserSpecifiedSerialNumber() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BISERIALNUM
        );
    }

    public int getSyncMode() throws JMCCULException {
        //TODO find lines in cbw.h for enum
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BISYNCMODE
        );
    }

    public int getTemperatureScansToAverage() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPAVG
        );
    }

    public TemperatureScale getTemperatureScale() throws JMCCULException {
        return TemperatureScale.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BITEMPSCALE
        ));
    }

    public int getTemperatureRejectionFrequency() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //DevNum is either ignored or specifies a base or expansion board.
                MeasurementComputingUniversalLibrary.BITEMPREJFREQ
        );
    }

    public boolean getTerminalCountOutputStatus(int bitNumber) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                bitNumber,
                MeasurementComputingUniversalLibrary.BITERMCOUNTSTATBIT
        ) == 1;
    }

    public boolean getWaitStateJumper() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIWAITSTATE
        ) == 1;
    }

    public boolean getExpansionBoardSupported() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIUSESEXPS
        ) == 1;
    }

    public int getUserSpecifiedString() throws JMCCULException {
        //todo this should probably use the getString instead of getInt
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIUSERDEVIDNUM
        );
    }
    //</editor-fold>

}
