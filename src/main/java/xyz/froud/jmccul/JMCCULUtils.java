package xyz.froud.jmccul;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import xyz.froud.jmccul.jna.DaqDeviceDescriptor;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

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
            throw new JMCCULException(message, errorCode);
        }
    }

    public static int getBoardNumberForDescriptor(DaqDeviceDescriptor descriptor) {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbGetBoardNumber.htm
        // the returned value is the board number. no error codes.
        return MeasurementComputingUniversalLibrary.INSTANCE.cbGetBoardNumber(descriptor);
    }

    public static float getDLLRevision() throws JMCCULException {
        /*
        VXD apparently is an old devide driver system for Windows 2, 3, and 9x.
        Since nobody cares about that anymore I will only return the DLL version.
        https://en.wikipedia.org/wiki/VxD
         */

        final FloatBuffer dllRevisionNumber = FloatBuffer.allocate(1);
        final FloatBuffer vxdRevisionNumber = FloatBuffer.allocate(1);

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Revision_Control_Functions/cbGetRevision.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetRevision(
                dllRevisionNumber,
                vxdRevisionNumber
        );
        checkError(errorCode);
        return dllRevisionNumber.get();
    }

}
