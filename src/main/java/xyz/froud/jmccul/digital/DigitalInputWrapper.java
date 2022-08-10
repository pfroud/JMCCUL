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

package xyz.froud.jmccul.digital;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class DigitalInputWrapper {

    private final DaqDevice DAQ_DEVICE;
    private final int BOARD_NUMBER;

    public DigitalInputWrapper(DaqDevice device) {
        DAQ_DEVICE = device;
        BOARD_NUMBER = device.getBoardNumber();
    }


    /**
     * Reads the state of a single digital input bit.
     * <p>
     * This function treats all of the DIO ports of a particular type on a board as a single port. It lets you read the
     * state of any individual bit within this port. Note that with some port types, such as 8255 ports, if the port is
     * configured for DIGITALOUT, cbDBitIn provides readback of the last output value.
     *
     * @param portType There are three general types of digital ports — ports that are programmable as input or
     *         output, ports that are fixed input or output, and ports for which each bit may be programmed as input or
     *         output. For the first of these types, set PortType to FIRSTPORTA. For the latter two types, set PortType
     *         to AUXPORT. For devices with both types of digital ports, set PortType to either FIRSTPORTA or AUXPORT,
     *         depending on which digital inputs you wish to read.
     * @param bitNumber Specifies the bit number within the single large port.
     *
     * @return False for logic low or true for logic high. Logic high does not necessarily mean 5 V – refer to the
     *         device hardware user guide for chip input specifications.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitIn.htm">cbDBitIn()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DBitIn.htm">DBitIn()</a>
     */
    public boolean bit(DigitalPortType portType, int bitNumber) throws JMCCULException {
        final ShortBuffer buf = ShortBuffer.allocate(1);
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDBitIn(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                bitNumber,
                buf
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get() == 1;
    }

    /**
     * Reads a digital input port.
     * <p>
     * Note that for some port types, such as 8255 ports, if the port is configured for DIGITALOUT, this function will
     * provide readback of the last output value.
     * <p>
     * Note: Port size can vary. The return value is 0 to 2^32–1 for a 32-bit port, 0 to 2^24–1 for a 24-bit port, 0 to
     * 65,535 for a 16-bit port, 0 to 255 for an 8-bit port, and 0 to 15 for a 4-bit port. To read a 32-bit port, use
     * cbDIn32().
     *
     * @param portType The digital port to read. Some hardware allows readback of the state of the output using
     *         this function; refer to the board-specific information in the Universal Library User's Guide.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn.htm">cbDIn()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DIn.htm">DIn()</a>
     */
    public short port(DigitalPortType portType) throws JMCCULException {
        final ShortBuffer buf = ShortBuffer.allocate(1);
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDIn(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                buf
        );
        JMCCULUtils.checkError(errorCode);
        // Java short is 16-bit signed integer
        return buf.get();
    }

    /**
     * Reads a 32-bit digital input port.
     * <p>
     * Note that for some port types, such as 8255 ports, if the port is configured for DIGITALOUT, this function will
     * provide readback of the last output value.
     * <p>
     * Note: Port size can vary. The return value is 0 to 2^32–1 for a 32-bit port, 0 to 2^24–1 for a 24-bit port, 0 to
     * 65,535 for a 16-bit port, 0 to 255 for an 8-bit port, and 0 to 15 for a 4-bit port.
     *
     * @param portType The digital port to read. Some hardware allows readback of the output state using this
     *         function; refer to board-specific information in the Universal Library User's Guide.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn32.htm">cbDIn32()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DIn32.htm">DIn32()</a>
     */
    public int port32(DigitalPortType portType) throws JMCCULException {
        final IntBuffer buf = IntBuffer.allocate(1);
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDIn32(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                buf
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get();
    }

    //<editor-fold defaultstate="collapsed" desc="skeleton methods for Universal Library functions that I have not implemented">
    private int inputPortArray(DigitalPortType lowPort, DigitalPortType highPort) throws JMCCULException {

        /*
        TODO figure out how to do this

        The JNAerator binding is not very helpful. The last argument is a ULONG* which probably
        means we need to allocate a LongBuffer or something and then pass the first element.

        The C# .NET function prototype gives us more information than the C function prototype.
        it says:
        public MccDaq.ErrorInfo DInArray(
            MccDaq.DigitalPortType lowPort,
            MccDaq.DigitalPortType highPort,
            out int[] dataArray
        );
        https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DInArray.htm
         */
        NativeLongByReference nativeLongByRef = new NativeLongByReference(new NativeLong());

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInArray.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDInArray(
                DAQ_DEVICE.getBoardNumber(),
                lowPort.VALUE,
                highPort.VALUE,
                nativeLongByRef
        );
        JMCCULUtils.checkError(errorCode);
        return -1;
    }

    private short[] inputPortScan(DigitalPortType portType, int count, long rateHz) throws JMCCULException {
        /*
        I do not have hardware that supports cbDInScan() so I cannot test this method!
        TODO why does this return shorts? What about the port size / resolution?
         */
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        final MeasurementComputingUniversalLibrary.HGLOBAL windowsBuffer = MeasurementComputingUniversalLibrary.INSTANCE.cbWinBufAlloc(new NativeLong(count));

        final NativeLongByReference rateByReference = new NativeLongByReference(new NativeLong(rateHz));

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInScan.htm
        final int errorCodeScan = MeasurementComputingUniversalLibrary.INSTANCE.cbDInScan(
                /* int BoardNum  */DAQ_DEVICE.getBoardNumber(),
                /* int portType  */ portType.VALUE,
                /* long Count    */ new NativeLong(count),
                /* long *Rate    */ rateByReference, //returns the value of the actual rate set, which may be different from the requested rate because of pacer limitations.
                /* int MemHandle */ windowsBuffer,
                /* int Options   */ 0
        );

        JMCCULUtils.checkError(errorCodeScan);

        final ShortBuffer javaBuffer = ShortBuffer.allocate(count);

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufToArray.htm
        final int errorCodeBufferConversion = MeasurementComputingUniversalLibrary.INSTANCE.cbWinBufToArray(
                /* int MemHandle     */windowsBuffer,
                /* double* DataArray */ javaBuffer,
                /* long FirstPoint   */ new NativeLong(0),
                /* long Count        */ new NativeLong(count)
        );

        JMCCULUtils.checkError(errorCodeBufferConversion);

        return javaBuffer.array();
    }
    //</editor-fold>

       /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDISOFILTER -> BI DI ISO FILTER -> boardInfo digitalInput isolation(?) filter
     Readable? yes
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDisoFilter.htm">BoardConfig.GetDisoFilter()</a>
     */
    public boolean getAcFilter(int bitNumber) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                bitNumber,
                MeasurementComputingUniversalLibrary.BIDISOFILTER
        ) == 1;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDisoFilter.htm">BoardConfig.SetDisoFilter()</a>
     */
    public void setAcFilter(int bitNumber, boolean enable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                bitNumber,
                MeasurementComputingUniversalLibrary.BIDISOFILTER,
                (enable ? 1 : 0)
        );
    }


    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDIDEBOUNCETIME -> BI DI DEBOUNCE TIME -> boardInfo digitalInput debounce time
     Readable? NO
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     */
    public void setDebounceTime(int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDIDEBOUNCETIME,
                n
        );
    }

        /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDIDEBOUNCESTATE -> BI DI DEBOUNCE STATE  -> boardInfo digitalInput debounce state
     Readable? NO
     Writable? yes
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     */
    public void setDebounceState(int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDIDEBOUNCESTATE,
                n
        );
    }

         /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BIDITRIGCOUNT -> BI DI TRIG COUNT -> boardInfo digitalInput trigger count
     Readable? yes
     Writable? yes
     */

    /**
     * Returns the number of digital input samples to acquire during each trigger event when ScanOptions.RetrigMode is
     * enabled.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDiRetrigCount.htm">BoardConfig.GetDiRetrigCount()</a>
     */
    public int getTriggerCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDITRIGCOUNT
        );
    }

    /**
     * Sets the number of digital input samples to acquire during each trigger event when ScanOptions.RetrigMode is
     * enabled.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDiRetrigCount.htm">BoardConfig.SetDiRetrigCount()</a>
     */
    public void setTriggerCount(int count) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDITRIGCOUNT,
                count
        );
    }

}
