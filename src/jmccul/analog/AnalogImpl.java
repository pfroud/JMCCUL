package jmccul.analog;

import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class AnalogImpl {

    /*
    https://github.com/mccdaq/mcculw/blob/master/mcculw/device_info/ai_info.py
     */
    private final MeasurementComputingUniversalLibrary LIBRARY = MeasurementComputingUniversalLibrary.INSTANCE;
    private final DaqDevice DAQ_DEVICE;

//    public final DigitalPort[] PORTS;
    public AnalogImpl(DaqDevice device) throws JMCCULException {
        DAQ_DEVICE = device;

    }

}
