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

    /* /////////////////////////////////////////////////////////////////////////////////
     BIBASEADR -> BI BASE ADR -> boardInfo base address
     Readable? yes
     Writabale? yes
     */
    public int getBaseAddress() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIBASEADR
        );
    }

    public void setBaseAddress(int baseAddress) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIBASEADR,
                baseAddress
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIBOARDTYPE -> BI BOARD TYPE -> boardInfo board type
     Readable? yes
     Writabale? NO
     */
    public int getBoardType() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIBOARDTYPE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BICALTABLETYPE -> BI CAL TABLE TYPE -> boardInfo calibration table type
     Readable? yes
     Writabale? yes
     */
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

    public void setCalibrationTable(CalibrationTableType calTable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BICALTABLETYPE,
                calTable.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BICLOCK -> BI CLOCK -> boardInfo clock
     Readable? yes
     Writabale? yes
     */
    public int getClockFrequenceMegahertz(int channel) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BICLOCK
        );
    }

    public void setClockFrequencyMegahertz(int channel, int clockFrequency) throws JMCCULException {
        // todo only supports 1, 4,6 or 10
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BICLOCK,
                clockFrequency
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
      BIDMACHAN -> BI DMA CHAN -> boardInfo directMemoryAccess channel
     Readable? yes
     Writabale? yes
     */
    public int getDmaChannel() throws JMCCULException {
        // direct memory access
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDMACHAN
        );
    }

    public void setDmaChannel(int dmaChannel) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDMACHAN,
                dmaChannel
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDTBOARD -> BI DT BOARD -> boardInfo DataTranslation board
     Readable? yes
     Writabale? NO
     */
    public int getDtBoardNumber() throws JMCCULException {
        // Data Translation, acquired by Measurement Computing in 2015
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0,//devNum is ignored
                MeasurementComputingUniversalLibrary.BIDTBOARD
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIEXTCLKTYPE -> BI EXT CLK TYPE -> boardInfo external clock type
     Readable? yes
     Writabale? yes
     */
    public ExternalClockType getExternalClockType() throws JMCCULException {
        return ExternalClockType.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devnum is ignored`
                MeasurementComputingUniversalLibrary.BIEXTCLKTYPE
        ));
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

    /* /////////////////////////////////////////////////////////////////////////////////
     BIEXTINPACEREDGE  -> BI EXT IN PACER EDGE -> boardInfo external input pacer edge
     Readable? yes
     Writabale? yes
     */
    public ExternalPacerClockEdge getInputPacerClockEdge() throws JMCCULException {
        return ExternalPacerClockEdge.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0,
                MeasurementComputingUniversalLibrary.BIEXTINPACEREDGE
        ));
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

    /* /////////////////////////////////////////////////////////////////////////////////
     BIEXTOUTPACEREDGE -> BI EXT OUT PACER EDGE -> boardInfo external output pacer edge
     Readable? yes
     Writabale? yes
     */
    public ExternalPacerClockEdge getOutputPacerClockEdge() throws JMCCULException {
        return ExternalPacerClockEdge.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0,
                MeasurementComputingUniversalLibrary.BIEXTOUTPACEREDGE
        ));
    }

    public void setOutputPacerClockEdge(ExternalPacerClockEdge externalPacerClockEdge) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIEXTOUTPACEREDGE,
                externalPacerClockEdge.VALUE //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIOUTPUTPACEROUT -> BI OUTPUT PACER OUT -> boardInfo output pacer output
     Readable? no but that is probably a typo
     Writabale? yes
     */
    public void setOutputPacerEnable(boolean enable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIOUTPUTPACEROUT,
                (enable ? 1 : 0) //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIINPUTPACEROUT -> BI INPUT PACER OUT -> boardInfo input pacer output
     Readable? yes
     Writabale? yes
     */
    public boolean getInputPacerClockState() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINPUTPACEROUT
        ) == 1;
    }

    public void setInputPacerClockEnable(boolean enable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIINPUTPACEROUT,
                (enable ? 1 : 0) //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIINTEDGE -> BI INT EDGE -> boardInfo interrupt edge
     Readable? yes
     Writabale? yes
     */
    public InterruptClockEdge getInterruptEdge() throws JMCCULException {
        return InterruptClockEdge.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINTEDGE
        ));
    }

    public void setInterruptEdge(InterruptClockEdge edge) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIINTEDGE,
                edge.VALUE //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIINTLEVEL  -> BI INT LEVEL -> boardInfo interrupt level
     Readable? yes
     Writabale? yes
     */
    public int getInterruptLevel() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINTLEVEL
        );
    }

    public void setInterruptLevel(int level) throws JMCCULException {
        // 0 for none, or 1 to 15.
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIINTLEVEL,
                level //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BINUMIOPORTS -> BI NUM IO PORTS -> boardInfo number of I/O ports
     Readable? yes
     Writabale? NO
     */
    public int getIoPortCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMIOPORTS
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIPANID  -> BI PAN ID -> boardInfo PersonalAreaNetwork ID
     Readable? yes
     Writabale? yes
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

    public void setPanId(int panID) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIPANID,
                panID //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIPATTERNTRIGPORT -> BI PATTERN TRIG PORT -> boardInfo pattern trigger port
     Readable? yes
     Writabale? yes
     */
    public int getPatternTriggerPort() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIPATTERNTRIGPORT
        );
    }

    public void setPatternTriggerPort(int port) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIPATTERNTRIGPORT,
                port //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     setAdc  -> BI RANGE -> boardInfo range
     Readable? yes
     Writabale? yes

    TODO what is the difference betweeb BI DAC RANGE and BI RANGE?
     */
    public int getRange() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIRANGE
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

    /* /////////////////////////////////////////////////////////////////////////////////
     BISERIALNUM -> BI SERIAL NUM -> boardInfo serial number
     Readable?
     Writabale?
     */
    public int getUserSpecifiedSerialNumber() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BISERIALNUM
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BISYNCMODE  -> BI SYNC MODE -> boardInfo sync mode
     Readable? yes
     Writabale? yes
     */
    public int getSyncMode() throws JMCCULException {
        //TODO find lines in cbw.h for enum
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BISYNCMODE
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

    /* /////////////////////////////////////////////////////////////////////////////////
     setAdc -> BI TERM COUNT STAT BIT -> boardInfo terminal count status bit
     Readable? yes
     Writabale? yes
     */
    public boolean getTerminalCountOutputStatus(int bitNumber) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                bitNumber,
                MeasurementComputingUniversalLibrary.BITERMCOUNTSTATBIT
        ) == 1;
    }

    public void setTerminalCountOutputStatus(boolean status) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BITERMCOUNTSTATBIT,
                (status ? 1 : 0) //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIWAITSTATE -> BI WAIT STATE -> boardInfo wait state
     Readable? yes
     Writabale? yes
     */
    public boolean getWaitStateJumper() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIWAITSTATE
        ) == 1;
    }

    public void setWaitStateJumper(boolean jumper) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIWAITSTATE,
                (jumper ? 1 : 0) //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIUSERDEVIDNUM -> BI USER DEV ID NUM -> boardInfo user device ID number
     Readable?
     Writabale?
     */
    public int getUserSpecifiedString() throws JMCCULException {
        //todo this should probably use the getString instead of getInt
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIUSERDEVIDNUM
        );
    }


    /* /////////////////////////////////////////////////////////////////////////////////
     BICALOUTPUT -> BI CAL OUTPUT -> boardInfo calibration output
     Readable? NO
     Writabale? yes
     */
    public void setCalPinVoltage(int calPinVoltage) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BICALOUTPUT,
                calPinVoltage
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDEVVERSION -> BI DEV VERSION -> boardInfo device version
     Readable? yes
     Writabale? NO
     */
    public String getVersion() throws JMCCULException {
        return Configuration.getString(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDEVVERSION,
                1024
        );
    }


    /*

    BIDEVSERIALNUM = factory serial number of USB or Bluetooth device
    BIDEVUNIQUEID = unique ID, suck as sn of usb or mac addr of ethernet
    BISERIALNUM = custom serial number assigned by user to a usb device
    BIUSERDEVIDNUM = user configured string that identifies a usb device
    BIUSERDEVID = user configured string from an ethernet, bluetooth, or usb device
     */


 /* /////////////////////////////////////////////////////////////////////////////////
     BISERIALNUM -> BI SERIAL NUM-> boardInfo serial number
     Readable? yes
     Writabale? yes
     */
    public void setUserConfigurableIdentifier(int identifier) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BISERIALNUM,
                identifier //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIHIDELOGINDLG -> BI HIDE LOGIN DLG -> boardInfo hide login dialog
     Readable? yes
     Writabale? yes
     */
    public void setDeviceLogin(boolean hide) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIHIDELOGINDLG,
                (hide ? 1 : 0) //new value
        );

    }



}
