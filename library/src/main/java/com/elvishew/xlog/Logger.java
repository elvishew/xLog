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

import com.elvishew.xlog.formatter.message.json.JsonFormatter;
import com.elvishew.xlog.formatter.message.method.MethodFormatter;
import com.elvishew.xlog.formatter.message.method.MethodInfo;
import com.elvishew.xlog.formatter.message.throwable.ThrowableFormatter;
import com.elvishew.xlog.formatter.message.xml.XmlFormatter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.util.StackTraceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A logger is used to do the real logging work, can use multiple log printers to print the log.
 * <p>
 * A {@link Logger} is always generated and mostly accessed by {@link XLog}, but for customization purpose,
 * you can configure a {@link Logger} via the {@link Builder} which is returned by {@link XLog}
 * when you trying to start a customization using {@link XLog#tag(String)} or other configuration method,
 * and to use the customized {@link Logger}, you should call the {@link Builder#build()} to build a
 * {@link Logger}, and then you can log using the {@link Logger} assuming that you are using the
 * {@link XLog} directly.
 */
public class Logger {

    private static final int BASE_IGNORED_STACK_TRACE_DEPTH = 1;

    /**
     * The log configuration which you should respect to when logging.
     */
    private LogConfiguration logConfiguration;

    /**
     * The log printers used to print the logs.
     */
    private List<Printer> printers;

    /**
     * Construct a logger.
     *
     * @param logConfiguration the log configuration which you should respect to when logging
     * @param printers         the log printers used to print the log
     */
    /*package*/ Logger(LogConfiguration logConfiguration, List<Printer> printers) {
        this.logConfiguration = logConfiguration;
        this.printers = new ArrayList<Printer>(printers);
    }

    /**
     * Construct a logger using builder.
     *
     * @param builder the logger builder
     */
    /*package*/ Logger(Builder builder) {
        LogConfiguration.Builder logConfigBuilder = new LogConfiguration.Builder(XLog.sLogConfiguration);
        if (builder.tag != null) {
            logConfigBuilder.tag(builder.tag);
        }
        if (builder.jsonFormatter != null) {
            logConfigBuilder.jsonFormatter(builder.jsonFormatter);
        }
        if (builder.xmlFormatter != null) {
            logConfigBuilder.xmlFormatter(builder.xmlFormatter);
        }
        if (builder.methodFormatter != null) {
            logConfigBuilder.methodFormatter(builder.methodFormatter);
        }
        if (builder.throwableFormatter != null) {
            logConfigBuilder.throwableFormatter(builder.throwableFormatter);
        }
        logConfiguration = logConfigBuilder.build();

        if (builder.printers != null && builder.printers.length > 0) {
            printers = new ArrayList<Printer>(Arrays.asList(builder.printers));
        } else {
            printers = new ArrayList<Printer>(XLog.sPrinters);
        }
    }

    /**
     * Log a message with level {@link LogLevel#VERBOSE}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public void v(String format, Object... args) {
        println(LogLevel.VERBOSE, String.format(format, args));
    }

    /**
     * Log a message with level {@link LogLevel#VERBOSE}.
     *
     * @param msg the message to log
     */
    public void v(String msg) {
        println(LogLevel.VERBOSE, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#VERBOSE}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public void v(String msg, Throwable tr) {
        println(LogLevel.VERBOSE, msg, tr);
    }

    /**
     * Log a message with level {@link LogLevel#DEBUG}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public void d(String format, Object... args) {
        println(LogLevel.DEBUG, String.format(format, args));
    }

    /**
     * Log a message with level {@link LogLevel#DEBUG}.
     *
     * @param msg the message to log
     */
    public void d(String msg) {
        println(LogLevel.DEBUG, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#DEBUG}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public void d(String msg, Throwable tr) {
        println(LogLevel.DEBUG, msg, tr);
    }

    /**
     * Log a message with level {@link LogLevel#INFO}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public void i(String format, Object... args) {
        println(LogLevel.INFO, String.format(format, args));
    }

    /**
     * Log a message with level {@link LogLevel#INFO}.
     *
     * @param msg the message to log
     */
    public void i(String msg) {
        println(LogLevel.INFO, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#INFO}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public void i(String msg, Throwable tr) {
        println(LogLevel.INFO, msg, tr);
    }

    /**
     * Log a message with level {@link LogLevel#WARN}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public void w(String format, Object... args) {
        println(LogLevel.WARN, String.format(format, args));
    }

    /**
     * Log a message with level {@link LogLevel#WARN}.
     *
     * @param msg the message to log
     */
    public void w(String msg) {
        println(LogLevel.WARN, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#WARN}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public void w(String msg, Throwable tr) {
        println(LogLevel.WARN, msg, tr);
    }

    /**
     * Log a message with level {@link LogLevel#ERROR}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public void e(String format, Object... args) {
        println(LogLevel.ERROR, String.format(format, args));
    }

    /**
     * Log a message with level {@link LogLevel#ERROR}.
     *
     * @param msg the message to log
     */
    public void e(String msg) {
        println(LogLevel.ERROR, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#ERROR}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public void e(String msg, Throwable tr) {
        println(LogLevel.ERROR, msg, tr);
    }

    /**
     * Print a log in a new line using every printer.
     *
     * @param logLevel the log level of the printing log
     * @param msg      the message you would like log
     */
    /*package*/ void println(int logLevel, String msg) {
        if (logLevel < XLog.sLogLevel) {
            return;
        }
        for (Printer printer : printers) {
            printer.println(logLevel, logConfiguration, msg);
        }
    }

    /**
     * Print a log in a new line using every printer.
     *
     * @param logLevel the log level of the printing log
     * @param msg      the message you would like log
     * @param tr       an throwable object to log
     */
    /*package*/ void println(int logLevel, String msg, Throwable tr) {
        if (logLevel < XLog.sLogLevel) {
            return;
        }
        for (Printer printer : printers) {
            printer.println(logLevel, logConfiguration, msg, tr);
        }
    }

    /**
     * Log a JSON string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param json the JSON string to log
     */
    public void json(String json) {
        if (LogLevel.JSON < XLog.sLogLevel) {
            return;
        }
        for (Printer printer : printers) {
            printer.json(logConfiguration, json);
        }
    }

    /**
     * Log a XML string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param xml the XML string to log
     */
    public void xml(String xml) {
        if (LogLevel.XML < XLog.sLogLevel) {
            return;
        }
        for (Printer printer : printers) {
            printer.xml(logConfiguration, xml);
        }
    }

    /**
     * Log a method, with level {@link LogLevel#DEBUG} by default.
     *
     * @param arguments the arguments of the method to log
     */
    public void method(Object... arguments) {
        method(1, arguments);
    }

    /**
     * Log a method, with level {@link LogLevel#DEBUG} by default.
     *
     * @param ignoredStackTraceDepth the stack trace depth to be ignored
     * @param arguments              the arguments of the method to log
     */
    /*package*/ void method(int ignoredStackTraceDepth, Object... arguments) {
        if (LogLevel.METHOD < XLog.sLogLevel) {
            return;
        }
        ignoredStackTraceDepth += BASE_IGNORED_STACK_TRACE_DEPTH;
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        int stackTraceDepthForUser = stackTrace.length - ignoredStackTraceDepth;
        StackTraceElement[] stackTraceForUser = new StackTraceElement[stackTraceDepthForUser];
        java.lang.System.arraycopy(stackTrace, ignoredStackTraceDepth, stackTraceForUser, 0,
                stackTraceDepthForUser);
        for (Printer printer : printers) {
            printer.method(logConfiguration, new MethodInfo(stackTraceForUser, arguments));
        }
    }

    /**
     * Log a stack trace, with level {@link LogLevel#DEBUG} by default.
     */
    public void stack() {
        stack("", 1);
    }

    /**
     * Log a stack trace, with level {@link LogLevel#DEBUG} by default.
     *
     * @param format the format of the extra message to log
     * @param args   the arguments of the extra message to log
     */
    public void stack(String format, Object... args) {
        stack(String.format(format, args), 1);
    }

    /**
     * Log a stack trace, with level {@link LogLevel#DEBUG} by default.
     *
     * @param msg the extra message to log
     */
    public void stack(String msg) {
        stack(msg, 1);
    }

    /**
     * Log a stack trace, with level {@link LogLevel#DEBUG} by default.
     *
     * @param msg                    the extra message to log
     * @param ignoredStackTraceDepth the stack trace depth to be ignored
     */
    /*package*/ void stack(String msg, int ignoredStackTraceDepth) {
        d(((msg == null || msg.trim().length() == 0) ? "" : (msg + System.lineSeparator)) +
                StackTraceUtil.getCallStackTraceString(
                        new Throwable().getStackTrace(),
                        ignoredStackTraceDepth + BASE_IGNORED_STACK_TRACE_DEPTH));
    }

    /**
     * Builder for {@link Logger}.
     */
    public static class Builder {

        /**
         * The tag string when {@link Logger} log.
         */
        private String tag;

        /**
         * The JSON formatter when {@link Logger} log a JSON string.
         */
        private JsonFormatter jsonFormatter;

        /**
         * The XML formatter when {@link Logger} log a XML string.
         */
        private XmlFormatter xmlFormatter;

        /**
         * The method formatter when {@link Logger} log a method.
         */
        private MethodFormatter methodFormatter;

        /**
         * The throwable formatter when {@link Logger} log a message with throwable.
         */
        private ThrowableFormatter throwableFormatter;

        /**
         * The printers used to print the log when {@link Logger} log.
         */
        private Printer[] printers;

        /*package */ Builder() {
        }

        /**
         * Set the tag string when {@link Logger} log.
         *
         * @param tag the tag string when {@link Logger} log
         * @return the builder
         */
        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        /**
         * Set the JSON formatter when {@link Logger} log a JSON string.
         *
         * @param jsonFormatter the JSON formatter when {@link Logger} log a JSON string
         * @return the builder
         */
        public Builder jsonFormatter(JsonFormatter jsonFormatter) {
            this.jsonFormatter = jsonFormatter;
            return this;
        }

        /**
         * Set the XML formatter when {@link Logger} log a XML string.
         *
         * @param xmlFormatter the XML formatter when {@link Logger} log a XML string
         * @return the builder
         */
        public Builder xmlFormatter(XmlFormatter xmlFormatter) {
            this.xmlFormatter = xmlFormatter;
            return this;
        }

        /**
         * Set the method formatter when {@link Logger} log a method.
         *
         * @param methodFormatter the method formatter when {@link Logger} log a method
         * @return the builder
         */
        public Builder methodFormatter(MethodFormatter methodFormatter) {
            this.methodFormatter = methodFormatter;
            return this;
        }

        /**
         * Set the throwable formatter when {@link Logger} log a message with throwable.
         *
         * @param throwableFormatter the throwable formatter when {@link Logger} log a message with
         *                           throwable
         * @return the builder
         */
        public Builder throwableFormatter(ThrowableFormatter throwableFormatter) {
            this.throwableFormatter = throwableFormatter;
            return this;
        }

        /**
         * Set the printers used to print the log when {@link Logger} log.
         *
         * @param printers the printers used to print the log when {@link Logger} log
         * @return the builder
         */
        public Builder printers(Printer... printers) {
            this.printers = printers;
            return this;
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#v(String, Object...)}.
         */
        public void v(String format, Object... args) {
            build().v(format, args);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#v(String)}.
         */
        public void v(String msg) {
            build().v(msg);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#v(String, Throwable)}.
         */
        public void v(String msg, Throwable tr) {
            build().v(msg, tr);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#d(String, Object...)}.
         */
        public void d(String format, Object... args) {
            build().d(format, args);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#d(String)}.
         */
        public void d(String msg) {
            build().d(msg);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#d(String, Throwable)}.
         */
        public void d(String msg, Throwable tr) {
            build().d(msg, tr);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#i(String, Object...)}.
         */
        public void i(String format, Object... args) {
            build().i(format, args);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#i(String)}.
         */
        public void i(String msg) {
            build().i(msg);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#i(String, Throwable)}.
         */
        public void i(String msg, Throwable tr) {
            build().i(msg, tr);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#w(String, Object...)}.
         */
        public void w(String format, Object... args) {
            build().w(format, args);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#w(String)}.
         */
        public void w(String msg) {
            build().w(msg);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#w(String, Throwable)}.
         */
        public void w(String msg, Throwable tr) {
            build().w(msg, tr);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#d(String, Object...)}.
         */
        public void e(String format, Object... args) {
            build().e(format, args);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#e(String)}.
         */
        public void e(String msg) {
            build().e(msg);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#e(String, Throwable)}.
         */
        public void e(String msg, Throwable tr) {
            build().e(msg, tr);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#json(String)}.
         */
        public void json(String json) {
            build().json(json);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#xml(String)}.
         */
        public void xml(String xml) {
            build().xml(xml);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#method(Object...)}.
         */
        public void method(Object... arguments) {
            build().method(1, arguments);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#stack()}.
         */
        public void stack() {
            build().stack("", 1);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#stack(String, Object...)}.
         */
        public void stack(String format, Object... args) {
            build().stack(String.format(format, args), 1);
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#stack(String)}.
         */
        public void stack(String msg) {
            build().stack(msg, 1);
        }

        /**
         * Builds configured {@link Logger} object.
         *
         * @return the built configured {@link Logger} object
         */
        public Logger build() {
            return new Logger(this);
        }
    }
}
