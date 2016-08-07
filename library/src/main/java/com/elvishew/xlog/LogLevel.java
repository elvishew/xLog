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
 * so if we setup a log level using <code>XLog.init(...)</code>, all the logs which is with a log level
 * smaller than the setup one would not be printed.
 * <p>
 * The priority of log levels is: {@link #VERBOSE} &lt; {@link #DEBUG} &lt; {@link #INFO} &lt; {@link #WARN} &lt; {@link #ERROR}.
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
     * Log level for Log.v.
     */
    public static final int VERBOSE = android.util.Log.VERBOSE;

    /**
     * Log level for Log.d.
     */
    public static final int DEBUG = android.util.Log.DEBUG;

    /**
     * Log level for Log.i.
     */
    public static final int INFO = android.util.Log.INFO;

    /**
     * Log level for Log.w.
     */
    public static final int WARN = android.util.Log.WARN;

    /**
     * Log level for Log.e.
     */
    public static final int ERROR = android.util.Log.ERROR;

    /**
     * Log level for Log#init, printing all logs.
     */
    public static final int ALL = VERBOSE;

    /**
     * Log level for Log#init, printing no log.
     */
    public static final int NONE = ERROR + 1;

    /**
     * Get a name representing the specified log level.
     * <p>
     * The returned name may be one of "VERBOSE", "DEBUG", "INFO", "WARN" and "ERROR".
     * <br>e.g. "VERBOSE" is the name of {@link #VERBOSE}.
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
                throw new IllegalArgumentException("Invalid log level: " + logLevel);
        }
        return levelName;
    }

    /**
     * Get a short name representing the specified log level.
     * <p>
     * The returned name may be one of "V", "D", "I", "W" and "E".
     * <br>e.g. "V" is the short name of {@link #VERBOSE}.
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
                throw new IllegalArgumentException("Invalid log level: " + logLevel);
        }
        return levelName;
    }
}
