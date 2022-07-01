package xyz.froud.jmccul_examples.temperature;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Peter Froud
 */
public class PrintTemperatureInfo {

    public static void main(String[] args) throws JMCCULException {

        final Predicate<DaqDevice> predicate = d -> d.temperature.isTemperatureInputSupported();
        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findDeviceMatchingPredicate(predicate);
        if (optionalDevice.isEmpty()) {
            System.out.println("Didn't find a device which supports temperature input.");
            return;
        }

        try (DaqDevice device = optionalDevice.get()) {
            System.out.println("Temperature input info for this device: " + device);

            System.out.println("CHANNEL_COUNT = " + device.temperature.CHANNEL_COUNT);

        }

    }

}
