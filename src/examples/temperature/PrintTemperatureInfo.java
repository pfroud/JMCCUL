package examples.temperature;

import java.util.Optional;
import java.util.function.Predicate;
import jmccul.DaqDevice;
import jmccul.DeviceDiscovery;
import jmccul.JMCCULException;

/**
 *
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
            System.out.println("Temperature input info for this device: " + device.toString());

            System.out.println("CHANNEL_COUNT = " + device.temperature.CHANNEL_COUNT);

        }

    }

}
