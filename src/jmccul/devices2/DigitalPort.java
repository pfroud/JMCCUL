package jmccul.devices2;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import jmccul.JMCCULException;
import static jmccul.JMCCULUtils.checkError;
import jmccul.enums.DigitalPortType;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * Adapted from a subclass of https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py
 *
 * @author Peter Froud
 */
public class DigitalPort {

    private final MeasurementComputingUniversalLibrary LIBRARY = MeasurementComputingUniversalLibrary.INSTANCE;
    public final DaqDevice device;
    public final int portIndex;
    public final DigitalPortType portType;
    public final int numBits;
    public final int inputMask;
    public final int outputMask;
    public final int firstBit;
    public final boolean isPortConfigurable;
    public final boolean isOutputSupported;
    public final boolean isInputSupported;
    public final boolean isBitConfigurable;
    public final boolean isInputScanSupported;
    public final boolean isOutputScanSupported;

    DigitalPort(DaqDevice device, int portIndex) throws JMCCULException {
        this.device = device;
        this.portIndex = portIndex;
        portType = getPortType();
        numBits = getNumBits();
        inputMask = getInputMask();
        outputMask = getOutputMask();
        firstBit = getFirstBit();
        isPortConfigurable = isPortConfigurable();
        isOutputSupported = isOutputSupported();
        isInputSupported = isInputSupported();
        isBitConfigurable = isBitConfigurable();
        isInputScanSupported = isInputScanSupported();
        isOutputScanSupported = isOutputScanSupported();
    }

    private DigitalPortType getPortType() throws JMCCULException {
        return DigitalPortType.parseInt(
                getConfigItem(MeasurementComputingUniversalLibrary.DIDEVTYPE)
        );
    }

    private int getNumBits() throws JMCCULException {
        return getConfigItem(MeasurementComputingUniversalLibrary.DINUMBITS);
    }

    private int getInputMask() throws JMCCULException {
        return getConfigItem(MeasurementComputingUniversalLibrary.DIINMASK);
    }

    private int getOutputMask() throws JMCCULException {
        return getConfigItem(MeasurementComputingUniversalLibrary.DIOUTMASK);
    }

    private int getConfigItem(int item) throws JMCCULException {
        IntBuffer buf = IntBuffer.allocate(1);
        checkError(
                LIBRARY.cbGetConfig(
                        MeasurementComputingUniversalLibrary.DIGITALINFO,
                        device.BOARD_NUMBER,
                        portIndex,
                        item,
                        buf)
        );
        return buf.get(0);
    }

    private int getFirstBit() {
        /*
        https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L77
        A few devices (USB-SSR08 for example) start at FIRSTPORTCL and
        number the bits as if FIRSTPORTA and FIRSTPORTB exist for
        compatibility with older digital peripherals.
         */
        int rv = 0;
        if (portIndex == 0 && portType == DigitalPortType.FIRST_PORT_C_LOW) {
            rv = 16;
        }
        return rv;
    }

    private boolean isBitConfigurable() {
        //https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L113
        boolean rv = false;
        if ((inputMask & outputMask) == 0) {
            // AUXPORT might be configurable, check if we can configure it
            if (portType == DigitalPortType.AUX_PORT) {
                try {
                    // check if we can configure the port
                    checkError(LIBRARY.cbDConfigBit(device.BOARD_NUMBER, portType.VALUE, firstBit, DigitalPortDirection.OUTPUT.VALUE));
                    checkError(LIBRARY.cbDConfigBit(device.BOARD_NUMBER, portType.VALUE, firstBit, DigitalPortDirection.INPUT.VALUE));
                } catch (JMCCULException ex) {
                    rv = false;
                }
            }
        }
        return rv;
    }

    private boolean isPortConfigurable() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L130
        boolean rv = false;
        if ((inputMask & outputMask) == 0) {
            try {
                // check if we can configure the port
                checkError(LIBRARY.cbDConfigPort(device.BOARD_NUMBER, portType.VALUE, DigitalPortDirection.OUTPUT.VALUE));
                checkError(LIBRARY.cbDConfigPort(device.BOARD_NUMBER, portType.VALUE, DigitalPortDirection.INPUT.VALUE));
            } catch (JMCCULException ex) {
                rv = false;
            }
        }
        return rv;
    }

    private boolean isInputSupported() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L87
        return (inputMask > 0) || isPortConfigurable;
    }

    private boolean isOutputSupported() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L109
        return (outputMask > 0) || isPortConfigurable;
    }

    private boolean isInputScanSupported() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L91
        boolean rv = true;
        try {
            // getIOStatus() has been renamed getStatus() in a newer version of Universal Library
            checkError(
                    LIBRARY.cbGetIOStatus(
                            device.BOARD_NUMBER,
                            ShortBuffer.allocate(1),
                            new NativeLongByReference(new NativeLong(0)),
                            new NativeLongByReference(new NativeLong(0)),
                            MeasurementComputingUniversalLibrary.DIFUNCTION
                    )
            );
        } catch (JMCCULException ex) {
            rv = false;
        }
        return rv;
    }

    private boolean isOutputScanSupported() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L100
        boolean rv = true;
        try {
            // getIOStatus() has been renamed getStatus() in a newer version of Universal Library
            checkError(
                    LIBRARY.cbGetIOStatus(
                            device.BOARD_NUMBER,
                            ShortBuffer.allocate(1),
                            new NativeLongByReference(new NativeLong(0)),
                            new NativeLongByReference(new NativeLong(0)),
                            MeasurementComputingUniversalLibrary.DOFUNCTION
                    )
            );
        } catch (JMCCULException ex) {
            rv = false;
        }
        return rv;
    }
}
