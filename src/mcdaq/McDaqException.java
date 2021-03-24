package mcdaq;

/**
 *
 * @author Peter Froud
 */
public class McDaqException extends Exception {

    public McDaqException() {
        super();
    }

    public McDaqException(int errorCode) throws McDaqException {
        super("code " + errorCode + ": " + McDaqUtils.getErrorMessage(errorCode));
    }

    public McDaqException(String message) {
        super(message);
    }

    public McDaqException(Throwable cause) {
        super(cause);
    }

    public McDaqException(String message, Throwable cause) {
        super(message, cause);
    }

}
