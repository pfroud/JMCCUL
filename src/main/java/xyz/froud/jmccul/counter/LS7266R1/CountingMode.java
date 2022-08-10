/*
 * The MIT License
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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package xyz.froud.jmccul.counter.LS7266R1;


import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum CountingMode {

    /**
     * Each counter operates as a 24 bit counter that rolls over to 0 when the maximum count is reached.
     */
    NORMAL(MeasurementComputingUniversalLibrary.NORMAL_MODE),
    /**
     * In range limit count mode, an upper an lower limit is set, mimicking limit switches in the mechanical counterpart. The upper limit is set by loading the PRESET register with the cbCLoad() function after the counter has been configured. The lower limit is always 0. When counting up, the counter freezes whenever the count reaches the value that was loaded into the PRESET register. When counting down, the counter freezes at 0. In either case the counting is resumed only when the count direction is reversed.
     */
    RANGE_LIMIT(MeasurementComputingUniversalLibrary.RANGE_LIMIT),
    /**
     * In non-recycle mode the counter is disabled whenever a count overflow or underflow takes place. The counter is re-enabled when a reset or load operation is performed on the counter.
     */
    NO_RECYCLE(MeasurementComputingUniversalLibrary.NO_RECYCLE),
    /**
     * In modulo-n mode, an upper limit is set by loading the PRESET register with a maximum count. Whenever counting up, when the maximum count is reached, the counter will roll-over to 0 and continue counting up. Likewise when counting down, whenever the count reaches 0, it will roll over to the maximum count (in the PRESET register) and continue counting down.
     */
    MODULO_N(MeasurementComputingUniversalLibrary.MODULO_N);


    private static final Map<Integer, CountingMode> valueMap;

    static {
        final CountingMode[] allEnumValues = CountingMode.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (CountingMode type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static CountingMode parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private CountingMode(int value) {
        VALUE = value;
    }

}
