# Implementation status of config items

There are four Universal Library functions:

* [`cbGetConfig`](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfig.htm)
* [`cbGetConfigString`](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbGetConfigString.htm)
* [`cbSetConfig`](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfig.htm)
* [`cbSetConfigString`](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbSetConfigString.htm)

Glossary:

| Initialism  | Meaning                       |
| ----------- | ----------------------------- |
| `GI`        | global info                   |
| `BI`        | board info                    |
| `DI`        | digital info                  |
| `XI`        | expansion info                |
| `AD`, `ADC` | analog to digital (converter) |
| `DA`, `DAC` | digital to analog (converter) |
| `AI`        | analog input                  |
| `CI`        | counter info                  |
| `DO`        | digital output                |
| `DI`        | digital input                 |

What the columns mean in the tables below:

* Item - the thing in cbw.h.
* Expanded name - the name of the item with spaces added so you can read it. For example:
  * `GIVERSION` is `GI VERSION` not `GIVE RSION` or `GIVER SION`
  * `BIDACFORCESENSE` is `BI DAC FORCE SENSE` not `BI DAC FOR CESENSE`
  * `BIADAIMODE` is `BI AD AI MODE`
* Type - the data type you get from the DLL. cbGetConfig and cbSetConfig operate on integers, but some items get mapped to Boolean or an enum in Java.
* Description - pasted from MCC online docs.
* Read - whether you can read it with cbGetConfig or cbGetConfigString.
* Write - whether you can write it with cbSetConfig or cbSetConfigString.

## Global Info

| Item           | Expanded          | Type    | Description                                                  | Read | Write |
| -------------- | ----------------- | ------- | ------------------------------------------------------------ | ---- | ----- |
| `GIVERSION`      | `GI VERSION`        | integer | cb.cfg file format; used by the library to determine compatibility. | yes  | no    |
| `GINUMBOARDS`    | `GI NUM BOARDS`     | integer | Maximum number of boards that can be installed.              | yes  | no    |
| `GINUMEXPBOARDS` | `GI NUM EXP BOARDS` | integer | Maximum number of expansion boards that can be installed.   | yes  | no    |

## Board info

| Item               | Expanded               | Type              | Description                                                  | Read | Write                    |
| ------------------ | ---------------------- | ----------------- | ------------------------------------------------------------ | ---- | ------------------------ |
| `BIADAIMODE`         | `BI AD AI MODE         ` | integer           | Analog input mode.                                           | yes  | no, use the[`cbAInputMode`](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbAInputMode.htm) function instead |
| `BIADCHANAIMODE`     | `BI AD CHAN AI MODE    ` | integer           | Analog input mode by channel.                                | yes  | no, use the [`cbAChanInputMode`](https://www.mccdaq.com/pdfs/manuals/Mcculw_WebHelp/hh_goto.htm?ULStart.htm#Function_Reference/Configuration_Functions/cbAChanInputMode.htm) function instead |
| `BIADCHANTYPE`       | `BI AD CHAN TYPE       ` | integer           | Analog input channel type.                                   | yes  | yes                      |
| `BIADCSETTLETIME`    | `BI ADC SETTLE TIME    ` | integer           | ADC settling time.                                           | yes  | yes                      |
| `BIADDATARATE`       | `BI AD DATA RATE       ` | integer           | A/D data rate.                                               | yes  | yes                      |
| `BIADRES`            | `BI AD RES             ` | integer           | A/D resolution.                                              | yes  | no                       |
| `BIADXFERMODE`       | `BI AD XFER MODE       ` | integer           | Data transfer mode.                                          | yes  | yes                      |
| `BIADTIMINGMODE`     | `BI AD TIMING MODE     ` | integer           | Timing mode.                                                 | yes  | yes                      |
| `BIADTRIGCOUNT`      | `BI AD TRIG COUNT      ` | integer           | Number of analog input samples to acquire per trigger.      | yes  | yes                      |
| `BIADTRIGSRC`        | `BI AD TRIG SRC        ` | integer           | A/D trigger source.                                          | yes  | yes                      |
| `BIBASEADR`          | `BI BASE ADR           ` | integer           | Base address of the device.                                  | yes  | yes                      |
| `BIBOARDTYPE`        | `BI BOARD TYPE         ` | integer           | Unique number from 0 to 8000 Hex which describes the type of board installed. | yes  | no                       |
| `BICALTABLETYPE`     | `BI CAL TABLE TYPE     ` | integer           | The coefficients table used for calibration.                | yes  | yes                      |
| `BICHANRTDTYPE`      | `BI CHAN RTD TYPE      ` | integer           | RTD sensor type.                                            | yes  | yes                      |
| `BICHANTCTYPE`       | `BI CHAN TC TYPE       ` | integer           | Thermocouple sensor type.                                   | yes  | yes                      |
| `BICINUMDEVS`        | `BI CI NUM DEVS        ` | integer           | Number of counter devices.                                  | yes  | no                       |
| `BICLOCK`            | `BI CLOCK              ` | integer           | Clock frequency in megahertz.                               | yes  | yes                      |
| `BICTRTRIGCOUNT`     | `BI CTR TRIG COUNT     ` | integer           | Number of counter samples to acquire per trigger.           | yes  | yes                      |
| `BIDACFORCESENSE`    | `BI DAC FORCE SENSE    ` | integer           | Remote sensing state of an analog output channel.           | yes  | yes                      |
| `BIDACRANGE`         | `BI DAC RANGE          ` | integer           | Analog output voltage range.                                | yes  | yes                      |
| `BIDACRES`           | `BI DAC RES            ` | integer           | D/A resolution.                                             | yes  | no                       |
| `BIDACSTARTUP`       | `BI DAC STARTUP        ` | integer           | Configuration register STARTUP bit setting.                 | yes  | yes                      |
| `BIDACTRIGCOUNT`     | `BI DAC TRIG COUNT     ` | integer           | Number of analog output samples to acquire per trigger.     | yes  | yes                      |
| `BIDACUPDATEMODE`    | `BI DAC UPDATE MODE    ` | integer           | Update mode for a digital-to-analog converter (DAC).        | yes  | yes                      |
| `BIDETECTOPENTC`     | `BI DETECT OPEN TC     ` | integer (Boolean) | Open thermocouple detection setting.                        | yes  | yes                      |
| `BIDINUMDEVS`        | `BI DI NUM DEVS        ` | integer           | Number of digital devices.                                  | yes  | no                       |
| `BIDISOFILTER`       | `BI DI ISO FILTER (??) ` | integer (Boolean) | AC filter setting.                                          | yes  | yes                      |
| `BIDITRIGCOUNT`      | `BI DI TRIG COUNT      ` | integer           | Number of digital input samples to acquire per trigger.     | yes  | yes |
| `BIDMACHAN`          | `BI DMA CHAN           ` | integer           | DMA channel.                                                 | yes  | yes |
| `BIDOTRIGCOUNT`      | `BI DO TRIG COUNT      ` | integer           | Number of digital output samples to generate per trigger.   | yes  | yes |
| `BIDTBOARD`          | `BI DT BOARD           ` | integer           | Board number of the connected Data Translation board.       | yes  | no |
| `BIEXTCLKTYPE`       | `BI EXT CLK TYPE       ` | integer (enum)    | External clock type.                                        | yes  | yes |
| `BIEXTINPACEREDGE`   | `BI EXT IN PACER EDGE  ` | integer           | Input scan clock edge.                                      | yes  | yes |
| `BIEXTOUTPACEREDGE`  | `BI EXT OUT PACER EDGE ` | integer           | Output scan clock edge.                                     | yes  | yes |
| `BIINPUTPACEROUT`    | `BI INPUT PACER OUT    ` | integer           | Input pacer clock state.                                    | yes  | yes |
| `BIINTEDGE`          | `BI INT EDGE           ` | integer           | Interrupt edge. 0 = rising, 1 = falling.                    | yes  | yes |
| `BIINTLEVEL`         | `BI INT LEVEL          ` | integer           | Interrupt level. 0 for none, or 1 to 15.                    | yes  | yes |
| `BINETCONNECTCODE` or `BINETCONNECTIONCODE` | `BI NET CONNECT CODE   ` | integer           | Code used to connect with a device over a network connection. | ??? | ??? |
| `BINETIOTIMEOUT`     | `NI NET IO TIMEOUT     ` | integer           | Amount of time (in milliseconds) to wait for a WEB device to acknowledge a command or query sent to the device over a network connection. | yes  | yes                      |
| `BINUMADCHANS`       | `BI NUM AD CHANS       ` | integer           | Number of A/D channels                                      | yes  | yes??                  |
| `BINUMDACHANS`       | `BI NUM DA CHANS       ` | integer           | Number of D/A channels.                                     | yes  | no                       |
| `BINUMIOPORTS`       | `BI NUM IO PORTS       ` | integer           | Number of I/O ports used by the device.                     | yes  | no                       |
| `BINUMTEMPCHANS`     | `BI NUM TEMP CHANS     ` | integer           | Number of temperature channels.                             | yes  | no                       |
| `BIPANID`            | `BI PAN ID             ` | integer           | Personal Area Network (PAN) identifier for a USB device that supports wireless communication. | yes  | yes                      |
| `BIPATTERNTRIGPORT`  | `BI PATTERN TRIG PORT  ` | integer           | Pattern trigger port.                                       | yes  | yes                      |
| `BIRANGE`            | `BI RANGE              ` | integer           | Selected voltage range.                                     | yes  | yes                      |
| `BIRFCHANNEL`        | `BI RF CHANNEL         ` | integer           | RF channel number used to transmit/receive data by a USB device that supports wireless communication. | yes  | yes                      |
| `BIRSS`              | `BI RSS                ` | integer           | Received signal strength in dBm of a remote device.         | yes  | yes???                   |
| `BISERIALNUM`        | `BI SERIAL NUM         ` | integer           | Custom serial number assigned by a user to a USB device.    | yes  | yes |
| `BISYNCMODE`         | `BI SYNC MODE          ` | integer           | Simultaneous mode setting.                                  | yes  | yes                      |
| `BITEMPAVG`          | `BI TEMP AVG           ` | integer           | Number of temperature samples per average.                  | yes  | yes                      |
| `BITEMPSCALE`        | `BI TEMP SCALE         ` | integer           | Temperature scale.                                          | yes  | yes                      |
| `BITEMPREJFREQ`      | `BI TEMP REJ FREQ      ` | integer           | Temperature rejection frequency.                            | yes  | yes                      |
| `BITERMCOUNTSTATBIT` | `BI TERM COUNT STAT BIT` | integer           | Terminal count output status for a specified bit.           | yes  | yes                      |
| `BIWAITSTATE`        | `BI WAIT STATE         ` | integer           | Wait State jumper setting.                                  | yes  | yes                      |
| `BIUSESEXPS`         | `BI USES EXOS          ` | integer (Boolean) | Expansion board support.                                    | yes  | no |
| `BIUSERDEVIDNUM`     | `BI USER DEV ID NUM    ` | integer???        | User-configured string that identifies a USB device.        | yes  | no?? |
| `BICALOUTPUT`        | `BI CAL OUTPUT`          | integer | Cal pin voltage on supported USB devices. | no | yes |
| `BIDACUPDATECMD`     | `BI DAC UPDATE CMD`      | integer (void) | Updates all analog output channels. | no | yes |
| `BIDIDEBOUNCESTATE`  | `BI DI DEBOUNCE STATE`   | integer | State of the digital inputs when debounce timing is set. | no | yes |
| `BIDIDEBOUNCETIME`   | `BI DI DEBOUNCE TIME`    | integer | Debounce time of digital inputs. | no | yes |
| `BIHIDELOGINDLG`     | `BI HIDE LOGIN DLG`      | integer | Enables or disables the Device Login dialog. | no | yes |
| `BIOUTPUTPACEROUT`   | `BI OUTPUT PACER OUT`    | integer | Enables or disables the output pacer clock signal. When enabled, the output clock is set for output. | no??? | yes |
| `BIDEVMACADDR`       | `BI DEV MAC ADDR`        | string | MAC address of an Ethernet device. | yes | no |
| `BIDEVSERIALNUM`     | `BI DEV SERIAL NUM`      | string | Factory serial number of a USB or Bluetooth device. | yes | no |
| `BIDEVUNIQUEID`      | `BI DEV UNIQUE ID`       | string | Unique identifier of a discoverable device, such as the serial number of a USB device or MAC address of an Ethernet device. | yes | no |
| `BIDEVVERSION`       | `BI DEV VERSION`         | string | Firmware version and FPGA version installed on a device. | yes | no |
| `BIUSERDEVID`        | `BI USER DEV ID`         | string | User-configured string identifier of up to maxConfigLen character/bytes from an Ethernet, Bluetooth, or USB device. | yes | yes |

## Digital info

| Item              | Expanded             | Type    | Description                                                  | Read | Write |
| ----------------- | -------------------- | ------- | ------------------------------------------------------------ | ---- | ----- |
| `DIDEVTYPE`         | `DI DEV TYPE         ` | integer | Device Type – AUXPORT, FIRSTPORTA, and so on.               | yes  | no    |
| `DICONFIG`          | `DI CONFIG           ` | integer | Current configuration INPUT or OUTPUT.                      | yes  | no    |
| `DINUMBITS`         | `DI NUM BITS         ` | integer | Number of bits in the port.                                 | yes  | no    |
| `DICURVAL`          | `DI CUR VAL          ` | integer | Current value of outputs.                                   | yes  | no    |
| `DIDISABLEDIRCHECK` | `DI DISABLE DIR CHECK` | integer | The direction check setting for a specified port or bit when calling cbDOut(), cbDBitOut(), and cbDOutArray(). 0 = enabled, 1 =  disabled. | yes  | yes   |
| `DIINMASK`          | `DI IN MASK          ` | integer | Bit configuration of the specified port.                     | yes  | no    |
| `DIOUTMASK`         | `DI OUT MASK         ` | integer | Bit configuration of the specified port.                     | yes  | no    |

## Counter info

| Item      | Expanded    | Type    | Description                           | Read | Write |
| --------- | ----------- | ------- | ------------------------------------- | ---- | ----- |
| `CICTRNUM`  | `CI CTR NUM ` | integer | Counter number; indicated by DevNum. | yes  | no    |
| `CICTRTYPE` | `CI CTR TYPE` | integer | Counter type                          | yes  | no    |

## Expansion info

| Item          | Expanded         | Type    | Description                                     | Read | Write |
| ------------- | ---------------- | ------- | ----------------------------------------------- | ---- | ----- |
| `XIBOARDTYPE`   | `XI BOARD TYPE   ` | integer | Board type                                      | yes  | no    |
| `XIMUXADCHAN1`  | `XI MUX AD CHAN 1` | integer | First A/D channel connected to the EXP board.  | yes  | yes   |
| `XIMUXADCHAN2`  | `XI MUX AD CHAN 2` | integer | Second A/D channel connected to the EXP board. | yes  | yes   |
| `XIRANGE1`      | `XI RANGE 1      ` | integer | Range (gain) of the low 16 channels.           | yes  | yes   |
| `XIRANGE2`      | `XI RANGE 2      ` | integer | Range (gain) of the high 16 channels.          | yes  | yes   |
| `XICJCCHAN`     | `XO CJC CHAN     ` | integer | A/D channel connected to the CJC.              | yes  | yes   |
| `XITHERMTYPE`   | `XI THERM TYPE   ` | integer | Sensor type (thermocouple or RTD)               | yes  | yes   |
| `XINUMEXPCHANS` | `XI NUM EXP CHANS` | integer | Number of channels on the expansion board.     | yes  | no    |
| `XIPARENTBOARD` | `XI PARENT BOARD ` | integer | Board number of the base A/D board.            | yes  | no    |

