package jmccul;

import jmccul.jna.DaqDeviceDescriptor;

/**
 *
 * @author Peter Froud
 */
public class DaqDeviceNotFoundException extends JMCCULException {

    public final String MESSAGE;
    public final DaqDeviceDescriptor[] FOUND_DEVICES;

    public DaqDeviceNotFoundException(String message, DaqDeviceDescriptor[] foundDevices) {
        super();
        this.MESSAGE = message;
        this.FOUND_DEVICES = foundDevices;
    }

}
