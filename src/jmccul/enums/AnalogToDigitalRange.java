package jmccul.enums;

import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/ULStart.htm#Misc/Supported_A_D_Ranges.htm
 *
 * Confusingly this enum is also used for digital-to-analog ranges (cbAOut and cbAOutScan).
 *
 * @author Peter Froud
 */
public enum AnalogToDigitalRange {
    BIPOLAR_60_VOLTS(MeasurementComputingUniversalLibrary.BIP60VOLTS),
    BIPOLAR_20_VOLTS(MeasurementComputingUniversalLibrary.BIP20VOLTS),
    BIPOLAR_15_VOLTS(MeasurementComputingUniversalLibrary.BIP15VOLTS),
    BIPOLAR_10_VOLTS(MeasurementComputingUniversalLibrary.BIP10VOLTS),
    BIPOLAR_5_VOLTS(MeasurementComputingUniversalLibrary.BIP5VOLTS),
    BIPOLAR_4_VOLTS(MeasurementComputingUniversalLibrary.BIP4VOLTS),
    BIPOLAR_2_POINT_5_VOLTS(MeasurementComputingUniversalLibrary.BIP2PT5VOLTS),
    BIPOLAR_2_VOLTS(MeasurementComputingUniversalLibrary.BIP2VOLTS),
    BIPOLAR_1_POINT_67_VOLTS(MeasurementComputingUniversalLibrary.BIP1PT67VOLTS),
    BIPOLAR_1_POINT_25_VOLTS(MeasurementComputingUniversalLibrary.BIP1PT25VOLTS),
    BIPOLAR_1_VOLT(MeasurementComputingUniversalLibrary.BIP1VOLTS),
    BIPOLAR_ZERO_POINT_625_VOLTS(MeasurementComputingUniversalLibrary.BIPPT625VOLTS),
    BIPOLAR_ZERO_POINT_5_VOLTS(MeasurementComputingUniversalLibrary.BIPPT5VOLTS),
    BIPOLAR_ZERO_POINT_312_VOLTS(MeasurementComputingUniversalLibrary.BIPPT312VOLTS),
    BIPOLAR_ZERO_POINT_25_VOLTS(MeasurementComputingUniversalLibrary.BIPPT25VOLTS),
    BIPOLAR_ZERO_POINT_2_VOLTS(MeasurementComputingUniversalLibrary.BIPPT2VOLTS),
    BIPOLAR_ZERO_POINT_156_VOLTS(MeasurementComputingUniversalLibrary.BIPPT156VOLTS),
    BIPOLAR_ZERO_POINT_125_VOLTS(MeasurementComputingUniversalLibrary.BIPPT125VOLTS),
    BIPOLAR_ZERO_POINT_1_VOLTS(MeasurementComputingUniversalLibrary.BIPPT1VOLTS),
    BIPOLAR_ZERO_POINT_078_VOLTS(MeasurementComputingUniversalLibrary.BIPPT078VOLTS),
    BIPOLAR_ZERO_POINT_05_VOLTS(MeasurementComputingUniversalLibrary.BIPPT05VOLTS),
    BIPOLAR_ZERO_POINT_01_VOLTS(MeasurementComputingUniversalLibrary.BIPPT01VOLTS),
    BIPOLAR_ZERO_POINT_005_VOLTS(MeasurementComputingUniversalLibrary.BIPPT005VOLTS),
    ////////////////////////////////////////////////////////////
    UNIPOLAR_10_VOLTS(MeasurementComputingUniversalLibrary.UNI10VOLTS),
    UNIPOLAR_5_VOLTS(MeasurementComputingUniversalLibrary.UNI5VOLTS),
    UNIPOLAR_4_VOLTS(MeasurementComputingUniversalLibrary.UNI4VOLTS),
    UNIPOLAR_2_POINT_5_VOLTS(MeasurementComputingUniversalLibrary.UNI2PT5VOLTS),
    UNIPOLAR_2_VOLTS(MeasurementComputingUniversalLibrary.UNI2VOLTS),
    UNIPOLAR_1_POINT_67_VOLTS(MeasurementComputingUniversalLibrary.UNI1PT67VOLTS),
    UNIPOLAR_1_POINT_25_VOLTS(MeasurementComputingUniversalLibrary.UNI1PT25VOLTS),
    UNIPOLAR_1_VOLT(MeasurementComputingUniversalLibrary.UNI1VOLTS),
    UNIPOLAR_ZERO_POINT_5_VOLTS(MeasurementComputingUniversalLibrary.UNIPT5VOLTS),
    UNIPOLAR_ZERO_POINT_25_VOLTS(MeasurementComputingUniversalLibrary.UNIPT25VOLTS),
    UNIPOLAR_ZERO_POINT_2_VOLTS(MeasurementComputingUniversalLibrary.UNIPT2VOLTS),
    UNIPOLAR_ZERO_POINT_1_VOLTS(MeasurementComputingUniversalLibrary.UNIPT1VOLTS),
    UNIPOLAR_ZERO_POINT_01_VOLTS(MeasurementComputingUniversalLibrary.UNIPT01VOLTS),
    UNIPOLAR_ZERO_POINT_02_VOLTS(MeasurementComputingUniversalLibrary.UNIPT02VOLTS),
    UNIPOLAR_ZERO_POINT_05_VOLTS(MeasurementComputingUniversalLibrary.UNIPT05VOLTS),
    //////////////////////////////////////////////////////////
    MILLIAMPS_4_TO_20(MeasurementComputingUniversalLibrary.MA4TO20),
    MILLIAMPS_2_TO_10(MeasurementComputingUniversalLibrary.MA2TO10),
    MILLIAMPS_1_TO_5(MeasurementComputingUniversalLibrary.MA1TO5),
    MILLIAMPS_ZERO_POINT_5_TO_2_POINT_5(MeasurementComputingUniversalLibrary.MAPT5TO2PT5),
    MILLIAMPS_0_TO_20(MeasurementComputingUniversalLibrary.MA0TO20),
    BIPOLAR_ZERO_POINT_025_AMPS(MeasurementComputingUniversalLibrary.BIPPT025AMPS),
    ////////////////////////////////////////////////////////
    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED);

    public final int VALUE;

    private AnalogToDigitalRange(int value) {
        VALUE = value;
    }

}
