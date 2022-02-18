# JMCCUL

JMCCUL is a Java library for Measurement Computing Corporation Universal Library. Universal Library is software for PC-based data acquisition devices.

JMCCUL is a JNA wrapper around the C API.

## Not affiliated with Measurement Computing Corporation

JMCCUL and its author(s) are not are affiliated with, partnered with, endorsed by, authorized by, sponsored by, or officially connected in any way with Measurement Computing Corporation.

"Universal Library" is a trademark of Measurement Computing Corporation, see https://www.mccdaq.com/legal.aspx.

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

## Getting started

Install UL.

The Universal Library puts `C:\Program Files (x86)\Measurement Computing\DAQ\` into the PATH environment variable. That folder contains the DLL files.

## Known bugs / limitations / future work

not tested very much

not everything implemented
