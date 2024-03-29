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
import xyz.froud.jmccul.digital.DigitalPort;
import xyz.froud.jmccul.digital.DigitalPortDirection;

import java.util.Optional;

/**
 * @author Peter Froud
 */
public class DigitalOutputExample {

    public static void main(String[] args) throws JMCCULException {

        final Optional<DigitalPort> optionalPort = DaqDevice.findPortMatching(DigitalPort::isOutputSupported);

        if (optionalPort.isPresent()) {
            final DigitalPort port = optionalPort.get();
            try ( DaqDevice device = port.getDaqDevice()) {
                System.out.printf("Using device with model name %s, serial number %s, port %s.\n",
                        device.getBoardName(), device.getFactorySerialNumber(), port.getPortType());

                doDigitalOutput(device, port);
            }

        } else {
            System.out.println("Didn't find a device which supports digital output.");
        }

    }

    /**
     * Writes some arbitrary values to the port.
     *
     * @see <a href="https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html">Primitive Data Types</a>
     */
    private static void doDigitalOutput(DaqDevice device, DigitalPort port) throws JMCCULException {

        if (port.isDirectionOfEntirePortSettable()) {
            port.setPortDirection(DigitalPortDirection.OUTPUT);
        }

        // short is sixteen bits
        final short valueToWrite = (short) 0b1011_0110_0110_0110;
        System.out.println("Writing this value to the whole port: 0b" + Integer.toBinaryString(valueToWrite));
        device.digital.output.writePort(port.getPortType(), valueToWrite);

        // TODO probably need to check the port resolution before trying to write 32-bit value
        // int is thirty-two bits
        final int intToWriteInt = 0b1111_1111_1111_1111_1111_1111_1111_1111;
        System.out.println("Writing this value to the whole port: 0b" + Integer.toBinaryString(intToWriteInt));
        device.digital.output.writePort(port.getPortType(), valueToWrite);

        System.out.println("Now setting each bit to logic-high individually");
        for (int bitIdx = 0; bitIdx < port.getResolution(); bitIdx++) {
            port.setBitDirection(bitIdx, DigitalPortDirection.OUTPUT);
            device.digital.output.writeBit(port.getPortType(), bitIdx, true);
        }

    }

}
