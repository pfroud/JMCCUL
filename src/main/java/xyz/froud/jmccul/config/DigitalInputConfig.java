package xyz.froud.jmccul.config;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public class DigitalInputConfig {

    private final int BOARD_NUMBER;

    public DigitalInputConfig(DaqDevice device) {
        BOARD_NUMBER = device.getBoardNumber();
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDITRIGCOUNT -> BI DI TRIG COUNT -> boardInfo digitalInput trigger count
     Readable? yes
     Writable? yes
     */
    public int getDigitalInputTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDITRIGCOUNT
        );
    }

    public void setDigitalInputTriggerCount(int count) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDITRIGCOUNT,
                count
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDIDEBOUNCESTATE -> BI DI DEBOUNCE STATE  -> boardInfo digitalInput debounce state
     Readable? NO
     Writable? yes
     */
    public void setDigitalInputDebounceState(int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDIDEBOUNCESTATE,
                n
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDIDEBOUNCETIME -> BI DI DEBOUNCE TIME -> boardInfo digitalInput debounce time
     Readable? NO
     Writable? yes
     */
    public void setDigitalInputDebounceTime(int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDIDEBOUNCETIME,
                n
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDISOFILTER -> BI DI ISO FILTER -> boardInfo digitalInput isolation(?) filter
     Readable? yes
     Writable? yes
     */
    public boolean getDigitalInputAcFilter(int bitNumber) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                bitNumber,
                MeasurementComputingUniversalLibrary.BIDISOFILTER
        ) == 1;
    }

    public void setDigitalInputAcFilter(int bitNumber, boolean enable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                bitNumber,
                MeasurementComputingUniversalLibrary.BIDISOFILTER,
                (enable ? 1 : 0)
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDINUMDEVS -> BI DI NUM DEVS -> boardInfo digitalInput number of devices
     Readable? yes
     Writable? NO
     */
    public int getDigitalDeviceCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDINUMDEVS
        );
    }

}
