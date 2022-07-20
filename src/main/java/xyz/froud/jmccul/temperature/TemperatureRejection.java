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

package xyz.froud.jmccul.temperature;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum TemperatureRejection {

    NOT_USED(MeasurementComputingUniversalLibrary.NOTUSED),
    OFF(0),
    FIFTY_HERTZ(50),
    SIXTY_HERTZ(60);

    private static final Map<Integer, TemperatureRejection> valueMap;

    static {
        final TemperatureRejection[] allEnumValues = TemperatureRejection.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (TemperatureRejection type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static TemperatureRejection parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    TemperatureRejection(int value) {
        VALUE = value;
    }

}
