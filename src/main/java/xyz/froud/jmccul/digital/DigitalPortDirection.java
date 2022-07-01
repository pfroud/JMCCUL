package xyz.froud.jmccul.digital;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public enum DigitalPortDirection {
    OUTPUT(MeasurementComputingUniversalLibrary.DIGITALOUT),
    INPUT(MeasurementComputingUniversalLibrary.DIGITALIN);
    public final int VALUE;

    DigitalPortDirection(int value) {
        VALUE = value;
    }

}
