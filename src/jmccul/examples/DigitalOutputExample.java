package jmccul.examples;

import java.util.Arrays;
import java.util.Optional;
import jmccul.DaqDevice;
import jmccul.DeviceDiscovery;
import jmccul.JMCCULException;
import jmccul.digital.DigitalPort;
import jmccul.digital.DigitalPortDirection;
import jmccul.jna.DaqDeviceDescriptor;

/**
 *
 * @author Peter Froud
 */
public class DigitalOutputExample {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) throws JMCCULException {

        Optional<DeviceAndPort> thing = findDeviceAndPortWhichSupportDigitalOutput();

        if (thing.isPresent()) {
            DeviceAndPort dap = thing.get();
            DaqDevice device = dap.device;
            DigitalPort port = dap.port;

            System.out.printf("Using device with model name %s, serial number %s, port %s.\n",
                    device.BOARD_NAME, device.FACTORY_SERIAL_NUMBER, port.PORT_TYPE);

            doDigitalOutput(device, port);
            device.close();
        } else {
            System.out.println("didn't find a device supporting it");
        }

    }

    private static void doDigitalOutput(DaqDevice device, DigitalPort port) throws JMCCULException {

        if (port.IS_PORT_CONFIGURABLE) {
            device.digital.configurePort(port.PORT_TYPE, DigitalPortDirection.OUTPUT);
        }

        //                                      eight bits: 76453210
        device.digital.outputPort(port.PORT_TYPE, (short) 0b10110110);

        for (int bitIdx = 0; bitIdx < port.NUM_BITS; bitIdx++) {
            device.digital.outputBit(port.PORT_TYPE, bitIdx, true);
        }

    }

    private static class DeviceAndPort {

        DaqDevice device;
        DigitalPort port;

    }

    @SuppressWarnings("ConvertToTryWithResources")
    private static Optional<DeviceAndPort> findDeviceAndPortWhichSupportDigitalOutput() throws JMCCULException {
        var descrs = DeviceDiscovery.findDaqDeviceDescriptors();
        for (DaqDeviceDescriptor descr : descrs) {
            DaqDevice device = new DaqDevice(descr);
            if (device.digital.isDigitalIOSupported()) {
                final Optional<DigitalPort> optionalPortToUse
                        = Arrays.stream(device.digital.PORTS)
                                .filter(port -> port.IS_OUTPUT_SUPPORTED)
                                .findAny();
                if (optionalPortToUse.isPresent()) {
                    DeviceAndPort rv = new DeviceAndPort();
                    rv.device = device;
                    rv.port = optionalPortToUse.get();
                    return Optional.of(rv);
                }
            }
            device.close();
        }
        return Optional.empty();
    }

}
