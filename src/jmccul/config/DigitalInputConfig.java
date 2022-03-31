package jmccul.config;

import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class DigitalInputConfig {

    private final int BOARD_NUMBER;

    public DigitalInputConfig(DaqDevice device) {
        BOARD_NUMBER = device.BOARD_NUMBER;
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDITRIGCOUNT -> BI DI TRIG COUNT -> boardInfo digitalInput trigger count
     Readable? yes
     Writabale? yes
     */
    public int getDigitalInputTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDITRIGCOUNT
        );
    }

    public void setDigitalInputTriggerCount() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDITRIGCOUNT,
                0 //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDIDEBOUNCESTATE -> BI DI DEBOUNCE STATE  -> boardInfo digitalInput debounce state
     Readable? NO
     Writabale? yes
     */
    public void setDigitalInputDebounceState() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDIDEBOUNCESTATE,
                0 //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDIDEBOUNCETIME -> BI DI DEBOUNCE TIME -> boardInfo digitalInput debounce time
     Readable? NO
     Writabale? yes
     */
    public void setDigitalInputDebounceTime() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDIDEBOUNCETIME,
                0 //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDISOFILTER -> BI DI ISO FILTER -> boardInfo digitalInput isolation(?) filter
     Readable? yes
     Writabale? yes
     */
    public boolean getGititalInputAcFilter(int bitNumber) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                bitNumber,
                MeasurementComputingUniversalLibrary.BIDISOFILTER
        ) == 1;
    }

    public void setDigitalInputAcFilter() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDISOFILTER,
                0 //new value
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIDINUMDEVS -> BI DI NUM DEVS -> boardInfo digitalInput number of devices
     Readable? yes
     Writabale? NO
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
