package jmccul;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import jmccul.jna.DaqDeviceDescriptor;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class JMCCULUtils {

    private static final MeasurementComputingUniversalLibrary LIBRARY = MeasurementComputingUniversalLibrary.INSTANCE;

    private final static int INTERFACE_TYPE_ANY = MeasurementComputingUniversalLibrary.DaqDeviceInterface.ANY_IFC;

    private final static int ERROR_STRING_LENGTH = MeasurementComputingUniversalLibrary.ERRSTRLEN;

    public static String getErrorMessage(int errorCode) throws JMCCULException {
        final ByteBuffer buf = ByteBuffer.allocate(ERROR_STRING_LENGTH);
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Error_Handling_Functions/cbGetErrMsg.htm
        int errorCode2 = LIBRARY.cbGetErrMsg(errorCode, buf);
        if (errorCode2 != 0) {
            throw new JMCCULException("exception when looking up the error code!");
        }
        return new String(buf.array()).trim();
    }

    public static void checkError(int errorCode) throws JMCCULException {
        if (errorCode != MeasurementComputingUniversalLibrary.NOERRORS) {
            final String message = "code " + errorCode + ": " + JMCCULUtils.getErrorMessage(errorCode);
            throw new JMCCULException(message);
        }
    }

    public static DaqDeviceDescriptor[] findDaqDevices() throws JMCCULException {

        /*
        You can change the max device count to something bigger,
        I just made this number up for testing.
         */
        final int MAX_DEVICE_COUNT = 5;

        /*
        Allocate an array of empty DaqDeviceDescriptors. The cbGetDaqDeviceInventory() call will populate this array.
        See Stack Overflow answer to 'How to fill an array of structures in JNA?' https://stackoverflow.com/a/25186232
         */
        final DaqDeviceDescriptor[] buffer = (DaqDeviceDescriptor[]) new DaqDeviceDescriptor().toArray(MAX_DEVICE_COUNT);

        /*
        The deviceCount variable is used an both an input and output to cbGetDaqDeviceInventory().
        As an input, it is the length of the DaqDeviceDescriptor array.
        As an ooutput, it is how many DAQ devices were actually found.
         */
        final IntBuffer deviceCount = IntBuffer.wrap(new int[]{MAX_DEVICE_COUNT});

        /*
        https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbGetDaqDeviceInventory.htm
        About the DaqDeviceDescriptor argument:

        In the function's original declaration (in the .h file), the argument is type DaqDeviceDescriptor*.
        But JNAerator changed it to DaqDeviceDescriptor. Universal Library Help says this about the argument:
        "Pointer to a user-allocated array of DaqDeviceDescriptor types ..."

        According to https://stackoverflow.com/a/25186232, we need to pass the 0th element of the array.
         */
        checkError(LIBRARY.cbGetDaqDeviceInventory(INTERFACE_TYPE_ANY, buffer[0], deviceCount));

        // After calling cbGetDaqDeviceInventory(), deviceCount now contains how many devices were actually found.
        final int devicesFoundCount = deviceCount.get(0);

        // The buffer still has MAX_DEVICE_COUNT elements in it. Return a subarray with only found devices.
        return Arrays.copyOf(buffer, devicesFoundCount);
    }

}
