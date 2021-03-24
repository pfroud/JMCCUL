package mcdaq;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 *
 * @author Peter Froud
 */
public class McDaqUtils {

    private static final MeasurementComputingUniversalLibrary LIBRARY = MeasurementComputingUniversalLibrary.INSTANCE;

    private final static int INTERFACE_TYPE_USB = MeasurementComputingUniversalLibrary.DaqDeviceInterface.USB_IFC;

    private final static int ERROR_STRING_LENGTH = MeasurementComputingUniversalLibrary.ERRSTRLEN;

    public static String getErrorMessage(int errorCode) throws McDaqException {
        final ByteBuffer buf = ByteBuffer.allocate(ERROR_STRING_LENGTH);
        int errorCode2 = LIBRARY.cbGetErrMsg(errorCode, buf);
        if (errorCode2 != 0) {
            throw new McDaqException(errorCode);
        }
        return new String(buf.array()).trim();
    }

    public static void throwIfNeeded(int errorCode) throws McDaqException {
        if (errorCode != MeasurementComputingUniversalLibrary.NOERRORS) {
            throw new McDaqException(errorCode);
        }
    }

    public static DaqDeviceDescriptor[] findDaqDevices() throws McDaqException {

        final int MAX_DEVICE_COUNT = 5;

        /*
        Allocate an array of empty DaqDeviceDescriptors. The GetDaqDeviceInventory call will populate this array.
        See Stack Overflow answer to 'How to fill an array of structures in JNA?' https://stackoverflow.com/a/25186232
         */
        final DaqDeviceDescriptor[] buffer = (DaqDeviceDescriptor[]) new DaqDeviceDescriptor().toArray(MAX_DEVICE_COUNT);

        /*
        deviceCount is used an both an input and output to GetDaqDeviceInventory.
        As an input, it is the length of the DaqDeviceDescriptor array.
        As an ooutput, it is how many DAQ devices were actually found.
         */
        final IntBuffer deviceCount = IntBuffer.wrap(new int[]{MAX_DEVICE_COUNT});

        /*
        About the DaqDeviceDescriptor argument:

        In the function's original declaration (in the .h file), the argument is type DaqDeviceDescriptor*.
        But JNAerator changed it to DaqDeviceDescriptor. Universal Library Help says this about the argument:
        "Pointer to a user-allocated array of DaqDeviceDescriptor types ..."

        According to https://stackoverflow.com/a/25186232, we need to pass the 0th element of the array.
         */
        throwIfNeeded(LIBRARY.cbGetDaqDeviceInventory(INTERFACE_TYPE_USB, buffer[0], deviceCount));

        // Now, deviceCount contains how many devices were actually found.
        final int devicesFoundCount = deviceCount.get(0);

        // The buffer still has MAX_DEVICE_COUNT elements in it. Return a subarray with only found devices.
        return Arrays.copyOf(buffer, devicesFoundCount);
    }

}
