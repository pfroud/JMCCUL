# JMCCUL

JMCCUL is a Java library for Measurement Computing Corporation Universal Library. Universal Library is software for PC-based data acquisition devices.

JMCCUL is a JNA wrapper around the C API.

JMCCUL and its author(s) are not are affiliated with, partnered with, endorsed by, authorized by, sponsored by, or officially connected with Measurement Computing Corporation.

## Existing Universal Library APIs and libraries

* Running the Windows installer On Windows, installing UL includes APIs for:

  * C / C++

  * C# .NET

  * Visual Basic .NET

  * There is an API for Android, which is Java, but it uses `android.hardware.usb.*` stuff which we can't use on Java desktop.

* Python support separately. Windows only. https://www.mccdaq.com/daq-software/Python-Support.aspx
* Support for LabVIEW, windows only https://www.mccdaq.com/daq-software/universal-library-extensions-lv.aspx
* MATLAB - analog input and analog output only. Don't know what OSs. https://www.mccdaq.com/daq-software/MATLAB-Support.aspx

* You can compile [UL for Linux and macOS](https://www.mccdaq.com/daq-software/Ul-for-Linux-Support.aspx). It makes C/C++ and Python.

## Software requirements

JMCCUL is a wrapper for Universal Library For Windows. 

I have only tested it on 64-bit Windows 10.

UL says it supports Windows 10, 8, 7, and XP, and it supports 32-bit and 64-bit. Right now JMCCUL is hard-coded to load the 64-bit version of the UL DLL.

## Setup

### Install Universal Library and stuff

Run the mccdaq.exe installer. The installer adds `C:\Program Files (x86)\Measurement Computing\DAQ\` into the PATH environment variable. (the system one not the user one) That folder contains the DLL files.

### Connect your board(s)

[InstaCal, API Detection, or Both?](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Ical-APIDetect.htm)

If this is your first time using a mcc device, I think you should run InstaCal. InstaCal is installed when you run the mccdaq installer. It will check that you can detect the device. If using a USB device, it will prompt you that it dete

"C:\Program Files (x86)\Measurement Computing\DAQ\inscal32.exe"

https://www.mccdaq.com/daq-software/instacal.aspx

"C:\ProgramData\Measurement Computing\DAQ\CB.CFG"

Knowledgebase article [HID Devices with Windows 8.1 & Windows 10](https://kb.mccdaq.com/KnowledgebaseArticle50499.aspx)

"C:\Program Files (x86)\Measurement Computing\DAQ\HIDRegUpdater.exe"

### Verify the devices are discovered

To check if JMCCUL can see your board(s), you can run the PrintDiscoveredDevices.java example.

## Basic use

First you get a descriptor and create a daq device from it. There are a bunch of ways to do that.

Then the stuff is divided into modules:

* Digital I/O
* Analog input
* Analog output
* Temperature [reading a thermocouple]
* Counter (not implemented yet!)

Digital input & output are combined apparently because if you have input you also have output. Analog input and analog output are separated because apparently that's hard.

## Known bugs / limitations / future work

I want to make better abstractions for:

* Configuration (cbGet/SetConfig).
* Options (need to use a bit mask right now)
* Exceptions, do you don't have to check the error code?

Need to figure out how to make the JNA bindings for some stuff.

Don't have hardware which supports some stuff.

The counter functions look scary.

The device discovery stuff is confusing I think . Need to document the difference between DaqDeviceDescriptor and DaqDevice. I wrote some utility methods to filter stuff, I think I should either wrote a bunch more or remove all of them.

And some of the class names are not great like Configuration.java. I'm not sure if everything should have a JMCCUL prefix or something.

Probably give the methods in *Impl.java better names.

In the table of UL functions implementation status: include what the java function is.

Validate arguments a lot. In Java we can tell a lot of info (for example if a channel is in range) which we can validate before pasing it to the native C thing.


what's the deal with Port vs PortType?