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

package com.elvishew.xlog.printer;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.formatter.message.MessageFormatter;
import com.elvishew.xlog.formatter.message.method.MethodInfo;

/**
 * A printer is used for printing the log to somewhere, like android shell, terminal or file system.
 * <p/>
 * Generally, you don't need to implement this interface directly, {@link MessageFormattedPrinter}
 * is better for you because all the log messages are automatically formatted using the
 * {@link MessageFormatter} which are all configured in {@link LogConfiguration}.
 */
public interface Printer {

    /**
     * Print a log in a new line.
     *
     * @param logLevel         the log level of the printing log
     * @param logConfiguration the log configuration which you should respect to when logging
     * @param msg              the message you would like to log
     */
    void println(int logLevel, LogConfiguration logConfiguration, String msg);

    /**
     * Print a log in a new line.
     *
     * @param logLevel         the log level of the printing log
     * @param logConfiguration the log configuration which you should respect to when logging
     * @param msg              the message you would like log
     * @param tr               an throwable object to log
     */
    void println(int logLevel, LogConfiguration logConfiguration, String msg, Throwable tr);

    /**
     * Print a JSON string in a new line.
     *
     * @param logConfiguration the log configuration which you should respect to when logging
     * @param json             the JSON string to log
     */
    void json(LogConfiguration logConfiguration, String json);

    /**
     * Print a XML string in a new line.
     *
     * @param logConfiguration the log configuration which you should respect to when logging
     * @param xml              the XML string to log
     */
    void xml(LogConfiguration logConfiguration, String xml);

    /**
     * Print the info of current calling method in a new line.
     *
     * @param logConfiguration the log configuration which you should respect to when logging
     * @param methodInfo       the info of method to log
     */
    void method(LogConfiguration logConfiguration, MethodInfo methodInfo);
}
