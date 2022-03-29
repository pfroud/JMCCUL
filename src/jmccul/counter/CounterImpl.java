package jmccul.counter;

import jmccul.config.Configuration;
import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class CounterImpl {

    // https://github.com/mccdaq/mcculw/blob/master/mcculw/device_info/ctr_info.py
    private final DaqDevice DAQ_DEVICE;
    public final int CHANNEL_COUNT;

    public CounterImpl(DaqDevice device) throws JMCCULException {
        DAQ_DEVICE = device;
        CHANNEL_COUNT = getChannelCount();
    }

    private int getChannelCount() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ctr_info.py#L28
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                DAQ_DEVICE.BOARD_NUMBER,
                0,
                MeasurementComputingUniversalLibrary.BICINUMDEVS
        );
    }

    public boolean isCounterSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ctr_info.py#L33
        return CHANNEL_COUNT > 0;
    }

    /*
    class CounterChannel {
    }

    enum CounterChannelType {
    }
     */
}
