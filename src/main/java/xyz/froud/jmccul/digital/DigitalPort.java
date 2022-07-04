package xyz.froud.jmccul.digital;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.nio.ShortBuffer;

/**
 * Adapted from a subclass of https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py
 *
 * @author Peter Froud
 */
public class DigitalPort {

    private final DaqDevice DAQ_DEVICE;
    public final int PORT_INDEX;

    private DigitalPortType portType;
    private Integer bitCount;
    private Integer inputMask;
    private Integer outputMask;
    private Boolean isPortConfigurable;
    private Boolean isIndividualBitConfigurable;
    private Boolean isInputScanSupported;
    private Boolean isOutputScanSupported;

    DigitalPort(DaqDevice device, int portIndex) {
        DAQ_DEVICE = device;
        PORT_INDEX = portIndex;
    }

    @Override
    public String toString() {
        return portType.name();
    }

    public DigitalPortType getPortType() throws JMCCULException {
        if (portType == null) {
            final int portTypeInt = getConfigItem(MeasurementComputingUniversalLibrary.DIDEVTYPE);
            portType = DigitalPortType.parseInt(portTypeInt);
        }
        return portType;
    }

    public int getBitCount() throws JMCCULException {
        if (bitCount == null) {
            bitCount = getConfigItem(MeasurementComputingUniversalLibrary.DINUMBITS);
        }
        return bitCount;
    }

    public int getInputMask() throws JMCCULException {

        /*
        https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm
        Use the DIINMASK and DIOUTMASK options to determine if an AUXPORT is configurable.
        Execute cbGetConfig() twice to the same port – once using DIINMASK and once using DIOUTMASK.
        If both of the ConfigValue arguments returned have input and output bits that overlap,
        the port is not configurable.

        You can determine overlapping bits by Anding both arguments.

        Example: for a device with seven bits of digital I/O (four outputs and three inputs),
        the ConfigValue returned by DIINMASK is always 7 (0000 0111),
        while the ConfigValue argument returned by DIOUTMASK is always 15 (0000 1111).
        When you And both ConfigValue arguments together, you get a non-zero number (7).
        Any non-zero number indicates that input and output bits overlap for the specified port,
        and the port is a non-configurable AUXPORT.
         */
        if (inputMask == null) {
            inputMask = getConfigItem(MeasurementComputingUniversalLibrary.DIINMASK);
        }
        return inputMask;
    }

    public int getOutputMask() throws JMCCULException {
        /*
        https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm
        Use the DIINMASK and DIOUTMASK options to determine if an AUXPORT is configurable.
        Execute cbGetConfig() twice to the same port – once using DIINMASK and once using DIOUTMASK.
        If both of the ConfigValue arguments returned have input and output bits that overlap,
        the port is not configurable.

        You can determine overlapping bits by Anding both arguments.

        Example: for a device with seven bits of digital I/O (four outputs and three inputs),
        the ConfigValue returned by DIINMASK is always 7 (0000 0111),
        while the ConfigValue argument returned by DIOUTMASK is always 15 (0000 1111).
        When you And both ConfigValue arguments together, you get a non-zero number (7).
        Any non-zero number indicates that input and output bits overlap for the specified port,
        and the port is a non-configurable AUXPORT.
         */
        if (outputMask == null) {
            outputMask = getConfigItem(MeasurementComputingUniversalLibrary.DIOUTMASK);
        }
        return outputMask;
    }

    private int getConfigItem(int item) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.DIGITALINFO,
                DAQ_DEVICE.getBoardNumber(),
                PORT_INDEX,
                item
        );
    }

    public int getFirstBit() {
        /*
        https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L77
        A few devices (USB-SSR08 for example) start at FIRSTPORTCL and
        number the bits as if FIRSTPORTA and FIRSTPORTB exist for
        compatibility with older digital peripherals.
         */
        if ((PORT_INDEX == 0) && (portType == DigitalPortType.FIRST_PORT_C_LOW)) {
            return 16;
        } else {
            return 0;
        }
    }

    public boolean isIndividualBitConfigurable() {
        if (isIndividualBitConfigurable == null) {

            //https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L113
            if ((inputMask & outputMask) == 0) {
                // TODO simplify nested if statements
                // AUXPORT might be configurable, check if we can configure it
                if (portType == DigitalPortType.AUX_PORT) {
                    try {
                        // check if we can configure an individual bit in the port

                        final int errorCode1 = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigBit(DAQ_DEVICE.getBoardNumber(), portType.VALUE, getFirstBit(), DigitalPortDirection.OUTPUT.VALUE);
                        JMCCULUtils.checkError(errorCode1);

                        final int errorCode2 = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigBit(DAQ_DEVICE.getBoardNumber(), portType.VALUE, getFirstBit(), DigitalPortDirection.INPUT.VALUE);
                        JMCCULUtils.checkError(errorCode2);

                        isIndividualBitConfigurable = true;
                    } catch (JMCCULException ex) {
                        isIndividualBitConfigurable = false;
                    }
                } else {
                    // portType is not AUX_PORT
                    isIndividualBitConfigurable = false;
                }
            } else {
                // (inputMask & outputMask) is not zero
                isIndividualBitConfigurable = false;
            }
        }
        return isIndividualBitConfigurable;
    }

    public boolean isPortConfigurable() throws JMCCULException {
        if (isPortConfigurable == null) {
            // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L130
            /*
            https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm
            Use the DIINMASK and DIOUTMASK options to determine if an AUXPORT is configurable.
            Execute cbGetConfig() twice to the same port – once using DIINMASK and once using DIOUTMASK.
            If both of the ConfigValue arguments returned have input and output bits that overlap,
            the port is not configurable.

            You can determine overlapping bits by Anding both arguments.

            Example: for a device with seven bits of digital I/O (four outputs and three inputs),
            the ConfigValue returned by DIINMASK is always 7 (0000 0111),
            while the ConfigValue argument returned by DIOUTMASK is always 15 (0000 1111).
            When you And both ConfigValue arguments together, you get a non-zero number (7).
            Any non-zero number indicates that input and output bits overlap for the specified port,
            and the port is a non-configurable AUXPORT.
             */
            if ((inputMask & outputMask) == 0) {
                try {
                    // check if we can configure the port

                    final int errorCode1 = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigPort(DAQ_DEVICE.getBoardNumber(), portType.VALUE, DigitalPortDirection.OUTPUT.VALUE);
                    JMCCULUtils.checkError(errorCode1);

                    final int errorCode2 = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigPort(DAQ_DEVICE.getBoardNumber(), portType.VALUE, DigitalPortDirection.INPUT.VALUE);
                    JMCCULUtils.checkError(errorCode2);

                    isPortConfigurable = true;
                } catch (JMCCULException ex) {
                    ex.throwIfErrorIsNetworkDeviceInUse();
                    isPortConfigurable = false;
                }
            } else {
                isPortConfigurable = false;
            }
        }
        return isPortConfigurable;
    }

    public boolean isInputSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L87
        return (inputMask > 0) || isPortConfigurable;
    }

    public boolean isOutputSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L109
        return (outputMask > 0) || isPortConfigurable;
    }

    public boolean isInputScanSupported() throws JMCCULException {
        if (isInputScanSupported == null) {
        /*
        TODO what does "scan" mean? Is it the same as "synchronous"?
        Table of cbGetStatus()/cbGetIOStatus() arguments:
            DIFUNCTION      Specifies digital input scans started with cbDInScan().
            DOFUNCTION      Specifies digital output scans started with cbDOutScan().
            DAQIFUNCTION    Specifies a synchronous input scan started with cbDaqInScan().
            DAQOFUNCTION    Specifies a synchronous output scan started with cbDaqOutScan().
        Blog post:
        https://www.mccdaq.com/blog/2018/01/11/how-to-synchronous-analog-digital-and-encoder-measurements-in-labview/
         */
            // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L91
            try {
                // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetStatus.htm
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetIOStatus(
                        DAQ_DEVICE.getBoardNumber(),
                        ShortBuffer.allocate(1),
                        new NativeLongByReference(new NativeLong(0)),
                        new NativeLongByReference(new NativeLong(0)),
                        MeasurementComputingUniversalLibrary.DIFUNCTION
                );
                JMCCULUtils.checkError(errorCode);
                isInputScanSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                isInputScanSupported = false;
            }
        }
        return isInputScanSupported;
    }

    public boolean isOutputScanSupported() throws JMCCULException {
        if (isOutputScanSupported == null) {
        /*
        TODO what does "scan" mean? Is it the same as "synchronous"?
        Table of cbGetStatus()/cbGetIOStatus() arguments:
            DIFUNCTION      Specifies digital input scans started with cbDInScan().
            DOFUNCTION      Specifies digital output scans started with cbDOutScan().
            DAQIFUNCTION    Specifies a synchronous input scan started with cbDaqInScan().
            DAQOFUNCTION    Specifies a synchronous output scan started with cbDaqOutScan().
        Blog post:
        https://www.mccdaq.com/blog/2018/01/11/how-to-synchronous-analog-digital-and-encoder-measurements-in-labview/
         */
            // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L100
            try {
                // // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetStatus.htm
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetIOStatus(
                        DAQ_DEVICE.getBoardNumber(),
                        ShortBuffer.allocate(1),
                        new NativeLongByReference(new NativeLong(0)),
                        new NativeLongByReference(new NativeLong(0)),
                        MeasurementComputingUniversalLibrary.DOFUNCTION
                );
                JMCCULUtils.checkError(errorCode);
                isOutputScanSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                isOutputScanSupported = false;
            }
        }
        return isOutputScanSupported;
    }
}
