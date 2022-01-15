package jmccul.examples.analogOutput;

import java.util.Optional;
import jmccul.DaqDevice;
import jmccul.DeviceDiscovery;
import jmccul.JMCCULException;
import jmccul.analog.AnalogRange;
import jmccul.jna.DaqDeviceDescriptor;

/**
 *
 * @author Peter Froud
 */
public class AnalogOutputExample {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) throws JMCCULException {

        Optional<DaqDevice> thing = findDeviceWhichSupportsAnalogOutput();

        if (thing.isPresent()) {
            DaqDevice device = thing.get();
            System.out.printf("Using device with model name %s, serial number %s.\n",
                    device.BOARD_NAME, device.FACTORY_SERIAL_NUMBER);

            doAnalogOutput(device);
            device.close();
        } else {
            System.out.println("didn't find a device supporting it");
        }

    }

    private static void doAnalogOutput(DaqDevice device) throws JMCCULException {

        AnalogRange rangeToUse = device.analogOutput.SUPPORTED_RANGES.get(0);

        device.analogOutput.analogOutput(0, rangeToUse, (short) 0);

    }

    @SuppressWarnings("ConvertToTryWithResources")
    private static Optional<DaqDevice> findDeviceWhichSupportsAnalogOutput() throws JMCCULException {
        final DaqDeviceDescriptor[] deviceDescriptors = DeviceDiscovery.findDaqDeviceDescriptors();

        for (DaqDeviceDescriptor descriptor : deviceDescriptors) {
            final DaqDevice device = new DaqDevice(descriptor);

            if (device.analogOutput.isAnalogOutputSupported()) {
                return Optional.of(device);
            } else {
                device.close();
            }
        }

        return Optional.empty();
    }

}
