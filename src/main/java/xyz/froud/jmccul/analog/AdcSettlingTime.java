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

import xyz.froud.jmccul.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 * @see AnalogInputWrapper#getSettlingTime()
 * @see AnalogInputWrapper#setSettlingTime(AdcSettlingTime)
 */
public enum AdcSettlingTime {

    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
    DEFAULT(MeasurementComputingUniversalLibrary.SETTLE_DEFAULT),
    ONE_MICROSECOND(MeasurementComputingUniversalLibrary.SETTLE_1us),
    FIVE_MICROSECONDS(MeasurementComputingUniversalLibrary.SETTLE_5us),
    TEN_MICROSECONDS(MeasurementComputingUniversalLibrary.SETTLE_10us),
    ONE_MILLISECOND(MeasurementComputingUniversalLibrary.SETTLE_1ms);

    private static final Map<Integer, AdcSettlingTime> valueMap;

    static {
        final AdcSettlingTime[] allEnumValues = AdcSettlingTime.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (AdcSettlingTime type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static AdcSettlingTime parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    AdcSettlingTime(int value) {
        VALUE = value;
    }

}
