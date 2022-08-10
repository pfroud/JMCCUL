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

package xyz.froud.jmccul_examples.analog_input;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;

import java.util.Optional;

/**
 * @author Peter Froud
 */
public class PrintAnalogInputInfo {

    public static void main(String[] args) throws JMCCULException {

        final Optional<DaqDevice> optionalDevice = DeviceDiscovery.findFirstDeviceMatching(
                d -> d.analog.input.isSupported()
        );

        if (optionalDevice.isPresent()) {
            try (DaqDevice device = optionalDevice.get()) {
                System.out.println("Analog input info for this device: " + device);
                System.out.println("getChannelCount = " + device.analog.input.getChannelCount());
                System.out.println("getResolution = " + device.analog.input.getResolution());
                System.out.println("getPacketSize = " + device.analog.input.getPacketSize());
                System.out.println("getTriggerResolution = " + device.analog.input.getTriggerResolution());
                System.out.println("getSupportedRanges = " + device.analog.input.getSupportedRanges());
                System.out.println("getTriggerRange = " + device.analog.input.getTriggerRange());
                System.out.println("isVoltageInputSupported = " + device.analog.input.isVoltageInputSupported());
                System.out.println("isAnalogTriggerSupported = " + device.analog.input.isTriggerSupported());
                System.out.println("isGainQueueSupported = " + device.analog.input.isGainQueueSupported());
                System.out.println("isScanSupported = " + device.analog.input.isScanSupported());
            }
        } else {
            System.out.println("Didn't find a device which supports analog input.");
        }

    }

}
