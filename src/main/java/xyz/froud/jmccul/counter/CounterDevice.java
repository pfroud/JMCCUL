package xyz.froud.jmccul.counter;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.config.ConfigurationWrapper;
import xyz.froud.jmccul.MeasurementComputingUniversalLibrary;

/**
 * @see <a
 *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ctr_info.py#L45">CtrChanInfo</a>
 *
 * @author Peter Froud
 */
public class CounterDevice {

    final DaqDevice DAQ_DEVICE;
    final int DEVICE_NUMBER;
    CounterDeviceType type;
    Object scanOptions; //need to use bit flag

    public CounterDevice(DaqDevice daqDevice, int deviceNumber) {
        DAQ_DEVICE = daqDevice;
        DEVICE_NUMBER = deviceNumber;
    }


    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     CICTRTYPE -> CI CTR TYPE -> counterInfo counter type
     Readable? yes
     Writable? NO

    Can't find anything in cbw.h for this. It says:
    Counter type, where:
    1 = 8254, 2 = 9513, 3 = 8536, 4 = 7266, 5 = event counter, 6 = scan counter,
    7 = timer counter, 8 = quadrature counter, and 9 = pulse counter.
     */
    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public CounterDeviceType getCounterType() throws JMCCULException {
        return CounterDeviceType.parseInt(
                ConfigurationWrapper.getInt(
                        MeasurementComputingUniversalLibrary.COUNTERINFO,
                        DAQ_DEVICE.getBoardNumber(),
                        DEVICE_NUMBER,
                        MeasurementComputingUniversalLibrary.CICTRTYPE
                )
        );
    }

}
