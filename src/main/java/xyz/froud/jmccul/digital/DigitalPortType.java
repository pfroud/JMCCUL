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

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 * @see <a
 *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Enumerations/MccDaq.DigitalPortType.htm">MccDaq.DigitalPortType</a>
 */
public enum DigitalPortType {
    AUX_PORT(MeasurementComputingUniversalLibrary.AUXPORT),
    AUX_PORT_0(MeasurementComputingUniversalLibrary.AUXPORT0),
    AUX_PORT_1(MeasurementComputingUniversalLibrary.AUXPORT1),
    AUX_PORT_2(MeasurementComputingUniversalLibrary.AUXPORT2),
    /////////////////////////////////////////////////////////////////////////////
    FIRST_PORT_A(MeasurementComputingUniversalLibrary.FIRSTPORTA),
    FIRST_PORT_B(MeasurementComputingUniversalLibrary.FIRSTPORTB),
    FIRST_PORT_C(MeasurementComputingUniversalLibrary.FIRSTPORTC),
    FIRST_PORT_C_LOW(MeasurementComputingUniversalLibrary.FIRSTPORTCL),
    FIRST_PORT_C_HIGH(MeasurementComputingUniversalLibrary.FIRSTPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    SECOND_PORT_A(MeasurementComputingUniversalLibrary.SECONDPORTA),
    SECOND_PORT_B(MeasurementComputingUniversalLibrary.SECONDPORTB),
    SECOND_PORT_C_LOW(MeasurementComputingUniversalLibrary.SECONDPORTCL),
    SECOND_PORT_C_HIGH(MeasurementComputingUniversalLibrary.SECONDPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    THIRD_PORT_A(MeasurementComputingUniversalLibrary.THIRDPORTA),
    THIRD_PORT_B(MeasurementComputingUniversalLibrary.THIRDPORTB),
    THIRD_PORT_C_LOW(MeasurementComputingUniversalLibrary.THIRDPORTCL),
    THIRD_PORT_C_HIGH(MeasurementComputingUniversalLibrary.THIRDPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    FOURTH_PORT_A(MeasurementComputingUniversalLibrary.FOURTHPORTA),
    FOURTH_PORT_B(MeasurementComputingUniversalLibrary.FOURTHPORTB),
    FOURTH_PORT_C_LOW(MeasurementComputingUniversalLibrary.FOURTHPORTCL),
    FOURTH_PORT_C_HIGH(MeasurementComputingUniversalLibrary.FOURTHPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    FIFTH_PORT_A(MeasurementComputingUniversalLibrary.FIFTHPORTA),
    FIFTH_PORT_B(MeasurementComputingUniversalLibrary.FIFTHPORTB),
    FIFTH_PORT_C_LOW(MeasurementComputingUniversalLibrary.FIFTHPORTCL),
    FIFTH_PORT_C_HIGH(MeasurementComputingUniversalLibrary.FIFTHPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    SIXTH_PORT_A(MeasurementComputingUniversalLibrary.SIXTHPORTA),
    SIXTH_PORT_B(MeasurementComputingUniversalLibrary.SIXTHPORTB),
    SIXTH_PORT_C_LOW(MeasurementComputingUniversalLibrary.SIXTHPORTCL),
    SIXTH_PORT_C_HIGH(MeasurementComputingUniversalLibrary.SIXTHPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    SEVENTH_PORT_A(MeasurementComputingUniversalLibrary.SEVENTHPORTA),
    SEVENTH_PORT_B(MeasurementComputingUniversalLibrary.SEVENTHPORTB),
    SEVENTH_PORT_C_LOW(MeasurementComputingUniversalLibrary.SEVENTHPORTCL),
    SEVENTH_PORT_C_HIGH(MeasurementComputingUniversalLibrary.SEVENTHPORTCH),
    /////////////////////////////////////////////////////////////////////////////
    EIGHTH_PORT_A(MeasurementComputingUniversalLibrary.EIGHTHPORTA),
    EIGHTH_PORT_B(MeasurementComputingUniversalLibrary.EIGHTHPORTB),
    EIGHTH_PORT_C_LOW(MeasurementComputingUniversalLibrary.EIGHTHPORTCL),
    EIGHTH_PORT_C_HIGH(MeasurementComputingUniversalLibrary.EIGHTHPORTCH);

    private static final Map<Integer, DigitalPortType> valueMap;

    static {
        final DigitalPortType[] allTypes = DigitalPortType.values();
        valueMap = new HashMap<>(allTypes.length, 1);
        for (DigitalPortType type : allTypes) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static DigitalPortType parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    DigitalPortType(int value) {
        VALUE = value;

    }
}
