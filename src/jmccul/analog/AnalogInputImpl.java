package jmccul.analog;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    public final int PACKET_SIZE;
    public final int TRIGGER_RESOLUTION;

    public final List<AnalogRange> SUPPORTED_RANGES;
    public final AnalogRange ANALOG_TRIGGER_RANGE;

    public final boolean IS_VOLTAGE_INPUT_SUPPORTED;
    public final boolean IS_ANALOG_TRIGGER_SUPPORTED;
    public final boolean IS_GAIN_QUEUE_SUPPORTED;
    public final boolean IS_SCAN_SUPPORTED;

    public AnalogInputImpl(DaqDevice device) throws JMCCULException {
        DAQ_DEVICE = device;

        CHANNEL_COUNT = getChannelCount();
        RESOLUTION = getResolution();
        PACKET_SIZE = getPacketSize();
        TRIGGER_RESOLUTION = getAnalogTriggerResolution();

        SUPPORTED_RANGES = getSupportedRanges();
        ANALOG_TRIGGER_RANGE = getAnalogTriggerRange();

        IS_VOLTAGE_INPUT_SUPPORTED = isVoltageInputSupported();
        IS_ANALOG_TRIGGER_SUPPORTED = isAnalogTriggerSupported();
        IS_GAIN_QUEUE_SUPPORTED = isGainQueueSupported();
        IS_SCAN_SUPPORTED = isScanSupported();

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

    private boolean isScanSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L59

        boolean rv = true;
        try {
            final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetIOStatus(
                    DAQ_DEVICE.BOARD_NUMBER,
                    ShortBuffer.allocate(1),
                    new NativeLongByReference(new NativeLong(0)),
                    new NativeLongByReference(new NativeLong(0)),
                    MeasurementComputingUniversalLibrary.AIFUNCTION
            );
            JMCCULUtils.checkError(errorCode);
        } catch (JMCCULException ex) {
            rv = false;
        }
        return rv;

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

    private int getPacketSize() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L93
        /*
        The hardware in the following table will return a packet size.
        This hardware must use an integer multiple of the packet size as
        the total_count for a_in_scan when using the CONTINUOUS option
        in BLOCKIO mode.

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

        int packet_size = 1;
        if (DAQ_DEVICE.PRODUCT_ID == 122) {
            packet_size = 64;
        } else if (DAQ_DEVICE.PRODUCT_ID == 130
                || DAQ_DEVICE.PRODUCT_ID == 161
                || DAQ_DEVICE.PRODUCT_ID == 240) {
            packet_size = 31;
        }

        return packet_size;
    }

    private boolean isVoltageInputSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L121

        boolean rv = true;
        if (SUPPORTED_RANGES.isEmpty()) {
            rv = false;
        } else {
            try {
                voltageInput(0, SUPPORTED_RANGES.get(0));
            } catch (JMCCULException ex) {
                rv = false;
            }
        }
        return rv;
    }

    private int getAnalogTriggerResolution() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L134


        /*
        PCI-DAS6030, 6031, 6032, 6033, 6052
        USB-1602HS, 1602HS-2AO, 1604HS, 1604HS-2AO
        PCI-2511, 2513, 2515, 2517, USB-2523, 2527, 2533, 2537
        USB-1616HS, 1616HS-2, 1616HS-4, 1616HS-BNC
         */
        Set<Integer> trig_res_12_bit_types = Set.of(95, 96, 97, 98, 102, 165, 166, 167, 168, 177,
                178, 179, 180, 203, 204, 205, 213, 214, 215,
                216, 217);

        // PCI-DAS6040, 6070, 6071
        Set<Integer> trig_res_8_bit_types = Set.of(101, 103, 104);

        int rv = 0;
        if (trig_res_12_bit_types.contains(DAQ_DEVICE.PRODUCT_ID)) {
            rv = 12;
        } else if (trig_res_8_bit_types.contains(DAQ_DEVICE.PRODUCT_ID)) {
            rv = 8;
        }
        return rv;

    }

    private AnalogRange getAnalogTriggerRange() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L155

        int trigSource;
        try {
            trigSource = Configuration.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    DAQ_DEVICE.BOARD_NUMBER,
                    0,
                    MeasurementComputingUniversalLibrary.BIADTRIGSRC
            );
        } catch (Exception ex) {
            trigSource = 0;
        }

        if (TRIGGER_RESOLUTION > 0 && trigSource <= 0) {
            return AnalogRange.BIPOLAR_10_VOLTS;
        } else {
            return AnalogRange.UNKNOWN;
        }

    }

    private boolean isAnalogTriggerSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L172

        boolean rv = true;
        try {
            final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbSetTrigger(
                    DAQ_DEVICE.BOARD_NUMBER,
                    MeasurementComputingUniversalLibrary.TRIGABOVE,
                    (short) 0,
                    (short) 0
            );
            JMCCULUtils.checkError(errorCode);
        } catch (JMCCULException ex) {
            rv = false;
        }
        return rv;
    }

    private boolean isGainQueueSupported() {
        // https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L190

        boolean rv = true;
        try {
            final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbALoadQueue(
                    DAQ_DEVICE.BOARD_NUMBER,
                    ShortBuffer.allocate(0),
                    ShortBuffer.allocate(0),
                    0
            );
            JMCCULUtils.checkError(errorCode);
        } catch (JMCCULException ex) {
            rv = false;
        }
        return rv;

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

    public float voltageInput(int channel, AnalogRange range) throws JMCCULException {

        final FloatBuffer buf = FloatBuffer.allocate(1);

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVIn.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbVIn(
                DAQ_DEVICE.BOARD_NUMBER,
                channel,
                range.VALUE,
                buf,
                0
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get();
    }

    public double voltageInput32(int channel, AnalogRange range) throws JMCCULException {

        final DoubleBuffer buf = DoubleBuffer.allocate(1);

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVIn32.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbVIn32(
                DAQ_DEVICE.BOARD_NUMBER,
                channel,
                range.VALUE,
                buf,
                0
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get();
    }

}
