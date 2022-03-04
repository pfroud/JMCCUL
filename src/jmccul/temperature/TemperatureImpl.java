package jmccul.temperature;

import java.nio.FloatBuffer;
import jmccul.Configuration;
import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.JMCCULUtils;
import jmccul.TemperatureScale;
import jmccul.ThermocoupleSensorType;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class TemperatureImpl {

    private final DaqDevice DAQ_DEVICE;
    public final int CHANNEL_COUNT;

    public TemperatureImpl(DaqDevice device) throws JMCCULException {
        DAQ_DEVICE = device;
        CHANNEL_COUNT = getChannelCount();
    }

    private int getChannelCount() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L45
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                DAQ_DEVICE.BOARD_NUMBER,
                0, //device number
                MeasurementComputingUniversalLibrary.BINUMTEMPCHANS
        );

    }

    public boolean isTemperatureInputSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L50
        return CHANNEL_COUNT > 0;
    }

    public void setThermocoupleType(int channel, ThermocoupleSensorType tc) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                DAQ_DEVICE.BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BICHANTCTYPE,
                tc.VALUE
        );
    }

    public float readTemperature(int channel, TemperatureScale scale) throws JMCCULException {
        final FloatBuffer temperatureFloat = FloatBuffer.allocate(1);

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions/cbTIn.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbTIn(
                DAQ_DEVICE.BOARD_NUMBER,
                channel,
                scale.VALUE,
                temperatureFloat,
                0// TODO support options
        );

        JMCCULUtils.checkError(errorCode);
        return temperatureFloat.get();
    }

}
