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

package xyz.froud.jmccul.digital;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

public class DigitalOutputImpl {


    private final DaqDevice DAQ_DEVICE;
    private final int BOARD_NUMBER;

    public DigitalOutputImpl(DaqDevice device) {
        DAQ_DEVICE = device;
        BOARD_NUMBER = device.getBoardNumber();
    }


    /**
     * Sets the state of a single digital output bit.
     * <p>
     * This function treats all of the DIO ports of a particular type on a board as a single large port. It lets you set
     * the state of any individual bit within this large port.
     * <p>
     * Most configurable ports require configuration before writing. Check board-specific information to determine if
     * the port should be configured for your hardware. When configurable, use cbDConfigPort() to configure a port for
     * output, and cbDConfigBit() to configure a bit for output.
     *
     * @param portType There are three general types of digital ports — ports that are programmable as input or
     *         output, ports that are fixed input or output, and ports for which each bit may be programmed as input or
     *         output. For the first of these types, set PortType to FIRSTPORTA. For the latter two types, set PortType
     *         to AUXPORT. For devices with both types of digital ports, set PortType to either FIRSTPORTA or AUXPORT,
     *         depending on which digital port you want to set.
     * @param bitNumber The bit number within the single large port. The specified bit must be in a port that is
     *         configured for output.
     * @param value The value to set the bit to. False for logic low or true for logic high. Logic high does not
     *         necessarily mean 5 V – refer to the device hardware user guide for chip input specifications.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitOut.htm">cbDBitOut()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DBitOut.htm">DBitOut()</a>
     */
    public void bit(DigitalPortType portType, int bitNumber, boolean value) throws JMCCULException {
        final int zeroOrOne = value ? 1 : 0;
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDBitOut(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                bitNumber,
                (short) zeroOrOne);
        JMCCULUtils.checkError(errorCode);
    }

    /**
     * Writes a byte to a digital output port.
     * <p>
     * Most configurable ports require configuration before writing. Check the board-specific information in the
     * Universal Library User's Guide to determine if the port should be configured for your hardware. When
     * configurable, use cbDConfigPort() to configure a port for output.
     * <p>
     * Note: Port size can vary. The output value is 0 to 2^32–1 for a 32-bit port, 0 to 2^24–1 for a 24-bit port, 0 to
     * 65,535 for a 16-bit port, 0 to 255 for an 8-bit port, and 0 to 15 for a 4-bit port. When writing to a 32-bit
     * port, use cbDOut32().
     *
     * @param portType The digital port type. There are three general types of digital ports — ports that are
     *         programmable as input or output, ports that are fixed input or output, and ports for which each bit may
     *         be programmed as input or output. For the first of these types, set PortType to FIRSTPORTA. For the
     *         latter two types, set PortType to AUXPORT. For devices with both types of digital ports, set PortType to
     *         either FIRSTPORTA or AUXPORT, depending on the digital port you want to set.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut.htm">cbDOut()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DOut.htm">DOut()</a>
     */
    public void port(DigitalPortType portType, short value) throws JMCCULException {
        // Java short is 16-bit signed integer
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDOut(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                value);
        JMCCULUtils.checkError(errorCode);
    }

    /**
     * Writes a byte to a 32-bit digital output port.
     * <p>
     * Most configurable ports require configuration before writing. Check the board-specific information in the
     * Universal Library User's Guide to determine if the port should be configured for your hardware. When
     * configurable, use cbDConfigPort() to configure a port for output.
     * <p>
     * Note: Port size can vary. The output value is 0 to 2^32–1 for a 32-bit port, 0 to 2^24–1 for a 24-bit port, 0 to
     * 65,535 for a 16-bit port, 0 to 255 for an 8-bit port, and 0 to 15 for a 4-bit port.
     *
     * @param portType The digital port type. There are three general types of digital ports — ports that are
     *         programmable as input or output, ports that are fixed input or output, and ports for which each bit may
     *         be programmed as input or output. For the first of these types, set PortType to FIRSTPORTA. For the
     *         latter two types, set PortType to AUXPORT. For devices with both types of digital ports, set PortType to
     *         either FIRSTPORTA or AUXPORT, depending on the digital port you want to set.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut32.htm">cbDOut32()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DOut32.htm">DOut32()</a>
     */
    public void port32(DigitalPortType portType, int value) throws JMCCULException {
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDOut32(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                value);
        JMCCULUtils.checkError(errorCode);
    }

    //<editor-fold defaultstate="collapsed" desc="skeleton methods for Universal Library functions that I have not implemented">
    private void outputPortArray(DigitalPortType lowPort, DigitalPortType highPort, long[] dataArray) throws JMCCULException {

        /*
        TODO figure out how to get a long[] into a NativeLongByReference!!

        The JNAerator binding says the last argument

        The C# .NET function prototype gives us more information that the C function prototype.
        It says:
        public MccDaq.ErrorInfo DOutArray(
            MccDaq.DigitalPortType lowPort,
            MccDaq.DigitalPortType highPort,
            out int[] dataArray
        );
        https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DOutArray.htm
         */
        NativeLongByReference nativeLongByRef = new NativeLongByReference(new NativeLong());

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOutArray.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDOutArray(
                DAQ_DEVICE.getBoardNumber(),
                lowPort.VALUE,
                highPort.VALUE,
                nativeLongByRef
        );

        JMCCULUtils.checkError(errorCode);
    }


    private void outputPortScan(DigitalPortType portType, int count, long rateHz, int[] data) throws JMCCULException {
        // I do not have hardware that supports cbDOutScan() so I cannot test this method!

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        final MeasurementComputingUniversalLibrary.HGLOBAL windowsBuffer = MeasurementComputingUniversalLibrary.INSTANCE.cbWinBufAlloc(new NativeLong(count));

        // TODO figure out how to get data into the windows buffer
        final NativeLongByReference rateByReference = new NativeLongByReference(new NativeLong(rateHz));

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOutScan.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDOutScan(
                /* int BoardNum  */DAQ_DEVICE.getBoardNumber(),
                /* int portType  */ portType.VALUE,
                /* long Count    */ new NativeLong(count),
                /* long *Rate    */ rateByReference, //returns the value of the actual rate set, which may be different from the requested rate because of pacer limitations.
                /* int MemHandle */ windowsBuffer,
                /* int Options   */ 0
        );
        JMCCULUtils.checkError(errorCode);

    }
    //</editor-fold>

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDOTRIGCOUNT -> BI DO TRIG COUNT -> boardInfo digitalOutput trigger count
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDoRetrigCount.htm">BoardConfig.GetDoRetrigCount()</a>
     */
    public int getTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDOTRIGCOUNT
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDoRetrigCount.htm">BoardConfig.SetDoRetrigCount()</a>
     */
    public void setTriggerCount(int trigCount) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDOTRIGCOUNT,
                trigCount
        );
    }


}
