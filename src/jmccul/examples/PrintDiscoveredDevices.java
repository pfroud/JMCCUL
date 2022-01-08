package jmccul.examples;

import jmccul.JMCCULUtils;
import jmccul.jna.DaqDeviceDescriptor;

/**
 *
 * @author Peter Froud
 */
public class PrintDiscoveredDevices {

    public static void main(String[] args) {
        try {
            DaqDeviceDescriptor[] descriptors = JMCCULUtils.findDaqDeviceDescriptors();
            if (descriptors.length == 0) {
                System.out.println("No daq devices found!");
            } else {
                System.out.printf("Found %d daq device(s):\n", descriptors.length);
                for (DaqDeviceDescriptor descriptor : descriptors) {
                    System.out.println(descriptor);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
