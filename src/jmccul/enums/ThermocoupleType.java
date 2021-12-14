package jmccul.enums;

import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * This enum is used by setting the BICHANTCTYPE config item.
 *
 * @author Peter Froud
 */
public enum ThermocoupleType {

    J(MeasurementComputingUniversalLibrary.TC_TYPE_J),
    K(MeasurementComputingUniversalLibrary.TC_TYPE_K),
    S(MeasurementComputingUniversalLibrary.TC_TYPE_S),
    R(MeasurementComputingUniversalLibrary.TC_TYPE_R),
    B(MeasurementComputingUniversalLibrary.TC_TYPE_B),
    E(MeasurementComputingUniversalLibrary.TC_TYPE_E),
    T(MeasurementComputingUniversalLibrary.TC_TYPE_T),
    N(MeasurementComputingUniversalLibrary.TC_TYPE_N);
    public final int VALUE;

    private ThermocoupleType(int value) {
        VALUE = value;
    }

}
