package jmccul.devices;

import java.nio.FloatBuffer;
import jmccul.JMCCULException;
import jmccul.enums.TemperatureScale;
import jmccul.enums.ThermocoupleType;
import jmccul.jna.MeasurementComputingUniversalLibrary;
import static jmccul.JMCCULUtils.checkError;

/**
 * Eight-channel thermocouple measurement device.
 *
 * https://www.mccdaq.com/usb-data-acquisition/USB-TEMP-Series.aspx
 *
 * https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Users_Guide/Temperature_Input_Boards/USB-TEMP-TC_Series.htm
 *
 * @author Peter Froud
 */
public class USB_TC extends AbstractJMCCULDevice {

    MeasurementComputingUniversalLibrary LIBRARY = MeasurementComputingUniversalLibrary.INSTANCE;
    private final static int CONFIG_TYPE_BOARD_INFO = MeasurementComputingUniversalLibrary.BOARDINFO;
    private final static int CONFIG_ITEM_THERMOCOUPLE_TYPE = MeasurementComputingUniversalLibrary.BICHANTCTYPE;

    public USB_TC() throws JMCCULException {
        super("USB-TC");
    }

    public float readTemperature(int channel, ThermocoupleType thermocoupleType, TemperatureScale temperatureScale, boolean tenSampleFilter) throws JMCCULException {
        if (channel < 0 || channel > 7) {
            throw new IllegalArgumentException("channel must be between 0 and 7 I think");
        }

// set thermocouple type
        checkError(LIBRARY.cbSetConfig(CONFIG_TYPE_BOARD_INFO, BOARD_NUMBER, channel, CONFIG_ITEM_THERMOCOUPLE_TYPE, thermocoupleType.VALUE));

        // temperature input operation
        final FloatBuffer floatBuffer = FloatBuffer.allocate(1);
        final int options = tenSampleFilter ? MeasurementComputingUniversalLibrary.FILTER : MeasurementComputingUniversalLibrary.NOFILTER;
        checkError(LIBRARY.cbTIn(BOARD_NUMBER, channel, temperatureScale.VALUE, floatBuffer, options));

        return floatBuffer.get();
    }

}
