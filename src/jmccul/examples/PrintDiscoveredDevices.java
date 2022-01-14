package jmccul.examples;

import jmccul.DeviceDiscovery;
import jmccul.JMCCULException;
import jmccul.jna.DaqDeviceDescriptor;

/**
 *
 * @author Peter Froud
 */
public class PrintDiscoveredDevices {

    public static void main(String[] args) throws JMCCULException {
        final DaqDeviceDescriptor[] descriptors = DeviceDiscovery.findDaqDeviceDescriptors();
        if (descriptors.length == 0) {
            System.out.println("No daq device descriptors found!");
        } else {
            System.out.printf("Found %d daq device descriptor(s):\n", descriptors.length);
            for (DaqDeviceDescriptor descriptor : descriptors) {
                System.out.println(descriptor);
            }
        }
    }
}
