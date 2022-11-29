# Implementation status of config items

There are four Universal Library functions to read/write configuration items:

* [`cbGetConfig`](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm)
* [`cbGetConfigString`](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfigString.htm)
* [`cbSetConfig`](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm)
* [`cbSetConfigString`](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfigString.htm)

Glossary:

| Initialism  | Meaning                       |
|-------------|-------------------------------|
| `GI`        | Global info                   |
| `BI`        | Board info                    |
| `DI`        | Digital info                  |
| `XI`        | Expansion info                |
| `CI`        | Counter info                  |
| `AD`, `ADC` | Analog to digital (converter) |
| `DA`, `DAC` | Digital to analog (converter) |
| `AI`        | Analog input                  |
| `DO`        | Digital output                |
| `DI`        | Digital input                 |

What the columns mean in the tables below:

* Item - the thing in cbw.h and MeasurementComputingUniversalLibrary.java.
* Expanded name - the name of the item with spaces added to make it easier to read. Examples of why this is helpful:
    * `GIVERSION` is `GI VERSION` not `GIVE RSION` or `GIVER SION`.
    * `BIDACFORCESENSE` is `BI DAC FORCE SENSE` not `BI DAC FOR CESENSE`.
    * `BIADAIMODE` is `BI AD AI MODE` not `BIADAI MODE`.
    * `BIDIDEBOUNCETIME` is `BI DI DEBOUNCE TIME` not `BIDIDE BOUNCE TIME`.
* Type - the data type you get from the DLL.
    * The DLL only returns an integer or a string. If JMCCUL maps the result to a different type, that type is included
      in parentheses.
* Description - pasted from MCC online docs.
* Read - the JMCCUL method to read the config item, or "not readable".
* Write - the JMCCUL method to write the config item, or "not writeable".

## Global Info

| Item             | Expanded            | Type | Description                                                               | Read                                          | Write         |
|------------------|---------------------|------|---------------------------------------------------------------------------|-----------------------------------------------|---------------|
| `GIVERSION`      | `GI VERSION`        | int  | cb.cfg file format, used by Universal Library to determine compatibility. | `Configuration#getConfigFileVersion()`        | Not writable  |
| `GINUMBOARDS`    | `GI NUM BOARDS`     | int  | Maximum number of boards that can be installed.                           | `Configuration#getMaxBoardCount()`            | Not writable  |
| `GINUMEXPBOARDS` | `GI NUM EXP BOARDS` | int  | Maximum number of expansion boards that can be installed.                 | `ExpansionConfig#getMaxExpansionBoardCount()` | Not writable  |

## Board info

| Item                 | Expanded                 | Type          | Description                                                                                                                 | Read                                                | Write                                                          |
|----------------------|--------------------------|---------------|-----------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------|----------------------------------------------------------------|
| `BIADAIMODE`         | `BI AD AI MODE         ` | int           | Analog input mode.                                                                                                          | `AnalogInputWrapper#getModeForBoard()`              | `AnalogInputWrapper#setModeForBoard()`                         |
| `BIADCHANAIMODE`     | `BI AD CHAN AI MODE    ` | int           | Analog input mode by channel.                                                                                               | `AnalogInputWrapper#getModeForChannel()`            | `AnalogInputWrapper#setModeForChannel()`                       |
| `BIADCHANTYPE`       | `BI AD CHAN TYPE       ` | int           | Analog input channel type.                                                                                                  | `AnalogInputWrapper#getChannelType()`               | `AnalogInputWrapper#setChannelType()`                          |
| `BIADCSETTLETIME`    | `BI ADC SETTLE TIME    ` | int           | ADC settling time.                                                                                                          | `AnalogInputWrapper#getSettlingTime()`              | `AnalogInputWrapper#setSettlingTime()`                         |
| `BIADDATARATE`       | `BI AD DATA RATE       ` | int           | A/D data rate.                                                                                                              | `AnalogInputWrapper#getDataRate()`                  | `AnalogInputWrapper#setDataRate()`                             |
| `BIADRES`            | `BI AD RES             ` | int           | A/D resolution.                                                                                                             | `AnalogInputWrapper#getResolution()`                | Not writable                                                   |
| `BIADTIMINGMODE`     | `BI AD TIMING MODE     ` | int           | Timing mode.                                                                                                                | `AnalogInputWrapper#getTimingMode()`                | `AnalogInputWrapper#setTimingMode()`                           |
| `BIADTRIGCOUNT`      | `BI AD TRIG COUNT      ` | int           | Number of analog input samples to acquire per trigger.                                                                      | `AnalogInputWrapper#getTriggerCount()`              | `AnalogInputWrapper#setTriggerCount()`                         |
| `BIADTRIGSRC`        | `BI AD TRIG SRC        ` | int           | A/D trigger source.                                                                                                         | `AnalogInputWrapper#getTriggerSourceChannel()`      | `AnalogInputWrapper#setTriggerSourceChannel()`                 |
| `BIADXFERMODE`       | `BI AD XFER MODE       ` | int           | Data transfer mode.                                                                                                         | `AnalogInputWrapper#getDataTransferMode()`          | `AnalogInputWrapper#setDataTransferMode()`                     |
| `BIBASEADR`          | `BI BASE ADR           ` | int           | Base address of the device.                                                                                                 | `DaqDevice#getBaseAddress()`                        | `DaqDevice#()setBaseAddress`                                   |
| `BIBOARDTYPE`        | `BI BOARD TYPE         ` | int           | Unique number from 0 to 0x8000 which describes the type of board installed.                                                 | `DaqDevice#getBoardType()`                          | Not writeable                                                  |
| `BICALOUTPUT`        | `BI CAL OUTPUT`          | int           | Cal pin voltage on supported USB devices.                                                                                   | Not readable                                        | `DaqDevice#setCalPinVoltage()`                                 |
| `BICALTABLETYPE`     | `BI CAL TABLE TYPE     ` | int           | The coefficients table used for calibration.                                                                                | `DaqDevice#getCalibrationTableType()`               | `DaqDevice#setCalibrationTableType()`                          |
| `BICHANRTDTYPE`      | `BI CHAN RTD TYPE      ` | int           | RTD (resistance temperature detector) sensor type.                                                                          | `TemperatureWrapper#getRtdSensorType()`             | `TemperatureWrapper#setRtdSensorType()`                        |
| `BICHANTCTYPE`       | `BI CHAN TC TYPE       ` | int           | Thermocouple sensor type.                                                                                                   | `TemperatureWrapper#getThermocoupleType()`          | `TemperatureWrapper#setThermocoupleType()`                     |
| `BICINUMDEVS`        | `BI CI NUM DEVS        ` | int           | Number of counter devices.                                                                                                  | `CounterWrapper#getChannelCount()`                  | Not writable                                                   |
| `BICLOCK`            | `BI CLOCK              ` | int           | Clock frequency in megahertz.                                                                                               | `DaqDevice#getClockFrequencyMegahertz()`            | `DaqDevice#setClockFrequencyMegahertz()`                       |
| `BICTRTRIGCOUNT`     | `BI CTR TRIG COUNT     ` | int           | Number of counter samples to acquire per trigger.                                                                           | `CounterWrapper#getCounterTriggerCount()`           | `CounterWrapper#setCounterTriggerCount()`                      |
| `BIDACFORCESENSE`    | `BI DAC FORCE SENSE    ` | int           | Remote sensing state of an analog output channel.                                                                           | `AnalogOutputWrapper#getForceSense()`               | `AnalogOutputWrapper#setForceSense()`                          |
| `BIDACRANGE`         | `BI DAC RANGE          ` | int           | Analog output voltage range.                                                                                                | `AnalogOutputWrapper#getRange()`                    | `AnalogOutputWrapper#setRange()`                               |
| `BIDACRES`           | `BI DAC RES            ` | int           | D/A resolution.                                                                                                             | `AnalogOutputWrapper#getResolution()`               | Not writable                                                   |
| `BIDACSTARTUP`       | `BI DAC STARTUP        ` | int           | Configuration register STARTUP bit setting.                                                                                 | `AnalogOutputWrapper#isSaveDacValues()`             | `AnalogOutputWrapper#setSaveDacvalues()`                       |
| `BIDACTRIGCOUNT`     | `BI DAC TRIG COUNT     ` | int           | Number of analog output samples to acquire per trigger.                                                                     | `AnalogOutputWrapper#getTriggerCount()`             | `AnalogOutputWrapper#setTriggerCount()`                        |
| `BIDACUPDATECMD`     | `BI DAC UPDATE CMD`      | int (void)    | Updates all analog output channels.                                                                                         | Not readable                                        | `AnalogOutputWrapper#update()`                                 |
| `BIDACUPDATEMODE`    | `BI DAC UPDATE MODE    ` | int           | Update mode for a digital-to-analog converter.                                                                              | `AnalogOutputWrapper#getUpdateMode()`               | `AnalogOutputWrapper#getUpdateMode()`                          |
| `BIDETECTOPENTC`     | `BI DETECT OPEN TC     ` | int (Boolean) | Open thermocouple detection setting.                                                                                        | `TemperatureWrapper#getOpenThermocoupleDetection()` | `TemperatureWrapper#setOpenThermocoupleDetection()`            |
| `BIDEVMACADDR`       | `BI DEV MAC ADDR`        | string        | MAC address of an Ethernet device.                                                                                          | `NetworkConfig#getMacAddress()`                     | Not writable                                                   |
| `BIDEVSERIALNUM`     | `BI DEV SERIAL NUM`      | string        | Factory serial number of a USB or Bluetooth device.                                                                         | `DaqDevice#getDactorySerialNumber()`                | Not writable                                                   |
| `BIDEVUNIQUEID`      | `BI DEV UNIQUE ID`       | string        | Unique identifier of a discoverable device, such as the serial number of a USB device or MAC address of an Ethernet device. | `DaqDevice#getUniqueID()`                           | Not writable                                                   |
| `BIDEVVERSION`       | `BI DEV VERSION`         | string        | Firmware version and FPGA version installed on a device.                                                                    | `DaqDevice#getVersion()`                            | Not writable                                                   |
| `BIDIDEBOUNCESTATE`  | `BI DI DEBOUNCE STATE`   | int           | State of the digital inputs when debounce timing is set.                                                                    | Not readable                                        | `DigitalInpuWrapper#setDebounceState()`                        |
| `BIDIDEBOUNCETIME`   | `BI DI DEBOUNCE TIME`    | int           | Debounce time of digital inputs.                                                                                            | Not readable                                        | `DigitalInputWrapper#setDebounceTime()`                        |
| `BIDINUMDEVS`        | `BI DI NUM DEVS        ` | int           | Number of digital devices.                                                                                                  | `DigitalWrapper#getPortCount()`                     | Not writable                                                   |
| `BIDISOFILTER`       | `BI DI ISO FILTER (??) ` | int (Boolean) | AC filter setting.                                                                                                          | `DigitalInputWrapper#isAcFilterEnabled()`           | `DigitalInputWrapper#setAcFilterEnabled()`                     |
| `BIDITRIGCOUNT`      | `BI DI TRIG COUNT      ` | int           | Number of digital input samples to acquire per trigger.                                                                     | `DigitalInputWrapper#getTriggerCount()`             | `DigitalInputWrapper#setTriggerCount()`                        |
| `BIDMACHAN`          | `BI DMA CHAN           ` | int           | DMA (direct memory access?) channel.                                                                                        | `DaqDevice#getDmaChannel()`                         | `DaqDevice#setDmaChannel()`                                    |
| `BIDOTRIGCOUNT`      | `BI DO TRIG COUNT      ` | int           | Number of digital output samples to generate per trigger.                                                                   | `DigitalOutputWrapper#getTriggerCount()`            | `DigitalOutputWrapper#setTriggerCount()`                       |
| `BIDTBOARD`          | `BI DT BOARD           ` | int           | Board number of the connected Data Translation board.                                                                       | `DaqDevice#getDataTranslationBoardNumber()`         | Not writable                                                   |
| `BIEXTCLKTYPE`       | `BI EXT CLK TYPE       ` | int (enum)    | External clock type.                                                                                                        | `DaqDevice#getExternalClockType()`                  | `DaqDevice#setExternalClockType()`                             |
| `BIEXTINPACEREDGE`   | `BI EXT IN PACER EDGE  ` | int           | Input scan clock edge.                                                                                                      | `DaqDevice#getInputPacerClockEdge()`                | `DaqDevice#setInputPacerClockEdge()`                           |
| `BIEXTOUTPACEREDGE`  | `BI EXT OUT PACER EDGE ` | int           | Output scan clock edge.                                                                                                     | `DaqDevice#getOutputPacerClockEdge()`               | `DaqDevice#setOutputPacerClockEdge()`                          |
| `BIHIDELOGINDLG`     | `BI HIDE LOGIN DLG`      | int           | Enables or disables the Device Login dialog.                                                                                | Not readable                                        | `DaqDevice#setHideLoginDialog()`                               |
| `BIINPUTPACEROUT`    | `BI INPUT PACER OUT    ` | int           | Input pacer clock state.                                                                                                    | `DaqDevice#getInputPacerClockEnable()`              | `DaqDevice#setInputPacerClockEnable()`                         |
| `BIINTEDGE`          | `BI INT EDGE           ` | int           | Interrupt edge.                                                                                                             | `DaqDevice#getInterruptEdge()`                      | `DaqDevice#setInterruptEdge()`                                 |
| `BIINTLEVEL`         | `BI INT LEVEL          ` | int           | Interrupt level.                                                                                                            | `DaqDevice#getInterruptLevel()`                     | `DaqDevice#setInterruptLevel()`                                |
| `BINETCONNECTCODE`   | `BI NET CONNECT CODE   ` | int           | Code used to connect with a device over a network connection.                                                               | `NetworkConfig#getConnectionCode()`                 | `NetworkConfig#setConnectionCode()`                            |
| `BINETIOTIMEOUT`     | `BI NET IO TIMEOUT     ` | int           | Amount of time to wait for a web device to acknowledge a command or query sent over a network connection.                   | `NetworkConfig#getNetworkIoTimeoutMillisec()`       | `NetworkConfig#setNetworkIoTimeoutMillisec()`                  |
| `BINUMADCHANS`       | `BI NUM AD CHANS       ` | int           | Number of A/D channels.                                                                                                     | `AnalogInputWrapper#getChannelCount()`              | `AnalogInputWrapper#setAChannelCount()`  (wtf does this mean?) |
| `BINUMDACHANS`       | `BI NUM DA CHANS       ` | int           | Number of D/A channels.                                                                                                     | `AnalogOutputWrapper#getChannelCount()`             | Not writable                                                   |
| `BINUMIOPORTS`       | `BI NUM IO PORTS       ` | int           | Number of I/O ports used by the device.                                                                                     | `DaqDevice#getPortCount()`                          | Not writable                                                   |
| `BINUMTEMPCHANS`     | `BI NUM TEMP CHANS     ` | int           | Number of temperature channels.                                                                                             | `TemperatureWrapper#getChannelCount()`              | Not writable                                                   |
| `BIOUTPUTPACEROUT`   | `BI OUTPUT PACER OUT`    | int           | Enables or disables the output pacer clock signal.                                                                          | Not readable???                                     | `DaqDevice#setOutputPacerClockEnable()`                        |
| `BIPANID`            | `BI PAN ID             ` | int           | Personal Area Network (PAN) identifier for a USB device that supports wireless communication.                               | `DaqDevice#()`                                      | `WirelessConfig#()`                                            |
| `BIPATTERNTRIGPORT`  | `BI PATTERN TRIG PORT  ` | int           | Pattern trigger port.                                                                                                       | `DaqDevice#getPatternTriggerPort()`                 | `DaqDevice#setPatternTriggerPort()`                            |
| `BIRANGE`            | `BI RANGE              ` | int           | Selected voltage range.                                                                                                     | yes - what about BIDACRANGE                         | yes - what about BIDACRANGE                                    |
| `BIRFCHANNEL`        | `BI RF CHANNEL         ` | int           | RF channel number used to transmit/receive data by a USB device that supports wireless communication.                       | `WirelessConfig#getRfChannel()`                     | `WirelessConfig#setRfChannel()`                                |
| `BIRSS`              | `BI RSS                ` | int           | Received signal strength in dBm of a remote device.                                                                         | `WirelessConfig#getSignalStrength()`                | yes???                                                         |
| `BISERIALNUM`        | `BI SERIAL NUM         ` | int           | Custom serial number assigned by a user to a USB device.                                                                    | `DaqDevice#getUserSpecifiedSerialNumber()`          | `DaqDevice#setUserSpecifiedSerialNumber()`                     |
| `BISYNCMODE`         | `BI SYNC MODE          ` | int           | Simultaneous mode setting.                                                                                                  | `DaqDevice#getSyncMode()`                           | `DaqDevice#setSyncMode()`                                      |
| `BITEMPAVG`          | `BI TEMP AVG           ` | int           | Number of temperature samples per average.                                                                                  | `TemperatureWrapper#geScansToAverage()`             | `TemperatureWrapper#setScansToAverage()`                       |
| `BITEMPREJFREQ`      | `BI TEMP REJ FREQ      ` | int           | Temperature rejection frequency.                                                                                            | `TemperatureWrapper#getRejectionFrequency()`        | `TemperatureWrapper#setRejectionFrequency()`                   |
| `BITEMPSCALE`        | `BI TEMP SCALE         ` | int           | Temperature scale.                                                                                                          | `TemperatureWrapper#getUnits()`                     | `TemperatureWrapper#setUnits()`                                |
| `BITERMCOUNTSTATBIT` | `BI TERM COUNT STAT BIT` | int           | Terminal count output status for a specified bit.                                                                           | `DaqDevice#getTerminalCountOutputStatus()`          | `DaqDevice#setTerminalCountOutputStatus()`                     |
| `BIUSERDEVIDNUM`     | `BI USER DEV ID NUM    ` | int???        | User-configured string that identifies a USB device.                                                                        | `DaqDevice#getUserSpecifiedString()`                | Not writable??                                                 |
| `BIUSERDEVID`        | `BI USER DEV ID`         | string        | User-configured string identifier from an Ethernet, Bluetooth, or USB device.                                               | `DaqDevice#getUserSpecifiedID()`                    | `DaqDevice#setUserSpecifiedID()`                               |
| `BIUSESEXPS`         | `BI USES EXPS          ` | int (Boolean) | Expansion board support.                                                                                                    | `ExpansionConfig#isExpansionBoardSupported()`       | Not writable                                                   |
| `BIWAITSTATE`        | `BI WAIT STATE         ` | int           | Wait State jumper setting.                                                                                                  | `DaqDevice#getWaitStateJumper()`                    | `DaqDevice#setWaitStateJumper()`                               |

## Digital info

| Item                | Expanded               | Type | Description                                                                                   | Read                                     | Write                                    |
|---------------------|------------------------|------|-----------------------------------------------------------------------------------------------|------------------------------------------|------------------------------------------|
| `DIDEVTYPE`         | `DI DEV TYPE         ` | int  | Device type (AUXPORT, FIRSTPORTA, etc).                                                       | `DigitalPort#getPortType()`              | Not writeable                            |
| `DICONFIG`          | `DI CONFIG           ` | int  | Current configuration (INPUT or OUTPUT).                                                      | `DigitalPort#getDirection()`             | Not writeable                            |
| `DINUMBITS`         | `DI NUM BITS         ` | int  | Number of bits in the port.                                                                   | `DigitalPort#getBitCount()`              | Not writeable                            |
| `DICURVAL`          | `DI CUR VAL          ` | int  | Current value of outputs.                                                                     | `DigitalPort#getPresentValue()`          | Not writeable                            |
| `DIDISABLEDIRCHECK` | `DI DISABLE DIR CHECK` | int  | Whether to check the direction setting when calling cbDOut(), cbDBitOut(), and cbDOutArray(). | `DigitalPort#getDirectionCheckEnabled()` | `DigitalPort#setDirectionCheckEnabled()` |
| `DIINMASK`          | `DI IN MASK          ` | int  | Which bits are configured for input.                                                          | `DigitalPort#getInputMask()`             | Not writeable                            |
| `DIOUTMASK`         | `DI OUT MASK         ` | int  | Which bits are configured for output.                                                         | `DigitalPort#getOutputMask()`            | Not writeable                            |

## Counter info

| Item        | Expanded      | Type | Description     | Read              | Write |
|-------------|---------------|------|-----------------|-------------------|-------|
| `CICTRNUM`  | `CI CTR NUM ` | int  | Counter number. | yes CounterConfig | NO    |
| `CICTRTYPE` | `CI CTR TYPE` | int  | Counter type.   | yes CounterConfig | NO    |

## Expansion info

| Item            | Expanded           | Type | Description                                    | Read                | Write               |
|-----------------|--------------------|------|------------------------------------------------|---------------------|---------------------|
| `XIBOARDTYPE`   | `XI BOARD TYPE   ` | int  | Board type.                                    | yes ExpansionConfig | Not writeable       |
| `XIMUXADCHAN1`  | `XI MUX AD CHAN 1` | int  | First A/D channel connected to the EXP board.  | yes ExpansionConfig | yes ExpansionConfig |
| `XIMUXADCHAN2`  | `XI MUX AD CHAN 2` | int  | Second A/D channel connected to the EXP board. | yes                 | yes                 |
| `XIRANGE1`      | `XI RANGE 1      ` | int  | Range (gain) of the low 16 channels.           | yes                 | yes                 |
| `XIRANGE2`      | `XI RANGE 2      ` | int  | Range (gain) of the high 16 channels.          | yes                 | yes                 |
| `XICJCCHAN`     | `XO CJC CHAN     ` | int  | A/D channel connected to the CJC.              | yes                 | yes                 |
| `XITHERMTYPE`   | `XI THERM TYPE   ` | int  | Sensor type (thermocouple or RTD).             | yes                 | yes                 |
| `XINUMEXPCHANS` | `XI NUM EXP CHANS` | int  | Number of channels on the expansion board.     | yes                 | Not writeable       |
| `XIPARENTBOARD` | `XI PARENT BOARD ` | int  | Board number of the base A/D board.            | yes                 | Not writeable       |

