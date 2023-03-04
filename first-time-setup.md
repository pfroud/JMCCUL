# First-time setup for DAQ devices 

Follow this guide each time a DAQ device is connected to a computer which it has not been previously connected to. It will walk you though some quirks of the hardware and software.

This is focused on DAQ devices which connect to a PC via USB. I don't know what setup is needed for other types of DAQ devices.

1. Run the Universal Library installer (mccdaq.exe) if you haven't already.

   1. When you launch the installer, first a WinZip prompt will appear, click OK.  
   ![Screenshot of prompt to wun WinZip](img/for-first-time-setup/installer/01-WinZip-prompt.png)

   1. Then the main WinZip window will appear, click Setup.  
   ![Screenshot of WinZip self-extractor main window](img/for-first-time-setup/installer/02-WinZip-main-window.png)

   11. Now the installer actually appears:  
   ![Screenshot of MCC DAQ installer](img/for-first-time-setup/installer/03-MCC-DAQ-installer.png)
       * [DAQami](https://www.mccdaq.com/daq-software/DAQami.aspx) is nice for troubleshooting.
       * InstaCal & Universal Library is required.
       * [TracerDaq](https://www.mccdaq.com/daq-software/tracerdaq-series.aspx) is deprecated in favor of DAQami I think.
       * You may need to install the DirectX dependency.

1. Connect a USB cable to the DAQ device and the PC.

1. Wait for Windows to say "Setting up a device".  
   ![Screenshot of Windows info box which says a new device is being set up](img/for-first-time-setup/01-windows-setting-up-a-device.png)

1. Wait for Windows to say "Device is ready".  
   ![Screenshot of Windows info box which says a device is ready to use](img/for-first-time-setup/02-windows-device-is-ready.png)
   
1. Check if the device's indicator LED is on. If not, unplug the device then plug it back in.

1. Launch InstaCal.  
   ![InstaCal splash screen](img/for-first-time-setup/instacal-splash-screen.bmp)
    * Link to information about InstaCal: https://www.mccdaq.com/daq-software/instacal.aspx
    * Path to the InstaCal executable:
      > C:\Program Files (x86)\Measurement Computing\DAQ\inscal32.exe
    

1. InstaCal will show a dialog box titled "System Modifications Required". Click Yes.  
   ![Screenshot of dialog box titled System Modifications Required](img/for-first-time-setup/03-instacal-system-modifications-required.png)

1. InstaCal will close itself and launch HID Registry Updater. It is a window with a blank white rectangle.  
   ![Screenshot of HID Registry Updater window with blank white ](img/for-first-time-setup/04-hid-registry-updater-before-running.png)
    * Link to information about HID Registry Updater: [HID Devices with Windows 8.1 & Windows 10](https://kb.mccdaq.com/KnowledgebaseArticle50499.aspx)
    * Path to the HID Registry Updater executable:
       > C:\Program Files (x86)\Measurement Computing\DAQ\HIDRegUpdater.exe

1. Click the Update button.

1. HID Registry Updater will say "Updating registry keys" and then "Update successful". Then HID Registry Updater will show a dialog box titled "Reset required".  
   ![Screenshot of HID Registry Updater with a dialog box titled Reset Required](img/for-first-time-setup/05-hid-registry-updater-finished.png)

1. Click OK and then click Close.

1. Reset the device by unplugging it and plugging it back in.

1. Launch InstaCal again.

1. InstaCal will show a dialog box titled "Plug and Play Board Detection". Click OK.
   ![Screenshot of InstaCal with a dialog box titled Plug and Play Board Detection](img/for-first-time-setup/06-instacal-plug-and-play-board-detected.png)
    * The configuration file they refer to is a text file in this location:
      > C:\ProgramData\Measurement Computing\DAQ\CB.CFG

1. The board will appear in InstaCal.  
   ![Screenshot of InstaCal with one USB DAQ device listed](img/for-first-time-setup/07-instacal-board-added.png)

1. Close InstaCal then run [DeviceDiscoveryExample.java](src/main/java/xyz/froud/jmccul_examples/DeviceDiscoveryExample.java).
