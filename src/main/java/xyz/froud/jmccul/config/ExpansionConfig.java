package xyz.froud.jmccul.config;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public class ExpansionConfig {

    private final int BOARD_NUMBER;

    public ExpansionConfig(DaqDevice device) {
        BOARD_NUMBER = device.getBoardNumber();
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     XIBOARDTYPE -> XI BOARD TYPE -> expansionInfo board type
     Readable? yes
     Writable? NO
     */
    public int getExpansionBoardType() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIBOARDTYPE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     BIUSESEXPS -> BI USES EXPS -> boardInfo uses expansions
     Readable? yes
     Writable? no
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
     Writable? yes
     */
    public int getExpansionBoardChannel1() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIMUX_AD_CHAN1
        );
    }

    public void setExpansionBoardChannel1(int ch) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIMUX_AD_CHAN1,
                ch
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     XIMUXADCHAN2 -> XI MUX AD CHAN 2 -> expansionInfo multiplexer(?) analog-to-digital channel 2
     Readable? yes
     Writable? yes
     */
    public int getExpansionBoardChannel2() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIMUX_AD_CHAN2
        );
    }

    public void setExpansionBoardChannel2(int ch) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIMUX_AD_CHAN2,
                ch
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     XIRANGE1 -> XI RANGE 1 -> expansionInfo range 1
     Readable? yes
     Writable? yes

    TODO does this use the same range enum as the non-expansion board?
     */
    public int getExpansionBoardRange1() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIRANGE1
        );
    }

    public void setExpansionBoardRange1(int ch) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIRANGE1,
                ch
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     XIRANGE2 -> XI RANGE 1 -> expansionInfo range 2
     Readable? yes
     Writable? yes

    TODO does this use the same range enum as the non-expansion board?
     */
    public int getExpansionBoardRange2() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIRANGE2
        );
    }

    public void setExpansionBoardRange2(int ch) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIRANGE2,
                ch
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     XICJCCHAN -> XI CJC CHAN -> expansionInfo coldJunctionCompensation(?) channel
     Readable? yes
     Writable? yes
     */
    public int getExpansionCjcChannel() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XICJCCHAN
        );
    }

    public void setExpansionCjcChannel(int ch) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XICJCCHAN,
                ch
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     XITHERMTYPE -> XI THERM TYPE -> expansionInfo thermocouple(?) type

    The docs cbSetConfig says "thermocouple type" but the docs for cbGetConfig just says
    "sensor type" and lists thermocouples *and* RTDs. But the RTDs are different from the ones
    on RtdSensorType.java, and I can't find them in cbw.h.

     Readable? yes
     Writable? yes
     */
    public int getExpansionThermocoupleType() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XITHERMTYPE
        );
    }

    public void setExpansionThermocoupleType(int type) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XITHERMTYPE,
                type
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     XINUMEXPCHANS -> XI NUM EXP CHANS -> expansionInfo number of expansion channels

     Readable? yes
     Writable? no
     */
    public int getExpansionChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.XINUMEXPCHANS
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     XIPARENTBOARD -> XI PARENT BOARD -> expansionInfo parent board

     Readable? yes
     Writable? no
     */
    public int getExpansionParentBoard() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.XIPARENTBOARD
        );
    }

}
