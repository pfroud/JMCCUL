package examples.analog_output;

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
public class AnalogOutputExample {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) throws JMCCULException {

        final Predicate<DaqDevice> predicate = d -> d.analogOutput.isAnalogOutputSupported();
        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findDeviceMatchingPredicate(predicate);
        if (optionalDevice.isEmpty()) {
            System.out.println("Didn't find a device which supports analog output.");
            return;
        }

        final DaqDevice device = optionalDevice.get();

        System.out.println("Opened this device: " + device.toString());

        // TODO set differential vs single-ended!!
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

}
