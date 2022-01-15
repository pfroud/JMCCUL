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
public class AnalogInputImpl {

    /*
    https://github.com/mccdaq/mcculw/blob/master/mcculw/device_info/ai_info.py
     */
    private final DaqDevice DAQ_DEVICE;
    public final int CHANNEL_COUNT;
    public final int RESOLUTION;
    public final List<AnalogRange> SUPPORTED_RANGES;

    public AnalogInputImpl(DaqDevice device) throws JMCCULException {
        DAQ_DEVICE = device;
        CHANNEL_COUNT = getChannelCount();
        RESOLUTION = getResolution();
        SUPPORTED_RANGES = getSupportedRanges();

    }

    private int getChannelCount() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L36
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                DAQ_DEVICE.BOARD_NUMBER,
                0,
                MeasurementComputingUniversalLibrary.BINUMADCHANS
        );
    }

    public boolean isAnalogInputSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L41
        return CHANNEL_COUNT > 0;
    }

    private int getResolution() throws JMCCULException {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L54
        return Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                DAQ_DEVICE.BOARD_NUMBER,
                0,
                MeasurementComputingUniversalLibrary.BIADRES
        );
    }

    void isScanSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L59
    }

    private List<AnalogRange> getSupportedRanges() throws JMCCULException {
        List<AnalogRange> rv = new ArrayList<>();

        // Check if the board has a switch-selectable, or only one, range
        final int hardRange = Configuration.getInt(
                MeasurementComputingUniversalLibrary.BOARDINFO,
                DAQ_DEVICE.BOARD_NUMBER,
                0,
                MeasurementComputingUniversalLibrary.BIRANGE
        );

        if (hardRange >= 0) {
            rv.add(AnalogRange.parseInt(hardRange));
        } else {
            // try all the ranges
            for (AnalogRange rangeToCheck : AnalogRange.values()) {
                try {
                    if (RESOLUTION <= 16) {
                        analogInput(0, rangeToCheck);
                    } else {
                        analogInput32(0, rangeToCheck);
                    }
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

    int getPacketSize() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L93
        /*
         The hardware in the following table will return a packet size.
        This hardware must use an integer multiple of the packet size as
        the total_count for a_in_scan when using the
        :const:`~mcculw.enums.CONTINUOUS` option in
        :const:`~mcculw.enums.BLOCKIO` mode.

        For all other hardware, this method will return 1.

        ==========  ==========  ===========
        Hardware    Product Id  Packet Size
        ==========  ==========  ===========
        USB-1208LS  122         64
        USB-1208FS  130         31
        USB-1408FS  161         31
        USB-7204    240         31
        ==========  ==========  ===========
         */
 /*
        int boardType = -1
        int packet_size = 1;
        if (board_type == 122) {
            packet_size = 64;
        } else if (board_type in[130, 161, 240]){
            packet_size = 31;
        }
         */

//        return packet_size;
        return -1;
    }

    void isVoltageInputSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L121
    }

    void analogTriggerResolution() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L134
    }

    void analogTriggerRange() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L155
    }

    void isAnalogTriggerSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L172
    }

    void isGainQueueSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L190
    }

    public short analogInput(int channel, AnalogRange range) throws JMCCULException {

        final ShortBuffer buf = ShortBuffer.allocate(1);

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAIn(
                DAQ_DEVICE.BOARD_NUMBER,
                channel,
                range.VALUE,
                buf
        );
        JMCCULUtils.checkError(errorCode);

        return buf.get();

    }

    public long analogInput32(int channel, AnalogRange range) throws JMCCULException {

        final NativeLongByReference nlbr = new NativeLongByReference(new NativeLong());

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn32.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAIn32(
                DAQ_DEVICE.BOARD_NUMBER,
                channel,
                range.VALUE,
                nlbr,
                0
        );
        JMCCULUtils.checkError(errorCode);
        return nlbr.getValue().longValue();
    }

}
