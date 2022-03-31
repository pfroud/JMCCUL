package jmccul.config;

import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.enums.CalibrationTableType;
import jmccul.enums.InterruptClockEdge;
import jmccul.enums.ExternalPacerClockEdge;
import jmccul.enums.ExternalClockType;
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

    //<editor-fold defaultstate="collapsed" desc="get config int">
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

    public int getClockFrequenceMegahertz(int channel) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BICLOCK
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

    public int getIoPortCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMIOPORTS
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
    ////////////////////////// resume finishing the abstraction here //////////////
    public void setBaseAddress(int baseAddress) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIBASEADR,
                baseAddress
        );
    }

    public void setCalibrationTable(CalibrationTableType calTable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BICALTABLETYPE,
                calTable.VALUE
        );
    }

    public void setCalPinVoltage(int calPinVoltage) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BICALOUTPUT,
                calPinVoltage
        );
    }

    public void setClockFrequencyMegahertz() throws JMCCULException {
        // todo only supports 1, 4,6 or 10
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BICLOCK,
                0 //new value
        );
    }

    public void setDmaChannel(int dmaChannel) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDMACHAN,
                dmaChannel
        );
    }

    public void setExternalClockType(ExternalClockType externalClockType) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIEXTCLKTYPE,
                externalClockType.VALUE
        );
    }

    public void setInputPacerClockEdge(ExternalPacerClockEdge externalPacerClockEdge) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIEXTINPACEREDGE,
                externalPacerClockEdge.VALUE
        );
    }

    public void setOutputPacerClockEdge(ExternalPacerClockEdge externalPacerClockEdge) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIEXTOUTPACEREDGE,
                0 //new value
        );
    }

    public void setInputPacerClockEnable() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIINPUTPACEROUT,
                0 //new value
        );
    }

    public void setInterruptEdge() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIINTEDGE,
                0 //new value
        );
    }

    public void setInterruptLevel() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIINTLEVEL,
                0 //new value
        );
    }

    public void setA2DChannelCount() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BINUMADCHANS,
                0 //new value
        );
    }

    public void setOutputPacerEnable() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIOUTPUTPACEROUT,
                0 //new value
        );
    }

    public void setPanId() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIPANID,
                0 //new value
        );
    }

    public void setPatternTriggerPort() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIPATTERNTRIGPORT,
                0 //new value
        );
    }

    public void setA2DRange() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIRANGE,
                0 //new value
        );
    }

    public void setRfChannel() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIRFCHANNEL,
                0 //new value
        );
    }

    public void setUserConfigurableIdentifier() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BISERIALNUM,
                0 //new value
        );
    }

    public void setSyncMode() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BISYNCMODE,
                0 //new value
        );
    }

    public void setTerminalCountOutputStatus() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BITERMCOUNTSTATBIT,
                0 //new value
        );
    }

    public void setWaitStateJumper() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIWAITSTATE,
                0 //new value
        );
    }

}
