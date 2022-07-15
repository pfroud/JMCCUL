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

import java.nio.ShortBuffer;

/**
 * @author Peter Froud
 * @see <a
 *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L50">class
 *         PortInfo in dio_info.py</a>
 */
public class DigitalPort {

    private final DaqDevice DAQ_DEVICE;
    public final int PORT_INDEX;

    private DigitalPortType portType;
    private Integer bitCount;
    private Integer inputMask;
    private Integer outputMask;
    private Boolean isPortConfigurable;
    private Boolean isIndividualBitConfigurable;
    private Boolean isInputScanSupported;
    private Boolean isOutputScanSupported;

    DigitalPort(DaqDevice device, int portIndex) {
        DAQ_DEVICE = device;
        PORT_INDEX = portIndex;
    }

    @Override
    public String toString() {
        return portType.name();
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDevType.htm">DioConfig.GetDevType()</a>
     */
    public DigitalPortType getPortType() throws JMCCULException {
        if (portType == null) {
            final int portTypeInt = getConfigItem(MeasurementComputingUniversalLibrary.DIDEVTYPE);
            portType = DigitalPortType.parseInt(portTypeInt);
        }
        return portType;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetNumBits.htm">DioConfig.GetNumBits()</a>
     */

    public int getBitCount() throws JMCCULException {
        if (bitCount == null) {
            bitCount = getConfigItem(MeasurementComputingUniversalLibrary.DINUMBITS);
        }
        return bitCount;
    }

    /**
     * Use getInputMask() and getOutputMask() to to determine if an AuxPort is configurable. If you apply both methods
     * to the same port, and both configVal parameters returned have input and output bits that overlap, the port is not
     * configurable. You can determine overlapping bits by ANDing both parameters.
     * <p>
     * For example, the PCI-DAS08 board has seven bits of digital I/O (four outputs and three inputs). For this board,
     * getInputMask() always returns 7 (0b0111) and getOutputMask() always returns 15 (0b1111). When you And those
     * values together, you get a non-zero number (7). Any non-zero number indicates that input and output bits overlap
     * for the specified port, and that port is a non-configurable AuxPort.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDInMask.htm">DioConfig.GetDInMask()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L61">in_mask
     *         in class PortInfo in dio_info.py</a>
     */
    public int getInputMask() throws JMCCULException {
        if (inputMask == null) {
            inputMask = getConfigItem(MeasurementComputingUniversalLibrary.DIINMASK);
        }
        return inputMask;
    }

    /**
     * Use getInputMask() and getOutputMask() to to determine if an AuxPort is configurable. If you apply both methods
     * to the same port, and both configVal parameters returned have input and output bits that overlap, the port is not
     * configurable. You can determine overlapping bits by ANDing both parameters.
     * <p>
     * For example, the PCI-DAS08 board has seven bits of digital I/O (four outputs and three inputs). For this board,
     * getInputMask() always returns 7 (0b0111) and getOutputMask() always returns 15 (0b1111). When you And those
     * values together, you get a non-zero number (7). Any non-zero number indicates that input and output bits overlap
     * for the specified port, and that port is a non-configurable AuxPort.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDOutMask.htm">DioConfig.GetDOutMask()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L66">out_mask
     *         in class PortInfo in dio_info.py</a>
     */

    public int getOutputMask() throws JMCCULException {
        if (outputMask == null) {
            outputMask = getConfigItem(MeasurementComputingUniversalLibrary.DIOUTMASK);
        }
        return outputMask;
    }

    private int getConfigItem(int item) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.DIGITALINFO,
                DAQ_DEVICE.getBoardNumber(),
                PORT_INDEX,
                item
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetCurVal.htm">DioConfig.GetCurVal</a>
     */

    public int getPresentValue() throws JMCCULException {
        return getConfigItem(MeasurementComputingUniversalLibrary.DICURVAL);
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/IsDirectionCheckDisabled.htm">DioConfig.IsDirectionCheckDisabled()</a>
     */
    public boolean getDirectionCheckEnabled() throws JMCCULException {
        final boolean isDisabled = (getConfigItem(MeasurementComputingUniversalLibrary.DIDISABLEDIRCHECK) == 1);
        return !isDisabled;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetDirectionCheckDisabled.htm">DioConfig.SetDirectionCheckDisabled()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     */
    public void setDirectionCheckEnabled(boolean enable) throws JMCCULException {
        final boolean disable = !enable;
        Configuration.setInt(
                MeasurementComputingUniversalLibrary.DIGITALINFO,
                DAQ_DEVICE.getBoardNumber(),
                PORT_INDEX,
                MeasurementComputingUniversalLibrary.DIDISABLEDIRCHECK,
                (disable ? 1 : 0)
        );

    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L77">first_bit
     *         in class PortInfo in dio_info.py</a>
     */
    public int getFirstBit() {
        /*
        A few devices (USB-SSR08 for example) start at FIRSTPORTCL and
        number the bits as if FIRSTPORTA and FIRSTPORTB exist for
        compatibility with older digital peripherals.
         */
        if ((PORT_INDEX == 0) && (portType == DigitalPortType.FIRST_PORT_C_LOW)) {
            return 16;
        } else {
            return 0;
        }
    }

    /**
     * @see #getInputMask()
     * @see #getOutputMask()
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L113">is_bit_configurable
     *         in class PortInfo in dio_info.py</a>
     */
    public boolean isIndividualBitConfigurable() {
        if (isIndividualBitConfigurable == null) {

            if ((inputMask & outputMask) == 0) {
                // TODO simplify nested if statements
                // AUXPORT might be configurable, check if we can configure it
                if (portType == DigitalPortType.AUX_PORT) {
                    try {
                        // check if we can configure an individual bit in the port

                        final int errorCode1 = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigBit(DAQ_DEVICE.getBoardNumber(), portType.VALUE, getFirstBit(), DigitalPortDirection.OUTPUT.VALUE);
                        JMCCULUtils.checkError(errorCode1);

                        final int errorCode2 = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigBit(DAQ_DEVICE.getBoardNumber(), portType.VALUE, getFirstBit(), DigitalPortDirection.INPUT.VALUE);
                        JMCCULUtils.checkError(errorCode2);

                        isIndividualBitConfigurable = true;
                    } catch (JMCCULException ex) {
                        isIndividualBitConfigurable = false;
                    }
                } else {
                    // portType is not AUX_PORT
                    isIndividualBitConfigurable = false;
                }
            } else {
                // (inputMask & outputMask) is not zero
                isIndividualBitConfigurable = false;
            }
        }
        return isIndividualBitConfigurable;
    }

    /**
     * @see #getInputMask()
     * @see #getOutputMask()
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L130">is_port_configurable
     *         in class PortInfo in dio_info.py</a>
     */
    public boolean isPortConfigurable() throws JMCCULException {
        if (isPortConfigurable == null) {
            if ((inputMask & outputMask) == 0) {
                try {
                    // check if we can configure the port

                    final int errorCode1 = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigPort(DAQ_DEVICE.getBoardNumber(), portType.VALUE, DigitalPortDirection.OUTPUT.VALUE);
                    JMCCULUtils.checkError(errorCode1);

                    final int errorCode2 = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigPort(DAQ_DEVICE.getBoardNumber(), portType.VALUE, DigitalPortDirection.INPUT.VALUE);
                    JMCCULUtils.checkError(errorCode2);

                    isPortConfigurable = true;
                } catch (JMCCULException ex) {
                    ex.throwIfErrorIsNetworkDeviceInUse();
                    isPortConfigurable = false;
                }
            } else {
                isPortConfigurable = false;
            }
        }
        return isPortConfigurable;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L87">supports_input
     *         in class PortInfo in dio_info.py</a>
     */
    public boolean isInputSupported() {
        return (inputMask > 0) || isPortConfigurable;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L109">supports_output
     *         in class PortInfo in dio_info.py</a>
     */
    public boolean isOutputSupported() {
        return (outputMask > 0) || isPortConfigurable;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L91">supports_input_scan
     *         in class PortInfo in dio_info.py</a>
     */
    public boolean isInputScanSupported() throws JMCCULException {
        if (isInputScanSupported == null) {
        /*
        TODO what does "scan" mean? Is it the same as "synchronous"?
        Table of cbGetStatus()/cbGetIOStatus() arguments:
            DIFUNCTION      Specifies digital input scans started with cbDInScan().
            DOFUNCTION      Specifies digital output scans started with cbDOutScan().
            DAQIFUNCTION    Specifies a synchronous input scan started with cbDaqInScan().
            DAQOFUNCTION    Specifies a synchronous output scan started with cbDaqOutScan().
        Blog post:
        https://www.mccdaq.com/blog/2018/01/11/how-to-synchronous-analog-digital-and-encoder-measurements-in-labview/
         */
            try {
                // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetStatus.htm
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetIOStatus(
                        DAQ_DEVICE.getBoardNumber(),
                        ShortBuffer.allocate(1),
                        new NativeLongByReference(new NativeLong(0)),
                        new NativeLongByReference(new NativeLong(0)),
                        MeasurementComputingUniversalLibrary.DIFUNCTION
                );
                JMCCULUtils.checkError(errorCode);
                isInputScanSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                isInputScanSupported = false;
            }
        }
        return isInputScanSupported;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L100">supports_output_scan
     *         in class PortInfo in dio_info.py</a>
     */
    public boolean isOutputScanSupported() throws JMCCULException {
        if (isOutputScanSupported == null) {
        /*
        TODO what does "scan" mean? Is it the same as "synchronous"?
        Table of cbGetStatus()/cbGetIOStatus() arguments:
            DIFUNCTION      Specifies digital input scans started with cbDInScan().
            DOFUNCTION      Specifies digital output scans started with cbDOutScan().
            DAQIFUNCTION    Specifies a synchronous input scan started with cbDaqInScan().
            DAQOFUNCTION    Specifies a synchronous output scan started with cbDaqOutScan().
        Blog post:
        https://www.mccdaq.com/blog/2018/01/11/how-to-synchronous-analog-digital-and-encoder-measurements-in-labview/
         */
            try {
                // // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetStatus.htm
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetIOStatus(
                        DAQ_DEVICE.getBoardNumber(),
                        ShortBuffer.allocate(1),
                        new NativeLongByReference(new NativeLong(0)),
                        new NativeLongByReference(new NativeLong(0)),
                        MeasurementComputingUniversalLibrary.DOFUNCTION
                );
                JMCCULUtils.checkError(errorCode);
                isOutputScanSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                isOutputScanSupported = false;
            }
        }
        return isOutputScanSupported;
    }
}
