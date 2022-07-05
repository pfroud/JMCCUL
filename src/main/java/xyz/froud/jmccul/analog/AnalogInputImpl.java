package xyz.froud.jmccul.analog;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import xyz.froud.jmccul.DaqDevice;
import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.config.Configuration;
import xyz.froud.jmccul.enums.AnalogRange;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Peter Froud
 * @see <a href="https://github.com/mccdaq/mcculw/blob/master/mcculw/device_info/ai_info.py">ai_info.py</a>
 */
public class AnalogInputImpl {

    private final DaqDevice DAQ_DEVICE;

    private Integer channelCount;
    private Integer resolution;
    private Integer packetSize;
    private Integer triggerResolution;

    private List<AnalogRange> supportedRanges;
    private AnalogRange analogTriggerRange;

    private Boolean isVoltageInputSupported;
    private Boolean isAnalogTriggerSupported;
    private Boolean isGainQueueSupported;
    private Boolean isScanSupported;

    public AnalogInputImpl(DaqDevice device) {
        DAQ_DEVICE = device;
    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L36">num_chans in ai_info.py</a>
     */
    public int getChannelCount() throws JMCCULException {
        if (channelCount == null) {
            //
            channelCount = Configuration.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    DAQ_DEVICE.getBoardNumber(),
                    0,
                    MeasurementComputingUniversalLibrary.BINUMADCHANS
            );
        }
        return channelCount;
    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L41">is_supported in ai_info.py</a>
     */
    public boolean isAnalogInputSupported() throws JMCCULException {
        return getChannelCount() > 0;
    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L54">resolution in ai_info.py</a>
     */
    public int getResolution() throws JMCCULException {
        if (resolution == null) {
            resolution = Configuration.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    DAQ_DEVICE.getBoardNumber(),
                    0,
                    MeasurementComputingUniversalLibrary.BIADRES
            );
        }
        return resolution;
    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L59">supports_scan in ai_info.py</a>
     */
    public boolean isScanSupported() throws JMCCULException {
        if (isScanSupported == null) {
            try {
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetIOStatus(
                        DAQ_DEVICE.getBoardNumber(),
                        ShortBuffer.allocate(1),
                        new NativeLongByReference(new NativeLong(0)),
                        new NativeLongByReference(new NativeLong(0)),
                        MeasurementComputingUniversalLibrary.AIFUNCTION
                );
                JMCCULUtils.checkError(errorCode);
                isScanSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                isScanSupported = false;
            }
        }
        return isScanSupported;

    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L68">supported_rangees in ai_info.py</a>
     */
    public List<AnalogRange> getSupportedRanges() throws JMCCULException {
        if (supportedRanges == null) {
            supportedRanges = new ArrayList<>();

            // Check if the board has a switch-selectable, or only one, range
            final int hardRange = Configuration.getInt(
                    MeasurementComputingUniversalLibrary.BOARDINFO,
                    DAQ_DEVICE.getBoardNumber(),
                    0,
                    MeasurementComputingUniversalLibrary.BIRANGE
            );

            if (hardRange >= 0) {
                supportedRanges.add(AnalogRange.parseInt(hardRange));
            } else {
                // try all the ranges
                for (AnalogRange rangeToCheck : AnalogRange.values()) {
                    try {
                        if (getResolution() <= 16) {
                            analogInput(0, rangeToCheck);
                        } else {
                            analogInput32(0, rangeToCheck);
                        }
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
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L93">packet_size in ai_info.py</a>
     */
    public int getPacketSize() {
        if (packetSize == null) {
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

            packetSize = switch (DAQ_DEVICE.getProductID()) {
                case 122 -> 64;
                case 130, 161, 240 -> 31;
                default -> 1;
            };

        }
        return packetSize;

    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L121">supports_v_in in ai_info.py</a>
     */
    public boolean isVoltageInputSupported() throws JMCCULException {
        if (isVoltageInputSupported == null) {

            final List<AnalogRange> supportedRanges = getSupportedRanges();
            if (supportedRanges.isEmpty()) {
                isVoltageInputSupported = false;
            } else {
                try {
                    voltageInput(0, supportedRanges.get(0));
                    isVoltageInputSupported = true;
                } catch (JMCCULException ignore) {
                    isVoltageInputSupported = false;
                }
            }
        }
        return isVoltageInputSupported;
    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L134">analog_trig_resolution in ai_info.py</a>
     */
    public int getAnalogTriggerResolution() {
        if (triggerResolution == null) {
        /*
        PCI-DAS6030, 6031, 6032, 6033, 6052
        USB-1602HS, 1602HS-2AO, 1604HS, 1604HS-2AO
        PCI-2511, 2513, 2515, 2517, USB-2523, 2527, 2533, 2537
        USB-1616HS, 1616HS-2, 1616HS-4, 1616HS-BNC
         */
            triggerResolution = switch (DAQ_DEVICE.getProductID()) {
                case 95, 96, 97, 98, 102, 165, 166, 167, 168, 177, 178,
                        179, 180, 203, 204, 205, 213, 214, 215, 216, 217 -> 12;
                case 101, 103, 104 -> 8;
                default -> 0;
            };
        }
        return triggerResolution;

    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L155">analog_trig_range in ai_info.py</a>
     */
    public AnalogRange getAnalogTriggerRange() throws JMCCULException {
        if (analogTriggerRange == null) {
            int trigSource;
            try {
                trigSource = Configuration.getInt(
                        MeasurementComputingUniversalLibrary.BOARDINFO,
                        DAQ_DEVICE.getBoardNumber(),
                        0,
                        MeasurementComputingUniversalLibrary.BIADTRIGSRC
                );
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                trigSource = 0;
            }

            if (getAnalogTriggerResolution() > 0 && trigSource <= 0) {
                analogTriggerRange = AnalogRange.BIPOLAR_10_VOLTS;
            } else {
                analogTriggerRange = AnalogRange.UNKNOWN;
            }
        }
        return analogTriggerRange;

    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L">supports_analog_trid in ai_info.py</a>
     */
    public boolean isAnalogTriggerSupported() throws JMCCULException {
        if (isAnalogTriggerSupported == null) {
            try {
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbSetTrigger(
                        DAQ_DEVICE.getBoardNumber(),
                        MeasurementComputingUniversalLibrary.TRIGABOVE,
                        (short) 0,
                        (short) 0
                );
                JMCCULUtils.checkError(errorCode);
                isAnalogTriggerSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                isAnalogTriggerSupported = false;
            }
        }
        return isAnalogTriggerSupported;
    }

    /**
     * @see <a href="https://github.com/mccdaq/mcculw/blob/d5d4a3eebaace9544a356a1243963c7af5f8ca53/mcculw/device_info/ai_info.py#L190">supports_gain_queue in ai_info.py</a>
     */
    public boolean isGainQueueSupported() throws JMCCULException {
        if (isGainQueueSupported == null) {
            try {
                final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbALoadQueue(
                        DAQ_DEVICE.getBoardNumber(),
                        ShortBuffer.allocate(0),
                        ShortBuffer.allocate(0),
                        0
                );
                JMCCULUtils.checkError(errorCode);
                isGainQueueSupported = true;
            } catch (JMCCULException ex) {
                ex.throwIfErrorIsNetworkDeviceInUse();
                isGainQueueSupported = false;
            }
        }
        return isGainQueueSupported;

    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn.htm">cbAIn()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/AIn.htm">AIn()</a>
     */
    public short analogInput(int channel, AnalogRange range) throws JMCCULException {

        final ShortBuffer buf = ShortBuffer.allocate(1);

        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAIn(
                DAQ_DEVICE.getBoardNumber(),
                channel,
                range.VALUE,
                buf
        );
        JMCCULUtils.checkError(errorCode);

        return buf.get();

    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn32.htm">cbAIn32()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/AIn32.htm">AIn32()</a>
     */
    public long analogInput32(int channel, AnalogRange range) throws JMCCULException {

        final NativeLongByReference nlbr = new NativeLongByReference(new NativeLong());

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn32.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbAIn32(
                DAQ_DEVICE.getBoardNumber(),
                channel,
                range.VALUE,
                nlbr,
                0
        );
        JMCCULUtils.checkError(errorCode);
        return nlbr.getValue().longValue();
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVIn.htm">cbVIn()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/VIn.htm">VIn()</a>
     */
    public float voltageInput(int channel, AnalogRange range) throws JMCCULException {

        final FloatBuffer buf = FloatBuffer.allocate(1);

        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbVIn(
                DAQ_DEVICE.getBoardNumber(),
                channel,
                range.VALUE,
                buf,
                0
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get();
    }

    /**
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVIn32.htm">cbVIn32()</a>
     * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions_for_NET/VIn32.htm">VIn32()</a>
     */
    public double voltageInput32(int channel, AnalogRange range) throws JMCCULException {

        final DoubleBuffer buf = DoubleBuffer.allocate(1);

        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbVIn32(
                DAQ_DEVICE.getBoardNumber(),
                channel,
                range.VALUE,
                buf,
                0
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get();
    }

}
