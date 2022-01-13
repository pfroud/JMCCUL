package jmccul.digital;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import java.nio.ShortBuffer;
import jmccul.Configuration;
import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.JMCCULUtils;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * Adapted from a subclass of https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py
 *
 * @author Peter Froud
 */
public class DigitalPort {

    private final DaqDevice DAQ_DEVICE;
    private final int PORT_INDEX;
    public final DigitalPortType PORT_TYPE;
    public final int NUM_BITS;
    public final int INPUT_MASK;
    public final int OUTPUT_MASK;
    public final int FIRST_BIT;
    public final boolean IS_PORT_CONFIGURABLE;
    public final boolean IS_OUTPUT_SUPPORTED;
    public final boolean IS_INPUT_SUPPORTED;
    public final boolean IS_BIT_CONFIGURABLE;
    public final boolean IS_INPUT_SCAN_SUPPORTED;
    public final boolean IS_OUTPUT_SCAN_SUPPORTED;

    DigitalPort(DaqDevice device, int portIndex) throws JMCCULException {
        DAQ_DEVICE = device;
        PORT_INDEX = portIndex;
        PORT_TYPE = getPortType();
        NUM_BITS = getNumBits();
        INPUT_MASK = getInputMask();
        OUTPUT_MASK = getOutputMask();
        FIRST_BIT = getFirstBit();
        IS_PORT_CONFIGURABLE = isConfigurable();
        IS_OUTPUT_SUPPORTED = isOutputSupported();
        IS_INPUT_SUPPORTED = isInputSupported();
        IS_BIT_CONFIGURABLE = isIndividualBitConfigurable();
        IS_INPUT_SCAN_SUPPORTED = isInputScanSupported();
        IS_OUTPUT_SCAN_SUPPORTED = isOutputScanSupported();
    }

    @Override
    public String toString() {
        return PORT_TYPE.name();
    }

    private DigitalPortType getPortType() throws JMCCULException {
        final int portTypeInt = getConfigItem(MeasurementComputingUniversalLibrary.DIDEVTYPE);
        return DigitalPortType.parseInt(portTypeInt);
    }

    private int getNumBits() throws JMCCULException {
        return getConfigItem(MeasurementComputingUniversalLibrary.DINUMBITS);
    }

    private int getInputMask() throws JMCCULException {
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
        return getConfigItem(MeasurementComputingUniversalLibrary.DIINMASK);
    }

    private int getOutputMask() throws JMCCULException {
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
        return getConfigItem(MeasurementComputingUniversalLibrary.DIOUTMASK);
    }

    private int getConfigItem(int item) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.DIGITALINFO,
                DAQ_DEVICE.BOARD_NUMBER,
                PORT_INDEX,
                item
        );
    }

    private int getFirstBit() {
        /*
        https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L77
        A few devices (USB-SSR08 for example) start at FIRSTPORTCL and
        number the bits as if FIRSTPORTA and FIRSTPORTB exist for
        compatibility with older digital peripherals.
         */
        int rv = 0;
        if ((PORT_INDEX == 0) && (PORT_TYPE == DigitalPortType.FIRST_PORT_C_LOW)) {
            rv = 16;
        }
        return rv;
    }

    private boolean isIndividualBitConfigurable() {
        //https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L113
        boolean rv = false;
        if ((INPUT_MASK & OUTPUT_MASK) == 0) {
            // AUXPORT might be configurable, check if we can configure it
            if (PORT_TYPE == DigitalPortType.AUX_PORT) {
                try {
                    // check if we can configure an individual bit in the port

                    final int errorCode1 = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigBit(DAQ_DEVICE.BOARD_NUMBER, PORT_TYPE.VALUE, FIRST_BIT, DigitalPortDirection.OUTPUT.VALUE);
                    JMCCULUtils.checkError(errorCode1);

                    final int errorCode2 = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigBit(DAQ_DEVICE.BOARD_NUMBER, PORT_TYPE.VALUE, FIRST_BIT, DigitalPortDirection.INPUT.VALUE);
                    JMCCULUtils.checkError(errorCode2);

                    rv = true;
                } catch (JMCCULException ex) {
                    rv = false;
                }
            }
        }
        return rv;
    }

    private boolean isConfigurable() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L130
        boolean rv = false;
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
        if ((INPUT_MASK & OUTPUT_MASK) == 0) {
            try {
                // check if we can configure the port

                final int errorCode1 = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigPort(DAQ_DEVICE.BOARD_NUMBER, PORT_TYPE.VALUE, DigitalPortDirection.OUTPUT.VALUE);
                JMCCULUtils.checkError(errorCode1);

                final int errorCode2 = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigPort(DAQ_DEVICE.BOARD_NUMBER, PORT_TYPE.VALUE, DigitalPortDirection.INPUT.VALUE);
                JMCCULUtils.checkError(errorCode2);

                rv = true;
            } catch (JMCCULException ex) {
                rv = false;
            }
        }
        return rv;
    }

    private boolean isInputSupported() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L87
        return (INPUT_MASK > 0) || IS_PORT_CONFIGURABLE;
    }

    private boolean isOutputSupported() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L109
        return (OUTPUT_MASK > 0) || IS_PORT_CONFIGURABLE;
    }

    private boolean isInputScanSupported() throws JMCCULException {
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
        boolean rv = true;
        try {
            // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetStatus.htm
            final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetIOStatus(
                    DAQ_DEVICE.BOARD_NUMBER,
                    ShortBuffer.allocate(1),
                    new NativeLongByReference(new NativeLong(0)),
                    new NativeLongByReference(new NativeLong(0)),
                    MeasurementComputingUniversalLibrary.DIFUNCTION
            );
            JMCCULUtils.checkError(errorCode);
        } catch (JMCCULException ex) {
            rv = false;
        }
        return rv;
    }

    private boolean isOutputScanSupported() throws JMCCULException {
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
        boolean rv = true;
        try {
            // // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetStatus.htm
            final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetIOStatus(
                    DAQ_DEVICE.BOARD_NUMBER,
                    ShortBuffer.allocate(1),
                    new NativeLongByReference(new NativeLong(0)),
                    new NativeLongByReference(new NativeLong(0)),
                    MeasurementComputingUniversalLibrary.DOFUNCTION
            );
            JMCCULUtils.checkError(errorCode);
        } catch (JMCCULException ex) {
            rv = false;
        }
        return rv;
    }
}
