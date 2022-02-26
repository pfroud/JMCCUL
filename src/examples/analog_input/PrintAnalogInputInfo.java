package examples.analog_input;

import java.util.Optional;
import java.util.function.Predicate;
import jmccul.DaqDevice;
import jmccul.DeviceDiscovery;
import jmccul.JMCCULException;

/**
 * @author Peter Froud
 */
public class PrintAnalogInputInfo {

    public static void main(String[] args) throws JMCCULException {

        final Predicate<DaqDevice> predicate = d -> d.analogInput.isAnalogInputSupported();
        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findDeviceMatchingPredicate(predicate);
        if (optionalDevice.isEmpty()) {
            System.out.println("Didn't find a device which supports analog input.");
            return;
        }

        try (DaqDevice device = optionalDevice.get()) {
            System.out.println("Analog input info for this device: " + device.toString());
            System.out.println("CHANNEL_COUNT = " + device.analogInput.CHANNEL_COUNT);
            System.out.println("RESOLUTION = " + device.analogInput.RESOLUTION);
            System.out.println("PACKET_SIZE = " + device.analogInput.PACKET_SIZE);
            System.out.println("TRIGGER_RESOLUTION = " + device.analogInput.TRIGGER_RESOLUTION);
            System.out.println("SUPPORTED_RANGES = " + device.analogInput.SUPPORTED_RANGES);
            System.out.println("ANALOG_TRIGGER_RANGE = " + device.analogInput.ANALOG_TRIGGER_RANGE);
            System.out.println("IS_VOLTAGE_INPUT_SUPPORTED = " + device.analogInput.IS_VOLTAGE_INPUT_SUPPORTED);
            System.out.println("IS_ANALOG_TRIGGER_SUPPORTED = " + device.analogInput.IS_ANALOG_TRIGGER_SUPPORTED);
            System.out.println("IS_GAIN_QUEUE_SUPPORTED = " + device.analogInput.IS_GAIN_QUEUE_SUPPORTED);
            System.out.println("IS_SCAN_SUPPORTED = " + device.analogInput.IS_SCAN_SUPPORTED);
        }

    }

}
