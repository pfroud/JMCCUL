package xyz.froud.jmccul.analog;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.enums.AnalogRange;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Peter Froud
 */
public class AnalogOutputImpl {

    /*
    https://github.com/mccdaq/mcculw/blob/master/mcculw/device_info/ao_info.py
     */
    private final DaqDevice DAQ_DEVICE;

    private Integer channelCount;
    private Integer resolution;
    private List<AnalogRange> supportedRanges;
    private Boolean isVoltageOutputSupported;

    public AnalogOutputImpl(DaqDevice device) {
        DAQ_DEVICE = device;
    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L32">num_chans in ao_info.py</a>
     */
    public int getChannelCount() throws JMCCULException {
        if (channelCount == null) {
            channelCount = Configuration.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    DAQ_DEVICE.getBoardNumber(),
                    0,
                    MeasurementComputingUniversalLibrary.BINUMDACHANS
            );
        }
        return channelCount;
    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L37">is_supported in ao_info.py</a>
     */
    public boolean isAnalogOutputSupported() throws JMCCULException {
        return getChannelCount() > 0;
    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L41">resolution in ao_info.py</a>
     */
    public int getResolution() throws JMCCULException {
        if (resolution == null) {
            resolution = Configuration.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    DAQ_DEVICE.getBoardNumber(),
                    0,
                    MeasurementComputingUniversalLibrary.BIDACRES
            );
        }
        return resolution;
    }

    /*
    void getSupPortedScanOptions() {
        //https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L50
        // Their Python program is querying the DACSCANOPTIONS config item, which is not present in the C header file
    }
    void isScanSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L46
        //todo
    }
     */

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L61">supported_ranges in ao_info.py</a>
     */
    public List<AnalogRange> getSupportedRanges() throws JMCCULException {
        if (supportedRanges == null) {

            supportedRanges = new ArrayList<>();

            // Check if the range is ignored by passing a bogus range in
            boolean isRangeIgnored = false;
            try {
                // cal cbAOut() with a bogus range.
                // If the D/A board does not have programmable ranges then the range argument will be ignored.
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAOut(
                        DAQ_DEVICE.getBoardNumber(),
                        0,
                        -5,
                        (short) 0
                );
                JMCCULUtils.checkError(errorCode);

                isRangeIgnored = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
            }

            if (isRangeIgnored) {
                // Try and get the range configured in InstaCal
                try {
                    int range = Configuration.getInt(
                            MeasurementComputingUniversalLibrary.BOARDINFO,
                            DAQ_DEVICE.getBoardNumber(),
                            0,
                            MeasurementComputingUniversalLibrary.BIDACRANGE
                    );
                    supportedRanges.add(AnalogRange.parseInt(range));
                } catch (JMCCULException ex) {
                    ex.throwIfErrorIsNetworkDeviceInUse();
                }
            } else {
                // try all possible analog ranges
                for (AnalogRange rangeToCheck : AnalogRange.values()) {
                    try {
                        analogOutput(0, rangeToCheck, (short) 0);
                        supportedRanges.add(rangeToCheck);
                    } catch (JMCCULException ex) {
                        ex.throwIfErrorIsNetworkDeviceInUse();
                    }
                }

            }
        }

        return supportedRanges;

    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ao_info.py#L97">supports_v_out in ao_info.py</a>
     */
    public boolean isVoltageOutputSupported() throws JMCCULException {
        if (isVoltageOutputSupported == null) {
            if (getSupportedRanges().isEmpty()) {
                isVoltageOutputSupported = false;
            } else {
                try {
                    voltageOutput(0, getSupportedRanges().get(0), 0);
                    isVoltageOutputSupported = true;
                } catch (JMCCULException ex) {
                    ex.throwIfErrorIsNetworkDeviceInUse();
                    isVoltageOutputSupported = false;
                }
            }
        }
        return isVoltageOutputSupported;
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/CBAOut.htm">cbAOut()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/AOut.htm">AOut()</a>
     */

    public void analogOutput(int channel, AnalogRange range, short value) throws JMCCULException {
        // The value must be between zero and 2^(resolution)-1.
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/CBAOut.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAOut(
                DAQ_DEVICE.getBoardNumber(),
                channel,
                range.VALUE,
                value
        );
        JMCCULUtils.checkError(errorCode);
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAOutScan.htm">cbAOutScan()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/AOutScan.htm">AOutScan()</a>
     */
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
                /* int BoardNum  */DAQ_DEVICE.getBoardNumber(),
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

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVOut.htm">cbVOut()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/VOut.htm">VOut()</a>
     */
    public void voltageOutput(int channel, AnalogRange range, float value) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVOut.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbVOut(
                DAQ_DEVICE.getBoardNumber(),
                channel,
                range.VALUE,
                value,
                0
        );
        JMCCULUtils.checkError(errorCode);

    }

}
