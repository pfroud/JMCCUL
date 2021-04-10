package jmccul;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javax.swing.JOptionPane;
import static jmccul.JMCCULUtils.throwIfNeeded;

/**
 *
 * @author Peter Froud
 */
public abstract class AbstractJMCCULDevice {

    public static boolean debugPrintouts = false;
    private static boolean alreadyCalledIgnoreInstaCal = false;
    private static int nextBoardNumber = 0;

    private final MeasurementComputingUniversalLibrary LIBRARY = MeasurementComputingUniversalLibrary.INSTANCE;

    private final static int BOARD_NAME_LENGTH = MeasurementComputingUniversalLibrary.BOARDNAMELEN;
    private final static int CONFIG_ITEM_LENGTH = MeasurementComputingUniversalLibrary.BOARDNAMELEN;
    private final static int CONFIG_TYPE_BOARD_INFO = MeasurementComputingUniversalLibrary.BOARDINFO;
    private final static int CONFIG_ITEM_FACTORY_SERIAL_NUMBER = MeasurementComputingUniversalLibrary.BIDEVSERIALNUM;
    private final static int CONFIG_ITEM_USER_DEVICE_ID = MeasurementComputingUniversalLibrary.BIUSERDEVID;

    protected final int BOARD_NUMBER;
    public final String FACTORY_SERIAL_NUMBER;
    public final String BOARD_NAME;
    public final String USER_DEVICE_IDENTIFIER;

    public AbstractJMCCULDevice(String desiredBoardName) throws JMCCULException {
        /*
        There are two ways to set up MC DAQ boards.
        See https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Ical-APIDetect.htm

        (1) Use InstaCal to detect boards and assign a board number.
            The path to InstaCal is "C:\Program Files (x86)\Measurement Computing\DAQ\inscal32.exe".
            InstaCal saves confugration to a config file on disk at "C:\ProgramData\Measurement Computing\DAQ\CB.CFG".
            Using InstaCal is troublesome for two reasons:
              (a) A human must open InstaCal and click a few buttons when a board is first connected.
              (b) InstaCal settings are sometimes erased, I think during some Windows Update operations.

        (2) Use the Universal Library to detect boards and assign a board number.
            This is better than using InstaCal because the whole thing is totally automatic.
         */

        if (!alreadyCalledIgnoreInstaCal) {
            /*
             Tell the MCDAQ driver to ignore the CB.CFG config file generated by InstaCal.

             Through experimentation, I have discovered that if you call cbIgnoreInstaCal() after
             opening any devices with cbCreateDaqDevice(), operations on an already openeded board
             will throw error 1028 "Tried to release a board which doesn't exist."
             Therefore we'll use boolean field to make sure we only call cbIgnoreInstaCal() once.
             */
            throwIfNeeded(LIBRARY.cbIgnoreInstaCal());
            alreadyCalledIgnoreInstaCal = true;
        }

        final DaqDeviceDescriptor[] foundDevicesArray = JMCCULUtils.findDaqDevices();
        final int foundDevicesCount = foundDevicesArray.length;

        if (foundDevicesCount < 1) {
            final String title = "Can't find any MC DAQ devices 找不到数据采集设备";
            final String message
                    = "Can't find a Measurement Computing USB-1408FS data acquisition device.\n"
                    + "The program will still run, but some functionality won't work.\n"
                    + "Suggestions: Check the USB connection. Install the Measurement Computing Universal Library software (mccdaq.exe).\n"
                    + "找不到USB-1408FS数据采集设备。 该软件仍将运行，但是某些功能将无法使用。";
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
            throw new JMCCULException("No MC DAQ devices found!");
        }
        final StringBuilder allDevicesToPrintOut = new StringBuilder();
        for (int i = 0; i < foundDevicesCount; i++) {
            final DaqDeviceDescriptor deviceDescriptor = foundDevicesArray[i];
            if (new String(deviceDescriptor.ProductName).trim().equals(desiredBoardName)) {
                // Open the daq device and assign it a board number.
                BOARD_NUMBER = nextBoardNumber;
                nextBoardNumber++;
                throwIfNeeded(LIBRARY.cbCreateDaqDevice(BOARD_NUMBER, deviceDescriptor));
                BOARD_NAME = getBoardName();
                FACTORY_SERIAL_NUMBER = getFactorySerialNumber();
                USER_DEVICE_IDENTIFIER = getUserDeviceIdentifier();
                if (debugPrintouts) {
                    System.out.printf("model=%s, SN=%s assigned to BOARD_NUMBER %d. nextBoardNumber is now %d.\n", desiredBoardName, FACTORY_SERIAL_NUMBER, BOARD_NUMBER, nextBoardNumber);
                } else {
                    System.out.printf("DAQ model %s, serial number %s\n", BOARD_NAME, FACTORY_SERIAL_NUMBER);
                }
                return;
            } else {
                allDevicesToPrintOut.append(i).append(": ").append(foundDevicesArray[i].toString());
                if (i < foundDevicesCount - 1) {
                    allDevicesToPrintOut.append("\n");
                }
            }

        }

        // we looked at all found daq devices but did not found one with the correct productName.
        System.err.printf("Found %d MC DAQ device(s), but none match product name \"%s\":\n", foundDevicesCount, desiredBoardName);
        System.err.println(allDevicesToPrintOut.toString());

        final String title = "Couldn't find MC DAQ device 找不到数据采集设备";
        final String message
                = "Couldn't find a Measurement Computing data acquisition devices with model name " + desiredBoardName + ".\n"
                + "The program will still run, but some functionality won't work.\n"
                + "找不到正确的数据采集设备型号。\n"
                + "该软件仍将运行，但是某些功能将无法使用。";
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);

        throw new JMCCULException("multiple MC DAQ devices found");

    }

    public final void release() throws JMCCULException {
        if (debugPrintouts) {
            System.out.println(BOARD_NAME + ": releasing board number " + BOARD_NUMBER);
        }
        throwIfNeeded(LIBRARY.cbReleaseDaqDevice(BOARD_NUMBER));
    }

    private String getBoardName() throws JMCCULException {
        final ByteBuffer buf = ByteBuffer.allocate(BOARD_NAME_LENGTH);
        throwIfNeeded(LIBRARY.cbGetBoardName(BOARD_NUMBER, buf));
        return new String(buf.array()).trim();
    }

    private String getFactorySerialNumber() throws JMCCULException {
        final ByteBuffer buf = ByteBuffer.allocate(CONFIG_ITEM_LENGTH);
        final IntBuffer maxConfigLen = IntBuffer.wrap(new int[]{CONFIG_ITEM_LENGTH});
        final int DEVICE_NUMBER_BASE_BOARD = 0; //set to 1 to get the factory serial number of an expansion board
        throwIfNeeded(LIBRARY.cbGetConfigString(CONFIG_TYPE_BOARD_INFO, BOARD_NUMBER, DEVICE_NUMBER_BASE_BOARD, CONFIG_ITEM_FACTORY_SERIAL_NUMBER, buf, maxConfigLen));
        return new String(buf.array(), 0, maxConfigLen.get(0));
    }

    private String getUserDeviceIdentifier() throws JMCCULException {
        final ByteBuffer buf = ByteBuffer.allocate(CONFIG_ITEM_LENGTH);
        final IntBuffer maxConfigLen = IntBuffer.wrap(new int[]{CONFIG_ITEM_LENGTH});
        final int DEVICE_NUMBER_DEFAULT = 0;
        throwIfNeeded(LIBRARY.cbGetConfigString(CONFIG_TYPE_BOARD_INFO, BOARD_NUMBER, DEVICE_NUMBER_DEFAULT, CONFIG_ITEM_USER_DEVICE_ID, buf, maxConfigLen));
        return new String(buf.array(), 0, maxConfigLen.get(0));
    }

}
