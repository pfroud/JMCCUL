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

package xyz.froud.jmccul.jna;

import com.sun.jna.Structure;
import xyz.froud.jmccul.DaqDeviceInterfaceType;

import java.util.Arrays;
import java.util.List;

/**
 * <i>native declaration : C:\Users\Public\Documents\Measurement Computing\DAQ\C\cbw.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br> a tool written by <a
 * href="http://ochafik.com/">Olivier Chafik</a> that <a
 * href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br> For help,
 * please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a
 * href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 *
 * @see <a
 *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/DaqDeviceDescriptor-type.htm">DaqDeviceDescriptor</a>
 */
public class DaqDeviceDescriptor extends Structure {

    /**
     * Product name of the detected device.
     */
    public byte[] ProductName = new byte[64];

    /**
     * A number associated with the detected device.
     */
    public int ProductID;

    /**
     * The bus type associated with the detected DAQ device.
     *
     * @see MeasurementComputingUniversalLibrary.DaqDeviceInterface
     */
    public int InterfaceType;

    /**
     * A character array associated with the detected device. For Ethernet devices, this value represents a NetBIOS
     * name. This value may be the same as the ProductName on some devices, but may contain more specific information on
     * other devices.
     */
    public byte[] DevString = new byte[64];

    /**
     * A string identifier that indicates the serial number of a detected USB device, or the MAC address of a detected
     * Bluetooth or Ethernet device.
     */
    public byte[] UniqueID = new byte[64];

    /**
     * Numeric representation of the unique identifier of the detected device.
     */
    public long NUID;

    /**
     * Reserved for the future use.
     */
    public byte[] Reserved = new byte[512];

    public DaqDeviceDescriptor() {
        super();
    }

    public DaqDeviceDescriptor(byte[] ProductName, int ProductID, int InterfaceType, byte[] DevString, byte[] UniqueID, long NUID, byte[] Reserved) {
        super();
        if ((ProductName.length != this.ProductName.length)) {
            throw new IllegalArgumentException("Wrong array size !");
        }
        this.ProductName = ProductName;
        this.ProductID = ProductID;
        this.InterfaceType = InterfaceType;
        if ((DevString.length != this.DevString.length)) {
            throw new IllegalArgumentException("Wrong array size !");
        }
        this.DevString = DevString;
        if ((UniqueID.length != this.UniqueID.length)) {
            throw new IllegalArgumentException("Wrong array size !");
        }
        this.UniqueID = UniqueID;
        this.NUID = NUID;
        if ((Reserved.length != this.Reserved.length)) {
            throw new IllegalArgumentException("Wrong array size !");
        }
        this.Reserved = Reserved;
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("ProductName", "ProductID", "DaqDeviceInterfaceType", "DevString", "UniqueID", "NUID", "Reserved");
    }

    @Override
    public String toString() {
        final DaqDeviceInterfaceType theInterface = DaqDeviceInterfaceType.parseInt(InterfaceType);
        return "product name \"" + getProductName() + "\" (productID " + ProductID
                + "); interface type " + theInterface.DISPLAY_NAME + "; "
                + theInterface.LABEL_FOR_UNIQUE_ID + " \"" + getUniqueID() + "\" (" + NUID + ")";
    }

    public String getUniqueID() {
        return new String(UniqueID).trim();
    }

    public String getProductName() {
        return new String(ProductName).trim();
    }

    public static class ByReference extends DaqDeviceDescriptor implements Structure.ByReference {

    }

    public static class ByValue extends DaqDeviceDescriptor implements Structure.ByValue {

    }
}
