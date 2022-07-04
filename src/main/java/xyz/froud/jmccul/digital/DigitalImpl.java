package xyz.froud.jmccul.digital;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary.HGLOBAL;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * @author Peter Froud
 */
public class DigitalImpl {

    private final DaqDevice DAQ_DEVICE;

    private DigitalPort[] ports;
    private Integer portCount;

    public DigitalImpl(DaqDevice device) {
        DAQ_DEVICE = device;
    }

    public DigitalPort[] getPorts() throws JMCCULException {
        if (ports == null) {
            final int portCount = getPortCount();
            ports = new DigitalPort[portCount];
            for (int i = 0; i < portCount; i++) {
                ports[i] = new DigitalPort(DAQ_DEVICE, i);
            }
        }
        return ports;
    }

    public int getPortCount() throws JMCCULException {
        if (portCount == null) {
            // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L29
            // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm
            portCount = Configuration.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    DAQ_DEVICE.getBoardNumber(),
                    0,
                    MeasurementComputingUniversalLibrary.BIDINUMDEVS
            );
        }
        return portCount;
    }

    public boolean isDigitalIOSupported() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L39
        return getPorts().length > 0;
    }

    public void configureBit(DigitalPortType portType, int bitNumber, DigitalPortDirection direction) throws JMCCULException {
        // configure an individual bit. Not all devices support doing this.
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigBit.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigBit(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                bitNumber,
                direction.VALUE
        );
        JMCCULUtils.checkError(errorCode);
    }

    public void configurePort(DigitalPortType portType, DigitalPortDirection direction) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigPort.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDConfigPort(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                direction.VALUE
        );
        JMCCULUtils.checkError(errorCode);
    }

    public boolean inputBit(DigitalPortType portType, int bitNumber) throws JMCCULException {
        final ShortBuffer buf = ShortBuffer.allocate(1);
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitIn.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDBitIn(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                bitNumber,
                buf
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get() == 1;
    }

    public short inputPort(DigitalPortType portType) throws JMCCULException {
        final ShortBuffer buf = ShortBuffer.allocate(1);
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDIn(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                buf
        );
        JMCCULUtils.checkError(errorCode);
        // Java short is 16-bit signed integer
        return buf.get();
    }

    public int inputPort32(DigitalPortType portType) throws JMCCULException {
        final IntBuffer buf = IntBuffer.allocate(1);
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn32.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDIn32(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                buf
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get();
    }

    public void outputBit(DigitalPortType portType, int bitNumber, boolean value) throws JMCCULException {
        final int zeroOrOne = value ? 1 : 0;
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitOut.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDBitOut(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                bitNumber,
                (short) zeroOrOne);
        JMCCULUtils.checkError(errorCode);
    }

    public void outputPort(DigitalPortType portType, short value) throws JMCCULException {
        // Java short is 16-bit signed integer
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDOut(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                value);
        JMCCULUtils.checkError(errorCode);
    }

    public void outputPort32(DigitalPortType portType, int value) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut32.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDOut32(
                DAQ_DEVICE.getBoardNumber(),
                portType.VALUE,
                value);
        JMCCULUtils.checkError(errorCode);
    }

    //<editor-fold defaultstate="collapsed" desc="skeleton methods for Universal Library functions that I have not implemented">
    private int inputPortArray(DigitalPortType lowPort, DigitalPortType highPort) throws JMCCULException {

        /*
        TODO figure out how to do this

        The JNAerator binding is not very helpful. The last argument is a ULONG* which probably
        means we need to allocate a LongBuffer or something and then pass the first element.

        The C# .NET function prototype gives us more information than the C function prototype.
        it says:
        public MccDaq.ErrorInfo DInArray(
            MccDaq.DigitalPortType lowPort,
            MccDaq.DigitalPortType highPort,
            out int[] dataArray
        );
        https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DInArray.htm
         */
        NativeLongByReference nativeLongByRef = new NativeLongByReference(new NativeLong());

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInArray.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDInArray(
                DAQ_DEVICE.getBoardNumber(),
                lowPort.VALUE,
                highPort.VALUE,
                nativeLongByRef
        );
        JMCCULUtils.checkError(errorCode);
        return -1;
    }

    private void outputPortArray(DigitalPortType lowPort, DigitalPortType highPort, long[] dataArray) throws JMCCULException {

        /*
        TODO figure out how to get a long[] into a NativeLongByReference!!

        The JNAerator binding says the last argument

        The C# .NET function prototype gives us more information that the C function prototype.
        It says:
        public MccDaq.ErrorInfo DOutArray(
            MccDaq.DigitalPortType lowPort,
            MccDaq.DigitalPortType highPort,
            out int[] dataArray
        );
        https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DOutArray.htm
         */
        NativeLongByReference nativeLongByRef = new NativeLongByReference(new NativeLong());

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOutArray.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDOutArray(
                DAQ_DEVICE.getBoardNumber(),
                lowPort.VALUE,
                highPort.VALUE,
                nativeLongByRef
        );

        JMCCULUtils.checkError(errorCode);
    }

    private short[] inputPortScan(DigitalPortType portType, int count, long rateHz) throws JMCCULException {
        /*
        I do not have hardware that supports cbDInScan() so I cannot test this method!
        TODO why does this return shorts? What about the port size / resolution?
         */
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        final HGLOBAL windowsBuffer = MeasurementComputingUniversalLibrary.INSTANCE.cbWinBufAlloc(new NativeLong(count));

        final NativeLongByReference rateByReference = new NativeLongByReference(new NativeLong(rateHz));

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInScan.htm
        final int errorCodeScan = MeasurementComputingUniversalLibrary.INSTANCE.cbDInScan(
                /* int BoardNum  */DAQ_DEVICE.getBoardNumber(),
                /* int portType  */ portType.VALUE,
                /* long Count    */ new NativeLong(count),
                /* long *Rate    */ rateByReference, //returns the value of the actual rate set, which may be different from the requested rate because of pacer limitations.
                /* int MemHandle */ windowsBuffer,
                /* int Options   */ 0
        );

        JMCCULUtils.checkError(errorCodeScan);

        final ShortBuffer javaBuffer = ShortBuffer.allocate(count);

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufToArray.htm
        final int errorCodeBufferConversion = MeasurementComputingUniversalLibrary.INSTANCE.cbWinBufToArray(
                /* int MemHandle     */windowsBuffer,
                /* double* DataArray */ javaBuffer,
                /* long FirstPoint   */ new NativeLong(0),
                /* long Count        */ new NativeLong(count)
        );

        JMCCULUtils.checkError(errorCodeBufferConversion);

        return javaBuffer.array();
    }

    private void outputPortScan(DigitalPortType portType, int count, long rateHz, int[] data) throws JMCCULException {
        // I do not have hardware that supports cbDOutScan() so I cannot test this method!

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        final HGLOBAL windowsBuffer = MeasurementComputingUniversalLibrary.INSTANCE.cbWinBufAlloc(new NativeLong(count));

        // TODO figure out how to get data into the windows buffer
        final NativeLongByReference rateByReference = new NativeLongByReference(new NativeLong(rateHz));

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOutScan.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbDOutScan(
                /* int BoardNum  */DAQ_DEVICE.getBoardNumber(),
                /* int portType  */ portType.VALUE,
                /* long Count    */ new NativeLong(count),
                /* long *Rate    */ rateByReference, //returns the value of the actual rate set, which may be different from the requested rate because of pacer limitations.
                /* int MemHandle */ windowsBuffer,
                /* int Options   */ 0
        );
        JMCCULUtils.checkError(errorCode);

    }
    //</editor-fold>

}
