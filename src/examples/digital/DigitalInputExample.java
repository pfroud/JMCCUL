package examples.digital;

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
public class DigitalInputExample {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) throws JMCCULException {

        final Optional<DeviceAndPort> optionalDeviceAndPort = findDeviceAndPortWhichSupportsDigitalInput();

        if (optionalDeviceAndPort.isPresent()) {
            final DeviceAndPort deviceAndPort = optionalDeviceAndPort.get();
            final DaqDevice device = deviceAndPort.device;
            final DigitalPort port = deviceAndPort.port;

            System.out.printf("Using device with model name %s, serial number %s, port %s.\n",
                    device.BOARD_NAME, device.FACTORY_SERIAL_NUMBER, port.PORT_TYPE);

            doDigitalInput(device, port);
            device.close();
        } else {
            System.out.println("Didn't find a device which supports digital input.");
        }

    }

    private static void doDigitalInput(DaqDevice device, DigitalPort port) throws JMCCULException {

        if (port.IS_PORT_CONFIGURABLE) {
            device.digital.configurePort(port.PORT_TYPE, DigitalPortDirection.INPUT);
        }

        System.out.println("Reading the whole port:");
        final short portInput = device.digital.inputPort(port.PORT_TYPE);
        System.out.println("0b" + Integer.toBinaryString(portInput));

        System.out.println("Reading one bit at a time:");
        System.out.print("0b");
        for (int bitIndex = port.NUM_BITS - 1; bitIndex >= 0; bitIndex--) {
            boolean bitInput = device.digital.inputBit(port.PORT_TYPE, bitIndex);
            final char zeroOrOne = bitInput ? '1' : '0';
            System.out.print(zeroOrOne);
        }
        System.out.println();

    }

    private static class DeviceAndPort {

        DaqDevice device;
        DigitalPort port;

    }

    @SuppressWarnings("ConvertToTryWithResources")
    private static Optional<DeviceAndPort> findDeviceAndPortWhichSupportsDigitalInput() throws JMCCULException {
        final DaqDeviceDescriptor[] allDeviceDescriptors = DeviceDiscovery.findDaqDeviceDescriptors();

        for (DaqDeviceDescriptor descriptor : allDeviceDescriptors) {
            final DaqDevice device = new DaqDevice(descriptor);

            if (device.digital.isDigitalIOSupported()) {

                final Optional<DigitalPort> optionalPortToUse
                        = Arrays.stream(device.digital.PORTS)
                                .filter(port -> port.IS_INPUT_SUPPORTED)
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
