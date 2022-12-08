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

package xyz.froud.jmccul;



import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 */
public enum DaqDeviceInterfaceType {

    USB(MeasurementComputingUniversalLibrary.DaqDeviceInterface.USB_IFC, "USB", "factory serial number"),
    BLUETOOTH(MeasurementComputingUniversalLibrary.DaqDeviceInterface.BLUETOOTH_IFC, "Bluetooth", "Bluetooth address"),
    ETHERNET(MeasurementComputingUniversalLibrary.DaqDeviceInterface.ETHERNET_IFC, "Ethernet", "MAC address"),
    ANY(MeasurementComputingUniversalLibrary.DaqDeviceInterface.ANY_IFC, null, null);


    private static final Map<Integer, DaqDeviceInterfaceType> valueMap;

    static {
        final DaqDeviceInterfaceType[] allEnumValues = DaqDeviceInterfaceType.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (DaqDeviceInterfaceType type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static DaqDeviceInterfaceType parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    /**
     * @see xyz.froud.jmccul.jna.DaqDeviceDescriptor#toString()
     */
    public final String DISPLAY_NAME;
    /**
     * @see xyz.froud.jmccul.jna.DaqDeviceDescriptor#UniqueID
     * @see xyz.froud.jmccul.jna.DaqDeviceDescriptor#toString()
     */
    public final String LABEL_FOR_UNIQUE_ID;

    private DaqDeviceInterfaceType(int value, String displayName, String labelForUniqueID) {
        VALUE = value;
        DISPLAY_NAME = displayName;
        LABEL_FOR_UNIQUE_ID = labelForUniqueID;
    }

    public static int bitwiseOr(DaqDeviceInterfaceType[] interfaces) {
        return Arrays.stream(interfaces).map(e -> e.VALUE).reduce(0, (i1, i2) -> i1 | i2);
    }

}
