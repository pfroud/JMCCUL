package jmccul;

import jmccul.jna.DaqDeviceDescriptor;

/**
 *
 * @author Peter Froud
 */
public class DaqDeviceNotFoundException extends JMCCULException {

    public final String DESIRED_BOARD_NAME;
    public final DaqDeviceDescriptor[] FOUND_DEVICES;

    public DaqDeviceNotFoundException(String desiredBoardName, DaqDeviceDescriptor[] foundDevices) {
        super();
        this.DESIRED_BOARD_NAME = desiredBoardName;
        this.FOUND_DEVICES = foundDevices;
    }

}
