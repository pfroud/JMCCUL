package jmccul;

import java.nio.ByteBuffer;
import jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class JMCCULUtils {

    private final static int ERROR_STRING_LENGTH = MeasurementComputingUniversalLibrary.ERRSTRLEN;

    public static String getErrorMessage(int errorCodeToLookUp) throws JMCCULException {
        final ByteBuffer buf = ByteBuffer.allocate(ERROR_STRING_LENGTH);
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Error_Handling_Functions/cbGetErrMsg.htm
        int errorCodeWhenGettingErrorMessage = MeasurementComputingUniversalLibrary.INSTANCE.cbGetErrMsg(errorCodeToLookUp, buf);
        if (errorCodeWhenGettingErrorMessage != 0) {
            throw new JMCCULException("exception when looking up the error code!");
        }
        return new String(buf.array()).trim();
    }

    public static void checkError(int errorCode) throws JMCCULException {
        if (errorCode != MeasurementComputingUniversalLibrary.NOERRORS) {
            final String message = "code " + errorCode + ": " + JMCCULUtils.getErrorMessage(errorCode);
            throw new JMCCULException(message);
        }
    }

}
