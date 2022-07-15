package xyz.froud.jmccul_examples.temperature;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;
import xyz.froud.jmccul.temperature.TemperatureScale;

import java.util.Optional;

/**
 * @author Peter Froud
 */
public class TemperatureExample {

    public static void main(String[] args) throws JMCCULException {

        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findFirstDeviceMatching(
                d -> d.temperature.isTemperatureInputSupported()
        );

        if (optionalDevice.isPresent()) {
            try (DaqDevice device = optionalDevice.get()) {
                System.out.println("Opened this device: " + device);
                doTemperatureInput(device);
            }
        } else {
            System.out.println("Didn't find a device which supports temperature input.");
        }

    }

    private static void doTemperatureInput(DaqDevice device) throws JMCCULException {

        // TODO set thermocouple type!!!!
        try {
            System.out.printf("Temperature is %f C\n",
                    device.temperature.readTemperature(0, TemperatureScale.CELSIUS));
        } catch (JMCCULException ex) {
            if (ex.ERROR_CODE == MeasurementComputingUniversalLibrary.OPENCONNECTION) {
                System.out.println("The thermocouple connection is open.");
            } else {
                throw ex;
            }

        }

    }

}
