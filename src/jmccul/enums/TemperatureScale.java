package jmccul.enums;

import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum TemperatureScale {

    CELSIUS(MeasurementComputingUniversalLibrary.CELSIUS),
    FAHRENHEIT(MeasurementComputingUniversalLibrary.FAHRENHEIT),
    KELVIN(MeasurementComputingUniversalLibrary.KELVIN),
    VOLTS(MeasurementComputingUniversalLibrary.VOLTS),
    NOSCALE(MeasurementComputingUniversalLibrary.NOSCALE);

    public final int VALUE;

    private TemperatureScale(int value) {
        VALUE = value;
    }
}
