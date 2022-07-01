package xyz.froud.jmccul;

import xyz.froud.jmccul.jna.DaqDeviceDescriptor;

/**
 * @author Peter Froud
 */
public class DaqDeviceNotFoundException extends JMCCULException {

    public final DaqDeviceDescriptor[] FOUND_DEVICE_DESCRIPTORS;

    public DaqDeviceNotFoundException(String message, DaqDeviceDescriptor[] foundDevices) {
        super(message);
        FOUND_DEVICE_DESCRIPTORS = foundDevices;
    }

}
