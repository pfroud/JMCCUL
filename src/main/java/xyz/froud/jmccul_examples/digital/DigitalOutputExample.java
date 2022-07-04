package xyz.froud.jmccul_examples.digital;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.digital.DigitalPort;
import xyz.froud.jmccul.digital.DigitalPortDirection;
import xyz.froud.jmccul.jna.DaqDeviceDescriptor;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Peter Froud
 */
public class DigitalOutputExample {

    public static void main(String[] args) throws JMCCULException {

        final Optional<DeviceAndDigitalPort> optionalDeviceAndPort = findDeviceAndPortWhichSupportDigitalOutput();

        if (optionalDeviceAndPort.isPresent()) {
            try (DeviceAndDigitalPort deviceAndPort = optionalDeviceAndPort.get()) {
                final DaqDevice device = deviceAndPort.device;
                final DigitalPort port = deviceAndPort.port;

                System.out.printf("Using device with model name %s, serial number %s, port %s.\n",
                        device.getBoardName(), device.getFactorySerialNumber(), port.getPortType());

                doDigitalOutput(device, port);
            }
        } else {
            System.out.println("Didn't find a device which supports digital output.");
        }

    }

    private static void doDigitalOutput(DaqDevice device, DigitalPort port) throws JMCCULException {

        if (port.isPortConfigurable()) {
            device.digital.configurePort(port.getPortType(), DigitalPortDirection.OUTPUT);
        }

        //                       eight bits: 76453210
        final short valueToWrite = (short) 0b10110110;
        System.out.println("Writing this value to the whole port: 0b" + Integer.toBinaryString(valueToWrite));
        device.digital.outputPort(port.getPortType(), valueToWrite);

        System.out.println("Now setting each bit on individually");
        for (int bitIdx = 0; bitIdx < port.getBitCount(); bitIdx++) {
            device.digital.outputBit(port.getPortType(), bitIdx, true);
        }

    }

    private static Optional<DeviceAndDigitalPort> findDeviceAndPortWhichSupportDigitalOutput() throws JMCCULException {
        final DaqDeviceDescriptor[] allDeviceDescriptors = DeviceDiscovery.findDescriptors();

        for (DaqDeviceDescriptor descriptor : allDeviceDescriptors) {
            final DaqDevice device = new DaqDevice(descriptor);

            if (device.digital.isDigitalIOSupported()) {

                final Optional<DigitalPort> optionalPortToUse
                        = Arrays.stream(device.digital.getPorts())
                        .filter(DigitalPort::isOutputSupported)
                        .findFirst();

                if (optionalPortToUse.isPresent()) {
                    DeviceAndDigitalPort rv = new DeviceAndDigitalPort();
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
