package jmccul.analog;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import jmccul.Configuration;
import jmccul.DaqDevice;
import jmccul.JMCCULException;
import jmccul.JMCCULUtils;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class AnalogOutputImpl {

    /*
    https://github.com/mccdaq/mcculw/blob/master/mcculw/device_info/ao_info.py
     */
    private final DaqDevice DAQ_DEVICE;
    public final int CHANNEL_COUNT;
    public final int RESOLUTION;
    public final List<AnalogRange> SUPPORTED_RANGES;
    public boolean IS_VOLTAGAE_OUTPUT_SUPPORTED;

    public AnalogOutputImpl(DaqDevice device) throws JMCCULException {
        DAQ_DEVICE = device;
        CHANNEL_COUNT = getChannelCount();
        RESOLUTION = getResolution();
        SUPPORTED_RANGES = getSupportedRanges();
        IS_VOLTAGAE_OUTPUT_SUPPORTED = isVoltageOutputSupported();

    }

    private int getChannelCount() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L32
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                DAQ_DEVICE.BOARD_NUMBER,
                0,
                MeasurementComputingUniversalLibrary.BINUMDACHANS
        );
    }

    public boolean isAnalogOutputSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L37
        return CHANNEL_COUNT > 0;
    }

    private int getResolution() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L41
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                DAQ_DEVICE.BOARD_NUMBER,
                0,
                MeasurementComputingUniversalLibrary.BIDACRES
        );
    }

    /*
    void getSuportedScanOptions() {
        //https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L50
        // Their Python program is querying the DACSCANOPTIONS config item, which is not present in the C header file
    }
    void isScanSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L46
        //todo
    }
     */
    private List<AnalogRange> getSupportedRanges() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L61

        List<AnalogRange> rv = new ArrayList<>();

        // Check if the range is ignored by passing a bogus range in
        boolean isRangeIgnored = false;
        try {
            // cal cbAOut() with a bogus range.
            // If the D/A board does not have programmable ranges then the range argument will be ignored.
            final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAOut(
                    DAQ_DEVICE.BOARD_NUMBER,
                    0,
                    -5,
                    (short) 0
            );
            JMCCULUtils.checkError(errorCode);

            isRangeIgnored = true;
        } catch (JMCCULException ex) {
            if (ex.ERROR_CODE == MeasurementComputingUniversalLibrary.NETDEVINUSE
                    || ex.ERROR_CODE == MeasurementComputingUniversalLibrary.NETDEVINUSEBYANOTHERPROC) {
                throw ex;
            }
        }

        if (isRangeIgnored) {
            // Try and get the range configured in InstaCal
            try {
                int range = Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        DAQ_DEVICE.BOARD_NUMBER,
                        0,
                        MeasurementComputingUniversalLibrary.BIDACRANGE
                );
                rv.add(AnalogRange.parseInt(range));
            } catch (JMCCULException ex) {
                if (ex.ERROR_CODE == MeasurementComputingUniversalLibrary.NETDEVINUSE
                        || ex.ERROR_CODE == MeasurementComputingUniversalLibrary.NETDEVINUSEBYANOTHERPROC) {
                    throw ex;
                }
            }
        } else {
            // try all possible analog ranges
            for (AnalogRange rangeToCheck : AnalogRange.values()) {
                try {
                    analogOutput(0, rangeToCheck, (short) 0);
                    rv.add(rangeToCheck);
                } catch (JMCCULException ex) {
                    if (ex.ERROR_CODE == MeasurementComputingUniversalLibrary.NETDEVINUSE
                            || ex.ERROR_CODE == MeasurementComputingUniversalLibrary.NETDEVINUSEBYANOTHERPROC) {
                        throw ex;
                    }
                }
            }

        }

        return rv;

    }

    private boolean isVoltageOutputSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L97
        boolean rv = false;
        if (!SUPPORTED_RANGES.isEmpty()) {
            try {
                voltageOutput(0, SUPPORTED_RANGES.get(0), 0);
                rv = true;
            } catch (JMCCULException ex) {
                rv = false;
            }
        }
        return rv;
    }

    public void analogOutput(int channel, AnalogRange range, short value) throws JMCCULException {
        // The value must be between zero and 2^(resolution)-1.
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/CBAOut.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAOut(
                DAQ_DEVICE.BOARD_NUMBER,
                channel,
                range.VALUE,
                value
        );
        JMCCULUtils.checkError(errorCode);
    }

    public void analogOutputScan(int lowChannel, int highChannel, long count, long rateHz, AnalogRange range, short[] values, int options) throws JMCCULException {

        /*
        For 16-bit data, create the buffer with cbWinBufAlloc().
        For data that is >16-bit and ≤32-bit, use cbWinBufAlloc32().
        For data that is >32-bit and ≤64-bit, use cbWinBufAlloc64().
        When the device supports output scanning of scaled data, such as cbAOutScan() using the SCALEDATA option,
        create the buffer with cbScaledWinBufAlloc().

        You can load the data values with cbWinArrayToBuf() or cbScaledWinArrayToBuf() (for scaled data).
         */
        //https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm
        MeasurementComputingUniversalLibrary.HGLOBAL windowsBuffer
                = MeasurementComputingUniversalLibrary.INSTANCE.cbWinBufAlloc(new NativeLong(count));

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinArrayToBuf.htm
        final int errorCodeWinArrayToBuf = MeasurementComputingUniversalLibrary.INSTANCE.cbWinArrayToBuf(
                ShortBuffer.wrap(values),
                windowsBuffer,
                new NativeLong(0),
                new NativeLong(values.length)
        );
        JMCCULUtils.checkError(errorCodeWinArrayToBuf);

        final NativeLongByReference rateByReference = new NativeLongByReference(new NativeLong(rateHz));

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAOutScan.htm
        final int errorCodeAOutScan = MeasurementComputingUniversalLibrary.INSTANCE.cbAOutScan(
                /* int BoardNum  */DAQ_DEVICE.BOARD_NUMBER,
                /* int LowChan   */ lowChannel,
                /* int HighChan  */ highChannel,
                /* int NumPoint  */ new NativeLong(count),
                /* long *Rate    */ rateByReference, // returns the value of the actual rate set, which may be different from the requested rate due to pacer limitations.
                /* int Range     */ range.VALUE,
                /* int MemHandle */ windowsBuffer,
                /* int           */ options
        );
        JMCCULUtils.checkError(errorCodeAOutScan);

    }

    public void voltageOutput(int channel, AnalogRange range, float value) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVOut.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbVOut(
                DAQ_DEVICE.BOARD_NUMBER,
                channel,
                range.VALUE,
                value,
                0
        );
        JMCCULUtils.checkError(errorCode);

    }

}
