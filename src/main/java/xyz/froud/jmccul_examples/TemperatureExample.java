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
import xyz.froud.jmccul.MeasurementComputingUniversalLibrary;
import xyz.froud.jmccul.temperature.TemperatureUnit;

import java.util.Optional;
import xyz.froud.jmccul.temperature.ThermocoupleType;

/**
 * @author Peter Froud
 */
public class TemperatureExample {

    public static void main(String[] args) throws JMCCULException {

        final Optional<DaqDevice> optionalDevice = DaqDevice.findFirstMatching(
                device -> device.temperature.isSupported()
        );

        if (optionalDevice.isPresent()) {
            try ( DaqDevice device = optionalDevice.get()) {
                System.out.println("Opened this device: " + device);
                doThermocoupleInput(device, 0);
            }
        } else {
            System.out.println("Didn't find a DAQ device which supports temperature input.");
        }

    }


    private static void doThermocoupleInput(DaqDevice device, int channel) throws JMCCULException {
        try {
            device.temperature.setThermocoupleType(channel, ThermocoupleType.K);

            // TODO why can we set units here and in the read() method??
            device.temperature.setUnits(TemperatureUnit.CELSIUS);

            final float temperature = device.temperature.read(channel, TemperatureUnit.CELSIUS);
            System.out.printf("Temperature is %f C\n", temperature);
        } catch (JMCCULException ex) {
            if (ex.ERROR_CODE == MeasurementComputingUniversalLibrary.OPENCONNECTION) {
                System.out.println("The thermocouple connection is open.");
            } else {
                throw ex;
            }
        }
    }

}
