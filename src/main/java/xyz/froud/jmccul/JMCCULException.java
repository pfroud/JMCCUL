package xyz.froud.jmccul;

import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 * @author Peter Froud
 */
public class JMCCULException extends Exception {

    public final int ERROR_CODE;

    public JMCCULException(String message) {
        super(message);
        ERROR_CODE = Integer.MIN_VALUE;
    }

    public JMCCULException(String message, int errorCode) {
        super(message);
        ERROR_CODE = errorCode;
    }

    public void throwIfErrorIsNetworkDeviceInUse() throws JMCCULException {
        // TODO move this method to Utils class?
        if (ERROR_CODE == MeasurementComputingUniversalLibrary.NETDEVINUSE
                || ERROR_CODE == MeasurementComputingUniversalLibrary.NETDEVINUSEBYANOTHERPROC) {
            throw this;
        }
    }

}
