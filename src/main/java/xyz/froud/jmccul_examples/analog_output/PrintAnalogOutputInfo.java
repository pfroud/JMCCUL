package xyz.froud.jmccul_examples.analog_output;

import java.util.Optional;
import java.util.function.Predicate;
import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;

/**
 * @author Peter Froud
 */
public class PrintAnalogOutputInfo {

    public static void main(String[] args) throws JMCCULException {

        final Predicate<DaqDevice> predicate = d -> d.analogOutput.isAnalogOutputSupported();
        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findDeviceMatchingPredicate(predicate);
        if (optionalDevice.isEmpty()) {
            System.out.println("Didn't find a device which supports analog input.");
            return;
        }

        try (DaqDevice device = optionalDevice.get()) {
            System.out.println("Analog output info for this device: " + device.toString());
            System.out.println("CHANNEL_COUNT = " + device.analogOutput.CHANNEL_COUNT);
            System.out.println("RESOLUTION = " + device.analogOutput.RESOLUTION);
            System.out.println("SUPPORTED_RANGES = " + device.analogOutput.SUPPORTED_RANGES);
            System.out.println("IS_VOLTAGE_OUTPUT_SUPPORTED = " + device.analogOutput.IS_VOLTAGAE_OUTPUT_SUPPORTED);

        }

    }

}
