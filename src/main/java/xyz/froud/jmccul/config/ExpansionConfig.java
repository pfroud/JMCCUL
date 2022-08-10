/*
 * The MIT License.
 *
 * Copyright (c) 2022 Peter Froud.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     XIBOARDTYPE -> XI BOARD TYPE -> expansionInfo board type
     Readable? yes
     Writable? NO
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/GetBoardType_Expansion.htm">ExpansionConfig.GetBoardType()</a>
     */
    public int getExpansionBoardType() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIBOARDTYPE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIUSESEXPS -> BI USES EXPS -> boardInfo uses expansions
     Readable? yes
     Writable? no
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetUsesExps.htm">BoardConfig.GetUsesExps()</a>
     */
    public boolean isExpansionBoardSupported() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIUSESEXPS
        ) == 1;
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     XIMUXADCHAN1 -> XI MUX AD CHAN 1 -> expansionInfo multiplexer(?) analog-to-digital channel 1
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/GetMuxAdChan1.htm">ExpansionConfig.GetMuxAdChan1()</a>
     */
    public int getExpansionBoardChannel1() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIMUX_AD_CHAN1
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/SetMuxAdChan1.htm">ExpansionConfig.SetMuxAdChan1()</a>
     */
    public void setExpansionBoardChannel1(int ch) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIMUX_AD_CHAN1,
                ch
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     XIMUXADCHAN2 -> XI MUX AD CHAN 2 -> expansionInfo multiplexer(?) analog-to-digital channel 2
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/GetMuxAdChan2.htm">ExpansionConfig.GetMuxAdChan2()</a>
     */
    public int getExpansionBoardChannel2() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIMUX_AD_CHAN2
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/SetMuxAdChan2.htm">ExpansionConfig.SetMuxAdChan2()</a>
     */
    public void setExpansionBoardChannel2(int ch) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIMUX_AD_CHAN2,
                ch
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     XIRANGE1 -> XI RANGE 1 -> expansionInfo range 1
     Readable? yes
     Writable? yes

    TODO does this use the same range enum as the non-expansion board?
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/GetRange1.htm">ExpansionConfig.GetRange1()</a>
     */
    public int getExpansionBoardRange1() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIRANGE1
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/SetRange1.htm">ExpansionConfig.SetRange1()</a>
     */
    public void setExpansionBoardRange1(int ch) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIRANGE1,
                ch
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     XIRANGE2 -> XI RANGE 1 -> expansionInfo range 2
     Readable? yes
     Writable? yes

    TODO does this use the same range enum as the non-expansion board?
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/GetRange2.htm">ExpansionConfig.GetRange2()</a>
     */
    public int getExpansionBoardRange2() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIRANGE2
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/SetRange2.htm">ExpansionConfig.SetRange2()</a>
     */
    public void setExpansionBoardRange2(int ch) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XIRANGE2,
                ch
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     XICJCCHAN -> XI CJC CHAN -> expansionInfo coldJunctionCompensation(?) channel
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/GetCjcChan.htm">ExpansionConfig.GetCjcChan()</a>
     */
    public int getExpansionCjcChannel() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XICJCCHAN
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/SetCjcChan.htm">ExpansionConfig.SetCjcChan()</a>
     */
    public void setExpansionCjcChannel(int ch) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XICJCCHAN,
                ch
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     XITHERMTYPE -> XI THERM TYPE -> expansionInfo thermocouple(?) type

    The docs cbSetConfig says "thermocouple type" but the docs for cbGetConfig just says
    "sensor type" and lists thermocouples *and* RTDs. But the RTDs are different from the ones
    on RtdSensorType.java, and I can't find them in cbw.h.

     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/GetThermType.htm">ExpansionConfig.GetThermType()</a>
     */
    public int getExpansionThermocoupleType() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XITHERMTYPE
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/SetThermType.htm">ExpansionConfig.SetThermType()</a>
     */
    public void setExpansionThermocoupleType(int type) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //don't know what devNum does
                MeasurementComputingUniversalLibrary.XITHERMTYPE,
                type
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     XINUMEXPCHANS -> XI NUM EXP CHANS -> expansionInfo number of expansion channels

     Readable? yes
     Writable? no
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Expansion_Board_Functions_for_NET/GetNumExpChans.htm">ExpansionConfig.GetNumExpChans()</a>
     */
    public int getExpansionChannelCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.XINUMEXPCHANS
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     XIPARENTBOARD -> XI PARENT BOARD -> expansionInfo parent board

     Readable? yes
     Writable? no
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public int getExpansionParentBoard() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.XIPARENTBOARD
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     GINUMEXPBOARDS -> GI NUM EXP BOARDS -> globalInfo number of expansion boards

     Readable? yes
     Writable? no
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public int getMaxExpansionBoardCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.EXPANSIONINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.GINUMEXPBOARDS
        );
    }

}
