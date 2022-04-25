package jmccul.config;

import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class ExpansionConfig {

    private final int BOARD_NUMBER;

    public ExpansionConfig(DaqDevice device) {
        BOARD_NUMBER = device.BOARD_NUMBER;
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     XIBOARDTYPE -> XI BOARD TYPE -> expansionInfo board type
     Readable? yes
     Writabale? NO
     */
    public int getExpansionBoardType() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.XIBOARDTYPE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIUSESEXPS -> BI USES EXPS -> boardInfo uses expansions
     Readable? yes
     Writabale? no
     */
    public boolean getExpansionBoardSupported() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIUSESEXPS
        ) == 1;
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     XIMUXADCHAN1 -> XI MUX AD CHAN 1 -> expansionInfo multiplexer(?) analog-to-digital channel 1
     Readable? yes
     Writabale? no
     */
    public int getExpansionBoardChannel1() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.XIMUX_AD_CHAN1
        );
    }

    public void setExpansionBoardChannel1(int ch) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.XIMUX_AD_CHAN1,
                ch
        );
    }

}
