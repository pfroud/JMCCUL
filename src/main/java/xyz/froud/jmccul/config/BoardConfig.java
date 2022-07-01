package xyz.froud.jmccul.config;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.enums.BaseOrExpansionBoard;
import xyz.froud.jmccul.enums.CalibrationTableType;
import xyz.froud.jmccul.enums.InterruptClockEdge;
import xyz.froud.jmccul.enums.ExternalPacerClockEdge;
import xyz.froud.jmccul.enums.ExternalClockType;
import xyz.froud.jmccul.enums.FirmwareVersionType;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

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
    public CalibrationTableType getCalibrationTableType(BaseOrExpansionBoard baseOrExpansionBoard) throws JMCCULException {
        return CalibrationTableType.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        baseOrExpansionBoard.VALUE,
                        MeasurementComputingUniversalLibrary.BICALTABLETYPE
                )
        );
    }

    public void setCalibrationTableType(CalibrationTableType calTable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BICALTABLETYPE,
                calTable.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BICLOCK -> BI CLOCK -> boardInfo clock
     Readable? yes
     Writabale? yes
     */
    public int getClockFrequencyMegahertz() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored when getting
                MeasurementComputingUniversalLibrary.BICLOCK
        );
    }

    public void setClockFrequencyMegahertz(int channel, int clockFrequency) throws JMCCULException {
        // todo only supports 1, 4,6 or 10
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel, //devNum is the channel when setting
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
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDMACHAN
        );
    }

    public void setDmaChannel(int dmaChannel) throws JMCCULException {
        // todo can only be 0, 1, or 3
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
    public int getDataTranslationBoardNumber() throws JMCCULException {
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
                0, //devNum is ignored
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
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIEXTINPACEREDGE
        ));
    }

    public void setInputPacerClockEdge(ExternalPacerClockEdge externalPacerClockEdge) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
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
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIEXTOUTPACEREDGE
        ));
    }

    public void setOutputPacerClockEdge(ExternalPacerClockEdge externalPacerClockEdge) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIEXTOUTPACEREDGE,
                externalPacerClockEdge.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIOUTPUTPACEROUT -> BI OUTPUT PACER OUT -> boardInfo output pacer output
     Readable? no but that is probably a mistake in the docs
     Writabale? yes
     */
    public void setOutputPacerEnable(boolean enable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //they don't say whether devnum is ignored
                MeasurementComputingUniversalLibrary.BIOUTPUTPACEROUT,
                (enable ? 1 : 0)
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
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINPUTPACEROUT,
                (enable ? 1 : 0)
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
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINTEDGE,
                edge.VALUE
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
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINTLEVEL,
                level
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
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIPANID,
                panID
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
        // it can only be AUXPORT0 or AUXPORT1
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIPATTERNTRIGPORT,
                port
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

    public void setSyncMode(int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.BISYNCMODE,
                n
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     setAdc -> BI TERM COUNT STAT BIT -> boardInfo terminal count output status bit
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

    public void setTerminalCountOutputStatus(int bitNumber, boolean status) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                bitNumber,
                MeasurementComputingUniversalLibrary.BITERMCOUNTSTATBIT,
                (status ? 1 : 0)
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
                0, //not specified whether devNum is ignored
                MeasurementComputingUniversalLibrary.BIWAITSTATE,
                (jumper ? 1 : 0)
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
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BICALOUTPUT,
                calPinVoltage
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDEVVERSION -> BI DEV VERSION -> boardInfo device version
     Readable? yes
     Writabale? NO
     */
    public String getVersion(FirmwareVersionType version) throws JMCCULException {
        return Configuration.getString(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                version.VALUE,
                MeasurementComputingUniversalLibrary.BIDEVVERSION,
                1024
        );
    }


    /* /////////////////////////////////////////////////////////////////////////////////
     BIDEVSERIALNUM -> BI DEV SERIAL NUM -> boardInfo device serial number

    Factory serial number of a USB or Bluetooth device.


     Readable? yes
     Writabale? no
     */
    public String getFactorySerialNumber(BaseOrExpansionBoard baseOrExpansionBoard) throws JMCCULException {
        return Configuration.getString(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                baseOrExpansionBoard.VALUE,
                MeasurementComputingUniversalLibrary.BIDEVSERIALNUM,
                1024
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDEVUNIQUEID -> BI DEV UNIQUE ID -> boardInfo device unique identifier

    Unique identifier of a discoverable device, such as the serial number of a USB device or MAC address of an Ethernet device.

     Readable? yes
     Writabale? no
     */
    public String getUniqueID() throws JMCCULException {
        return Configuration.getString(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //not specified what DevNum does
                MeasurementComputingUniversalLibrary.BIDEVUNIQUEID,
                1024
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BISERIALNUM -> BI SERIAL NUM -> boardInfo serial number

    User-configured identifier of a supported USB device.
    Custom serial number assigned by a user to a USB device.

     Readable? yes
     Writabale? yes
     */
    public int getUserSpecifiedSerialNumber() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BISERIALNUM
        );
    }

    public void setUserSpecifiedSerialNumber(int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BISERIALNUM,
                n
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIUSERDEVID -> BI USER DEV ID -> boardInfo user device ID

    User-configured string of up to maxConfigLen character/bytes to an Ethernet, Bluetooth, or USB device.

     Readable? yes
     Writabale? yes
     */
    public String getUserSpecifiedID() throws JMCCULException {
        return Configuration.getString(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIUSERDEVID,
                1024
        );
    }

    public void setUserSpecifiedID(String str) throws JMCCULException {
        Configuration.setString(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIUSERDEVID,
                str
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIUSERDEVIDNUM -> BI USER DEV ID NUM -> boardInfo user device ID number3

    User-configured string that identifies a USB device.


     Readable?
     Writabale? no
     */
    public int getUserSpecifiedString() throws JMCCULException {
        //todo the docs are probably wrong, this should probably use the getString instead of getInt
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIUSERDEVIDNUM
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIHIDELOGINDLG -> BI HIDE LOGIN DLG -> boardInfo hide login dialog
     Readable? yes
     Writabale? yes
     */
    public void setHideLoginDialog(boolean hide) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIHIDELOGINDLG,
                (hide ? 1 : 0)
        );

    }

}
