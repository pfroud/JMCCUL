package jmccul.analog;

import jmccul.DaqDevice;
import jmccul.JMCCULException;

/**
 *
 * @author Peter Froud
 */
public class AnalogInputImpl {

    /*
    https://github.com/mccdaq/mcculw/blob/master/mcculw/device_info/ai_info.py
     */
    private final DaqDevice DAQ_DEVICE;

    public AnalogInputImpl(DaqDevice device) throws JMCCULException {
        DAQ_DEVICE = device;

    }

}
