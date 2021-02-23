package com.hms_networks.americas.sc.logging;

import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.SocketConnection;

/**
 * SocketLogger.java
 *
 * <p>Provides an interface for logging via socket connection.
 *
 * @since 1.1
 * @author HMS Networks, MU Americas Solution Center
 */
public class SocketLogger implements Runnable {

  /** Constant for socket port */
  private static final String SOCKET_PORT = "766";

  /** Constant for socket fatal error state */
  private static final int STATE_FATAL_ERROR = -1;

  /** Constant for socket disconnected state */
  private static final int STATE_DISCONNECTED = 0;

  /** Constant for socket connecting state */
  private static final int STATE_CONNECTING = 1;

  /** Constant for socket connected state */
  private static final int STATE_CONNECTED = 2;

  /** Constant for socket delay option */
  private static final byte SOCKET_DELAY_SEC = 0;

  /** Constant for socket linger option */
  private static final byte SOCKET_LINGER_SEC = 5;

  /** Constant for socket keep alive option */
  private static final byte SOCKET_KEEPALIVE_SEC = 15;

  /** Constant for socket receive buffer size option */
  private static final byte SOCKET_RCVBUF_BYTES = 1;

  /** Constant for socket send buffer size option */
  private static final byte SOCKET_SNDBUF_BYTES = 127;

  /** Server socket connection used for logging */
  private static ServerSocketConnection socketServerConnection;

  /** Underlying socket connection used for logging */
  private static SocketConnection socketConnection;

  /** Data output stream for adding log entries to */
  private static DataOutputStream dataOuputStream;

  /** Socket connection state variable */
  private static int socketConnectionState = STATE_DISCONNECTED;

  /** Open the socket server connection */
  private static void OPEN() {
    if (socketConnectionState == STATE_DISCONNECTED) {
      socketConnectionState = STATE_CONNECTING;
      Logger.LOG_DEBUG("SocketLogger connection starting.");
      // Create the server listening socket
      try {
        socketServerConnection =
            (ServerSocketConnection) Connector.open("socket://:" + SOCKET_PORT);
      } catch (Exception e) {
        Logger.LOG_DEBUG("SocketLogger connection could not open.");
        socketConnectionState = STATE_DISCONNECTED;
      }
      if (socketConnectionState == STATE_CONNECTING) {
        SocketLogger sl = new SocketLogger();
        Thread connectThread = new Thread(sl);
        connectThread.start();
      }
    }
  }

  /** Close the socket server connection */
  private static void CLOSE() {
    Logger.LOG_DEBUG("SocketLogger connection closing.");

    // Try to close all elements of connection.
    try {
      dataOuputStream.close();
    } catch (IOException e) {
      Logger.LOG_DEBUG("SocketLogger data output stream close failed.");
    }
    try {
      socketConnection.close();
    } catch (IOException e) {
      Logger.LOG_DEBUG("SocketLogger socket connection close failed.");
    }
    try {
      socketServerConnection.close();
    } catch (IOException e) {
      Logger.LOG_DEBUG("SocketLogger socket server connection close failed.");
    }

    socketConnectionState = STATE_DISCONNECTED;
  }

  /**
   * Run method
   *
   * <p>Handles waiting for a incoming connection and setting up the socket connection
   */
  public void run() {
    try {
      // Wait for a incoming connection
      socketConnection = (SocketConnection) socketServerConnection.acceptAndOpen();

      // Set socket connection options
      socketConnection.setSocketOption(SocketConnection.DELAY, SOCKET_DELAY_SEC);
      socketConnection.setSocketOption(SocketConnection.LINGER, SOCKET_LINGER_SEC);
      socketConnection.setSocketOption(SocketConnection.KEEPALIVE, SOCKET_KEEPALIVE_SEC);
      socketConnection.setSocketOption(SocketConnection.RCVBUF, SOCKET_RCVBUF_BYTES);
      socketConnection.setSocketOption(SocketConnection.SNDBUF, SOCKET_SNDBUF_BYTES);

      // Open a data output stream to add log information to
      dataOuputStream = socketConnection.openDataOutputStream();
    } catch (IllegalArgumentException e) {
      // Socket options are incorrect, set state to fatal error.
      Logger.LOG_DEBUG("SocketLogger invalid socket options specified.");
      socketConnectionState = STATE_FATAL_ERROR;
    } catch (IOException e) {
      Logger.LOG_DEBUG("SocketLogger connection could not start.");
      socketConnectionState = STATE_DISCONNECTED;
    }

    if (socketConnectionState == STATE_CONNECTING) {
      // Connection has been established
      socketConnectionState = STATE_CONNECTED;
    }
  }

  /**
   * Log a string to the socket connection if connected. If no connection exists this method will
   * start one.
   *
   * @param logEntry string to log
   */
  public static void LOG(String logEntry) {
    if (socketConnectionState == STATE_CONNECTED) {
      try {
        if (logEntry.length() > 0) {
          // Format log entries "Timestamp: logEntry\n"
          String logEntryFormatted = System.currentTimeMillis() + ": " + logEntry + "\n";

          // Convert string to byte array
          byte[] logByteArray = logEntryFormatted.getBytes("UTF-8");
          dataOuputStream.write(logByteArray);
        }
      } catch (IOException e) {
        // Socket is no longer connected, close resources and reopen
        Logger.LOG_DEBUG("SocketLogger connection lost.");
        CLOSE();
        OPEN();
      }
    } else if (socketConnectionState == STATE_DISCONNECTED) {
      // State is disconnected, open socket connection
      OPEN();
    }
  }
}
