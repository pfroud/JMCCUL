/*
 * Copyright (c) 2022 Peter Froud.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.froud.jmccul;

import xyz.froud.jmccul.jna.DaqDeviceDescriptor;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import static xyz.froud.jmccul.JMCCULUtils.checkError;

/**
 * @author Peter Froud
 */
public class DeviceDiscovery {

    /**
     * @param interfaceTypeBitMask use either:
     *         <ul>
     *             <li>{@link MeasurementComputingUniversalLibrary.DaqDeviceInterface#ANY_IFC}</li>
     *                 <li>Or create a bit mask by OR-ing together any of these items:
     *                 <ul>
     *                     <li>{@link MeasurementComputingUniversalLibrary.DaqDeviceInterface#USB_IFC}</li>
     *                     <li>{@link MeasurementComputingUniversalLibrary.DaqDeviceInterface#BLUETOOTH_IFC}</li>
     *                     <li>{@link MeasurementComputingUniversalLibrary.DaqDeviceInterface#ETHERNET_IFC}</li>
     *                 </ul>
     *             </li>
     *         </ul>
     */
    public static DaqDeviceDescriptor[] findDescriptors(int interfaceTypeBitMask) throws JMCCULException {

        // Can be any arbitrary number, just needed so C can allocate stuff
        final int MAX_DEVICE_COUNT = 16;

        /*
        Allocate an array of empty DaqDeviceDescriptors. The cbGetDaqDeviceInventory() call will populate this array.
        See Stack Overflow answer to 'How to fill an array of structures in JNA?' https://stackoverflow.com/a/25186232
         */
        final DaqDeviceDescriptor[] buffer = (DaqDeviceDescriptor[]) new DaqDeviceDescriptor().toArray(MAX_DEVICE_COUNT);

        /*
        The deviceCount variable is used both an input and output to cbGetDaqDeviceInventory().
        As an input, it is the length of the DaqDeviceDescriptor array.
        As an output, it is how many DAQ devices were actually found.
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
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetDaqDeviceInventory(interfaceTypeBitMask, buffer[0], deviceCount);
        checkError(errorCode);

        // After calling cbGetDaqDeviceInventory(), deviceCount now contains how many devices were actually found.
        final int devicesFoundCount = deviceCount.get(0);

        // The buffer still has MAX_DEVICE_COUNT elements in it. Return a subarray with only found devices.
        return Arrays.copyOf(buffer, devicesFoundCount);
    }

    public static DaqDeviceDescriptor[] findDescriptors() throws JMCCULException {
        return findDescriptors(MeasurementComputingUniversalLibrary.DaqDeviceInterface.ANY_IFC);
    }

    public static Optional<DaqDevice> findByBoardName(String desiredBoardName) throws JMCCULException {
        // Best to do this by filtering descriptors because we don't need to open and close devices.
        // Can't do this cleanly with Stream because the DaqDevice constructor throws a checked exception.
        return getOptionalDeviceFromOptionalDescriptor(
                findFirstDescriptorMatching(d -> d.getProductName().equals(desiredBoardName))
        );
    }

    public static Optional<DaqDevice> findByUniqueID(String desiredUniqueID) throws JMCCULException {
        // Best to do this by filtering descriptors because we don't need to open and close devices.
        // Can't do this cleanly with Stream because the DaqDevice constructor throws a checked exception.
        return getOptionalDeviceFromOptionalDescriptor(
                findFirstDescriptorMatching(d -> d.getUniqueID().equals(desiredUniqueID))
        );
    }

    public static Optional<DaqDevice> findByUniqueID(long desiredUniqueID) throws JMCCULException {
        // Best to do this by filtering descriptors because we don't need to open and close devices.
        // Can't do this cleanly with Stream because the DaqDevice constructor throws a checked exception.
        return getOptionalDeviceFromOptionalDescriptor(
                findFirstDescriptorMatching(d -> d.NUID == desiredUniqueID)
        );
    }

    public static Optional<DaqDeviceDescriptor> findFirstDescriptorMatching(Predicate<DaqDeviceDescriptor> predicate) throws JMCCULException {
        return Arrays.stream(findDescriptors()).filter(predicate).findFirst();
    }

    public static Optional<DaqDevice> findFirstDeviceMatching(PredicateThrowsJMCCULException<DaqDevice> predicate) throws JMCCULException {
        // Can't do this cleanly with Stream because the DaqDevice constructor throws a checked exception.
        for (DaqDeviceDescriptor descriptor : findDescriptors()) {
            DaqDevice device = new DaqDevice(descriptor);
            if (predicate.test(device)) {
                return Optional.of(device);
            } else {
                device.close();
            }
        }
        return Optional.empty();
    }

    public static Optional<DaqDeviceDescriptor> findAnyDescriptor() throws JMCCULException {
        return Arrays.stream(findDescriptors()).findAny();
    }

    public static Optional<DaqDevice> findAnyDevice() throws JMCCULException {
        // Can't do this cleanly with Stream because the DaqDevice constructor throws a checked exception.
        return getOptionalDeviceFromOptionalDescriptor(
                findAnyDescriptor()
        );
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Optional<DaqDevice> getOptionalDeviceFromOptionalDescriptor(Optional<DaqDeviceDescriptor> optDescr) throws JMCCULException {
        /*
        Use this method instead of
            optDescr.map(DaqDevice::new);
        because the DaqDevice constructor throws a checked exception.
         */
        if (optDescr.isPresent()) {
            return Optional.of(new DaqDevice(optDescr.get()));
        } else {
            return Optional.empty();
        }
    }

    /** Same as {@link java.util.function.Predicate} but it throws a JMCCUL exception. */
    @FunctionalInterface
    public interface PredicateThrowsJMCCULException<T> {
        boolean test(T t) throws JMCCULException;
    }

}
