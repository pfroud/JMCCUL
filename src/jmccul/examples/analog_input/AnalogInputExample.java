package jmccul.examples.analog_input;

import java.util.Optional;
import java.util.function.Predicate;
import jmccul.DaqDevice;
import jmccul.DeviceDiscovery;
import jmccul.JMCCULException;
import jmccul.analog.AnalogRange;
import jmccul.jna.DaqDeviceDescriptor;

/**
 *
 * @author Peter Froud
 */
public class AnalogInputExample {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) throws JMCCULException {

        /*
        In my test setup:
        I am using the device with serial number 1AC198E as analog input in Daqami.
        So, I need to use the device with serial number 1AC1968 to do analog output in this java program.
         */
        Predicate<DaqDeviceDescriptor> predicate = desc -> desc.NUID == 0x1AC1968;
        Optional<DaqDeviceDescriptor> optionalDesc = DeviceDiscovery.findFirstDescriptorMatching(predicate);

        if (optionalDesc.isEmpty()) {
            System.out.println("No description found with that serial number");
            return;
        }

        DaqDevice device = new DaqDevice(optionalDesc.get());

        System.out.println("Opened this device: " + device.toString());

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
