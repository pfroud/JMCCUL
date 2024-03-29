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

package xyz.froud.jmccul_examples;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.analog.AnalogRange;

import java.util.Optional;

/**
 * @author Peter Froud
 */
public class AnalogOutputExample {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) throws JMCCULException {

        final Optional<DaqDevice> optionalDevice = DaqDevice.findFirstMatching(
                device -> device.analog.output.isSupported()
        );

        if (optionalDevice.isPresent()) {
            try (DaqDevice device = optionalDevice.get()) {
                System.out.println("Opened this device: " + device);
                doAnalogOutput(device, 0);
                doVoltageOutput(device, 0);
            }
        } else {
            System.out.println("Didn't find a DAQ device which supports analog output.");
        }

    }



    private static void doAnalogOutput(DaqDevice device, int channel) throws JMCCULException {

        final AnalogRange rangeToUse = device.analog.output.getSupportedRanges().get(0);

        final int max = (1 << device.analog.output.getResolution()) - 1;
        final int middle = (int) Math.round(max / 2.0);

        device.analog.output.write(channel, rangeToUse, (short) 0);
        delay(1000);
        device.analog.output.write(channel, rangeToUse, (short) middle);
        delay(1000);
        device.analog.output.write(channel, rangeToUse, (short) max);
        delay(1000);
        device.analog.output.write(channel, rangeToUse, (short) middle);
        delay(1000);
        device.analog.output.write(channel, rangeToUse, (short) 0);

    }

    private static void doVoltageOutput(DaqDevice device, int channel) throws JMCCULException {

        final AnalogRange rangeToUse = device.analog.output.getSupportedRanges().get(0);
        final double middle = ((rangeToUse.MAXIMUM - rangeToUse.MINIMUM) / 2.0) + rangeToUse.MINIMUM;

        device.analog.output.writeVoltage(channel, rangeToUse, (float) rangeToUse.MINIMUM);
        delay(1000);
        device.analog.output.writeVoltage(channel, rangeToUse, (float) middle);
        delay(1000);
        device.analog.output.writeVoltage(channel, rangeToUse, (float) rangeToUse.MAXIMUM);
        delay(1000);
        device.analog.output.writeVoltage(channel, rangeToUse, (float) middle);
        delay(1000);
        device.analog.output.writeVoltage(channel, rangeToUse, (float) rangeToUse.MINIMUM);

    }

    private static void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
