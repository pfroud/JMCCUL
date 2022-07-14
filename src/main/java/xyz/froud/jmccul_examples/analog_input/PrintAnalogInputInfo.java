package xyz.froud.jmccul_examples.analog_input;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;

import java.util.Optional;

/**
 * @author Peter Froud
 */
public class PrintAnalogInputInfo {

    public static void main(String[] args) throws JMCCULException {

        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findFirstDeviceMatching(
                d -> d.analogInput.isAnalogInputSupported()
        );

        if (optionalDevice.isPresent()) {
            try (DaqDevice device = optionalDevice.get()) {
                System.out.println("Analog input info for this device: " + device);
                System.out.println("getAdcChannelCount = " + device.analogInput.getAdcChannelCount());
                System.out.println("getResolution = " + device.analogInput.getResolution());
                System.out.println("getPacketSize = " + device.analogInput.getPacketSize());
                System.out.println("getTriggerResolution = " + device.analogInput.getAnalogTriggerResolution());
                System.out.println("getSupportedRanges = " + device.analogInput.getSupportedRanges());
                System.out.println("getAnalogTriggerRange = " + device.analogInput.getAnalogTriggerRange());
                System.out.println("isVoltageInputSupported = " + device.analogInput.isVoltageInputSupported());
                System.out.println("isAnalogTriggerSupported = " + device.analogInput.isAnalogTriggerSupported());
                System.out.println("isGainQueueSupported = " + device.analogInput.isGainQueueSupported());
                System.out.println("isScanSupported = " + device.analogInput.isScanSupported());
            }
        } else {
            System.out.println("Didn't find a device which supports analog input.");
        }

    }

}
