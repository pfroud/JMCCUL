package xyz.froud.jmccul_examples;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.counter.CounterDevice;
import xyz.froud.jmccul.digital.DigitalPort;
import xyz.froud.jmccul.DaqDeviceDescriptor;

/**
 *
 * This example has been tested on:
 * <ul>
 * <li>USB-1608GX-2AO</li>
 * <li>USB-1408FS-Plus</li>
 * <li>USB-1208FS</li>
 * <li>USB-TC</li>
 * <li>USB-2001-TC</li>
 * </ul>
 *
 * @author Peter Froud
 */
public class PrintDeviceCapabilities {

    public static void main(String[] args) throws JMCCULException {
        final DaqDeviceDescriptor[] descriptors = DaqDeviceDescriptor.find();
        if (descriptors.length == 0) {
            System.out.println("No DAQ devices found.");
        } else {
            System.out.printf("Found %d DAQ device descriptors(s).\n\n", descriptors.length);
            for (int i = 0; i < descriptors.length; i++) {
                System.out.printf("Opening descriptor %d / %d\n", i + 1, descriptors.length);
                System.out.println(descriptors[i]);
                try ( DaqDevice device = new DaqDevice(descriptors[i])) {
                    printDigitalInfo(device);
                    printAnalogInputInfo(device);
                    printAnalogOutputInfo(device);
                    printCounterInfo(device);
                    printTemperatureInfo(device);
                    printExpansionInfo(device);
                }
                System.out.println();
                System.out.println();
            }
        }
    }

    private static void printDigitalInfo(DaqDevice device) throws JMCCULException {
        if (device.digital.isSupported()) {
            System.out.println("  Digital I/O");
            final DigitalPort[] ports = device.digital.getPorts();
            System.out.printf("    The DAQ device has %d digital port(s):\n", ports.length);
            for (int i = 0; i < ports.length; i++) {
                final DigitalPort port = ports[i];
                System.out.printf("      Port %d / %d is type %s\n", i + 1, ports.length, port);
                System.out.println("        getResolution = " + port.getResolution());
                System.out.println("        getInputMask  = " + port.getInputMask());
                System.out.println("        getOutputMask = " + port.getOutputMask());
                System.out.println("        getFirstBit   = " + port.getFirstBit());
                System.out.println("        isOutputSupported  = " + port.isOutputSupported());
                System.out.println("        isInputSupported   = " + port.isInputSupported());
                System.out.println("        isDirectionOfEntirePortSettable     = " + port.isDirectionOfEntirePortSettable());
                System.out.println("        isDirectionOfIndividualBitSettable  = " + port.isDirectionOfIndividualBitSettable());
                System.out.println("        isInputScanSupported  = " + port.isInputScanSupported());
                System.out.println("        isOutputScanSupported = " + port.isOutputScanSupported());
            }
        } else {
            System.out.println("  Digital I/O is not supported on this device.");
        }
    }

    private static void printAnalogOutputInfo(DaqDevice device) throws JMCCULException {
        if (device.analog.output.isSupported()) {
            System.out.println("  Analog output");
            System.out.println("    getChannelCount          = " + device.analog.output.getChannelCount());
            System.out.println("    getResolution            = " + device.analog.output.getResolution());
            System.out.println("    getSupportedRanges       = " + device.analog.output.getSupportedRanges());
            System.out.println("    isVoltageOutputSupported = " + device.analog.output.isVoltageOutputSupported());
        } else {

            System.out.println("  Analog output is not supported on this device.");
        }
    }

    private static void printAnalogInputInfo(DaqDevice device) throws JMCCULException {
        if (device.analog.input.isSupported()) {
            System.out.println("  Analog input");
            System.out.println("    getChannelCount          = " + device.analog.input.getChannelCount());
            System.out.println("    getResolution            = " + device.analog.input.getResolution());
            System.out.println("    getPacketSize            = " + device.analog.input.getPacketSize());
            System.out.println("    getTriggerResolution     = " + device.analog.input.getTriggerResolution());
            System.out.println("    getSupportedRanges       = " + device.analog.input.getSupportedRanges());
            System.out.println("    getTriggerRange          = " + device.analog.input.getTriggerRange());
            System.out.println("    isVoltageInputSupported  = " + device.analog.input.isVoltageInputSupported());
            System.out.println("    isAnalogTriggerSupported = " + device.analog.input.isTriggerSupported());
            System.out.println("    isGainQueueSupported     = " + device.analog.input.isGainQueueSupported());
            System.out.println("    isScanSupported          = " + device.analog.input.isScanSupported());
        } else {
            System.out.println("  Analog input is not supported on this device.");

        }
    }

    private static void printCounterInfo(DaqDevice device) throws JMCCULException {
        if (device.counter.isSupported()) {
            System.out.println("  Counter");
            final CounterDevice[] counters = device.counter.getDevices();
            System.out.printf("    The DAQ device has %d counter device(s):\n", counters.length);
            for (int i = 0; i < counters.length; i++) {
                final CounterDevice counter = counters[i];
                System.out.printf("      Counter %d / %d is type %s \n",
                        i + 1, counters.length, counter.getCounterType());
            }
        } else {
            System.out.println("  This DAQ device does not have any counter devices.");

        }
    }

    private static void printTemperatureInfo(DaqDevice device) throws JMCCULException {
        if (device.temperature.isSupported()) {
            System.out.println("  Temperature");
            System.out.println("    getChannelCount = " + device.temperature.getChannelCount());
        } else {

            System.out.println("  Temperature input is not supported on this DAQ device.");
        }
    }

    private static void printExpansionInfo(DaqDevice device) throws JMCCULException {
        if (device.expansionConfig.isExpansionBoardSupported()) {
            System.out.println("  Expansion");
            System.out.println("    getMaxExpansionBoardCount = " + device.expansionConfig.getMaxExpansionBoardCount());
        } else {

            System.out.println("  Expansion boards are not supported on this DAQ device.");
        }
    }

}
