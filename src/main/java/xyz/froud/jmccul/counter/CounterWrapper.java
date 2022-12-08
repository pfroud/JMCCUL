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
package xyz.froud.jmccul.counter;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.config.ConfigurationWrapper;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 * @see <a href="https://github.com/mccdaq/mcculw/blob/master/mcculw/device_info/ctr_info.py">ctr_info.py</a>
 */
public class CounterWrapper {

    private final DaqDevice DAQ_DEVICE;
    private final int BOARD_NUMBER;

    /*
    Underscore prefix means the field is lazy-loaded in the getter method.
    It is a reminder to call the getter method instead of reading the field directly.
     */
    private Integer _channelCount;
    private CounterDevice[] _devices;

    public CounterWrapper(DaqDevice device) {
        DAQ_DEVICE = device;
        BOARD_NUMBER = device.getBoardNumber();
    }

    /**
     *
     * @return how many counters the DAQ device has.
     */
    public int getDeviceCount() throws JMCCULException {
        if (_channelCount == null) {
            // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ctr_info.py#L28
            _channelCount = ConfigurationWrapper.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    DAQ_DEVICE.getBoardNumber(),
                    0,
                    MeasurementComputingUniversalLibrary.BICINUMDEVS
            );
        }
        return _channelCount;
    }

    public boolean isSupported() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ctr_info.py#L33
        return getDeviceCount() > 0;
    }

    public CounterDevice[] getDevices() throws JMCCULException {
        if (_devices == null) {
            final int deviceCount = getDeviceCount();
            _devices = new CounterDevice[deviceCount];
            for (int i = 0; i < deviceCount; i++) {
                _devices[i] = new CounterDevice(DAQ_DEVICE, i);
            }
        }
        return _devices;
    }


    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BICTRTRIGCOUNT -> BI CTR TRIG COUNT -> boardInfo counter trigger count
     Readable? yes
     Writable? NO
     */
    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public int getCounterTriggerCount() throws JMCCULException {
        return ConfigurationWrapper.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BICTRTRIGCOUNT
        );
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm">cbSetConfig()</a>
     */
    public void setCounterTriggerCount(int trigCount) throws JMCCULException {
        ConfigurationWrapper.setInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BICTRTRIGCOUNT,
                trigCount
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     CICTRNUM -> CI CTR NUM -> counterInfo counter number
     Readable? yes
     Writable? NO
     */
    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public int getCounterNumber(int dev) throws JMCCULException {
        return ConfigurationWrapper.getInt(
                MeasurementComputingUniversalLibrary.COUNTERINFO,
                BOARD_NUMBER,
                dev,
                MeasurementComputingUniversalLibrary.CICTRNUM
        );
    }



}
