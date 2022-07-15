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
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary.HGLOBAL;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * @author Peter Froud
 */
public class DigitalImpl {

    private final DaqDevice DAQ_DEVICE;
    private final int BOARD_NUMBER;

    private DigitalPort[] ports;
    private Integer portCount;

    public DigitalImpl(DaqDevice device) {
        DAQ_DEVICE = device;
        BOARD_NUMBER = device.getBoardNumber();
    }

    public DigitalPort[] getPorts() throws JMCCULException {
        if (ports == null) {
            final int portCount = getPortCount();
            ports = new DigitalPort[portCount];
            for (int i = 0; i < portCount; i++) {
                ports[i] = new DigitalPort(DAQ_DEVICE, i);
            }
        }
        return ports;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDiNumDevs.htm">BoardConfig.GetDiNumDevs()</a>
     */
    public int getPortCount() throws JMCCULException {
        if (portCount == null) {
            portCount = Configuration.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    DAQ_DEVICE.getBoardNumber(),
                    0,
                    MeasurementComputingUniversalLibrary.BIDINUMDEVS
            );
        }
        return portCount;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L39">is_supported
     *         in dio_info.py</a>
     */
    public boolean isDigitalIOSupported() throws JMCCULException {
        return getPorts().length > 0;
    }

    /**
     * Configures a specific digital bit for input or output. This function treats all DIO ports on a board as a single
     * port (AUXPORT); it is not supported by 8255 type DIO ports.
     *
     * @param portType The port (AUXPORT) whose bits are to be configured. The port specified must be bitwise
     * @param bitNumber The bit number to configure as input or output. configurable.
     * @param direction DIGITALOUT or DIGITALIN configures the specified bit for output or input, respectively.
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigBit.htm">cbDConfigBit()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DConfigBit.htm">DConfigBit()</a>
     */
    public void configureBit(DigitalPortType portType, int bitNumber, DigitalPortDirection direction) throws JMCCULException {
        // configure an individual bit. Not all devices support doing this.
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigBit(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                bitNumber,
                direction.VALUE
        );
        JMCCULUtils.checkError(errorCode);
    }

    /**
     * Configures a digital port as input or output.
     * <p>
     * This function is for use with ports that may be programmed as input or output, such as those on the 82C55 chips
     * and 8536 chips. Refer to the 82C55A data sheet (82C55A.pdf) for details of chip operation. This document is
     * installed in the Documents subdirectory where the UL is installed. Refer to the Zilog 8536 manual for details of
     * 8536 chip operation.
     * <p>
     * Note: When used on ports within an 8255 chip, this function will reset all ports on that chip configured for
     * output to a zero state. This means that if you set an output value on FIRSTPORTA and then change the
     * configuration on FIRSTPORTB from OUTPUT to INPUT, the output value at FIRSTPORTA will be all zeros. You can,
     * however, set the configuration on SECONDPORTx without affecting the value at FIRSTPORTA. For this reason, this
     * function is usually called at the beginning of the program for each port requiring configuration.
     *
     * @param portType The port to configure. The port must be configurable. For most boards, AUXPORT is not
     *         configurable. Check the board-specific information in the Universal Library User's Guide for details.
     * @param direction DIGITALOUT or DIGITALIN configures the entire eight or four bit port for output or
     *         input.
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigPort.htm">cbDConfigPort()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DConfigPort.htm">DConfigPort()</a>
     */
    public void configurePort(DigitalPortType portType, DigitalPortDirection direction) throws JMCCULException {
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigPort(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                direction.VALUE
        );
        JMCCULUtils.checkError(errorCode);
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
     * @return False for logic low or true for logic high. Logic high does not necessarily mean 5 V – refer to the
     *         device hardware user guide for chip input specifications.
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitIn.htm">cbDBitIn()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DBitIn.htm">DBitIn()</a>
     */
    public boolean inputBit(DigitalPortType portType, int bitNumber) throws JMCCULException {
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
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn.htm">cbDIn()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DIn.htm">DIn()</a>
     */
    public short inputPort(DigitalPortType portType) throws JMCCULException {
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
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn32.htm">cbDIn32()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DIn32.htm">DIn32()</a>
     */
    public int inputPort32(DigitalPortType portType) throws JMCCULException {
        final IntBuffer buf = IntBuffer.allocate(1);
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDIn32(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                buf
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get();
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
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitOut.htm">cbDBitOut()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DBitOut.htm">DBitOut()</a>
     */
    public void outputBit(DigitalPortType portType, int bitNumber, boolean value) throws JMCCULException {
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
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut.htm">cbDOut()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DOut.htm">DOut()</a>
     */
    public void outputPort(DigitalPortType portType, short value) throws JMCCULException {
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
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut32.htm">cbDOut32()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DOut32.htm">DOut32()</a>
     */
    public void outputPort32(DigitalPortType portType, int value) throws JMCCULException {
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDOut32(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                value);
        JMCCULUtils.checkError(errorCode);
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

    private short[] inputPortScan(DigitalPortType portType, int count, long rateHz) throws JMCCULException {
        /*
        I do not have hardware that supports cbDInScan() so I cannot test this method!
        TODO why does this return shorts? What about the port size / resolution?
         */
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        final HGLOBAL windowsBuffer = MeasurementComputingUniversalLibrary.INSTANCE.cbWinBufAlloc(new NativeLong(count));

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

    private void outputPortScan(DigitalPortType portType, int count, long rateHz, int[] data) throws JMCCULException {
        // I do not have hardware that supports cbDOutScan() so I cannot test this method!

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        final HGLOBAL windowsBuffer = MeasurementComputingUniversalLibrary.INSTANCE.cbWinBufAlloc(new NativeLong(count));

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
    public int getDigitalInputTriggerCount() throws JMCCULException {
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
    public void setDigitalInputTriggerCount(int count) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDITRIGCOUNT,
                count
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
    public void setDigitalInputDebounceState(int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDIDEBOUNCESTATE,
                n
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
    public void setDigitalInputDebounceTime(int n) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDIDEBOUNCETIME,
                n
        );
    }

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
    public boolean getDigitalInputAcFilter(int bitNumber) throws JMCCULException {
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
    public void setDigitalInputAcFilter(int bitNumber, boolean enable) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                bitNumber,
                MeasurementComputingUniversalLibrary.BIDISOFILTER,
                (enable ? 1 : 0)
        );
    }

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
    public int getDigitalOutputTriggerCount() throws JMCCULException {
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
    public void setDigitalOutputTriggerCount(int trigCount) throws JMCCULException {
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BIDOTRIGCOUNT,
                trigCount
        );
    }

}
