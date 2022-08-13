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

package xyz.froud.jmccul_examples.digital;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DaqDeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.digital.DigitalPort;

import java.util.Optional;

/**
 * @author Peter Froud
 */
public class PrintDigitalInfo {

    public static void main(String[] args) throws JMCCULException {

        final Optional<DaqDevice> optionalDevice = DaqDeviceDiscovery.findFirstDeviceMatching(
                d -> d.digital.isSupported()
        );

        if (optionalDevice.isPresent()) {
            try (DaqDevice device = optionalDevice.get()) {
                System.out.println("Digital I/O info for this device: " + device);

                final DigitalPort[] ports = device.digital.getPorts();

                System.out.printf("There are %d digital port(s):\n", ports.length);
                for (int i = 0; i < ports.length; i++) {
                    final DigitalPort port = ports[i];
                    System.out.printf("Port %d / %d: %s\n", i + 1, ports.length, port.toString());
                    System.out.println("   getBitCount    = " + port.getBitCount());
                    System.out.println("   getInputMask  = " + port.getInputMask());
                    System.out.println("   getOutputMask = " + port.getOutputMask());
                    System.out.println("   getFirstBit   = " + port.getFirstBit());
                    System.out.println("   isOutputSupported  = " + port.isOutputSupported());
                    System.out.println("   isInputSupported   = " + port.isInputSupported());
                    System.out.println("   isPortConfigurable = " + port.isPortConfigurable());
                    System.out.println("   isBitConfigurable  = " + port.isIndividualBitConfigurable());
                    System.out.println("   isInputScanSupported  = " + port.isInputScanSupported());
                    System.out.println("   isOutputScanSupported = " + port.isOutputScanSupported());
                }
            }
        } else {
            System.out.println("Didn't find a device which supports digital I/O.");
        }

    }

}
