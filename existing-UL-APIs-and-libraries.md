# Existing Universal Library APIs & libraries

Measurement Computing Corporation created & supports several official, first-party ways to use Universal Library.


## Windows

The Universal Library installer (mccdaq.exe) includes APIs for:
* C / C++
* C#
* Visual Basic and Visual Basic .NET

Python is supported on Windows as a wrapper around the DLL using the [`ctypes`](https://docs.python.org/3/library/ctypes.html) library. Links:
* https://www.mccdaq.com/daq-software/Python-Support.aspx
* https://github.com/mccdaq/mcculw

LabVIEW is supported on Windows only:
* https://www.mccdaq.com/daq-software/universal-library-extensions-lv.aspx

MATLAB is supported on Windows only. Links:
* https://www.mccdaq.com/products/matlab.aspx
* https://www.mccdaq.com/daq-software/MATLAB-Support.aspx
* https://www.mathworks.com/hardware-support/measurement-computing.html
* It's hard to find out which OSs are supported. Go to [Hardware Support Package System Requirements](https://www.mathworks.com/hardware-support/system-requirements.html) then find the row called "Data Acquisition Toolbox Support Package for Measurement Computing Hardware". The "W" means Windows.


## Linux, macOS, Raspberry Pi

On Linux & macOS, you can compile a C/C++ library and a Python [`ctypes`](https://docs.python.org/3/library/ctypes.html) wrapper. Instructions are provided for distributions based on Debian, Arch, Red Hat, and OpenSUSE. Links:
* https://www.mccdaq.com/daq-software/Ul-for-Linux-Support.aspx
* https://github.com/mccdaq/uldaq
* https://pypi.org/project/uldaq/

A separate library is provided for the DAQ HAT devices for Raspberry Pi. Links:
* https://www.mccdaq.com/daq-software/DAQ-HAT-Library.aspx
* https://www.mccdaq.com/DAQ-HAT.aspx
* https://github.com/mccdaq/daqhats


## Android

Android is supported:
* https://www.mccdaq.com/daq-software/universal-library-android.aspx

It is written in Java, but uses the [`android.hardware.usb`](https://developer.android.com/reference/android/hardware/usb/package-summary) and [`android.bluetooth`](https://developer.android.com/reference/android/bluetooth/package-summary) packages. Source: decompilation of ul.jar using [JD-GUI](http://java-decompiler.github.io/).