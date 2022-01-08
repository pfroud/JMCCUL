package jmccul.digital;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import jmccul.Configuration;
import jmccul.DaqDevice;
import jmccul.JMCCULException;
import static jmccul.JMCCULUtils.checkError;
import jmccul.jna.MeasurementComputingUniversalLibrary;
import jmccul.jna.MeasurementComputingUniversalLibrary.HGLOBAL;

/**
 *
 * @author Peter Froud
 */
public class DigitalImpl {

    /*
    TODO give these methods better names!!
     */
    private final MeasurementComputingUniversalLibrary LIBRARY = MeasurementComputingUniversalLibrary.INSTANCE;
    private final DaqDevice DAQ_DEVICE;

    public final DigitalPort[] PORTS;

    public DigitalImpl(DaqDevice device) throws JMCCULException {
        DAQ_DEVICE = device;
        PORTS = initPorts();

    }

    private DigitalPort[] initPorts() throws JMCCULException {
        final int portCount = getPortCount();
        DigitalPort[] rv = new DigitalPort[portCount];
        for (int i = 0; i < portCount; i++) {
            rv[i] = new DigitalPort(DAQ_DEVICE, i);
        }
        return rv;
    }

    private int getPortCount() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L29
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                DAQ_DEVICE.BOARD_NUMBER,
                0,
                MeasurementComputingUniversalLibrary.BIDINUMDEVS);
    }

    public boolean isDigitalIOSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/dio_info.py#L39
        return PORTS.length > 0;
    }

    public boolean bitInput(DigitalPortType portType, int bitNumber) throws JMCCULException {
        ShortBuffer buf = ShortBuffer.allocate(1);
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitIn.htm
        checkError(LIBRARY.cbDBitIn(
                DAQ_DEVICE.BOARD_NUMBER,
                portType.VALUE,
                bitNumber,
                buf)
        );
        return buf.get() == 1;
    }

    public void bitOutput(DigitalPortType portType, int bitNumber, boolean value) throws JMCCULException {
        int zeroOrOne = value ? 1 : 0;
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitOut.htm
        checkError(LIBRARY.cbDBitOut(
                DAQ_DEVICE.BOARD_NUMBER,
                portType.VALUE,
                bitNumber,
                (short) zeroOrOne)
        );
    }

    public void configureIndividualBit(DigitalPortType portType, int bitNumber, DigitalPortDirection direction) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigBit.htm
        checkError(LIBRARY.cbDConfigBit(
                DAQ_DEVICE.BOARD_NUMBER,
                portType.VALUE,
                bitNumber,
                direction.VALUE)
        );
    }

    public void configurePort(DigitalPortType portType, DigitalPortDirection direction) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigPort.htm
        checkError(LIBRARY.cbDConfigPort(
                DAQ_DEVICE.BOARD_NUMBER,
                portType.VALUE,
                direction.VALUE)
        );
    }

    public int portInput(DigitalPortType portType) throws JMCCULException {
        // TODO I think I need to check the resolution to see how many shorts I need
        ShortBuffer buf = ShortBuffer.allocate(1);
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn.htm
        checkError(LIBRARY.cbDIn(
                DAQ_DEVICE.BOARD_NUMBER,
                portType.VALUE,
                buf)
        );
        return buf.get();
    }

    public int portInput32(DigitalPortType portType) throws JMCCULException {
        IntBuffer buf = IntBuffer.allocate(1);
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn32.htm
        checkError(LIBRARY.cbDIn32(
                DAQ_DEVICE.BOARD_NUMBER,
                portType.VALUE,
                buf)
        );
        return buf.get();
    }

    private int portInputArray(DigitalPortType lowPort, DigitalPortType highPort) throws JMCCULException {

        /*
        This method will not work as written.

        The JNAerator binding is not very helpful. The last argument is a ULONG* which probably
        means we need to allocate a LongBuffer or something and then pass the first element.

        There are two C# functions:
        https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DInArray.htm
            public MccDaq.ErrorInfo DInArray(MccDaq.DigitalPortType lowPort, MccDaq.DigitalPortType highPort, out int[] dataArray)

         */
        var nlbr = new NativeLongByReference(new NativeLong());

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInArray.htm
        checkError(LIBRARY.cbDInArray(
                DAQ_DEVICE.BOARD_NUMBER,
                lowPort.VALUE,
                highPort.VALUE,
                nlbr)
        );
//        return buf.get();
        return 0;
    }

    private short[] portInputScan(DigitalPortType portType, int count, long rateHz) throws JMCCULException {
        /*
        I do not have hardware that supports this function so I cannot test it!
        TODO why does this return shorts? what about the port size / resolution?
         */
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        final HGLOBAL windowsBuffer = LIBRARY.cbWinBufAlloc(new NativeLong(count));

        final NativeLongByReference rateByReference = new NativeLongByReference(new NativeLong(rateHz));

        checkError(
                // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInScan.htm
                LIBRARY.cbDInScan(
                        /* int BoardNum  */DAQ_DEVICE.BOARD_NUMBER,
                        /* int portType  */ portType.VALUE,
                        /* long Count    */ new NativeLong(count),
                        /* long *Rate    */ rateByReference, //returns the value of the actual rate set, which may be different from the requested rate because of pacer limitations.
                        /* int MemHandle */ windowsBuffer,
                        /* int Options   */ 0
                )
        );

        final ShortBuffer javaBuffer = ShortBuffer.allocate(count);

        checkError(
                // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufToArray.htm
                LIBRARY.cbWinBufToArray(
                        /* int MemHandle     */windowsBuffer,
                        /* double* DataArray */ javaBuffer,
                        /* long FirstPoint   */ new NativeLong(0),
                        /* long Count        */ new NativeLong(count)
                )
        );

        return javaBuffer.array();
    }

    public void portOutput(DigitalPortType portType, short value) throws JMCCULException {
        //TODO why does this use a short? the C prototype uses unsigned short.
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut.htm
        checkError(LIBRARY.cbDOut(
                DAQ_DEVICE.BOARD_NUMBER,
                portType.VALUE,
                value)
        );
    }

    public void portOutput32(DigitalPortType portType, int value) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut32.htm
        checkError(LIBRARY.cbDOut32(
                DAQ_DEVICE.BOARD_NUMBER,
                portType.VALUE,
                value)
        );
    }

    private void portOutputArray(DigitalPortType lowPort, DigitalPortType highPort, long[] dataArray) throws JMCCULException {

        // TODO figure out how to get a long[] into a NativeLongByReference!
        var nlbr = new NativeLongByReference(new NativeLong());

        /*
        The C# function prototype is:
            public MccDaq.ErrorInfo DOutArray(
                MccDaq.DigitalPortType lowPort,
                MccDaq.DigitalPortType highPort,
                out int[] dataArray
            );
        https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DOutArray.htm
         */
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOutArray.htm
        checkError(LIBRARY.cbDInArray(
                DAQ_DEVICE.BOARD_NUMBER,
                lowPort.VALUE,
                highPort.VALUE,
                nlbr)
        );
    }

    private void portOutputScan(DigitalPortType portType, int count, long rateHz, int[] data) throws JMCCULException {
        // I do not have hardware that supports this function so I cannot test it!

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        final HGLOBAL windowsBuffer = LIBRARY.cbWinBufAlloc(new NativeLong(count));

        // TODO figure out how to get data into the windows buffer
        final NativeLongByReference rateByReference = new NativeLongByReference(new NativeLong(rateHz));

        checkError(
                // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOutScan.htm
                LIBRARY.cbDOutScan(
                        /* int BoardNum  */DAQ_DEVICE.BOARD_NUMBER,
                        /* int portType  */ portType.VALUE,
                        /* long Count    */ new NativeLong(count),
                        /* long *Rate    */ rateByReference, //returns the value of the actual rate set, which may be different from the requested rate because of pacer limitations.
                        /* int MemHandle */ windowsBuffer,
                        /* int Options   */ 0
                )
        );

    }

}
