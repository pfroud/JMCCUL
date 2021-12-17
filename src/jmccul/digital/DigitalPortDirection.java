package jmccul.digital;

import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public enum DigitalPortDirection {
    OUTPUT(MeasurementComputingUniversalLibrary.DIGITALOUT),
    INPUT(MeasurementComputingUniversalLibrary.DIGITALIN);
    public final int VALUE;

    private DigitalPortDirection(int value) {
        VALUE = value;
    }

}
