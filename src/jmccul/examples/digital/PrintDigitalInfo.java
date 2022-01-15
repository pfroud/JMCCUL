package jmccul.examples.digital;

import java.util.Optional;
import java.util.function.Predicate;
import jmccul.DaqDevice;
import jmccul.DeviceDiscovery;
import jmccul.JMCCULException;
import jmccul.digital.DigitalPort;

/**
 *
 * @author Peter Froud
 */
public class PrintDigitalInfo {

    public static void main(String[] args) throws JMCCULException {

        final Predicate<DaqDevice> predicate = d -> d.digital.isDigitalIOSupported();
        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findFirstDeviceMatching(predicate);
        if (optionalDevice.isEmpty()) {
            System.out.println("No device found which supports digital IO!");
            return;
        }

        try (DaqDevice device = optionalDevice.get()) {
            System.out.println("Digital I/O info for this device: " + device.toString());

            final DigitalPort[] ports = device.digital.PORTS;

            System.out.printf("There are %d port(s):\n", ports.length);
            for (int i = 0; i < ports.length; i++) {
                final DigitalPort port = ports[i];
                System.out.printf("Port %d / %d: %s\n", i + 1, ports.length, port.toString());
                System.out.println("   NUM_BITS    = " + port.NUM_BITS);
                System.out.println("   INPUT_MASK  = " + port.INPUT_MASK);
                System.out.println("   OUTPUT_MASK = " + port.OUTPUT_MASK);
                System.out.println("   FIRST_BIT   = " + port.FIRST_BIT);
                System.out.println("   IS_OUTPUT_SUPPORTED  = " + port.IS_OUTPUT_SUPPORTED);
                System.out.println("   IS_INPUT_SUPPORTED   = " + port.IS_INPUT_SUPPORTED);
                System.out.println("   IS_PORT_CONFIGURABLE = " + port.IS_PORT_CONFIGURABLE);
                System.out.println("   IS_BIT_CONFIGURABLE  = " + port.IS_BIT_CONFIGURABLE);
                System.out.println("   IS_INPUT_SCAN_SUPPORTED  = " + port.IS_INPUT_SCAN_SUPPORTED);
                System.out.println("   IS_OUTPUT_SCAN_SUPPORTED = " + port.IS_OUTPUT_SCAN_SUPPORTED);
            }
        }

    }

}
