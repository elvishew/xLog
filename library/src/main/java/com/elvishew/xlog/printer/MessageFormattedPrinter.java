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
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.System;
import com.elvishew.xlog.formatter.message.method.MethodInfo;

/**
 * The message of log is formatted by the formatter the {@link LogConfiguration} provide.
 */
public abstract class MessageFormattedPrinter implements Printer {

    @Override
    public final void println(int logLevel, LogConfiguration logConfiguration, String msg) {
        onPrintFormattedMessage(logLevel, logConfiguration.tag, msg);
    }

    @Override
    public final void println(int logLevel, LogConfiguration logConfiguration, String msg, Throwable tr) {
        onPrintFormattedMessage(logLevel, logConfiguration.tag,
                ((msg == null || msg.trim().length() == 0) ? "" : (msg + System.lineSeparator))
                        + logConfiguration.throwableFormatter.format(tr));
    }

    @Override
    public final void json(LogConfiguration logConfiguration, String json) {
        onPrintFormattedMessage(LogLevel.JSON, logConfiguration.tag,
                logConfiguration.jsonFormatter.format(json));
    }

    @Override
    public final void xml(LogConfiguration logConfiguration, String xml) {
        onPrintFormattedMessage(LogLevel.XML, logConfiguration.tag,
                logConfiguration.xmlFormatter.format(xml));
    }

    @Override
    public final void method(LogConfiguration logConfiguration, MethodInfo methodInfo) {
        onPrintFormattedMessage(LogLevel.METHOD, logConfiguration.tag,
                logConfiguration.methodFormatter.format(methodInfo));
    }

    /**
     * Print a log with formatted message.
     *
     * @param logLevel the log level of the printing log
     * @param tag      the tag string
     * @param msg      the formatted message you would like to log
     */
    protected abstract void onPrintFormattedMessage(int logLevel, String tag, String msg);
}
