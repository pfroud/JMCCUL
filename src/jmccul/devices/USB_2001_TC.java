package jmccul.devices;

import jmccul.jna.MeasurementComputingUniversalLibrary;
import java.awt.Component;
import java.nio.FloatBuffer;
import jmccul.JMCCULException;
import static jmccul.JMCCULUtils.throwIfNeeded;

/**
 * Single-Channel thermocouple device.
 *
 * https://www.mccdaq.com/usb-data-acquisition/usb-2001-tc.aspx
 *
 * https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Users_Guide/Temperature_Input_Boards/USB-2001-TC.htm
 *
 * @author Peter Froud
 */
public class USB_2001_TC extends AbstractJMCCULDevice {

    private final MeasurementComputingUniversalLibrary LIBRARY = MeasurementComputingUniversalLibrary.INSTANCE;

    private final int CONFIG_TYPE_BOARD_INFO = MeasurementComputingUniversalLibrary.BOARDINFO;
    private final int CONFIG_ITEM_THERMOCOUPLE_TYPE = MeasurementComputingUniversalLibrary.BICHANTCTYPE;
    private final int THERMOCOUPLE_TYPE_K = MeasurementComputingUniversalLibrary.TC_TYPE_K;

    private final int TEMPERATURE_UNITS_CELCIUS = MeasurementComputingUniversalLibrary.CELSIUS;
    private final int TC_OPTION_TEN_SAMPLES_AVERAGE = MeasurementComputingUniversalLibrary.FILTER;

    public USB_2001_TC(Component parentComponent) throws JMCCULException {
        super("USB-2001-TC");
    }

    public float readTemperature() throws JMCCULException {
        final int channel = 0;
        throwIfNeeded(LIBRARY.cbSetConfig(CONFIG_TYPE_BOARD_INFO, BOARD_NUMBER, channel, CONFIG_ITEM_THERMOCOUPLE_TYPE, THERMOCOUPLE_TYPE_K));

        final FloatBuffer temperatureFloat = FloatBuffer.allocate(1);
        throwIfNeeded(LIBRARY.cbTIn(BOARD_NUMBER, channel, TEMPERATURE_UNITS_CELCIUS, temperatureFloat, TC_OPTION_TEN_SAMPLES_AVERAGE));
        return temperatureFloat.get();
    }

}
