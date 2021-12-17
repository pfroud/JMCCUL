package jmccul;

import jmccul.jna.DaqDeviceDescriptor;

/**
 *
 * @author Peter Froud
 */
public class DaqDeviceNotFoundException extends JMCCULException {

    public final String MESSAGE;
    public final DaqDeviceDescriptor[] FOUND_DEVICE_DESCRIPTORS;

    public DaqDeviceNotFoundException(String message, DaqDeviceDescriptor[] foundDevices) {
        super();
        MESSAGE = message;
        FOUND_DEVICE_DESCRIPTORS = foundDevices;
    }

}
