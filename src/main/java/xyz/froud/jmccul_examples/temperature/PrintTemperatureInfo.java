package xyz.froud.jmccul_examples.temperature;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;

import java.util.Optional;

/**
 * @author Peter Froud
 */
public class PrintTemperatureInfo {

    public static void main(String[] args) throws JMCCULException {

        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findFirstDeviceMatching(
                d -> d.temperature.isTemperatureInputSupported()
        );

        if (optionalDevice.isPresent()) {
            try (DaqDevice device = optionalDevice.get()) {
                System.out.println("Temperature input info for this device: " + device);
                System.out.println("CHANNEL_COUNT = " + device.temperature.getChannelCount());
            }
        } else {
            System.out.println("Didn't find a device which supports temperature input.");
        }

    }

}
