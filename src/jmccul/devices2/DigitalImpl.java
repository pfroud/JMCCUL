package jmccul.devices2;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import jmccul.JMCCULException;
import static jmccul.JMCCULUtils.checkError;
import jmccul.enums.DigitalPortType;
import jmccul.jna.MeasurementComputingUniversalLibrary;
import jmccul.jna.MeasurementComputingUniversalLibrary.HGLOBAL;

/**
 *
 * @author Peter Froud
 */
public class DigitalImpl {

    private final MeasurementComputingUniversalLibrary LIBRARY = MeasurementComputingUniversalLibrary.INSTANCE;
    private final DaqDevice device;

    public DigitalImpl(DaqDevice device) {
        this.device = device;

    }

    boolean bitInput(DigitalPortType portType, int bitNumber) throws JMCCULException {
        ShortBuffer buf = ShortBuffer.allocate(1);
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitIn.htm
        checkError(LIBRARY.cbDBitIn(
                device.BOARD_NUMBER,
                portType.VALUE,
                bitNumber,
                buf)
        );
        return buf.get() == 1;
    }

    void bitOutput(DigitalPortType portType, int bitNumber, boolean value) throws JMCCULException {
        int zeroOrOne = value ? 1 : 0;
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitOut.htm
        checkError(LIBRARY.cbDBitOut(
                device.BOARD_NUMBER,
                portType.VALUE,
                bitNumber,
                (short) zeroOrOne)
        );
    }

    void configureBit(DigitalPortType portType, int bitNumber, DigitalPortDirection direction) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigBit.htm
        checkError(LIBRARY.cbDConfigBit(
                device.BOARD_NUMBER,
                portType.VALUE,
                bitNumber,
                direction.VALUE)
        );
    }

    void configurePort(DigitalPortType portType, DigitalPortDirection direction) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigPort.htm
        checkError(LIBRARY.cbDConfigPort(
                device.BOARD_NUMBER,
                portType.VALUE,
                direction.VALUE)
        );
    }

    int portInput(DigitalPortType portType) throws JMCCULException {
        // TODO I think I need to check the resolution to see how many shorts I need
        ShortBuffer buf = ShortBuffer.allocate(1);
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn.htm
        checkError(LIBRARY.cbDIn(
                device.BOARD_NUMBER,
                portType.VALUE,
                buf)
        );
        return buf.get();
    }

    int portInput32(DigitalPortType portType) throws JMCCULException {
        IntBuffer buf = IntBuffer.allocate(1);
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn32.htm
        checkError(LIBRARY.cbDIn32(
                device.BOARD_NUMBER,
                portType.VALUE,
                buf)
        );
        return buf.get();
    }

    int portInputArray(DigitalPortType lowPort, DigitalPortType highPort) throws JMCCULException {

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
                device.BOARD_NUMBER,
                lowPort.VALUE,
                highPort.VALUE,
                nlbr)
        );
//        return buf.get();
        return 0;
    }

    public short[] inputScan(DigitalPortType portType, int count, long rateHz) throws JMCCULException {
        /*
        TODO why does this return shorts? what about the port size / resolution?
         */
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        final HGLOBAL windowsBuffer = LIBRARY.cbWinBufAlloc(new NativeLong(count));

        final NativeLongByReference rateByReference = new NativeLongByReference(new NativeLong(rateHz));

        checkError(
                // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInScan.htm
                LIBRARY.cbDInScan(
                        /* int BoardNum  */device.BOARD_NUMBER,
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
                device.BOARD_NUMBER,
                portType.VALUE,
                value)
        );
    }

    public void portOutput32(DigitalPortType portType, int value) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut32.htm
        checkError(LIBRARY.cbDOut32(
                device.BOARD_NUMBER,
                portType.VALUE,
                value)
        );
    }

    public void portOutputArray(DigitalPortType lowPort, DigitalPortType highPort, long[] dataArray) throws JMCCULException {

        /*
        The C# function shows that the LowPort and HighPort arguments are port types
        https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions_for_NET/DOutArray.htm
        public MccDaq.ErrorInfo DOutArray(MccDaq.DigitalPortType lowPort, MccDaq.DigitalPortType highPort, out int[] dataArray)
         */
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOutArray.htm
        var nlbr = new NativeLongByReference(new NativeLong());

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInArray.htm
        checkError(LIBRARY.cbDInArray(
                device.BOARD_NUMBER,
                lowPort.VALUE,
                highPort.VALUE,
                nlbr)
        );
    }

    public void portOutputScan(DigitalPortType portType, int count, long rateHz, int[] data) throws JMCCULException {

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        final HGLOBAL windowsBuffer = LIBRARY.cbWinBufAlloc(new NativeLong(count));

        // TODO figure out how to get data into the windows buffer
        final NativeLongByReference rateByReference = new NativeLongByReference(new NativeLong(rateHz));

        checkError(
                // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInScan.htm
                LIBRARY.cbDOutScan(
                        /* int BoardNum  */device.BOARD_NUMBER,
                        /* int portType  */ portType.VALUE,
                        /* long Count    */ new NativeLong(count),
                        /* long *Rate    */ rateByReference, //returns the value of the actual rate set, which may be different from the requested rate because of pacer limitations.
                        /* int MemHandle */ windowsBuffer,
                        /* int Options   */ 0
                )
        );

    }

}
