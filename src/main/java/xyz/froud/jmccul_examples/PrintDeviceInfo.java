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
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.jna.DaqDeviceDescriptor;

/**
 * @author Peter Froud
 */
public class PrintDeviceInfo {

    public static void main(String[] args) throws JMCCULException {
        final DaqDeviceDescriptor[] descriptors = DeviceDiscovery.findDescriptors();
        if (descriptors.length == 0) {
            System.out.println("No daq devices found!");
        } else {

            for (int i = 0; i < descriptors.length; i++) {
                DaqDeviceDescriptor descriptor = descriptors[i];

                System.out.printf("This is device %d / %d\n", i + 1, descriptors.length);
                try (DaqDevice device = new DaqDevice(descriptor)) {
                    System.out.println("  BOARD NAME = " + device.getBoardName());
                    System.out.println("  FACTORY_SERIAL_NUMBER = " + device.getFactorySerialNumber());
//                    System.out.println("  PRODUCT_ID = " + device.PRODUCT_ID);
                    // TODO look at the user device identifier thing
                    System.out.println("  Is digital I/O supported?   " + device.digital.isSupported());
                    System.out.println("  Is analog input supported?  " + device.analogInput.isAnalogInputSupported());
                    System.out.println("  Is analog output supported? " + device.analogOutput.isAnalogOutputSupported());
                    System.out.println("  Is temperature supported?   " + device.temperature.isTemperatureInputSupported());
                    System.out.println("  Is counter supported?       " + device.counter.isCounterSupported());
                }
                System.out.println();

            }

        }
    }
}
