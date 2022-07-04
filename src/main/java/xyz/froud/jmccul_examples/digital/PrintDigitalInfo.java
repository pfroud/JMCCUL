package xyz.froud.jmccul_examples.digital;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.digital.DigitalPort;

import java.util.Optional;

/**
 * @author Peter Froud
 */
public class PrintDigitalInfo {

    public static void main(String[] args) throws JMCCULException {

        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findFirstDeviceMatching(
                d -> d.digital.isDigitalIOSupported()
        );

        if (optionalDevice.isPresent()) {
            try (DaqDevice device = optionalDevice.get()) {
                System.out.println("Digital I/O info for this device: " + device);

                final DigitalPort[] ports = device.digital.getPorts();

                System.out.printf("There are %d digital port(s):\n", ports.length);
                for (int i = 0; i < ports.length; i++) {
                    final DigitalPort port = ports[i];
                    System.out.printf("Port %d / %d: %s\n", i + 1, ports.length, port.toString());
                    System.out.println("   getBitCount    = " + port.getBitCount());
                    System.out.println("   getInputMask  = " + port.getInputMask());
                    System.out.println("   getOutputMask = " + port.getOutputMask());
                    System.out.println("   getFirstBit   = " + port.getFirstBit());
                    System.out.println("   isOutputSupported  = " + port.isOutputSupported());
                    System.out.println("   isInputSupported   = " + port.isInputSupported());
                    System.out.println("   isPortConfigurable = " + port.isPortConfigurable());
                    System.out.println("   isBitConfigurable  = " + port.isIndividualBitConfigurable());
                    System.out.println("   isInputScanSupported  = " + port.isInputScanSupported());
                    System.out.println("   isOutputScanSupported = " + port.isOutputScanSupported());
                }
            }
        } else {
            System.out.println("Didn't find a device which supports digital I/O.");
        }

    }

}
