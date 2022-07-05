package xyz.froud.jmccul.temperature;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.enums.TemperatureScale;
import xyz.froud.jmccul.enums.ThermocoupleSensorType;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.nio.FloatBuffer;

/**
 * @author Peter Froud
 */
public class TemperatureImpl {

    private final DaqDevice DAQ_DEVICE;
    private Integer channelCount;

    public TemperatureImpl(DaqDevice device) {
        DAQ_DEVICE = device;
    }

    public int getChannelCount() throws JMCCULException {
        if (channelCount == null) {
            // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L45
            channelCount = Configuration.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    DAQ_DEVICE.getBoardNumber(),
                    0, //device number
                    MeasurementComputingUniversalLibrary.BINUMTEMPCHANS
            );
        }
        return channelCount;

    }

    public boolean isTemperatureInputSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L50
        return channelCount > 0;
    }

    public void setThermocoupleType(int channel, ThermocoupleSensorType tc) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                DAQ_DEVICE.getBoardNumber(),
                channel,
                MeasurementComputingUniversalLibrary.BICHANTCTYPE,
                tc.VALUE
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions/cbTIn.htm">cbTIn()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions_for_NET/TIn.htm">TIn()</a>
     */
    public float readTemperature(int channel, TemperatureScale scale) throws JMCCULException {
        final FloatBuffer temperatureFloat = FloatBuffer.allocate(1);

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions/cbTIn.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbTIn(
                DAQ_DEVICE.getBoardNumber(),
                channel,
                scale.VALUE,
                temperatureFloat,
                0// TODO support options
        );

        JMCCULUtils.checkError(errorCode);
        return temperatureFloat.get();
    }

}
