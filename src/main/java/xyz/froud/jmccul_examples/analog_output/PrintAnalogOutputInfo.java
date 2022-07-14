package xyz.froud.jmccul_examples.analog_output;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;

import java.util.Optional;

/**
 * @author Peter Froud
 */
public class PrintAnalogOutputInfo {

    public static void main(String[] args) throws JMCCULException {

        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findFirstDeviceMatching(
                d -> d.analogOutput.isAnalogOutputSupported()
        );

        if (optionalDevice.isPresent()) {
            try (DaqDevice device = optionalDevice.get()) {
                System.out.println("Analog output info for this device: " + device);
                System.out.println("getDacChannelCount = " + device.analogOutput.getDacChannelCount());
                System.out.println("getDacResolution = " + device.analogOutput.getDacResolution());
                System.out.println("getSupportedRanges = " + device.analogOutput.getSupportedRanges());
                System.out.println("isVoltageOutputSupported = " + device.analogOutput.isVoltageOutputSupported());
            }
        } else {
            System.out.println("Didn't find a device which supports analog input.");
        }

    }

}
