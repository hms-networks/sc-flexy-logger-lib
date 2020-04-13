# sc-flexy-logger-lib

THE CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND. HMS DOES NOT WARRANT THAT THE FUNCTIONS OF THE CODE WILL MEET YOUR REQUIREMENTS, OR THAT THE OPERATION OF THE CODE WILL BE UNINTERRUPTED OR ERROR-FREE, OR THAT DEFECTS IN IT CAN BE CORRECTED.
---

## [Table of Contents](#table-of-contents)

1. [Description](#description)
2. [Log Level](#log-level)
3. [Developer Documentation](#developer-documentation)
4. [Dependencies](#dependencies)

---

## [Description](#table-of-contents)

Logging library to enable runtime configurable log output in an Ewon Flexy Java application.

## [Log Level](#table-of-contents)

There are seven levels of configurable log level. The logging level is set by the SET_LOG_LEVEL(int) method. Each log level includes the output for the log levels below it (lower numerical value). For example, log level 3 (Warning) includes the output for log level 2 (Serious) and log Level 1 (Critical). All log levels print to the Flexy realtime logs. Negative logging levels write to text files in the /usr directory of the Flexy instead of the realtime log. These text files are named logN.txt where N is an integer.

| LoggingLevel     | Description |
|------------------|---|
| 6, -6 (Trace)    | Exception stack traces |
| 5, -5 (Debug)    | Low level information about the state of the application |
| 4, -4 (Info)     | General Application state information |
| 3, -3 (Warning)  | Issues encountered in the application that are not serious |
| 2, -2 (Serious)  | Serious application log messages (Errors) |
| 1, -1 (Critical) | Critical application log messages (Startup, Initialization, Unrecoverable Error) |
| 0 (None)         | Logging is disabled |

## [Developer Documentation](#table-of-contents)

Developer documentation is available in Javadoc format at [https://hms-networks.github.io/sc-flexy-logger-lib/index.html](https://hms-networks.github.io/sc-flexy-logger-lib/index.html).

## [Dependencies](#table-of-contents)
None