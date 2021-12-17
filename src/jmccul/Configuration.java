package jmccul;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static jmccul.JMCCULUtils.checkError;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class Configuration {

    public static int getInt(int infoType, int boardNumber, int deviceNumber, int configItem) throws JMCCULException {
        IntBuffer buf = IntBuffer.allocate(1);
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm
        checkError(
                MeasurementComputingUniversalLibrary.INSTANCE.cbGetConfig(
                        infoType,
                        boardNumber,
                        deviceNumber,
                        configItem,
                        buf)
        );
        return buf.get(0);
    }

    public static String getString(int infoType, int boardNumber, int deviceNumber, int configItem, int maxLength) throws JMCCULException {
        final ByteBuffer buf = ByteBuffer.allocate(maxLength);
        final IntBuffer length = IntBuffer.wrap(new int[]{maxLength}); // also acts as an output and is how many bytes were actually read
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfigString.htm
        checkError(
                MeasurementComputingUniversalLibrary.INSTANCE.cbGetConfigString(
                        infoType,
                        boardNumber,
                        deviceNumber,
                        configItem,
                        buf,
                        length)
        );
        return new String(buf.array(), 0, length.get(0));
    }

    public static void setInt(int infoType, int boardNumber, int deviceNumber, int configItem, int valueToWrite) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm
        checkError(MeasurementComputingUniversalLibrary.INSTANCE.cbSetConfig(
                infoType,
                boardNumber,
                deviceNumber,
                configItem,
                valueToWrite
        ));
    }

    public static void setString(int infoType, int boardNumber, int deviceNumber, int configItem, String valueToWrite) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfigString.htm
        checkError(MeasurementComputingUniversalLibrary.INSTANCE.cbSetConfigString(
                infoType,
                boardNumber,
                deviceNumber,
                configItem,
                ByteBuffer.wrap(valueToWrite.getBytes()),
                IntBuffer.wrap(new int[]{valueToWrite.length()})
        ));

    }

}
