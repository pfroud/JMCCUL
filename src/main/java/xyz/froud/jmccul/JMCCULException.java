package xyz.froud.jmccul;

/**
 *
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

}
