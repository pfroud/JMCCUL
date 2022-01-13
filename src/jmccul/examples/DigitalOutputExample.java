package jmccul.examples;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import jmccul.DaqDevice;
import jmccul.digital.DigitalPort;
import jmccul.digital.DigitalPortDirection;
import jmccul.jna.DaqDeviceDescriptor;

/**
 * https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/examples/console/digital_out.py
 *
 * @author Peter Froud
 */
public class DigitalOutputExample {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) {

        try {
            /*
            In my setup for testing:
            The device with serial number 1AC198E is doing digital input in DAQami.
            So, we want to use the device with serial number 1AC1968 for digital output from this program.
             */
            Predicate<DaqDeviceDescriptor> predicate = desc -> desc.NUID == 0x1AC1968;
            Optional<DaqDeviceDescriptor> optionalDesc = DaqDevice.findFirstDescriptorMatching(predicate);

            if (optionalDesc.isEmpty()) {
                System.out.println("No description found with that serial number");
                return;
            }

            DaqDevice device = new DaqDevice(optionalDesc.get());

            System.out.println("Opened this device: " + device.toString());

            final DigitalPort[] ports = device.digital.PORTS;

            final Optional<DigitalPort> optionalPortToUse = Arrays.stream(ports)
                    .filter(port -> port.IS_OUTPUT_SUPPORTED).findAny();

            if (optionalPortToUse.isEmpty()) {
                System.out.println("none of the ports support digital output");
                return;
            }

            final DigitalPort portToUse = optionalPortToUse.get();
            System.out.println("Using this port: " + portToUse);

            device.digital.configurePort(portToUse.PORT_TYPE, DigitalPortDirection.OUTPUT);

            //                                            bits       76453210
            device.digital.outputPort(portToUse.PORT_TYPE, (short) 0b10110110);

            for (int bitIdx = 0; bitIdx < portToUse.NUM_BITS; bitIdx++) {
                device.digital.outputBit(portToUse.PORT_TYPE, bitIdx, true);
            }

            //                              bits               76543210
            device.digital.outputPort32(portToUse.PORT_TYPE, 0b10110100);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
