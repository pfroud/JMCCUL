package xyz.froud.jmccul_examples;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.DaqDeviceDescriptor;

/**
 * @author Peter Froud
 */
public class PrintDeviceInfo {

    public static void main(String[] args) throws JMCCULException {
        final DaqDeviceDescriptor[] descriptors = DeviceDiscovery.findDaqDeviceDescriptors();
        if (descriptors.length == 0) {
            System.out.println("No daq devices found!");
        } else {

            for (int i = 0; i < descriptors.length; i++) {
                DaqDeviceDescriptor descriptor = descriptors[i];

                System.out.printf("This is device %d / %d\n", i + 1, descriptors.length);
                try (DaqDevice device = new DaqDevice(descriptor)) {
                    System.out.println("  BOARD NAME = " + device.BOARD_NAME);
                    System.out.println("  FACTORY_SERIAL_NUMBER = " + device.FACTORY_SERIAL_NUMBER);
//                    System.out.println("  PRODUCT_ID = " + device.PRODUCT_ID);
                    // TODO look at the user device identifier thing
                    System.out.println("  Is digital I/O supported?   " + device.digital.isDigitalIOSupported());
                    System.out.println("  Is analog input supported?  " + device.analogInput.isAnalogInputSupported());
                    System.out.println("  Is analog output supported? " + device.analogOutput.isAnalogOutputSupported());
                    System.out.println("  Is temperature supported?   " + device.temperature.isTemperatureInputSupported());
                    System.out.println("  Is counter supported?       " + device.counter.isCounterSupported());
                }
                System.out.println();

            }

        }
    }
}
