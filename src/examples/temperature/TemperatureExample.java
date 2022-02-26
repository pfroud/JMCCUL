package examples.temperature;

import java.util.Optional;
import java.util.function.Predicate;
import jmccul.DaqDevice;
import jmccul.DeviceDiscovery;
import jmccul.JMCCULException;
import jmccul.jna.MeasurementComputingUniversalLibrary;
import jmccul.temperature.TemperatureScale;

/**
 *
 * @author Peter Froud
 */
public class TemperatureExample {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) throws JMCCULException {

        final Predicate<DaqDevice> predicate = d -> d.temperature.isTemperatureInputSupported();
        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findDeviceMatchingPredicate(predicate);
        if (optionalDevice.isEmpty()) {
            System.out.println("Didn't find a device which supports temperature input.");
            return;
        }

        final DaqDevice device = optionalDevice.get();
        System.out.println("Opened this device: " + device.toString());

        doTemperatureInput(device);

        device.close();

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
