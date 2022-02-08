package jmccul.examples.analog_output;

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
public class AnalogOutputExample {

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

        doAnalogOutput(device);
        doVoltageOutput(device);

        device.close();

    }

    private static void doAnalogOutput(DaqDevice device) throws JMCCULException {

        final AnalogRange rangeToUse = device.analogOutput.SUPPORTED_RANGES.get(0);

        final int max = (1 << device.analogOutput.RESOLUTION) - 1;
        final int middle = (int) Math.round(max / 2.0);

        device.analogOutput.analogOutput(0, rangeToUse, (short) 0);
        delay(1000);
        device.analogOutput.analogOutput(0, rangeToUse, (short) middle);
        delay(1000);
        device.analogOutput.analogOutput(0, rangeToUse, (short) max);
        delay(1000);
        device.analogOutput.analogOutput(0, rangeToUse, (short) middle);
        delay(1000);
        device.analogOutput.analogOutput(0, rangeToUse, (short) 0);

    }

    private static void doVoltageOutput(DaqDevice device) throws JMCCULException {

        final AnalogRange rangeToUse = device.analogOutput.SUPPORTED_RANGES.get(0);
        final double middle = ((rangeToUse.MAXIMUM - rangeToUse.MINIMUM) / 2.0) + rangeToUse.MINIMUM;

        device.analogOutput.voltageOutput(0, rangeToUse, (float) rangeToUse.MINIMUM);
        delay(1000);
        device.analogOutput.voltageOutput(0, rangeToUse, (float) middle);
        delay(1000);
        device.analogOutput.voltageOutput(0, rangeToUse, (float) rangeToUse.MAXIMUM);
        delay(1000);
        device.analogOutput.voltageOutput(0, rangeToUse, (float) middle);
        delay(1000);
        device.analogOutput.voltageOutput(0, rangeToUse, (float) rangeToUse.MINIMUM);

    }

    private static void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("ConvertToTryWithResources")
    private static Optional<DaqDevice> findDeviceWhichSupportsAnalogOutput() throws JMCCULException {
        final DaqDeviceDescriptor[] deviceDescriptors = DeviceDiscovery.findDaqDeviceDescriptors();

        for (DaqDeviceDescriptor descriptor : deviceDescriptors) {
            final DaqDevice device = new DaqDevice(descriptor);

            if (device.analogOutput.isAnalogOutputSupported()) {
                return Optional.of(device);
            } else {
                device.close();
            }
        }

        return Optional.empty();
    }

}
