package jmccul.temperature;

import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * This enum is used for cbTIn() and cbTInScan()
 *
 *
 * @author Peter Froud
 */
public enum TemperatureScale {
    // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Enumerations/MccDaq.TempScale.htm
    CELSIUS(MeasurementComputingUniversalLibrary.CELSIUS),
    FAHRENHEIT(MeasurementComputingUniversalLibrary.FAHRENHEIT),
    KELVIN(MeasurementComputingUniversalLibrary.KELVIN),
    VOLTS(MeasurementComputingUniversalLibrary.VOLTS),
    NO_SCALE(MeasurementComputingUniversalLibrary.NOSCALE);

    public final int VALUE;

    private TemperatureScale(int value) {
        VALUE = value;
    }
}
