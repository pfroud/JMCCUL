package xyz.froud.jmccul.config;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public class CounterConfig {

    private final int BOARD_NUMBER;

    public CounterConfig(DaqDevice device) {
        BOARD_NUMBER = device.getBoardNumber();
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BICINUMDEVS -> BI CI NUM DEVS -> boardInfo counterInfo number of devices
     Readable? yes
     Writable? NO
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public int getCounterDeviceCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BICINUMDEVS
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BICTRTRIGCOUNT -> BI CTR TRIG COUNT -> boardInfo counter trigger count
     Readable? yes
     Writable? NO
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public int getCounterTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BICTRTRIGCOUNT
        );
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     */
    public void setCounterTriggerCount(int trigCount) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BICTRTRIGCOUNT,
                trigCount
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     CICTRNUM -> CI CTR NUM -> counterInfo counter number
     Readable? yes
     Writable? NO
     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public int getCounterNumber(int dev) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.COUNTERINFO,
                BOARD_NUMBER,
                dev,
                MeasurementComputingUniversalLibrary.CICTRNUM
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     CICTRTYPE -> CI CTR TYPE -> counterInfo counter type
     Readable? yes
     Writable? NO

    Can't find anything in cbw.h for this. It says:
    Counter type, where:
    1 = 8254, 2 = 9513, 3 = 8536, 4 = 7266, 5 = event counter, 6 = scan counter,
    7 = timer counter, 8 = quadrature counter, and 9 = pulse counter.


     */

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public int getCounterType(int idx) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.COUNTERINFO,
                BOARD_NUMBER,
                idx,
                MeasurementComputingUniversalLibrary.CICTRTYPE
        );
    }
}