# Information about JNA and JNAerator

JNA stands for [Java Native Access](https://github.com/java-native-access/jna). It lets you use native shared libraries (DLL files) from Java.

[JNAerator](https://github.com/nativelibs4java/JNAerator) is a tool to generate bindings for JNA.

It appears JNAerator was originally hosted on Google Code and was been partly migrated to GitHub. To download a ready-to-use jar file, go to https://code.google.com/archive/p/jnaerator/downloads. It appears the latest version is “jnaerator-0.12-SNAPSHOT-20130727.jar”, even though it says deprecated.

![](C:\Users\lumenetix\OneDrive - ERP Power LLC\Documents\JMCCUL\img\screenshot-of-google-code-jnaerator.png)

## How to run JNAerator

Assuming you installed Universal Library with the default settings, on 64-bit Windows, this is the command to run:
```
java -jar jnaerator-0.12-SNAPSHOT-20130727.jar -library MeasurementComputingUniversal -mode Directory -runtime JNA -package jmccul -o outputDirectory -f "C:\Program Files (x86)\Measurement Computing\DAQ\cbw64.dll" "C:\Users\Public\Documents\Measurement Computing\DAQ\C\cbw.h"
```
Explanation of the JNAerator options used:

* `-library MeasurementComputingUniversal`
  Names the generated file `MeasurementComputingUniversalLibrary.java`.
* `-mode Directory`
  Outputs a a directory of .java files instead of compiling them into a .jar.
* `-runtime JNA`
  Use JNA instead of [BridJ](https://github.com/nativelibs4java/BridJ).
* `-package jmccul`
  Sets the package name of the generated .java files. This puts a package declaration at the top of the java files, and creates a subfolder for the package name.
* `-o outputDirectory`
  Put all the output in a folder called `outputDirectory`.
* `-f`
  Overwrite existing files. Without this flag, if the Java files already exists, JNAerator will crash with an IOException.

For the list of all available JNAerator options, visit the wiki page on GitHub called [Command Line Options And Environment Variables](https://github.com/nativelibs4java/JNAerator/wiki/Command-Line-Options-And-Environment-Variables).

## Output

Expected console output is something like this:
```
Auto-configuring parser...
Parsing native headers...
Normalizing parsed code...
Generating libraries...
Generating CASLibrary.java
#
# SUCCESS: JNAeration completed !
# Output mode is 'Directory(Bindings sources in simple file hierarchy)
#
# => 'C:\path\to\outputDirectory'
#
```

If you have Visual Studio installed, I think it will print some additional lines like this:
```
com.ochafik.admin.visualstudio.VisualStudioUtils getProp
INFO: [environment] VISUAL_STUDIO_HOME=C:\Program Files (x86)\Microsoft Visual Studio
```
If there are errors because it can't find `windows.h` or `time.h`, it's okay.

When finished, you should have these two Java files:

* `outputDirectory\jmccul\MeasurementComputingUniversalLibrary.java`
* `outputDirectory\jmccul\DaqDeviceDescriptor.java`

And it copies the DLL file to here:

* `outputDirectory\lib\win64\cbw64.dll`

### Removing deprecated methods

For some C functions, JNAerator produces two Java method declarations, one marked deprecated and one not. Here's a simplified example of the Java output for the C function [`cbDIn()`](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn.htm) :

```java
/**
* Original signature: int cbDIn(int, int, USHORT*)
* Native declaration: C:\...\cbw.h:1609
* @deprecated Use the safer methods cbDIn(int, int, java.nio.ShortBuffer) and cbDIn(int, int, com.sun.jna.ptr.ShortByReference) instead.
*/
@Deprecated 
int cbDIn(int BoardNum, int PortType, ShortByReference DataValue);

/**
* Original signature: int cbDIn(int, int, USHORT*)
* Native declaration: C:\...\cbw.h:1609
*/
int cbDIn(int BoardNum, int PortType, ShortBuffer DataValue);
```

(Confusingly, the `@deprecated` Javadoc tag includes the method it is deprecating as a suggested alternative.)

I want to remove all the deprecated methods to streamline my IDE's code completion. According to [Command Line Options And Environment Variables](https://github.com/nativelibs4java/JNAerator/wiki/Command-Line-Options-And-Environment-Variables), this option should work:

> * -skipDeprecated
>
>   Don't generate members that would be tagged as @Deprecated

But the JNAerator jar I'm using doesn't recognize that option and will not run if I add it.

Instead, you can use the crazy regular expression below to match the deprecated method declarations. If you replace all matches with an empty string, it will get the job done.
```
\/\*\*\s+\*[\w\s:<>\(\),\*\/\\\.@\{\#\}]+@deprecated\s+int \w+\([\w ,\.]+\);
```


Link to try the regular expression: https://regexr.com/6bokv
