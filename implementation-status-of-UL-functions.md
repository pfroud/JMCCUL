# Implementation status of all Universal Library functions

This is a liSt of all Universal Library functions from https://files.digilent.com/manuals/Mcculw_WebHelp/ULStart.htm

Universal Library Function Reference > UL for Windows.

To get there, look in the left sidebar and expand Universal Library Function Reference > UL for Windows.

The Status column means whether the JNA wrapper has been written in JMCCUL.

TODO for rows which have been written, say what the Java method is.


## Analog I/O functions

| UL function                                                                                                                                                                | Status                                                               |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------|
| [ `cbACalibrateData()`      ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbACalibrateData.htm)      | Not written                                                          |
| [ `cbAConvertData()`        ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAConvertData.htm)        | Not written                                                          |
| [ `cbAConvertPreTrigData()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAConvertPreTrigData.htm) | Not written                                                          |
| [ `cbAIn()`                 ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn.htm)                 | ✅ `AnalogInputWrapper#read()`                                        |
| [ `cbAIn32()`               ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn32.htm)               | ✅ `AnalogInputWrapper#read32()`                                      |
| [ `cbAInScan()`             ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAInScan.htm)             | ❔ `AnalogInputWrapper#scan()` but I don't know how to do JNA binding |
| [ `cbALoadQueue()`          ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbALoadQueue.htm)          | Not written                                                          |
| [ `cbAOut()`                ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAOut.htm)                | ✅ `AnalogOutputWrapper#write()`                                      |
| [ `cbAOutScan()`            ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAOutScan.htm)            | Written but not tested                                               |
| [ `cbAPretrig()`            ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAPretrig.htm)            | Not written                                                          |
| [ `cbATrig()`               ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbATrig.htm)               | Not written                                                          |
| [ `cbVIn()`                 ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVIn.htm)                 | ✅ `AnalogInputWrapper#readVoltage()`                                 |
| [ `cbVIn32()`               ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVIn32.htm)               | ✅ `AnalogInputWrapper#readVoltage32()`                               |
| [ `cbVOut()`                ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVOut.htm)                | ✅ `AnalogOutputWrapper#writeVoltage()`                               |


## Configuration functions

| UL function                                                                                                                                                            | Status                                     |
|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------|
| [ `cbAChanInputMode()`  ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbAChanInputMode.htm)  | ✅ `AnalogInputWrapper#setModeForChannel()` |
| [ `cbAInputMode()`      ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbAInputMode.htm)      | ✅ `AnalogInputWrapper#setModeForBoard()`   |
| [ `cbGetConfig()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm)       | ✅ `ConfigurationWrapper#getInt()`          |
| [ `cbGetConfigString()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfigString.htm) | ✅ `ConfigurationWrapper#getString()`       |
| [ `cbGetSignal()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetSignal.htm)       | ❌ Won't implement                          |
| [ `cbSelectSignal()`    ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSelectSignal.htm)    | ❌ Won't implement                          |
| [ `cbSetConfig()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm)       | ✅ `ConfigurationWrapper#setInt()`          |
| [ `cbSetConfigString()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfigString.htm) | ✅ `ConfigurationWrapper#setString()`       |
| [ `cbSetTrigger()`      ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetTrigger.htm)      | Not written                                |


## Counter functions

| UL function                                                                                                                                                  | Status      |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------|
| [ `cbC7266Config()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbC7266Config.htm)   | Not written |
| [ `cbC8254Config()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbC8254Config.htm)   | Not written |
| [ `cbC8536Config()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbC8536Config.htm)   | Not written |
| [ `cbC8536Init()`     ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbC8536Init.htm)     | Not written |
| [ `cbC9513Config()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbC9513Config.htm)   | Not written |
| [ `cbC9513Init()`     ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbC9513Init.htm)     | Not written |
| [ `cbCClear()`        ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCClear.htm)        | Not written |
| [ `cbCConfigScan()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCConfigScan.htm)   | Not written |
| [ `cbCFreqIn()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCFreqIn.htm)       | Not written |
| [ `cbCIn()`           ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCIn.htm)           | Not written |
| [ `cbCIn32()`         ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCIn32.htm)         | Not written |
| [ `cbCIn64()`         ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCIn64.htm)         | Not written |
| [ `cbCInScan()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCInScan.htm)       | Not written |
| [ `cbCLoad()`         ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCLoad.htm)         | Not written |
| [ `cbCLoad32()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCLoad32.htm)       | Not written |
| [ `cbCLoad64()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCLoad64.htm)       | Not written |
| [ `cbCStatus()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCStatus.htm)       | Not written |
| [ `cbCStoreOnInt()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCStoreOnInt.htm)   | Not written |
| [ `cbPulseOutStart()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbPulseOutStart.htm) | Not written |
| [ `cbPulseOutStop()`  ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbPulseOutStop.htm)  | Not written |
| [ `cbTimerOutStart()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbTimerOutStart.htm) | Not written |
| [ `cbTimerOutStop()`  ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbTimerOutStop.htm)  | Not written |


## Data Logger functions

| UL function                                                                                                                                                                   | Status            |
|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------|
| [ `cbLogConvertFile()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogConvertFile.htm)       | ❌ Won't implement |
| [ `cbLogGetAIChannelCount()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetAIChannelCount.htm) | ❌ Won't implement |
| [ `cbLogGetAIInfo()`         ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetAIInfo.htm)         | ❌ Won't implement |
| [ `cbLogGetCJCInfo()`        ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetCJCInfo.htm)        | ❌ Won't implement |
| [ `cbLogGetDIOInfo()`        ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetDIOInfo.htm)        | ❌ Won't implement |
| [ `cbLogGetFileInfo()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetFileInfo.htm)       | ❌ Won't implement |
| [ `cbLogGetFileName()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetFileName.htm)       | ❌ Won't implement |
| [ `cbLogGetPreferences()`    ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetPreferences.htm)    | ❌ Won't implement |
| [ `cbLogGetSampleInfo()`     ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetSampleInfo.htm)     | ❌ Won't implement |
| [ `cbLogReadAIChannels()`    ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogReadAIChannels.htm)    | ❌ Won't implement |
| [ `cbLogReadCJCChannels()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogReadCJCChannels.htm)   | ❌ Won't implement |
| [ `cbLogReadDIOChannels()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogReadDIOChannels.htm)   | ❌ Won't implement |
| [ `cbLogReadTimeTags()`      ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogReadTimeTags.htm)      | ❌ Won't implement |
| [ `cbLogSetPreferences()`    ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogSetPreferences.htm)    | ❌ Won't implement |


## Device Discovery functions

| UL function                                                                                                                                                                   | Status                                   |
|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------|
| [ `cbCreateDaqDevice()`        ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbCreateDaqDevice.htm)        | ✅ `DaqDevice` constructor                |
| [ `cbGetBoardNumber()`         ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbGetBoardNumber.htm)         | ✅ `DaqDeviceDescriptor#getBoardNumber()` |
| [ `cbGetDaqDeviceInventory()`  ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbGetDaqDeviceInventory.htm)  | ✅ `DaqDeviceDiscovery#findDescriptors()` |
| [ `cbGetNetDeviceDescriptor()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbGetNetDeviceDescriptor.htm) | ❔ Not written - don't have hardware      |
| [ `cbIgnoreInstaCal()`         ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbIgnoreInstaCal.htm)         | ✅ `DaqDevice` static initializer block   |
| [ `cbReleaseDaqDevice()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbReleaseDaqDevice.htm)       | ✅ `DaqDevice#close()`                    |


## Digital I/O functions

| UL function                                                                                                                                                 | Status                                                                          |
|-------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------|
| [ `cbDBitIn()`      ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitIn.htm)      | ✅ `DigitalInputWrapper#readBit()`                                               |
| [ `cbDBitOut()`     ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitOut.htm)     | ✅ `DigitalOutputWrapper#writeBit()`                                             |
| [ `cbDClearAlarm()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDClearAlarm.htm) | Not written                                                                     |
| [ `cbDConfigBit()`  ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigBit.htm)  | ✅ `DigitalWrapper#setBitDirection()`                                            |
| [ `cbDConfigPort()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigPort.htm) | ✅ `DigitalWrapper#setPortDirection()`                                           |
| [ `cbDIn()`         ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn.htm)         | ✅ `DigitalInputWrapper#readPort()`                                              |
| [ `cbDIn32()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn32.htm)       | ✅ `DigitalInputWrapper#readPort32()`                                            |
| [ `cbDInArray()`    ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInArray.htm)    | ❔ `DigitalInputWrapper#inputPortArray()` but I don't know how to do JNA binding |
| [ `cbDInScan()`     ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInScan.htm)     | ❔ Written but not tested - don't have hardware                                  |
| [ `cbDOut()`        ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut.htm)        | ✅ `DigitalOutputWrapper#writePort()`                                            |
| [ `cbDOut32()`      ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut32.htm)      | ✅ `DigitalOutputWrapper#writePort32()`                                          |
| [ `cbDOutArray()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOutArray.htm)   | ❔ Not written - don't know how to do JNA binding                                |
| [ `cbDOutScan()`    ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOutScan.htm)    | ❔ Written but not tested - don't have hardware                                  |


## Error Handling functions

| UL function                                                                                                                                                     | Status                            |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------|
| [ `cbErrHandling()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Error_Handling_Functions/cbErrHandling.htm) | ❌ Won't implement                 |
| [ `cbGetErrMsg()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Error_Handling_Functions/cbGetErrMsg.htm)   | ✅ `JMCCULUtils#getErrorMessage()` |


## Memory Board functions

| UL function                                                                                                                                                         | Status      |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------|
| [ `cbMemRead()`        ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Memory_Board_Functions/cbMemRead.htm)        | Not written |
| [ `cbMemReadPretrig()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Memory_Board_Functions/cbMemReadPretrig.htm) | Not written |
| [ `cbMemReset()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Memory_Board_Functions/cbMemReset.htm)       | Not written |
| [ `cbMemSetDTMode()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Memory_Board_Functions/cbMemSetDTMode.htm)   | Not written |
| [ `cbMemWrite()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Memory_Board_Functions/cbMemWrite.htm)       | Not written |


## Revision Control functions

| UL function                                                                                                                                                               | Status                           |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------|
| [ `cbDeclareRevision()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Revision_Control_Functions/cbDeclareRevision.htm) | ❌ Won't implement                |
| [ `cbGetRevision()`     ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Revision_Control_Functions/cbGetRevision.htm)     | ✅ `JMCCULUtils#getDLLRevision()` |


## Streamer File functions

| UL function                                                                                                                                                    | Status            |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------|
| [ `cbFileAInScan()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Streamer_File_Functions/cbFileAInScan.htm) | ❌ Won't implement |
| [ `cbFileGetInfo()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Streamer_File_Functions/cbFileGetInfo.htm) | ❌ Won't implement |
| [ `cbFilePretrig()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Streamer_File_Functions/cbFilePretrig.htm) | ❌ Won't implement |
| [ `cbFileRead()`    ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Streamer_File_Functions/cbFileRead.htm)    | ❌ Won't implement |


## Synchronous I/O functions

| UL function                                                                                                                                                             | Status      |
|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------|
| [ `cbDaqInScan()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Synchronous_IO_Functions/cbDaqInScan.htm)       | Not written |
| [ `cbDaqOutScan()`      ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Synchronous_IO_Functions/cbDaqOutScan.htm)      | Not written |
| [ `cbDaqSetSetpoints()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Synchronous_IO_Functions/cbDaqSetSetpoints.htm) | Not written |
| [ `cbDaqSetTrigger()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Synchronous_IO_Functions/cbDaqSetTrigger.htm)   | Not written |


## Temperature Input functions

| UL function                                                                                                                                                | Status                                           |
|------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------|
| [ `cbTIn()`     ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions/cbTIn.htm)     | ✅ `TemperatureWrapper#read()`                    |
| [ `cbTInScan()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions/cbTInScan.htm) | ❔ Not written - don't know how to do JNA binding |


## Windows Memory Management functions

| UL function                                                                                                                                                                                | Status |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------|
| [ `cbScaledWinArrayToBuf()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbScaledWinArrayToBuf.htm) | ???    |
| [ `cbScaledWinBufAlloc()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbScaledWinBufAlloc.htm)   | ???    |
| [ `cbScaledWinBufToArray()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbScaledWinBufToArray.htm) | ???    |
| [ `cbWinArrayToBuf()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinArrayToBuf.htm)       | ???    |
| [ `cbWinArrayToBuf32()`     ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinArrayToBuf32.htm)     | ???    |
| [ `cbWinBufAlloc()`         ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm)         | ???    |
| [ `cbWinBufAlloc32()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc32.htm)       | ???    |
| [ `cbWinBufAlloc64()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc64.htm)       | ???    |
| [ `cbWinBufFree()`          ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufFree.htm)          | ???    |
| [ `cbWinBufToArray()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufToArray.htm)       | ???    |
| [ `cbWinBufToArray32()`     ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufToArray32.htm)     | ???    |
| [ `cbWinBufToArray64()`     ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufToArray64.htm)     | ???    |


## Miscellaneous functions

| UL function                                                                                                                                                          | Status                       |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------|
| [ `cbDeviceLogin()`    ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbDeviceLogin.htm)    | Not written                  |
| [ `cbDeviceLogout()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbDeviceLogout.htm)   | Not written                  |
| [ `cbDisableEvent()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbDisableEvent.htm)   | Not written                  |
| [ `cbEnableEvent()`    ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbEnableEvent.htm)    | Not written                  |
| [ `cbFlashLED()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbFlashLED.htm)       | Not written                  |
| [ `cbFromEngUnits()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbFromEngUnits.htm)   | Not written                  |
| [ `cbGetBoardName()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetBoardName.htm)   | ✅ `DaqDevice#getBoardName()` |
| [ `cbGetStatus()`      ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetStatus.htm)      | Not written                  |
| [ `cbGetTCValues()`    ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetTCValues.htm)    | Not written                  |
| [ `cbInByte()`         ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbInByte.htm)         | ❌ Won't implement            |
| [ `cbInWord()`         ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbInWord.htm)         | ❌ Won't implement            |
| [ `cbOutByte()`        ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbOutByte.htm)        | ❌ Won't implement            |
| [ `cbOutWord()`        ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbOutWord.htm)        | ❌ Won't implement            |
| [ `cbRS485()`          ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbRS485.htm)          | Not written                  |
| [ `cbStopBackground()` ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbStopBackground.htm) | Not written                  |
| [ `cbTEDSRead()`       ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbTEDSRead.htm)       | Not written                  |
| [ `cbToEngUnits()`     ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbToEngUnits.htm)     | Not written                  |
| [ `cbToEngUnits32()`   ](https://files.digilent.com/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbToEngUnits32.htm)   | Not written                  |
