package jmccul.config;

import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.enums.AnalogRange;
import jmccul.enums.DacUpdateMode;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class AnalogOutputConfig {

    private final int BOARD_NUMBER;

    /*
    Analog output = digital to analog = "DAC"
     */
    public AnalogOutputConfig(DaqDevice device) {
        BOARD_NUMBER = device.BOARD_NUMBER;
    }

    public boolean isDacForceSense(int channel) throws JMCCULException {
        /*
        The remote sensing feature compensates for the voltage
        drop error that occurs in applications where the analog
        outputs on a device are connected to its load through a
        long wire or cable type interconnect.

        The remote sensing feature utilizes the force and sense
        output terminals on a supported device. Refer to the
        device hardware manual for more information.
         */
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIDACFORCESENSE
        ) == 1;
    }

    public AnalogRange getDacRange(int channel) throws JMCCULException {
        return AnalogRange.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        channel,
                        MeasurementComputingUniversalLibrary.BIDACRANGE
                )
        );
    }

    public int getD2AResolution() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignores
                MeasurementComputingUniversalLibrary.BIDACRES
        );
    }

    public int getDacStartup(int channel) throws JMCCULException {
        /*
        Use the BIDACSTARTUP option to determine whether a board's
        DAC values before the last power down are stored.

        With ConfigItem set to BIDACSTARTUP, Configval returns 0 (zero)
        when the startup bit is DISabled. Current DAC settings are stored
        as startup values.

        ConfigVal returns 1 (one) when the startup bit is ENabled. The
        last DAC values are stored as startup values.

        Refer to the cbSetConfig() Notes section for information about how to store the current or last DAC values as start-up values.
         */
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel,
                MeasurementComputingUniversalLibrary.BIDACSTARTUP
        );
    }

    public int getDacTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDACTRIGCOUNT
        );
    }

    public DacUpdateMode getDacUpdateMode() throws JMCCULException {
        return DacUpdateMode.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        0, //devNum is ignored
                        MeasurementComputingUniversalLibrary.BIDACUPDATEMODE
                )
        );
        /*
        Use the BIDACUPDATEMODE option to check the update mode for a DAC board.

        With ConfigItem set to BIDACUPDATEMODE, ConfigVal returns 0 when the
        DAC update mode is immediate. Values written with cbAOut() are
        automatically output by the DAC channels.
        ConfigVal returns 1 when the DAC update mode is set to on command.
        Values written with cbAOut() are not output by the DAC channels
        until a cbSetConfig() call is made with its ConfigItem argument
        set to BIDACUPDATECMD.

        #define UPDATEIMMEDIATE  0
        #define UPDATEONCOMMAND  1
         */
    }

    public int getD2AChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMDACHANS
        );
    }

    public void setDacForceSense() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDACFORCESENSE,
                0 //new value
        );
    }

    public void setDacStartup() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDACSTARTUP,
                0 //new value
        );
    }

    public void setUpdateAnalogOutput() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDACUPDATECMD,
                0 //new value
        );
    }

    public void setDacUpdateMode() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDACUPDATEMODE,
                0 //new value
        );
    }

    public void setRange() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDACRANGE,
                0 //new value
        );
    }

    public void setTrigerCount() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDACTRIGCOUNT,
                0 //new value
        );
    }

    /*
    public void setDacSettlingTime() throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum
                MeasurementComputingUniversalLibrary.BIDACSETTLETIME,
                0 //new value
        );
    }
     */
}
