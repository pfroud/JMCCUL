package xyz.froud.jmccul.counter;

import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

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
