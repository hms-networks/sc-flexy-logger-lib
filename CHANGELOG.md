# Ewon Flexy Logger Library Changelog

## v1.2.1
### Major Changes
- None
### Minor Changes
- Bugfix: Fixed invalid or missing Javadocs

## v1.2
### Major Changes
- Feature: Log queuing of log entries below configured log level
   * Logger.ENABLE_LOG_QUEUE()
   * Logger.DUMP_LOG_QUEUE()
- Feature: Log queuing of SocketLogger logs while disconnected. Messages queued will be outputted upon reconnection.
### Minor Changes
- Bugfix: Fixed infinite recursive loop when socket connection lost.
- Updated README.md for SocketLogger

## v1.1
### Major Changes
- Feature: Added socket logging functionality
### Minor Changes
- Feature: Added methods to enable and disable logging to Ewon Flexy realtime logs
   * Logger.ENABLE_REALTIME_LOG()
   * Logger.DISABLE_REALTIME_LOG()

## v1.0
Initial release