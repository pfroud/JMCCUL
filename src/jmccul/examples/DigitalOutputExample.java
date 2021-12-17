package jmccul.examples;

import java.util.Arrays;
import java.util.Optional;
import jmccul.devices2.DaqDevice;
import jmccul.devices2.DigitalPort;
import jmccul.devices2.DigitalPortDirection;

/**
 * https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/examples/console/digital_out.py
 *
 * @author Peter Froud
 */
public class DigitalOutputExample {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) {

        try {

            DaqDevice device = DaqDevice.searchByBoardName("USB-1208FS");

            System.out.println("Opened this device: " + device.toString());

            if (!device.digital.isDigitalIOSupported()) {
                System.out.println("Digital IO is not supported");
                return;
            }

            DigitalPort[] ports = device.digital.PORTS;

            System.out.printf("There are %d port(s):\n", ports.length);
            for (int i = 0; i < ports.length; i++) {
                DigitalPort port = ports[i];
                System.out.printf("Port %d / %d: %s\n", i + 1, ports.length, port.toString());
                System.out.println("   NUM_BITS = " + port.NUM_BITS);
                System.out.println("   INPUT_MASK = " + port.INPUT_MASK);
                System.out.println("   OUTPUT_MASK = " + port.OUTPUT_MASK);
                System.out.println("   FIRST_BIT = " + port.FIRST_BIT);
                System.out.println("   IS_OUTPUT_SUPPORTED  = " + port.IS_OUTPUT_SUPPORTED);
                System.out.println("   IS_INPUT_SUPPORTED   = " + port.IS_INPUT_SUPPORTED);
                System.out.println("   IS_PORT_CONFIGURABLE = " + port.IS_PORT_CONFIGURABLE);
                System.out.println("   IS_BIT_CONFIGURABLE  = " + port.IS_BIT_CONFIGURABLE);
                System.out.println("   IS_INPUT_SCAN_SUPPORTED  = " + port.IS_INPUT_SCAN_SUPPORTED);
                System.out.println("   IS_OUTPUT_SCAN_SUPPORTED = " + port.IS_OUTPUT_SCAN_SUPPORTED);
            }

            Optional<DigitalPort> optionalPortToUse = Arrays.stream(ports)
                    .filter(port -> port.IS_OUTPUT_SUPPORTED).findAny();

            if (optionalPortToUse.isEmpty()) {
                System.out.println("none of the ports support digital output!");
                return;
            }

            DigitalPort portToUse = optionalPortToUse.get();
            System.out.println("Using this port: " + portToUse);

            device.digital.configurePort(portToUse.PORT_TYPE, DigitalPortDirection.OUTPUT);

            device.digital.bitOutput(portToUse.PORT_TYPE, 0, true);
            System.out.println("Success");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
