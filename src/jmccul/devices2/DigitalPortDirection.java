/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package jmccul.devices2;

import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author lumenetix
 */
public enum DigitalPortDirection {
    OUTPUT(MeasurementComputingUniversalLibrary.DIGITALOUT),
    INPUT(MeasurementComputingUniversalLibrary.DIGITALIN);
    public final int VALUE;

    private DigitalPortDirection(int value) {
        VALUE = value;
    }

}
