package examples.analog_input;

import java.util.Optional;
import java.util.function.Predicate;
import jmccul.DaqDevice;
import jmccul.DeviceDiscovery;
import jmccul.JMCCULException;
import jmccul.analog.AnalogRange;

/**
 *
 * @author Peter Froud
 */
public class AnalogInputExample {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) throws JMCCULException {

        final Predicate<DaqDevice> predicate = d -> d.analogInput.isAnalogInputSupported();
        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findDeviceMatchingPredicate(predicate);
        if (optionalDevice.isEmpty()) {
            System.out.println("Didn't find a device which supports analog input.");
            return;
        }

        final DaqDevice device = optionalDevice.get();
        System.out.println("Opened this device: " + device.toString());
        // TODO set differential vs single-ended!!
        doAnalogInput(device);

        device.close();

    }

    private static void doAnalogInput(DaqDevice device) throws JMCCULException {

        final AnalogRange rangeToUse = device.analogInput.SUPPORTED_RANGES.get(0);
        System.out.println("Using thiss range: " + rangeToUse);

        System.out.println("Reading the raw ADC value:");
        System.out.println(device.analogInput.analogInput(0, rangeToUse));
        System.out.println(device.analogInput.analogInput32(0, rangeToUse));

        System.out.println("Reading it as voltage:");
        System.out.println(device.analogInput.voltageInput(0, rangeToUse));
        System.out.println(device.analogInput.voltageInput32(0, rangeToUse));

    }

}
