/*
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
import xyz.froud.jmccul.enums.BaseOrExpansionBoard;
import xyz.froud.jmccul.enums.CalibrationTableType;
import xyz.froud.jmccul.enums.ExternalClockType;
import xyz.froud.jmccul.enums.ExternalPacerClockEdge;
import xyz.froud.jmccul.enums.FirmwareVersionType;
import xyz.froud.jmccul.enums.InterruptClockEdge;
import xyz.froud.jmccul.enums.SyncMode;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public class BoardConfig {

    private final int BOARD_NUMBER;

    public BoardConfig(DaqDevice device) {
        BOARD_NUMBER = device.getBoardNumber();
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIBASEADR -> BI BASE ADR -> boardInfo base address
     Readable? yes
     Writable? yes
     */

    /**
     * Recommended for use only with ISA bus boards.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetBaseAdr.htm">BoardConfig.GetBaseAdr()</a>
     */
    public int getBaseAddress() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIBASEADR
        );
    }

    /**
     * Recommended for use only with ISA bus boards.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetBaseAdr.htm">BoardConfig.SetBaseAdr()</a>
     */
    public void setBaseAddress(int baseAddress) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIBASEADR,
                baseAddress
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIBOARDTYPE -> BI BOARD TYPE -> boardInfo board type
     Readable? yes
     Writable? NO
     */

    /**
     * Gets the unique number (device ID) assigned to the board (between 0 and 8000h) indicating the type of board
     * installed.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetBoardType.htm">BoardConfig.GetBoardType()</a>
     */
    public int getBoardType() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIBOARDTYPE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BICALTABLETYPE -> BI CAL TABLE TYPE -> boardInfo calibration table type
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetCalTableType.htm">BoardConfig.GetCalTableType()</a>
     */
    public CalibrationTableType getCalibrationTableType(BaseOrExpansionBoard baseOrExpansionBoard) throws JMCCULException {
        return CalibrationTableType.parseInt(
                Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        BOARD_NUMBER,
                        baseOrExpansionBoard.VALUE,
                        MeasurementComputingUniversalLibrary.BICALTABLETYPE
                )
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetCalTableType.htm">BoardConfig.SetCalTableType()</a>
     */
    public void setCalibrationTableType(CalibrationTableType calTable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BICALTABLETYPE,
                calTable.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BICLOCK -> BI CLOCK -> boardInfo clock
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetClock.htm">BoardConfig.GetClock()</a>
     */
    public int getClockFrequencyMegahertz() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored when getting
                MeasurementComputingUniversalLibrary.BICLOCK
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetClock.htm">BoardConfig.SetClock()</a>
     */
    public void setClockFrequencyMegahertz(int channel, int clockFrequency) throws JMCCULException {
        // todo only supports 1, 4,6 or 10
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                channel, //devNum is the channel when setting
                MeasurementComputingUniversalLibrary.BICLOCK,
                clockFrequency
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      BIDMACHAN -> BI DMA CHAN -> boardInfo directMemoryAccess channel
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDmaChan.htm">BoardConfig.GetDmaChan()</a>
     */
    public int getDmaChannel() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDMACHAN
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDmaChan.htm">BoardConfig.SetDmaChan()</a>
     */
    public void setDmaChannel(int dmaChannel) throws JMCCULException {
        // todo can only be 0, 1, or 3
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDMACHAN,
                dmaChannel
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDTBOARD -> BI DT BOARD -> boardInfo DataTranslation board
     Readable? yes
     Writable? NO
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDtBoard.htm">BoardConfig.GetDtBoard()</a>
     */
    public int getDataTranslationBoardNumber() throws JMCCULException {
        // Data Translation, acquired by Measurement Computing in 2015
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0,//devNum is ignored
                MeasurementComputingUniversalLibrary.BIDTBOARD
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIEXTCLKTYPE -> BI EXT CLK TYPE -> boardInfo external clock type
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetExtClockType.htm">BoardConfig.GetExtClockType()</a>
     */
    public ExternalClockType getExternalClockType() throws JMCCULException {
        return ExternalClockType.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devnum is ignored`
                MeasurementComputingUniversalLibrary.BIEXTCLKTYPE
        ));
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetExtClockType.htm">BoardConfig.SetExtClockType()</a>
     */
    public void setExternalClockType(ExternalClockType externalClockType) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIEXTCLKTYPE,
                externalClockType.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIEXTINPACEREDGE  -> BI EXT IN PACER EDGE -> boardInfo external input pacer edge
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetExtInPacerEdge.htm">BoardConfig.GetExtInPacerEdge()</a>
     */
    public ExternalPacerClockEdge getInputPacerClockEdge() throws JMCCULException {
        return ExternalPacerClockEdge.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIEXTINPACEREDGE
        ));
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetExtInPacerEdge.htm">BoardConfig.SetExtInPacerEdge()</a>
     */
    public void setInputPacerClockEdge(ExternalPacerClockEdge externalPacerClockEdge) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIEXTINPACEREDGE,
                externalPacerClockEdge.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIEXTOUTPACEREDGE -> BI EXT OUT PACER EDGE -> boardInfo external output pacer edge
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetExtOutPacerEdge.htm">BoardConfig.GetExtOutPacerEdge()</a>
     */
    public ExternalPacerClockEdge getOutputPacerClockEdge() throws JMCCULException {
        return ExternalPacerClockEdge.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIEXTOUTPACEREDGE
        ));
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetExtOutPacerEdge.htm">BoardConfig.SetExtOutPacerEdge()</a>
     */
    public void setOutputPacerClockEdge(ExternalPacerClockEdge externalPacerClockEdge) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIEXTOUTPACEREDGE,
                externalPacerClockEdge.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIOUTPUTPACEROUT -> BI OUTPUT PACER OUT -> boardInfo output pacer output
     Readable? no but that is probably a mistake in the docs
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetOutputPacerOut.htm">BoardConfig.GetOutputPacerOut()</a>
     */
    public boolean getOutputPacerClockEnable() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //they don't say whether devnum is ignored
                MeasurementComputingUniversalLibrary.BIOUTPUTPACEROUT
        ) == 1;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetOutputPacerOut.htm">BoardConfig.GetOutputPacerOut()</a>
     */
    public void setOutputPacerClockEnable(boolean enable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //they don't say whether devnum is ignored
                MeasurementComputingUniversalLibrary.BIOUTPUTPACEROUT,
                (enable ? 1 : 0)
        );
    }




    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIINPUTPACEROUT -> BI INPUT PACER OUT -> boardInfo input pacer output
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetInputPacerOut.htm">BoardConfig.GetInputPacerOut()</a>
     */
    public boolean getInputPacerClockEnable() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINPUTPACEROUT
        ) == 1;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetInputPacerOut.htm">BoardConfig.SetInputPacerOut()</a>
     */
    public void setInputPacerClockEnable(boolean enable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINPUTPACEROUT,
                (enable ? 1 : 0)
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIINTEDGE -> BI INT EDGE -> boardInfo interrupt edge
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetIntEdge.htm">BoardConfig.GetIntEdge()</a>
     */
    public InterruptClockEdge getInterruptEdge() throws JMCCULException {
        return InterruptClockEdge.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINTEDGE
        ));
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetIntEdge.htm">BoardConfig.SetIntEdge()</a>
     */
    public void setInterruptEdge(InterruptClockEdge edge) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINTEDGE,
                edge.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIINTLEVEL  -> BI INT LEVEL -> boardInfo interrupt level
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetIntLevel.htm">BoardConfig.GetIntLevel()</a>
     */
    public int getInterruptLevel() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINTLEVEL
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetIntLevel.htm">BoardConfig.SetIntLevel()</a>
     */
    public void setInterruptLevel(int level) throws JMCCULException {
        // 0 for none, or 1 to 15.
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIINTLEVEL,
                level
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BINUMIOPORTS -> BI NUM IO PORTS -> boardInfo number of I/O ports
     Readable? yes
     Writable? NO
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetNumIoPorts.htm">BoardConfig.GetNumIoPorts()</a>
     */
    public int getIoPortCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BINUMIOPORTS
        );
    }



    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIPATTERNTRIGPORT -> BI PATTERN TRIG PORT -> boardInfo pattern trigger port
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetPatternTrigPort.htm">BoardConfig.GetPatternTrigPort()</a>
     */
    public int getPatternTriggerPort() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIPATTERNTRIGPORT
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetPatternTrigPort.htm">BoardConfig.SetPatternTrigPort()</a>
     */
    public void setPatternTriggerPort(int port) throws JMCCULException {
        // it can only be AUXPORT0 or AUXPORT1
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIPATTERNTRIGPORT,
                port
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BISYNCMODE  -> BI SYNC MODE -> boardInfo sync mode
     Readable? yes
     Writable? yes
     */

    /**
     * Returns the simultaneous mode setting of supported analog output devices.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetSyncMode.htm">BoardConfig.GetSyncMode()</a>
     */
    public SyncMode getSyncMode() throws JMCCULException {
        return SyncMode.parseInt(Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BISYNCMODE
        ));
    }

    /**
     * Sets the Simultaneous mode option on supported analog output devices.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetSyncMode.htm">BoardConfig.SetSyncMode()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     */
    public void setSyncMode(SyncMode syncMode) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BISYNCMODE,
                syncMode.VALUE
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     setAdc -> BI TERM COUNT STAT BIT -> boardInfo terminal count output status bit
     Readable? yes
     Writable? yes
     */

    /**
     * Returns the terminal count output status for a specified bit.
     *
     * @return Returns True when the terminal count output is enabled, and False when disabled.
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/IsTerminalCountStatusBitEnabled().htm">BoardConfig.IsTerminalCountOutputEnabled()</a>
     */
    public boolean getTerminalCountOutputStatus(int bitNumber) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                bitNumber,
                MeasurementComputingUniversalLibrary.BITERMCOUNTSTATBIT
        ) == 1;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/DisableTerminalCountStatusBit.htm">BoardConfig.DisableTerminalCountStatusBit()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/EnableTerminalCountStatusBit.htm">BoardConfig.EnableTerminalCountStatusBit()</a>
     */
    public void setTerminalCountOutputStatus(int bitNumber, boolean status) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                bitNumber,
                MeasurementComputingUniversalLibrary.BITERMCOUNTSTATBIT,
                (status ? 1 : 0)
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIWAITSTATE -> BI WAIT STATE -> boardInfo wait state
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetWaitState.htm">BoardConfig.GetWaitState()</a>
     */
    public boolean getWaitStateJumper() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIWAITSTATE
        ) == 1;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetWaitState.htm">BoardConfig.SetWaitState()</a>
     */
    public void setWaitStateJumper(boolean jumper) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //not specified whether devNum is ignored
                MeasurementComputingUniversalLibrary.BIWAITSTATE,
                (jumper ? 1 : 0)
        );
    }


    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BICALOUTPUT -> BI CAL OUTPUT -> boardInfo calibration output
     Readable? NO
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     */
    public void setCalPinVoltage(int calPinVoltage) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BICALOUTPUT,
                calPinVoltage
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDEVVERSION -> BI DEV VERSION -> boardInfo device version
     Readable? yes
     Writable? NO
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDeviceVersion.htm">BoardConfig.GetDeviceVersion()</a>
     */
    public String getVersion(FirmwareVersionType version) throws JMCCULException {
        return Configuration.getString(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                version.VALUE,
                MeasurementComputingUniversalLibrary.BIDEVVERSION,
                1024
        );
    }


    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDEVSERIALNUM -> BI DEV SERIAL NUM -> boardInfo device serial number

    Factory serial number of a USB or Bluetooth device.


     Readable? yes
     Writable? no
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDeviceSerialNum.htm">BoardConfig.GetDeviceSerialNum()</a>
     */
    public String getFactorySerialNumber(BaseOrExpansionBoard baseOrExpansionBoard) throws JMCCULException {
        return Configuration.getString(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                baseOrExpansionBoard.VALUE,
                MeasurementComputingUniversalLibrary.BIDEVSERIALNUM,
                1024
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDEVUNIQUEID -> BI DEV UNIQUE ID -> boardInfo device unique identifier

    Unique identifier of a discoverable device, such as the serial number of a USB device or MAC address of an Ethernet device.

     Readable? yes
     Writable? no
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDeviceUniqueID.htm">BoardConfig.GetDeviceUniqueId()</a>
     */
    public String getUniqueID() throws JMCCULException {
        return Configuration.getString(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //not specified what DevNum does
                MeasurementComputingUniversalLibrary.BIDEVUNIQUEID,
                1024
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BISERIALNUM -> BI SERIAL NUM -> boardInfo serial number

    User-configured identifier of a supported USB device.
    Custom serial number assigned by a user to a USB device.

     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetUserDeviceId.htm">BoardConfig.GetUserDeviceId()</a>
     */
    public int getUserSpecifiedSerialNumber() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BISERIALNUM
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     */
    public void setUserSpecifiedSerialNumber(int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BISERIALNUM,
                n
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIUSERDEVID -> BI USER DEV ID -> boardInfo user device ID

    User-configured string of up to maxConfigLen character/bytes to an Ethernet, Bluetooth, or USB device.

     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetUserDeviceIdNum.htm">BoardConfig.GetUserDeviceIdNum()</a>
     */
    public String getUserSpecifiedID() throws JMCCULException {
        return Configuration.getString(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIUSERDEVID,
                1024
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetUserDeviceId.htm">BoardConfig.SetUserDeviceId()</a>
     */
    public void setUserSpecifiedID(String str) throws JMCCULException {
        Configuration.setString(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIUSERDEVID,
                str
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIUSERDEVIDNUM -> BI USER DEV ID NUM -> boardInfo user device ID number3

    User-configured string that identifies a USB device.


     Readable?
     Writable? no
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public int getUserSpecifiedString() throws JMCCULException {
        //todo the docs are probably wrong, this should probably use the getString instead of getInt
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIUSERDEVIDNUM
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIHIDELOGINDLG -> BI HIDE LOGIN DLG -> boardInfo hide login dialog
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     */
    public void setHideLoginDialog(boolean hide) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIHIDELOGINDLG,
                (hide ? 1 : 0)
        );

    }

}
