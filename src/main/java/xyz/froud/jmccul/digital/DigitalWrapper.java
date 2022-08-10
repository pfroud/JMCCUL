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

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public class DigitalWrapper {

    private final DaqDevice DAQ_DEVICE;
    private final int BOARD_NUMBER;

    private DigitalPort[] ports;
    private Integer portCount;

    public final DigitalOutputWrapper output;
    public final DigitalInputWrapper input;

    public DigitalWrapper(DaqDevice device) {
        DAQ_DEVICE = device;
        BOARD_NUMBER = device.getBoardNumber();

        output = new DigitalOutputWrapper(device);
        input = new DigitalInputWrapper(device);


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
                    BOARD_NUMBER,
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
    public boolean isSupported() throws JMCCULException {
        return getPorts().length > 0;
    }

    /**
     * Configures a specific digital bit for input or output. This function treats all DIO ports on a board as a single
     * port (AUXPORT); it is not supported by 8255 type DIO ports.
     *
     * @param portType The port (AUXPORT) whose bits are to be configured. The port specified must be bitwise
     * @param bitNumber The bit number to configure as input or output. configurable.
     * @param direction DIGITALOUT or DIGITALIN configures the specified bit for output or input, respectively.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigBit.htm">cbDConfigBit()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DConfigBit.htm">DConfigBit()</a>
     */
    public void configureBit(DigitalPortType portType, int bitNumber, DigitalPortDirection direction) throws JMCCULException {
        // configure an individual bit. Not all devices support doing this.
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigBit(
                BOARD_NUMBER,
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
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigPort.htm">cbDConfigPort()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DConfigPort.htm">DConfigPort()</a>
     */
    public void configurePort(DigitalPortType portType, DigitalPortDirection direction) throws JMCCULException {
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigPort(
                BOARD_NUMBER,
                portType.VALUE,
                direction.VALUE
        );
        JMCCULUtils.checkError(errorCode);
    }


}
