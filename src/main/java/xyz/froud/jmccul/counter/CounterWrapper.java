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
import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public class CounterWrapper {

    // https://github.com/mccdaq/mcculw/blob/master/mcculw/device_info/ctr_info.py
    private final DaqDevice DAQ_DEVICE;
    private final int BOARD_NUMBER;
    private Integer channelCount;

    public CounterWrapper(DaqDevice device) {
        DAQ_DEVICE = device;
        BOARD_NUMBER = device.getBoardNumber();
    }

    private int getChannelCount() throws JMCCULException {
        if (channelCount == null) {
            // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ctr_info.py#L28
            channelCount = Configuration.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    DAQ_DEVICE.getBoardNumber(),
                    0,
                    MeasurementComputingUniversalLibrary.BICINUMDEVS
            );
        }
        return channelCount;
    }

    public boolean isCounterSupported() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ctr_info.py#L33
        return getChannelCount() > 0;
    }

     /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     BICINUMDEVS -> BI CI NUM DEVS -> boardInfo counterInfo number of devices
     Readable? yes
     Writable? NO
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public int getCounterDeviceCount() throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                BOARD_NUMBER,
                0, //devNum is ignored
                MeasurementComputingUniversalLibrary.BICINUMDEVS
        );
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
        return Configuration.getInt(
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
        Configuration.setInt(
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
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.COUNTERINFO,
                BOARD_NUMBER,
                dev,
                MeasurementComputingUniversalLibrary.CICTRNUM
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     CICTRTYPE -> CI CTR TYPE -> counterInfo counter type
     Readable? yes
     Writable? NO

    Can't find anything in cbw.h for this. It says:
    Counter type, where:
    1 = 8254, 2 = 9513, 3 = 8536, 4 = 7266, 5 = event counter, 6 = scan counter,
    7 = timer counter, 8 = quadrature counter, and 9 = pulse counter.


     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     */
    public int getCounterType(int idx) throws JMCCULException {
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.COUNTERINFO,
                BOARD_NUMBER,
                idx,
                MeasurementComputingUniversalLibrary.CICTRTYPE
        );
    }


    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ctr_info.py#L45">CtrChanInfo</a>
     */
    class CounterChannel {

        int channelNumber;
        CounterChannelType type;
        Object scanOptions; //need to use bit flag
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions_for_NET/GetCtrType.htm">CtrConfig.GetCtrType()</a>
     */
    enum CounterChannelType {
    }

}
