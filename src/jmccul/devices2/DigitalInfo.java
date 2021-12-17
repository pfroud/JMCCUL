package jmccul.devices2;

import java.nio.IntBuffer;
import jmccul.JMCCULException;
import static jmccul.JMCCULUtils.checkError;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py
 *
 * @author Peter Froud
 */
public final class DigitalInfo {

    private final MeasurementComputingUniversalLibrary LIBRARY = MeasurementComputingUniversalLibrary.INSTANCE;
    private final DaqDevice device;
    public final DigitalPort[] PORTS;

    DigitalInfo(DaqDevice device) throws JMCCULException {
        this.device = device;
        PORTS = initPorts();

    }

    private DigitalPort[] initPorts() throws JMCCULException {
        final int portCount = getPortCount();
        DigitalPort[] rv = new DigitalPort[portCount];
        for (int i = 0; i < portCount; i++) {
            rv[i] = new DigitalPort(device, i);
        }
        return rv;
    }

    private int getPortCount() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L29
        IntBuffer buf = IntBuffer.allocate(1);
        checkError(
                LIBRARY.cbGetConfig(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        device.BOARD_NUMBER,
                        0,
                        MeasurementComputingUniversalLibrary.BIDINUMDEVS,
                        buf)
        );
        return buf.get();
    }

    public boolean isDigitalIOSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L39
        return PORTS.length > 0;
    }

}
