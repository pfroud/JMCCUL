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

package xyz.froud.jmccul.config;

import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.MeasurementComputingUniversalLibrary;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @author Peter Froud
 */
public class ConfigurationWrapper {

    public static int getInt(int infoType, int boardNumber, int deviceNumber, int configItem) throws JMCCULException {
        final IntBuffer buf = IntBuffer.allocate(1);

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetConfig(
                infoType,
                boardNumber,
                deviceNumber,
                configItem,
                buf
        );
        JMCCULUtils.checkError(errorCode);
        return buf.get(0);
    }

    public static String getString(int infoType, int boardNumber, int deviceNumber, int configItem, int maxLength) throws JMCCULException {
        final ByteBuffer buf = ByteBuffer.allocate(maxLength);
        final IntBuffer length = IntBuffer.wrap(new int[]{maxLength}); // also acts as an output and is how many bytes were actually read

        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfigString.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbGetConfigString(
                infoType,
                boardNumber,
                deviceNumber,
                configItem,
                buf,
                length
        );
        JMCCULUtils.checkError(errorCode);
        return new String(buf.array(), 0, length.get(0));
    }

    public static void setInt(int infoType, int boardNumber, int deviceNumber, int configItem, int valueToWrite) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbSetConfig(
                infoType,
                boardNumber,
                deviceNumber,
                configItem,
                valueToWrite
        );
        JMCCULUtils.checkError(errorCode);
    }

    public static void setString(int infoType, int boardNumber, int deviceNumber, int configItem, String valueToWrite) throws JMCCULException {
        // https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfigString.htm
        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbSetConfigString(
                infoType,
                boardNumber,
                deviceNumber,
                configItem,
                ByteBuffer.wrap(valueToWrite.getBytes()),
                IntBuffer.wrap(new int[]{valueToWrite.length()})
        );
        JMCCULUtils.checkError(errorCode);

    }

    /* /////////////////////////////////////////////////////////////////////////////////
     GIVERSION -> GI VERSION -> globalInfo version
     Readable? yes
     Writable? NO
     */
    public static int getConfigFileVersion() throws JMCCULException {
        return getInt(
                MeasurementComputingUniversalLibrary.GLOBALINFO,
                0,
                0,
                MeasurementComputingUniversalLibrary.GIVERSION
        );
    }

    /* /////////////////////////////////////////////////////////////////////////////////
     GINUMBOARDS -> GI NUM BOARDS -> globalInfo maximum number of boards
     Readable? yes
     Writable? NO
     */

    /**
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm">cbGetConfig()</a>
     * @see <a
     *         href="https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions_for_NET/NumBoards_property.htm">NumBoards
     *         property</a>
     */
    public static int getMaxBoardCount() throws JMCCULException {
        return getInt(
                MeasurementComputingUniversalLibrary.GLOBALINFO,
                0,
                0,
                MeasurementComputingUniversalLibrary.GINUMBOARDS
        );
    }

}
