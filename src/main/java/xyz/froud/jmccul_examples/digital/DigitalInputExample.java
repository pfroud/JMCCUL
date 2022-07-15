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

package xyz.froud.jmccul_examples.digital;

import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.DeviceDiscovery;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.digital.DigitalPort;
import xyz.froud.jmccul.digital.DigitalPortDirection;
import xyz.froud.jmccul.jna.DaqDeviceDescriptor;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Peter Froud
 */
public class DigitalInputExample {

    public static void main(String[] args) throws JMCCULException {

        final Optional<DeviceAndDigitalPort> optionalDeviceAndPort = findDeviceAndDigitalPortWhichSupportDigitalInput();

        if (optionalDeviceAndPort.isPresent()) {
            try (DeviceAndDigitalPort deviceAndPort = optionalDeviceAndPort.get()) {
                final DaqDevice device = deviceAndPort.device;
                final DigitalPort port = deviceAndPort.port;

                System.out.printf("Using device with model name %s, serial number %s, port %s.\n",
                        device.getBoardName(), device.getFactorySerialNumber(), port.getPortType());

                doDigitalInput(device, port);
            }
        } else {
            System.out.println("Didn't find a device which supports digital input.");
        }

    }

    private static void doDigitalInput(DaqDevice device, DigitalPort port) throws JMCCULException {

        if (port.isPortConfigurable()) {
            device.digital.configurePort(port.getPortType(), DigitalPortDirection.INPUT);
        }

        System.out.println("Reading the whole port:");
        final short portInput = device.digital.inputPort(port.getPortType());
        System.out.println("0b" + Integer.toBinaryString(portInput));

        System.out.println("Reading one bit at a time:");
        System.out.print("0b");
        for (int bitIndex = port.getBitCount() - 1; bitIndex >= 0; bitIndex--) {
            boolean bitInput = device.digital.inputBit(port.getPortType(), bitIndex);
            final char zeroOrOne = bitInput ? '1' : '0';
            System.out.print(zeroOrOne);
        }
        System.out.println();

    }

    private static Optional<DeviceAndDigitalPort> findDeviceAndDigitalPortWhichSupportDigitalInput() throws JMCCULException {
        final DaqDeviceDescriptor[] allDeviceDescriptors = DeviceDiscovery.findDescriptors();

        for (DaqDeviceDescriptor descriptor : allDeviceDescriptors) {
            final DaqDevice device = new DaqDevice(descriptor);

            if (device.digital.isDigitalIOSupported()) {

                final Optional<DigitalPort> optionalPortToUse
                        = Arrays.stream(device.digital.getPorts())
                        .filter(DigitalPort::isInputSupported)
                        .findFirst();

                if (optionalPortToUse.isPresent()) {
                    DeviceAndDigitalPort rv = new DeviceAndDigitalPort();
                    rv.device = device;
                    rv.port = optionalPortToUse.get();
                    return Optional.of(rv);
                }
            }
            device.close();
        }

        return Optional.empty();
    }

}
