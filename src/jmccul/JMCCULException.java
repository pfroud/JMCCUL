package jmccul;

/**
 *
 * @author Peter Froud
 */
public class JMCCULException extends Exception {

    public JMCCULException() {
        super();
    }

    public JMCCULException(int errorCode) throws JMCCULException {
        super("code " + errorCode + ": " + JMCCULUtils.getErrorMessage(errorCode));
    }

    public JMCCULException(String message) {
        super(message);
    }

    public JMCCULException(Throwable cause) {
        super(cause);
    }

    public JMCCULException(String message, Throwable cause) {
        super(message, cause);
    }

}
