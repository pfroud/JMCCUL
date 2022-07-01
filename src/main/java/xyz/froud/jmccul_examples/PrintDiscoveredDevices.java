package xyz.froud.jmccul_examples;

import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.DaqDeviceDescriptor;

/**
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
