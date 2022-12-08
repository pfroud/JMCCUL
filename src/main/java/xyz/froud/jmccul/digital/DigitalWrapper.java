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

import java.util.Optional;
import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.config.ConfigurationWrapper;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public class DigitalWrapper {

    private final DaqDevice DAQ_DEVICE;
    private final int BOARD_NUMBER;

    /*
    Underscore prefix means the field is lazy-loaded in the getter method.
    It is a reminder to call the getter method instead of reading the field directly.
     */
    private DigitalPort[] _ports;
    private Integer _portCount;

    public final DigitalOutputWrapper output;
    public final DigitalInputWrapper input;

    public DigitalWrapper(DaqDevice device) {
        DAQ_DEVICE = device;
        BOARD_NUMBER = device.getBoardNumber();

        output = new DigitalOutputWrapper(device);
        input = new DigitalInputWrapper(device);

    }

    public DigitalPort[] getPorts() throws JMCCULException {
        if (_ports == null) {
            final int portCount = getPortCount();
            _ports = new DigitalPort[portCount];
            for (int i = 0; i < portCount; i++) {
                _ports[i] = new DigitalPort(DAQ_DEVICE, i);
            }
        }
        return _ports;
    }

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/GetDiNumDevs.htm">BoardConfig.GetDiNumDevs()</a>
     */
    public int getPortCount() throws JMCCULException {
        if (_portCount == null) {
            _portCount = ConfigurationWrapper.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    BOARD_NUMBER,
                    0,
                    MeasurementComputingUniversalLibrary.BIDINUMDEVS
            );
        }
        return _portCount;
    }

    /**
     * @see <a
     *         href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L39">is_supported
     * in dio_info.py</a>
     */
    public boolean isSupported() throws JMCCULException {
        return getPorts().length > 0;
    }

    public Optional<DigitalPort> findPortMatching(DaqDevice.PredicateThrowsJMCCULException<DigitalPort> predicate) throws JMCCULException {
        for (DigitalPort port : getPorts()) {
            if (predicate.test(port)) {
                return Optional.of(port);
            }
        }
        return Optional.empty();
    }

}
