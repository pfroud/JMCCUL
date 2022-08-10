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

package xyz.froud.jmccul.analog;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 * @see AnalogInputWrapper#getAnalogInputChannelType(int)
 * @see AnalogInputWrapper#setAnalogInputChannelType(int, AnalogInputChannelType)
 */
public enum AnalogInputChannelType {

    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
    VOLTAGE(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_VOLTAGE),
    CURRENT(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_CURRENT),
    RESISTANCE_4WIRE_10KOHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RESISTANCE_10K4W),
    RESISTANCE_4WIRE_1KOHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RESISTANCE_1K4W),
    RESISTANCE_2WIRE_10KOHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RESISTANCE_10K2W),
    RESISTANCE_2WIRE_1KOHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RESISTANCE_1K2W),
    THERMOCOUPLE(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_TC),
    RTD_4WIRE_1KOHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RTD_1000OHM_4W),
    RTD_4WIRE_100OHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RTD_100OHM_4W),
    RTD_3WIRE_1KOHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RTD_1000OHM_3W),
    RTD_3WIRE_100OHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_RTD_100OHM_3W),
    QUARTER_BRIDGE_350OHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_QUART_BRIDGE_350OHM),
    QUARTER_BRIDGE_120OHM(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_QUART_BRIDGE_120OHM),
    HALF_BRIDGE(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_HALF_BRIDGE),
    FULL_BRIDGE_62_POINT_5_MILLIVOLTS_PER_VOLT(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_FULL_BRIDGE_62PT5mVV),
    FULL_BRIDGE_7_POINT_8_MILLIVOLTS_PER_VOLT(MeasurementComputingUniversalLibrary.AI_CHAN_TYPE_FULL_BRIDGE_7PT8mVV);

    private static final Map<Integer, AnalogInputChannelType> valueMap;

    static {
        final AnalogInputChannelType[] allEnumValues = AnalogInputChannelType.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (AnalogInputChannelType type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static AnalogInputChannelType parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    AnalogInputChannelType(int value) {
        VALUE = value;
    }

}
