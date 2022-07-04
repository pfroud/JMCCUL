package xyz.froud.jmccul;

import xyz.froud.jmccul.analog.AnalogInputImpl;
import xyz.froud.jmccul.analog.AnalogOutputImpl;
import xyz.froud.jmccul.config.AnalogInputConfig;
import xyz.froud.jmccul.config.AnalogOutputConfig;
import xyz.froud.jmccul.config.BoardConfig;
import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.config.CounterConfig;
import xyz.froud.jmccul.config.DigitalInputConfig;
import xyz.froud.jmccul.config.DigitalOutputConfig;
import xyz.froud.jmccul.config.ExpansionConfig;
import xyz.froud.jmccul.config.NetworkConfig;
import xyz.froud.jmccul.config.TemperatureConfig;
import xyz.froud.jmccul.config.WirelessConfig;
import xyz.froud.jmccul.counter.CounterImpl;
import xyz.froud.jmccul.digital.DigitalImpl;
import xyz.froud.jmccul.jna.DaqDeviceDescriptor;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;
import xyz.froud.jmccul.temperature.TemperatureImpl;

import java.nio.ByteBuffer;

/**
 * @author Peter Froud
 */
public class DaqDevice implements AutoCloseable {

    static {
        /*
        There are two ways to set up MC DAQ boards.
        See https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Ical-APIDetect.htm

        (1) Use InstaCal to detect boards and assign a board number.
            The path to InstaCal is "C:\Program Files (x86)\Measurement Computing\DAQ\inscal32.exe".
            InstaCal saves configuration to a config file on disk at "C:\ProgramData\Measurement Computing\DAQ\CB.CFG".
            Using InstaCal is troublesome for two reasons:
              (a) A human must open InstaCal and click a few buttons when a board is first connected.
              (b) InstaCal settings are sometimes erased, I think during some Windows Update operations.

        (2) Use the Universal Library to detect boards and assign a board number.
            This is better than using InstaCal because the whole thing is totally automatic.

         https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbIgnoreInstaCal.htm

         The cbIgnoreInstaCal() function tell the MCDAQ driver to ignore the CB.CFG config file
         generated by InstaCal.

         Through experimentation, I have discovered that if you call cbIgnoreInstaCal() after
         opening any devices with cbCreateDaqDevice(), operations on an already opened board
         will throw error 1028 "Tried to release a board which doesn't exist."
         Therefore, we'll use boolean field to make sure we only call cbIgnoreInstaCal() once.
         */
        try {
            final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbIgnoreInstaCal();
            JMCCULUtils.checkError(errorCode);
        } catch (JMCCULException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static int nextBoardNumber = 0;

    private final static int BOARD_NAME_LENGTH = MeasurementComputingUniversalLibrary.BOARDNAMELEN;
    private final static int CONFIG_ITEM_LENGTH = MeasurementComputingUniversalLibrary.BOARDNAMELEN;
    private final static int CONFIG_TYPE_BOARD_INFO = MeasurementComputingUniversalLibrary.BOARDINFO;
    private final static int CONFIG_ITEM_FACTORY_SERIAL_NUMBER = MeasurementComputingUniversalLibrary.BIDEVSERIALNUM;
    private final static int CONFIG_ITEM_USER_DEVICE_ID = MeasurementComputingUniversalLibrary.BIUSERDEVID;

    private final int boardNumber;
    private final int productID;
    private String factorySerialNumber;
    private String boardName;

    public final DigitalImpl digital;
    public final AnalogOutputImpl analogOutput;
    public final AnalogInputImpl analogInput;
    public final TemperatureImpl temperature;
    public final CounterImpl counter;

    public final AnalogInputConfig analogInputConfig;
    public final AnalogOutputConfig analogOutputConfig;
    public final BoardConfig boardConfig;
    public final CounterConfig counterConfig;
    public final DigitalInputConfig digitalInputConfig;
    public final DigitalOutputConfig digitalOutputConfig;
    public final ExpansionConfig expansionConfig;
    public final NetworkConfig networkConfig;
    public final TemperatureConfig temperatureConfig;
    public final WirelessConfig wirelessConfig;

    public DaqDevice(DaqDeviceDescriptor descriptor) throws JMCCULException {
        boardNumber = nextBoardNumber;
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbCreateDaqDevice.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbCreateDaqDevice(boardNumber, descriptor);
        JMCCULUtils.checkError(errorCode);
        nextBoardNumber++;

        // TODO can get the product name (any maybe mroe stuff) from the descriptor?
        productID = descriptor.ProductID;

        digital = new DigitalImpl(this);
        analogOutput = new AnalogOutputImpl(this);
        analogInput = new AnalogInputImpl(this);
        temperature = new TemperatureImpl(this);
        counter = new CounterImpl(this);

        analogInputConfig = new AnalogInputConfig(this);
        analogOutputConfig = new AnalogOutputConfig(this);
        boardConfig = new BoardConfig(this);
        counterConfig = new CounterConfig(this);
        digitalInputConfig = new DigitalInputConfig(this);
        digitalOutputConfig = new DigitalOutputConfig(this);
        expansionConfig = new ExpansionConfig(this);
        networkConfig = new NetworkConfig(this);
        temperatureConfig = new TemperatureConfig(this);
        wirelessConfig = new WirelessConfig(this);
    }

    public int getBoardNumber() {
        return boardNumber;
    }

    public int getProductID() {
        return productID;
    }

    public String getBoardName() throws JMCCULException {
        // TODO can we get this from the descriptor in the constructor?
        if (boardName == null) {
            final ByteBuffer buf = ByteBuffer.allocate(BOARD_NAME_LENGTH);

            // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetBoardName.htm
            final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetBoardName(boardNumber, buf);

            JMCCULUtils.checkError(errorCode);
            boardName = new String(buf.array()).trim();
        }
        return boardName;
    }

    public String getFactorySerialNumber() throws JMCCULException {
        if (factorySerialNumber == null) {
            final int DEVICE_NUMBER_BASE_BOARD = 0; //set to 1 to get the factory serial number of an expansion board
            factorySerialNumber = Configuration.getString(
                    CONFIG_TYPE_BOARD_INFO,
                    boardNumber,
                    DEVICE_NUMBER_BASE_BOARD,
                    CONFIG_ITEM_FACTORY_SERIAL_NUMBER,
                    CONFIG_ITEM_LENGTH
            );
        }
        return factorySerialNumber;
    }

    private String getUserDeviceIdentifier() throws JMCCULException {
        /*
        On a USB-1208FS, this throws error code 41 "This function can not be used with this board".
        In InstaCal, you can only set the Identifier field to a number, so I think we need to use
        BIUSERDEVIDNUM.
         */
        final int DEVICE_NUMBER_DEFAULT = 0;
        return Configuration.getString(
                CONFIG_TYPE_BOARD_INFO,
                boardNumber,
                DEVICE_NUMBER_DEFAULT,
                CONFIG_ITEM_USER_DEVICE_ID,
                CONFIG_ITEM_LENGTH
        );
    }

    @Override
    public String toString() {
        return "model " + boardName + ", serial number " + factorySerialNumber;
    }

    @Override
    public void close() throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbReleaseDaqDevice.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbReleaseDaqDevice(boardNumber);
        JMCCULUtils.checkError(errorCode);
    }

}
