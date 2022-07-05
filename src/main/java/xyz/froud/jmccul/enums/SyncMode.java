package xyz.froud.jmccul.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Froud
 * @see <a href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/SetSyncMode.htm">BoardConfig.SetSyncMode()</a>
 */
public enum SyncMode {

    /** Output the internal D/A Load signal */
    OUTPUT(0),

    /** Receive the D/A Load signal from an external source */
    RECEIVE(1);

    private static final Map<Integer, SyncMode> valueMap;

    static {
        final SyncMode[] allEnumValues = SyncMode.values();
        valueMap = new HashMap<>(allEnumValues.length, 1);
        for (SyncMode type : allEnumValues) {
            valueMap.put(type.VALUE, type);
        }
    }

    public static SyncMode parseInt(int value) {
        return valueMap.get(value);
    }

    public final int VALUE;

    SyncMode(int value) {
        VALUE = value;
    }

}
