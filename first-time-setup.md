# First-time setup for USB devices

1. Run the installer (mccdaq.exe) if you haven't already.

1. Connect USB cable to the daq device and the PC.

1. Wait for Windows to say "Setting up a device".  
   ![Screenshot of Windows info box which says a new device is being set up](img/for-first-time-setup/01-windows-setting-up.png)

1. Wait for windows to say "Device is ready".  
   ![Screenshot of Windows info box which says a device is ready to use](img/for-first-time-setup/02-windows-set-up-finished.png)
   
1. Check if the device's indicator LED is on. If not, unplug the device then plug it back in. (It might be a good idea to do this even if the indicator LED is on.)

1. Launch InstaCal.  
   ![InstaCal splash screen](img/for-first-time-setup/instacal-splash-screen.bmp)
    * Link to information about InstaCal: https://www.mccdaq.com/daq-software/instacal.aspx
    * Path to the InstaCal executable:
      > C:\Program Files (x86)\Measurement Computing\DAQ\inscal32.exe
    

1. InstaCal will show a dialog box titled "System Modifications Required". Click Yes.  
   ![Screenshot of dialog box titled System Modifications Required](img/for-first-time-setup/03-instacal-system-modifications-required.png)

1. InstaCal closes itself and launches HID Registry Updater. It has a blank white rectangle.  
   ![Screenshot of HID Registry Updater window with blank white ](img/for-first-time-setup/04-hid-registry-updater-before-running.png)
    * Link to information about HID Registry Updater: [HID Devices with Windows 8.1 & Windows 10](https://kb.mccdaq.com/KnowledgebaseArticle50499.aspx)
    * Path to the HID Registry Updater executable:
       > C:\Program Files (x86)\Measurement Computing\DAQ\HIDRegUpdater.exe

1. Click 'Update' button.

1. HID Registry Updater will say "Updating registry keys" and then "Update successful". Then HID Registry Updater will show a dialog box titled "Reset required".  
   ![Screenshot of HID Registry Updater with a dialog box titled Reset Required](img/for-first-time-setup/05-hid-registry-updater-finished.png)

1. Click OK and then clock Close.

1. Reset the device by unplugging it and plugging it back in.

1. Launch InstaCal again.

1. InstaCal will show a dialog box titled "Plug and Play Board Detection". Click OK.
   ![Screenshot of InstaCal with a dialog box titled Plug and Play Board Detection](img/for-first-time-setup/06-instacal-plug-and-play-board-detected.png)
    * The configuration file they refer to is a text file here:
      > C:\ProgramData\Measurement Computing\DAQ\CB.CFG

1. The board will appear in InstaCal.  
   ![Screenshot of InstaCal with one USB DAQ device listed](img/for-first-time-setup/07-instacal-board-added.png)

1. Close InstaCal and run a JMCCUL example.