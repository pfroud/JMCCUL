package jmccul.jna;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

/**
 * https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/DaqDeviceDescriptor-type.htm
 *
 * <i>native declaration : C:\Users\Public\Documents\Measurement Computing\DAQ\C\cbw.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class DaqDeviceDescriptor extends Structure {

    /**
     * C type : CHAR[64]
     */
    public byte[] ProductName = new byte[64];
    /**
     * product ID
     */
    public int ProductID;
    /**
     * @see DaqDeviceInterface<br>
     * USB, BLUETOOTH, ...<br>
     * C type : DaqDeviceInterface
     */
    public int InterfaceType;
    /**
     * C type : CHAR[64]
     */
    public byte[] DevString = new byte[64];
    /**
     * unique identifier for device. Serial number for USB deivces and MAC address for bth and net devices<br>
     * C type : CHAR[64]
     */
    public byte[] UniqueID = new byte[64];
    /**
     * numeric representation of uniqueID
     */
    public long NUID;
    /**
     * reserved for the future.<br>
     * C type : CHAR[512]
     */
    public byte[] Reserved = new byte[512];

    public DaqDeviceDescriptor() {
        super();
    }

    /**
     * @param ProductName C type : CHAR[64]<br>
     * @param ProductID product ID<br>
     * @param InterfaceType @see DaqDeviceInterface<br>
     * USB, BLUETOOTH, ...<br>
     * C type : DaqDeviceInterface<br>
     * @param DevString C type : CHAR[64]<br>
     * @param UniqueID unique identifier for device. Serial number for USB deivces and MAC address for bth and net devices<br>
     * C type : CHAR[64]<br>
     * @param NUID numeric representation of uniqueID<br>
     * @param Reserved reserved for the future.<br>
     * C type : CHAR[512]
     */
    public DaqDeviceDescriptor(byte ProductName[], int ProductID, int InterfaceType, byte DevString[], byte UniqueID[], long NUID, byte Reserved[]) {
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
        return Arrays.asList("ProductName", "ProductID", "InterfaceType", "DevString", "UniqueID", "NUID", "Reserved");
    }

    @Override
    public String toString() {
        // author Peter Froud (not generated by JNAerator)
        final String interfaceTypeString;
        final String labelForUniqueID;
        switch (InterfaceType) {
            case MeasurementComputingUniversalLibrary.DaqDeviceInterface.USB_IFC:
                interfaceTypeString = "USB";
                labelForUniqueID = "factory serial number";
                break;
            case MeasurementComputingUniversalLibrary.DaqDeviceInterface.BLUETOOTH_IFC:
                interfaceTypeString = "Bluetooth";
                labelForUniqueID = "MAC address";
                break;
            case MeasurementComputingUniversalLibrary.DaqDeviceInterface.ETHERNET_IFC:
                interfaceTypeString = "Ethernet";
                labelForUniqueID = "MAC address";
                break;
            default:
                interfaceTypeString = "(unknown)";
                labelForUniqueID = "unknown";
                break;
        }

        return "product name \"" + new String(ProductName).trim() + "\" (productID " + ProductID
                + "); interface type " + interfaceTypeString + "; "
                + labelForUniqueID + " \"" + new String(UniqueID).trim() + "\" (" + NUID + ")";

    }

    public static class ByReference extends DaqDeviceDescriptor implements Structure.ByReference {

    };

    public static class ByValue extends DaqDeviceDescriptor implements Structure.ByValue {

    };
}
