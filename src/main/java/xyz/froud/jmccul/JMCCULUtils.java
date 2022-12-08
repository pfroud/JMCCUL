/*
 * The MIT License.
 *
 * Copyright (c) 2022 Peter Froud.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.froud.jmccul;


import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
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

    /**
     * Gets the revision level of Universal Library DLL.
     *
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Revision_Control_Functions/cbGetRevision.htm">cbGetRevision()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Revision_Control_Functions_for_NET/GetRevision.htm">GetRevision()</a>
     */
    public static float getDLLRevision() throws JMCCULException {
        /*
        VXD apparently is an old device driver system for Windows 2, 3, and 9x.
        Since that is ancient I will only return the DLL version.
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
