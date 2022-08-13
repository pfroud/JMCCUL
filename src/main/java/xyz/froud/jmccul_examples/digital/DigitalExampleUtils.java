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
import xyz.froud.jmccul.jna.DaqDeviceDescriptor;

import java.util.Optional;

public class DigitalExampleUtils {

    /** Same as {@link java.util.function.Predicate} but it throws a JMCCUL exception. */
    @FunctionalInterface
    interface PredicateThrowsJMCCULException<T> {
        boolean test(T t) throws JMCCULException;
    }

    static Optional<DigitalPort> findPortWhichSupports(PredicateThrowsJMCCULException<DigitalPort> predicate) throws JMCCULException {
        final DaqDeviceDescriptor[] allDeviceDescriptors = DaqDeviceDiscovery.findDescriptors();

        for (DaqDeviceDescriptor descriptor : allDeviceDescriptors) {
            final DaqDevice device = new DaqDevice(descriptor);
            if (device.digital.isSupported()) {
                for (DigitalPort port : device.digital.getPorts()) {
                    if (predicate.test(port)) {
                        return Optional.of(port);
                    }
                }
            }
            // Checked all ports on the device, none satisfy the predicate.
            device.close();
        }
        // Checked all devices, none have a port that satisfies the predicate.
        return Optional.empty();
    }

}
