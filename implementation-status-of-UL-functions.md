# Implementation status of all Universal Library functions

This is a lsit of all Universal Library functions from https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/ULStart.htm

To get there, look in the left sidebar and expand Universal Library Function Reference > UL for Windows.

The Status column means whether the JNA wrapper has been written.

TODO for rows which have been written, say what the Java method is.

## Analog I/O functions

| UL function | Status |
| ------------| --------|
| [ `cbACalibrateData()`      ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbACalibrateData.htm)       |  Not written  |
| [ `cbAConvertData()`        ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAConvertData.htm)         |  Not written  |
| [ `cbAConvertPreTrigData()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAConvertPreTrigData.htm)  |  Not written  |
| [ `cbAIn()`                 ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn.htm)                  |  ✅ Written & tested  |
| [ `cbAIn32()`               ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAIn32.htm)                |  ✅ Written & tested  |
| [ `cbAInScan()`             ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAInScan.htm)              |  Not written  |
| [ `cbALoadQueue()`          ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbALoadQueue.htm)           |  Not written  |
| [ `cbAOut()`                ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAOut.htm)                 |  ✅ Written & tested  |
| [ `cbAOutScan()`            ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAOutScan.htm)             |  Not written  |
| [ `cbAPretrig()`            ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbAPretrig.htm)             |  Not written  |
| [ `cbATrig()`               ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbATrig.htm)                |  Not written  |
| [ `cbVIn()`                 ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVIn.htm)                  |  ✅ Written & tested  |
| [ `cbVIn32()`               ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVIn32.htm)                |  ✅ Written & tested  |
| [ `cbVOut()`                ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Analog_IO_Functions/cbVOut.htm)                 |  ✅ Written & tested  |

## Configuration functions

| UL function | Status |
| ------------| --------|
| [ `cbAChanInputMode()`  ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbAChanInputMode.htm)   |  Not written |
| [ `cbAInputMode()`      ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbAInputMode.htm)       |  Not written  |
| [ `cbGetConfig()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm)        |  ✅ Written & tested  |
| [ `cbGetConfigString()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfigString.htm)  |  ✅ Written & tested  |
| [ `cbGetSignal()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetSignal.htm)        |  ❌ Won't implement  |
| [ `cbSelectSignal()`    ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSelectSignal.htm)     |  ❌ Won't implement  |
| [ `cbSetConfig()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm)        |  Written but not tested yet  |
| [ `cbSetConfigString()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfigString.htm)  |  Written but not tested yet  |
| [ `cbSetTrigger()`      ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetTrigger.htm)       |  Not written  |

## Counter functions

| UL function | Status |
| ------------| --------|
| [ `cbC7266Config()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbC7266Config.htm)    |  Not written  |
| [ `cbC8254Config()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbC8254Config.htm)    |  Not written  |
| [ `cbC8536Config()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbC8536Config.htm)    |  Not written  |
| [ `cbC8536Init()`     ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbC8536Init.htm)      |  Not written  |
| [ `cbC9513Config()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbC9513Config.htm)    |  Not written  |
| [ `cbC9513Init()`     ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbC9513Init.htm)      |  Not written  |
| [ `cbCClear()`        ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCClear.htm)         |  Not written  |
| [ `cbCConfigScan()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCConfigScan.htm)    |  Not written  |
| [ `cbCFreqIn()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCFreqIn.htm)        |  Not written  |
| [ `cbCIn()`           ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCIn.htm)            |  Not written  |
| [ `cbCIn32()`         ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCIn32.htm)          |  Not written  |
| [ `cbCIn64()`         ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCIn64.htm)          |  Not written  |
| [ `cbCInScan()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCInScan.htm)        |  Not written  |
| [ `cbCLoad()`         ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCLoad.htm)          |  Not written  |
| [ `cbCLoad32()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCLoad32.htm)        |  Not written  |
| [ `cbCLoad64()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCLoad64.htm)        |  Not written  |
| [ `cbCStatus()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCStatus.htm)        |  Not written  |
| [ `cbCStoreOnInt()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbCStoreOnInt.htm)    |  Not written  |
| [ `cbPulseOutStart()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbPulseOutStart.htm)  |  Not written  |
| [ `cbPulseOutStop()`  ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbPulseOutStop.htm)   |  Not written  |
| [ `cbTimerOutStart()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbTimerOutStart.htm)  |  Not written  |
| [ `cbTimerOutStop()`  ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Counter_Functions/cbTimerOutStop.htm)   |  Not written  |

## Data Logger functions

| UL function | Status |
| ------------| --------|
| [ `cbLogConvertFile()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogConvertFile.htm)        |  ❌ Won't implement  |
| [ `cbLogGetAIChannelCount()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetAIChannelCount.htm)  |  ❌ Won't implement  |
| [ `cbLogGetAIInfo()`         ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetAIInfo.htm)          |  ❌ Won't implement  |
| [ `cbLogGetCJCInfo()`        ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetCJCInfo.htm)         |  ❌ Won't implement  |
| [ `cbLogGetDIOInfo()`        ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetDIOInfo.htm)         |  ❌ Won't implement  |
| [ `cbLogGetFileInfo()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetFileInfo.htm)        |  ❌ Won't implement  |
| [ `cbLogGetFileName()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetFileName.htm)        |  ❌ Won't implement  |
| [ `cbLogGetPreferences()`    ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetPreferences.htm)     |  ❌ Won't implement  |
| [ `cbLogGetSampleInfo()`     ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogGetSampleInfo.htm)      |  ❌ Won't implement  |
| [ `cbLogReadAIChannels()`    ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogReadAIChannels.htm)     |  ❌ Won't implement  |
| [ `cbLogReadCJCChannels()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogReadCJCChannels.htm)    |  ❌ Won't implement  |
| [ `cbLogReadDIOChannels()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogReadDIOChannels.htm)    |  ❌ Won't implement  |
| [ `cbLogReadTimeTags()`      ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogReadTimeTags.htm)       |  ❌ Won't implement  |
| [ `cbLogSetPreferences()`    ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/DataLogger_Functions/cbLogSetPreferences.htm)     |  ❌ Won't implement  |

## Device Discovery functions

| UL function | Status |
| ------------| --------|
| [ `cbCreateDaqDevice()`        ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbCreateDaqDevice.htm)         |  ✅ Written & tested  |
| [ `cbGetBoardNumber()`         ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbGetBoardNumber.htm)          |  ✅ Written & tested  |
| [ `cbGetDaqDeviceInventory()`  ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbGetDaqDeviceInventory.htm)   |  ✅ Written & tested  |
| [ `cbGetNetDeviceDescriptor()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbGetNetDeviceDescriptor.htm)  | ❔ Not written - don't have hardware |
| [ `cbIgnoreInstaCal()`         ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbIgnoreInstaCal.htm)          |  ✅ Written & tested  |
| [ `cbReleaseDaqDevice()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Device-Discovery/cbReleaseDaqDevice.htm)        |  ✅ Written & tested  |

## Digital I/O functions

| UL function | Status |
| ------------| --------|
| [ `cbDBitIn()`      ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitIn.htm)       |  ✅ Written & tested  |
| [ `cbDBitOut()`     ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDBitOut.htm)      |  ✅ Written & tested  |
| [ `cbDClearAlarm()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDClearAlarm.htm)  |  Not written  |
| [ `cbDConfigBit()`  ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigBit.htm)   |  ✅ Written & tested  |
| [ `cbDConfigPort()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDConfigPort.htm)  |  ✅ Written & tested  |
| [ `cbDIn()`         ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn.htm)          |  ✅ Written & tested  |
| [ `cbDIn32()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDIn32.htm)        |  ✅ Written & tested  |
| [ `cbDInArray()`    ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInArray.htm)     |  ❔ Not written - don't know how to do JNA binding  |
| [ `cbDInScan()`     ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDInScan.htm)      | ❔ Written but not tested - don't have hardware |
| [ `cbDOut()`        ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut.htm)         |  ✅ Written & tested  |
| [ `cbDOut32()`      ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOut32.htm)       |  ✅ Written & tested  |
| [ `cbDOutArray()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOutArray.htm)    |  ❔ Not written - don't know how to do JNA binding  |
| [ `cbDOutScan()`    ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Digital_IO_Functions/cbDOutScan.htm)     |  ❔ Written but not tested - don't have hardware  |

## Error Handling functions

| UL function | Status |
| ------------| --------|
| [ `cbErrHandling()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Error_Handling_Functions/cbErrHandling.htm)  |  ❌ Won't implement  |
| [ `cbGetErrMsg()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Error_Handling_Functions/cbGetErrMsg.htm)    |  ✅ Written & tested  |

## Memory Board functions

| UL function | Status |
| ------------| --------|
| [ `cbMemRead()`        ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Memory_Board_Functions/cbMemRead.htm)         |  Not written  |
| [ `cbMemReadPretrig()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Memory_Board_Functions/cbMemReadPretrig.htm)  |  Not written  |
| [ `cbMemReset()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Memory_Board_Functions/cbMemReset.htm)        |  Not written  |
| [ `cbMemSetDTMode()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Memory_Board_Functions/cbMemSetDTMode.htm)    |  Not written  |
| [ `cbMemWrite()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Memory_Board_Functions/cbMemWrite.htm)        |  Not written  |

## Revision Control functions

| UL function | Status |
| ------------| --------|
| [ `cbDeclareRevision()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Revision_Control_Functions/cbDeclareRevision.htm)  |  ❌ Won't implement  |
| [ `cbGetRevision()`     ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Revision_Control_Functions/cbGetRevision.htm)      |  ✅ Written & tested  |

## Streamer File functions

| UL function | Status |
| ------------| --------|
| [ `cbFileAInScan()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Streamer_File_Functions/cbFileAInScan.htm)  |  ❌ Won't implement  |
| [ `cbFileGetInfo()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Streamer_File_Functions/cbFileGetInfo.htm)  |  ❌ Won't implement  |
| [ `cbFilePretrig()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Streamer_File_Functions/cbFilePretrig.htm)  |  ❌ Won't implement  |
| [ `cbFileRead()`    ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Streamer_File_Functions/cbFileRead.htm)     |  ❌ Won't implement  |

## Synchronous I/O functions

| UL function | Status |
| ------------| --------|
| [ `cbDaqInScan()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Synchronous_IO_Functions/cbDaqInScan.htm)        |  Not written  |
| [ `cbDaqOutScan()`      ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Synchronous_IO_Functions/cbDaqOutScan.htm)       |  Not written  |
| [ `cbDaqSetSetpoints()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Synchronous_IO_Functions/cbDaqSetSetpoints.htm)  |  Not written  |
| [ `cbDaqSetTrigger()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Synchronous_IO_Functions/cbDaqSetTrigger.htm)    |  Not written  |

## Temperature Input functions

| UL function | Status |
| ------------| --------|
| [ `cbTIn()`     ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions/cbTIn.htm)      |  Not written  |
| [ `cbTInScan()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Temperature_Input_Functions/cbTInScan.htm)  |  Not written  |

## Windows Memory Management functions

| UL function | Status |
| ------------| --------|
| [ `cbScaledWinArrayToBuf()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbScaledWinArrayToBuf.htm)  |  ???  |
| [ `cbScaledWinBufAlloc()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbScaledWinBufAlloc.htm)    |  ???  |
| [ `cbScaledWinBufToArray()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbScaledWinBufToArray.htm)  |  ???  |
| [ `cbWinArrayToBuf()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinArrayToBuf.htm)        |  ???  |
| [ `cbWinArrayToBuf32()`     ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinArrayToBuf32.htm)      |  ???  |
| [ `cbWinBufAlloc()`         ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc.htm)          |  ???  |
| [ `cbWinBufAlloc32()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc32.htm)        |  ???  |
| [ `cbWinBufAlloc64()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufAlloc64.htm)        |  ???  |
| [ `cbWinBufFree()`          ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufFree.htm)           |  ???  |
| [ `cbWinBufToArray()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufToArray.htm)        |  ???  |
| [ `cbWinBufToArray32()`     ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufToArray32.htm)      |  ???  |
| [ `cbWinBufToArray64()`     ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Windows_Memory_Management_Functions/cbWinBufToArray64.htm)      |  ???  |

## Miscellaneous functions

| UL function | Status |
| ------------| --------|
| [ `cbDeviceLogin()`    ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbDeviceLogin.htm)     |  Not written  |
| [ `cbDeviceLogout()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbDeviceLogout.htm)    |  Not written  |
| [ `cbDisableEvent()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbDisableEvent.htm)    |  Not written  |
| [ `cbEnableEvent()`    ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbEnableEvent.htm)     |  Not written  |
| [ `cbFlashLED()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbFlashLED.htm)        |  Not written  |
| [ `cbFromEngUnits()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbFromEngUnits.htm)    |  Not written  |
| [ `cbGetBoardName()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetBoardName.htm)    |  ✅ Written & tested  |
| [ `cbGetStatus()`      ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetStatus.htm)       |  Not written  |
| [ `cbGetTCValues()`    ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbGetTCValues.htm)     |  Not written  |
| [ `cbInByte()`         ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbInByte.htm)          |  ❌ Won't implement  |
| [ `cbInWord()`         ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbInWord.htm)          |  ❌ Won't implement  |
| [ `cbOutByte()`        ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbOutByte.htm)         |  ❌ Won't implement  |
| [ `cbOutWord()`        ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbOutWord.htm)         |  ❌ Won't implement  |
| [ `cbRS485()`          ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbRS485.htm)           |  Not written  |
| [ `cbStopBackground()` ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbStopBackground.htm)  |  Not written  |
| [ `cbTEDSRead()`       ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbTEDSRead.htm)        |  Not written  |
| [ `cbToEngUnits()`     ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbToEngUnits.htm)      |  Not written  |
| [ `cbToEngUnits32()`   ](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Miscellaneous_Functions/cbToEngUnits32.htm)    |  Not written  |
