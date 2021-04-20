/*
 * Copyright 2015 Elvis Hew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elvishew.xlog;

/**
 * Log level indicate how important the log is.
 * <p>
 * Usually when we log a message, we also specify the log level explicitly or implicitly,
 * so if we setup a log level using <code>XLog.init(...)</code>, all the logs which is with
 * a log level smaller than the setup one would not be printed.
 * <p>
 * The priority of log levels is: {@link #VERBOSE} &lt; {@link #DEBUG} &lt; {@link #INFO} &lt;
 * {@link #WARN} &lt; {@link #ERROR}.
 * <br>And there are two special log levels which are usually used for Log#init:
 * {@link #NONE} and {@link #ALL}, {@link #NONE} for not printing any log and {@link #ALL} for
 * printing all logs.
 *
 * @see #VERBOSE
 * @see #DEBUG
 * @see #INFO
 * @see #WARN
 * @see #ERROR
 * @see #NONE
 * @see #ALL
 */
public class LogLevel {

  /**
   * Log level for XLog.v.
   */
  public static final int VERBOSE = 2;

  /**
   * Log level for XLog.d.
   */
  public static final int DEBUG = 3;

  /**
   * Log level for XLog.i.
   */
  public static final int INFO = 4;

  /**
   * Log level for XLog.w.
   */
  public static final int WARN = 5;

  /**
   * Log level for XLog.e.
   */
  public static final int ERROR = 6;

  /**
   * Log level for XLog#init, printing all logs.
   */
  public static final int ALL = Integer.MIN_VALUE;

  /**
   * Log level for XLog#init, printing no log.
   */
  public static final int NONE = Integer.MAX_VALUE;

  /**
   * Get a name representing the specified log level.
   * <p>
   * The returned name may be<br>
   * Level less than {@link LogLevel#VERBOSE}: "VERBOSE-N", N means levels below
   * {@link LogLevel#VERBOSE}<br>
   * {@link LogLevel#VERBOSE}: "VERBOSE"<br>
   * {@link LogLevel#DEBUG}: "DEBUG"<br>
   * {@link LogLevel#INFO}: "INFO"<br>
   * {@link LogLevel#WARN}: "WARN"<br>
   * {@link LogLevel#ERROR}: "ERROR"<br>
   * Level greater than {@link LogLevel#ERROR}: "ERROR+N", N means levels above
   * {@link LogLevel#ERROR}
   *
   * @param logLevel the log level to get name for
   * @return the name
   */
  public static String getLevelName(int logLevel) {
    String levelName;
    switch (logLevel) {
      case VERBOSE:
        levelName = "VERBOSE";
        break;
      case DEBUG:
        levelName = "DEBUG";
        break;
      case INFO:
        levelName = "INFO";
        break;
      case WARN:
        levelName = "WARN";
        break;
      case ERROR:
        levelName = "ERROR";
        break;
      default:
        if (logLevel < VERBOSE) {
          levelName = "VERBOSE-" + (VERBOSE - logLevel);
        } else {
          levelName = "ERROR+" + (logLevel - ERROR);
        }
        break;
    }
    return levelName;
  }

  /**
   * Get a short name representing the specified log level.
   * <p>
   * The returned name may be<br>
   * Level less than {@link LogLevel#VERBOSE}: "V-N", N means levels below
   * {@link LogLevel#VERBOSE}<br>
   * {@link LogLevel#VERBOSE}: "V"<br>
   * {@link LogLevel#DEBUG}: "D"<br>
   * {@link LogLevel#INFO}: "I"<br>
   * {@link LogLevel#WARN}: "W"<br>
   * {@link LogLevel#ERROR}: "E"<br>
   * Level greater than {@link LogLevel#ERROR}: "E+N", N means levels above
   * {@link LogLevel#ERROR}
   *
   * @param logLevel the log level to get short name for
   * @return the short name
   */
  public static String getShortLevelName(int logLevel) {
    String levelName;
    switch (logLevel) {
      case VERBOSE:
        levelName = "V";
        break;
      case DEBUG:
        levelName = "D";
        break;
      case INFO:
        levelName = "I";
        break;
      case WARN:
        levelName = "W";
        break;
      case ERROR:
        levelName = "E";
        break;
      default:
        if (logLevel < VERBOSE) {
          levelName = "V-" + (VERBOSE - logLevel);
        } else {
          levelName = "E+" + (logLevel - ERROR);
        }
        break;
    }
    return levelName;
  }
}
