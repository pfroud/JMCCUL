package xyz.froud.jmccul_examples.digital;

import java.util.Arrays;
import java.util.Optional;
import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.digital.DigitalPort;
import xyz.froud.jmccul.digital.DigitalPortDirection;
import xyz.froud.jmccul.jna.DaqDeviceDescriptor;

/**
 *
 * @author Peter Froud
 */
public class DigitalOutputExample {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) throws JMCCULException {

        final Optional<DeviceAndPort> optionalDeviceAndPort = findDeviceAndPortWhichSupportsDigitalOutput();

        if (optionalDeviceAndPort.isPresent()) {
            final DeviceAndPort deviceAndPort = optionalDeviceAndPort.get();
            final DaqDevice device = deviceAndPort.device;
            final DigitalPort port = deviceAndPort.port;

            System.out.printf("Using device with model name %s, serial number %s, port %s.\n",
                    device.BOARD_NAME, device.FACTORY_SERIAL_NUMBER, port.PORT_TYPE);

            doDigitalOutput(device, port);
            device.close();
        } else {
            System.out.println("Didn't find a device which supports digital output.");
        }

    }

    private static void doDigitalOutput(DaqDevice device, DigitalPort port) throws JMCCULException {

        if (port.IS_PORT_CONFIGURABLE) {
            device.digital.configurePort(port.PORT_TYPE, DigitalPortDirection.OUTPUT);
        }

        //                       eight bits: 76453210
        final short valueToWrite = (short) 0b10110110;
        System.out.println("Writing this value to the whole port: 0b" + Integer.toBinaryString(valueToWrite));
        device.digital.outputPort(port.PORT_TYPE, valueToWrite);

        System.out.println("Now setting each bit on individually");
        for (int bitIdx = 0; bitIdx < port.NUM_BITS; bitIdx++) {
            device.digital.outputBit(port.PORT_TYPE, bitIdx, true);
        }

    }

    private static class DeviceAndPort {

        DaqDevice device;
        DigitalPort port;

    }

    @SuppressWarnings("ConvertToTryWithResources")
    private static Optional<DeviceAndPort> findDeviceAndPortWhichSupportsDigitalOutput() throws JMCCULException {
        final DaqDeviceDescriptor[] allDeviceDescriptors = DeviceDiscovery.findDaqDeviceDescriptors();

        for (DaqDeviceDescriptor descriptor : allDeviceDescriptors) {
            final DaqDevice device = new DaqDevice(descriptor);

            if (device.digital.isDigitalIOSupported()) {

                final Optional<DigitalPort> optionalPortToUse
                        = Arrays.stream(device.digital.PORTS)
                                .filter(port -> port.IS_OUTPUT_SUPPORTED)
                                .findFirst();

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
