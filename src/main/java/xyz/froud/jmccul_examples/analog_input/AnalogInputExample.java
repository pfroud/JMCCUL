package xyz.froud.jmccul_examples.analog_input;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.analog.AnalogRange;

import java.util.Optional;

/**
 * @author Peter Froud
 */
public class AnalogInputExample {

    public static void main(String[] args) throws JMCCULException {

        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findFirstDeviceMatching(
                d -> d.analogInput.isAnalogInputSupported()
        );

        if (optionalDevice.isPresent()) {
            try (DaqDevice device = optionalDevice.get()) {
                System.out.println("Opened this device: " + device);
                // TODO set differential vs single-ended!!
                doAnalogInput(device);
            }
        } else {
            System.out.println("Didn't find a device which supports analog input.");
        }

    }

    private static void doAnalogInput(DaqDevice device) throws JMCCULException {

        final AnalogRange rangeToUse = device.analogInput.getSupportedRanges().get(0);
        System.out.println("Using this range: " + rangeToUse);

        System.out.println("Reading the raw ADC value:");
        System.out.println(device.analogInput.analogInput(0, rangeToUse));
        System.out.println(device.analogInput.analogInput32(0, rangeToUse));

        System.out.println("Reading it as voltage:");
        System.out.println(device.analogInput.voltageInput(0, rangeToUse));
        System.out.println(device.analogInput.voltageInput32(0, rangeToUse));

    }

}
