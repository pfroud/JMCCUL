package jmccul.devices;

import jmccul.JMCCULException;
import jmccul.jna.MeasurementComputingUniversalLibrary;
import jmccul.enums.AnalogToDigitalRange;
import static jmccul.JMCCULUtils.throwIfNeeded;

/**
 * Multifunction USB device.
 *
 * https://www.mccdaq.com/usb-data-acquisition/USB-1208FS-LS-1408FS-Series.aspx
 *
 * https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Users_Guide/Analog_Input_Boards/USB-12_1408FS-Plus.htm
 *
 * @author Peter Froud
 */
public final class USB_1408FS_Plus extends AbstractJMCCULDevice {

    private final MeasurementComputingUniversalLibrary LIBRARY = MeasurementComputingUniversalLibrary.INSTANCE;

    private final static int PORT_DIGITAL_IO = MeasurementComputingUniversalLibrary.FIRSTPORTA;
    private final static int PORT_DIRECTION_DIGITAL_OUT = MeasurementComputingUniversalLibrary.DIGITALOUT;

    public final static int PIN_ANALOG_OUT_DIM = 0;
    public final static int PIN_ANALOG_OUT_CCT = 1;

    public final static int PIN_DIGIATL_OUT_ZERO_TO_TEN = 4; // set pin high to connect 0-10V output to DUT
    public final static int PIN_DIGIATL_OUT_BARCODE_SCAN = 5; // set pin high to trigger QR code scanner

    public USB_1408FS_Plus() throws JMCCULException {
        super("USB-1408FS-Plus");

        // Digital outputs are used for matrix, barcode scanner trigger, and relay to (dis)connect 0-10V.
        throwIfNeeded(LIBRARY.cbDConfigPort(BOARD_NUMBER, PORT_DIGITAL_IO, PORT_DIRECTION_DIGITAL_OUT));
    }

    public void setDigitalPin(int pinNumber, boolean isOn) throws JMCCULException {
        throwIfNeeded(LIBRARY.cbDBitOut(BOARD_NUMBER, PORT_DIGITAL_IO, pinNumber, (short) (isOn ? 1 : 0)));
    }

    public void setAnalogVoltage(int pinNumber, float volts) throws JMCCULException {
        final int OPTIONS = 0;
        if (volts < 0 || volts > 5) {
            throw new IllegalArgumentException("setting analog volts to " + volts + ", sensible range is 0V - 5V");
        }
        throwIfNeeded(LIBRARY.cbVOut(BOARD_NUMBER, pinNumber, AnalogToDigitalRange.UNIPOLAR_5_VOLTS.VALUE, volts, OPTIONS));
    }

}
