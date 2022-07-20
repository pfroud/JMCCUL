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

package xyz.froud.jmccul.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 * @see <a
 *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetSyncMode.htm">BoardConfig.SetSyncMode()</a>
 */
public enum SyncMode {

    /** Output the internal D/A Load signal */
    OUTPUT(0),

    /** Receive the D/A Load signal from an external source */
    RECEIVE(1);

    private static final Map<Integer, SyncMode> valueMap;

    static {
        final SyncMode[] allEnumValues = SyncMode.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (SyncMode type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static SyncMode parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    SyncMode(int value) {
        VALUE = value;
    }

}
