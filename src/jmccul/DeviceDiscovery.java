package jmccul;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import static jmccul.JMCCULUtils.checkError;
import jmccul.jna.DaqDeviceDescriptor;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class DeviceDiscovery {

    public static DaqDeviceDescriptor[] findDaqDeviceDescriptors() throws JMCCULException {

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

        final int INTERFACE_TYPE_ANY = MeasurementComputingUniversalLibrary.DaqDeviceInterface.ANY_IFC;

        /*
        https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbGetDaqDeviceInventory.htm
        About the DaqDeviceDescriptor argument:

        In the function's original declaration (in the .h file), the argument is type DaqDeviceDescriptor*.
        But JNAerator changed it to DaqDeviceDescriptor. Universal Library Help says this about the argument:
        "Pointer to a user-allocated array of DaqDeviceDescriptor types ..."

        According to https://stackoverflow.com/a/25186232, we need to pass the 0th element of the array.
         */
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetDaqDeviceInventory(INTERFACE_TYPE_ANY, buffer[0], deviceCount);
        checkError(errorCode);

        // After calling cbGetDaqDeviceInventory(), deviceCount now contains how many devices were actually found.
        final int devicesFoundCount = deviceCount.get(0);

        // The buffer still has MAX_DEVICE_COUNT elements in it. Return a subarray with only found devices.
        return Arrays.copyOf(buffer, devicesFoundCount);
    }

    public static Optional<DaqDevice> searchByBoardName(String desiredBoardName) throws JMCCULException {
        Predicate<DaqDeviceDescriptor> boardNamePredicate = (DaqDeviceDescriptor descriptor) -> {
            final String prodNameFromDescriptor = (new String(descriptor.ProductName)).trim();
            return prodNameFromDescriptor.equals(desiredBoardName);
        };

        Optional<DaqDeviceDescriptor> descriptor = findFirstDescriptorMatching(boardNamePredicate);
        if (descriptor.isPresent()) {
            DaqDevice device = new DaqDevice(descriptor.get());
            return Optional.of(device);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<DaqDevice> findDeviceMatchingPredicate(Predicate<DaqDevice> predicate) throws JMCCULException {
        // can't do this cleanly with stream because DaqDevice constructors throws a checked exception
        DaqDeviceDescriptor[] descriptors = findDaqDeviceDescriptors();
        for (DaqDeviceDescriptor descriptor : descriptors) {
            try {
                DaqDevice device = new DaqDevice(descriptor);
                if (predicate.test(device)) {
                    return Optional.of(device);
                }
            } catch (JMCCULException ignore) {

            }
        }
        return Optional.empty();
    }

    public static Optional<DaqDeviceDescriptor> findFirstDescriptorMatching(Predicate<DaqDeviceDescriptor> predicate) throws JMCCULException {
        return Arrays.stream(findDaqDeviceDescriptors()).filter(predicate).findFirst();
    }

}
