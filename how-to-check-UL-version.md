# How to check UL version numbers

## How to check what version of UL is currently installed

### Check in header file

Open "C:\Users\Public\Documents\Measurement Computing\DAQ\C\cbw.h" and look for this on line 24:

``` C
/* Current Revision Number */
#define CURRENTREVNUM      6.7
```

### Check DLL version property

Open file explorer and navigate to "C:\Program Files (x86)\Measurement Computing\DAQ". Right click on cbw64.dll. Go to the Details tab and check the 'Product version' field.

Or run this in PowerShell:

```powershell
(Get-Item "C:\Program Files (x86)\Measurement Computing\DAQ\cbw64.dll").VersionInfo.ProductVersion
```

source https://stackoverflow.com/a/13118517/7376577

## How to check what the newest version of UL is

Go to https://www.mccdaq.com/Software-Downloads. Under the "MCC DAQ Software" heading, look for "Download includes the following applications", then it will say something like:

> Universal Library™ 6.72 – Programming libraries and components for developing 32/64-bit applications, using Windows programming languages.

## How to check what version of UL is in an mccdaq.exe installer

Open archive using 7-Zip. Go into the ICalUL folder. Right-click on ICalUL.msi and choose 'Open Inside'. Right-click on Data1.cab and choose 'Open Inside'.

Then you can right-click on cbw.h and choose View and look for `CURRENTREVNUM` as described above. To view the Product Version field of cbw64.dll, I think you need to copy the dll file out of the cab.