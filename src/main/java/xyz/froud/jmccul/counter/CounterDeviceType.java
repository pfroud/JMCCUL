/*
 * The MIT License
 *
 * Copyright 2022 lumenetix.
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
package xyz.froud.jmccul.counter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Can't find anything in cbw.h for this. It says: Counter type, where: 1 = 8254, 2 = 9513, 3 = 8536, 4 = 7266, 5 =
 * event counter, 6 = scan counter, 7 = timer counter, 8 = quadrature counter, and 9 = pulse counter.
 *
 * @see <a
 *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
 * @see <a
 *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions_for_NET/GetCtrType.htm">CtrConfig.GetCtrType()</a>
 *
 * @author PeterFroud
 */
public enum CounterDeviceType {

    COUNTER_8254(1),
    COUNTER_9513(2),
    COUNTER_8536(3),
    COUNTER_7266(4),
    /**
     * @see <a href="https://kb.mccdaq.com/knowledgebasearticle50475.aspx">Event Counter Basics</a>
     */
    EVENT_COUNTER(5),
    SCAN_COUNTER(6),
    TIMER_COUNTER(7),
    QUADRATURE_COUNTER(8),
    PULSE_COUNTER(9);

    private static final Map<Integer, CounterDeviceType> valueMap;

    static {
        final CounterDeviceType[] allEnumValues = CounterDeviceType.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (CounterDeviceType type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static CounterDeviceType parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    private CounterDeviceType(int value) {
        VALUE = value;
    }

}
