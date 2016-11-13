/*
 * Copyright 2016 Elvis Hew
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

import com.elvishew.xlog.formatter.border.BorderFormatter;
import com.elvishew.xlog.formatter.message.json.JsonFormatter;
import com.elvishew.xlog.formatter.message.object.ObjectFormatter;
import com.elvishew.xlog.formatter.message.throwable.ThrowableFormatter;
import com.elvishew.xlog.formatter.message.xml.XmlFormatter;
import com.elvishew.xlog.formatter.stacktrace.StackTraceFormatter;
import com.elvishew.xlog.formatter.thread.ThreadFormatter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.PrinterSet;
import com.elvishew.xlog.util.StackTraceUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A logger is used to do the real logging work, can use multiple log printers to print the log.
 * <p>
 * A {@link Logger} is always generated and mostly accessed by {@link XLog}, but for customization
 * purpose, you can configure a {@link Logger} via the {@link Builder} which is returned by
 * {@link XLog} when you trying to start a customization using {@link XLog#tag(String)}
 * or other configuration method, and to use the customized {@link Logger}, you should call
 * the {@link Builder#build()} to build a {@link Logger}, and then you can log using
 * the {@link Logger} assuming that you are using the {@link XLog} directly.
 */
public class Logger {

    /**
     * The log configuration which you should respect to when logging.
     */
    private LogConfiguration logConfiguration;

    /**
     * The log printer used to print the logs.
     */
    private Printer printer;

    /**
     * Construct a logger.
     *
     * @param logConfiguration the log configuration which you should respect to when logging
     * @param printer          the log printer used to print the log
     */
    /*package*/ Logger(LogConfiguration logConfiguration, Printer printer) {
        this.logConfiguration = logConfiguration;
        this.printer = printer;
    }

    /**
     * Construct a logger using builder.
     *
     * @param builder the logger builder
     */
    /*package*/ Logger(Builder builder) {
        LogConfiguration.Builder logConfigBuilder = new LogConfiguration.Builder(
                XLog.sLogConfiguration);

        if (builder.tag != null) {
            logConfigBuilder.tag(builder.tag);
        }

        if (builder.threadSet) {
            if (builder.withThread) {
                logConfigBuilder.t();
            } else {
                logConfigBuilder.nt();
            }
        }
        if (builder.stackTraceSet) {
            if (builder.withStackTrace) {
                logConfigBuilder.st(builder.stackTraceDepth);
            } else {
                logConfigBuilder.nst();
            }
        }
        if (builder.borderSet) {
            if (builder.withBorder) {
                logConfigBuilder.b();
            } else {
                logConfigBuilder.nb();
            }
        }

        if (builder.jsonFormatter != null) {
            logConfigBuilder.jsonFormatter(builder.jsonFormatter);
        }
        if (builder.xmlFormatter != null) {
            logConfigBuilder.xmlFormatter(builder.xmlFormatter);
        }
        if (builder.throwableFormatter != null) {
            logConfigBuilder.throwableFormatter(builder.throwableFormatter);
        }
        if (builder.threadFormatter != null) {
            logConfigBuilder.threadFormatter(builder.threadFormatter);
        }
        if (builder.stackTraceFormatter != null) {
            logConfigBuilder.stackTraceFormatter(builder.stackTraceFormatter);
        }
        if (builder.borderFormatter != null) {
            logConfigBuilder.borderFormatter(builder.borderFormatter);
        }
        if (builder.objectFormatters != null) {
            logConfigBuilder.objectFormatters(builder.objectFormatters);
        }
        logConfiguration = logConfigBuilder.build();

        if (builder.printer != null) {
            printer = builder.printer;
        } else {
            printer = XLog.sPrinter;
        }
    }

    /**
     * Log an object with level {@link LogLevel#VERBOSE}.
     *
     * @param object the object to log
     */
    public void v(Object object) {
        println(LogLevel.VERBOSE, object);
    }

    /**
     * Log an array with level {@link LogLevel#VERBOSE}.
     *
     * @param array the array to log
     */
    public void v(Object[] array) {
        println(LogLevel.VERBOSE, array);
    }

    /**
     * Log a message with level {@link LogLevel#VERBOSE}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public void v(String format, Object... args) {
        println(LogLevel.VERBOSE, format, args);
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
     * Log an object with level {@link LogLevel#DEBUG}.
     *
     * @param object the object to log
     */
    public void d(Object object) {
        println(LogLevel.DEBUG, object);
    }

    /**
     * Log an array with level {@link LogLevel#DEBUG}.
     *
     * @param array the array to log
     */
    public void d(Object[] array) {
        println(LogLevel.DEBUG, array);
    }

    /**
     * Log a message with level {@link LogLevel#DEBUG}.
     *
     * @param format the format of the message to log, null if just need to concat arguments
     * @param args   the arguments of the message to log
     */
    public void d(String format, Object... args) {
        println(LogLevel.DEBUG, format, args);
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
     * Log an object with level {@link LogLevel#INFO}.
     *
     * @param object the object to log
     */
    public void i(Object object) {
        println(LogLevel.INFO, object);
    }

    /**
     * Log an array with level {@link LogLevel#INFO}.
     *
     * @param array the array to log
     */
    public void i(Object[] array) {
        println(LogLevel.INFO, array);
    }

    /**
     * Log a message with level {@link LogLevel#INFO}.
     *
     * @param format the format of the message to log, null if just need to concat arguments
     * @param args   the arguments of the message to log
     */
    public void i(String format, Object... args) {
        println(LogLevel.INFO, format, args);
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
     * Log an object with level {@link LogLevel#WARN}.
     *
     * @param object the object to log
     */
    public void w(Object object) {
        println(LogLevel.WARN, object);
    }

    /**
     * Log an array with level {@link LogLevel#WARN}.
     *
     * @param array the array to log
     */
    public void w(Object[] array) {
        println(LogLevel.WARN, array);
    }

    /**
     * Log a message with level {@link LogLevel#WARN}.
     *
     * @param format the format of the message to log, null if just need to concat arguments
     * @param args   the arguments of the message to log
     */
    public void w(String format, Object... args) {
        println(LogLevel.WARN, format, args);
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
     * Log an object with level {@link LogLevel#ERROR}.
     *
     * @param object the object to log
     */
    public void e(Object object) {
        println(LogLevel.ERROR, object);
    }

    /**
     * Log an array with level {@link LogLevel#ERROR}.
     *
     * @param array the array to log
     */
    public void e(Object[] array) {
        println(LogLevel.ERROR, array);
    }

    /**
     * Log a message with level {@link LogLevel#ERROR}.
     *
     * @param format the format of the message to log, null if just need to concat arguments
     * @param args   the arguments of the message to log
     */
    public void e(String format, Object... args) {
        println(LogLevel.ERROR, format, args);
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
     * Log a JSON string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param json the JSON string to log
     */
    public void json(String json) {
        if (LogLevel.DEBUG < XLog.sLogLevel) {
            return;
        }
        println(LogLevel.DEBUG, logConfiguration.jsonFormatter.format(json));
    }

    /**
     * Log a XML string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param xml the XML string to log
     */
    public void xml(String xml) {
        if (LogLevel.DEBUG < XLog.sLogLevel) {
            return;
        }
        println(LogLevel.DEBUG, logConfiguration.xmlFormatter.format(xml));
    }

    /**
     * Print an object in a new line.
     *
     * @param logLevel the log level of the printing object
     * @param object   the object to print
     */
    private <T> void println(int logLevel, T object) {
        if (logLevel < XLog.sLogLevel) {
            return;
        }
        ObjectFormatter<? super T> objectFormatter = logConfiguration.getObjectFormatter(object);
        if (objectFormatter != null) {
            printlnInternal(logLevel, objectFormatter.format(object));
        } else {
            printlnInternal(logLevel, object.toString());
        }
    }

    /**
     * Print an array in a new line.
     *
     * @param logLevel the log level of the printing array
     * @param array    the array to print
     */
    private void println(int logLevel, Object[] array) {
        if (logLevel < XLog.sLogLevel) {
            return;
        }
        printlnInternal(logLevel, Arrays.deepToString(array));
    }

    /**
     * Print a log in a new line.
     *
     * @param logLevel the log level of the printing log
     * @param format   the format of the printing log, null if just need to concat arguments
     * @param args     the arguments of the printing log
     */
    private void println(int logLevel, String format, Object... args) {
        if (logLevel < XLog.sLogLevel) {
            return;
        }
        printlnInternal(logLevel, formatArgs(format, args));
    }

    /**
     * Print a log in a new line.
     *
     * @param logLevel the log level of the printing log
     * @param msg      the message you would like to log
     */
    /*package*/ void println(int logLevel, String msg) {
        if (logLevel < XLog.sLogLevel) {
            return;
        }
        printlnInternal(logLevel, msg);
    }

    /**
     * Print a log in a new line.
     *
     * @param logLevel the log level of the printing log
     * @param msg      the message you would like to log
     * @param tr       an throwable object to log
     */
    private void println(int logLevel, String msg, Throwable tr) {
        if (logLevel < XLog.sLogLevel) {
            return;
        }
        printlnInternal(logLevel, ((msg == null || msg.length() == 0)
                ? "" : (msg + SystemCompat.lineSeparator))
                + logConfiguration.throwableFormatter.format(tr));
    }

    /**
     * Print a log in a new line internally.
     *
     * @param logLevel the log level of the printing log
     * @param msg      the message you would like to log
     */
    private void printlnInternal(int logLevel, String msg) {
        String thread = logConfiguration.withThread
                ? logConfiguration.threadFormatter.format(Thread.currentThread())
                : null;
        String stackTrace = logConfiguration.withStackTrace
                ? logConfiguration.stackTraceFormatter.format(
                StackTraceUtil.getCroppedRealStackTrack(new Throwable().getStackTrace(),
                        logConfiguration.stackTraceDepth))
                : null;
        printer.println(logLevel, logConfiguration.tag, logConfiguration.withBorder
                ? logConfiguration.borderFormatter.format(new String[]{thread, stackTrace, msg})
                : ((thread != null ? (thread + SystemCompat.lineSeparator) : "")
                + (stackTrace != null ? (stackTrace + SystemCompat.lineSeparator) : "")
                + msg));
    }

    /**
     * Format a string with arguments.
     *
     * @param format the format string, null if just to concat the arguments
     * @param args   the arguments
     * @return the formatted string
     */
    private String formatArgs(String format, Object... args) {
        if (format != null) {
            return String.format(format, args);
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0, N = args.length; i < N; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(args[i]);
            }
            return sb.toString();
        }
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
         * Whether we should log with thread info.
         */
        private boolean withThread;

        /**
         * Whether we have enabled/disabled thread info.
         */
        private boolean threadSet;

        /**
         * Whether we should log with stack trace.
         */
        private boolean withStackTrace;

        /**
         * The number of stack trace elements we should log when logging with stack trace,
         * 0 if no limitation.
         */
        private int stackTraceDepth;

        /**
         * Whether we have enabled/disabled stack trace.
         */
        private boolean stackTraceSet;

        /**
         * Whether we should log with border.
         */
        private boolean withBorder;

        /**
         * Whether we have enabled/disabled border.
         */
        private boolean borderSet;

        /**
         * The JSON formatter when {@link Logger} log a JSON string.
         */
        private JsonFormatter jsonFormatter;

        /**
         * The XML formatter when {@link Logger} log a XML string.
         */
        private XmlFormatter xmlFormatter;

        /**
         * The throwable formatter when {@link Logger} log a message with throwable.
         */
        private ThrowableFormatter throwableFormatter;

        /**
         * The thread formatter when {@link Logger} logging.
         */
        private ThreadFormatter threadFormatter;

        /**
         * The stack trace formatter when {@link Logger} logging.
         */
        private StackTraceFormatter stackTraceFormatter;

        /**
         * The border formatter when {@link Logger} logging.
         */
        private BorderFormatter borderFormatter;

        /**
         * The object formatters, used when {@link Logger} logging an object.
         */
        private Map<Class<?>, ObjectFormatter<?>> objectFormatters;

        /**
         * The printer used to print the log when {@link Logger} log.
         */
        private Printer printer;

        /**
         * Construct a builder, which will perform the same as the global one by default.
         */
        public Builder() {
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
         * Enable thread info.
         *
         * @return the builder
         */
        public Builder t() {
            this.withThread = true;
            this.threadSet = true;
            return this;
        }

        /**
         * Disable thread info.
         *
         * @return the builder
         */
        public Builder nt() {
            this.withThread = false;
            this.threadSet = true;
            return this;
        }

        /**
         * Enable stack trace.
         *
         * @param depth the number of stack trace elements we should log, 0 if no limitation
         * @return the builder
         */
        public Builder st(int depth) {
            this.withStackTrace = true;
            this.stackTraceDepth = depth;
            this.stackTraceSet = true;
            return this;
        }

        /**
         * Disable stack trace.
         *
         * @return the builder
         */
        public Builder nst() {
            this.withStackTrace = false;
            this.stackTraceDepth = 0;
            this.stackTraceSet = true;
            return this;
        }

        /**
         * Enable border.
         *
         * @return the builder
         */
        public Builder b() {
            this.withBorder = true;
            this.borderSet = true;
            return this;
        }

        /**
         * Disable border.
         *
         * @return the builder
         */
        public Builder nb() {
            this.withBorder = false;
            this.borderSet = true;
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
         * Set the thread formatter when {@link Logger} logging.
         *
         * @param threadFormatter the thread formatter when {@link Logger} logging
         * @return the builder
         */
        public Builder threadFormatter(ThreadFormatter threadFormatter) {
            this.threadFormatter = threadFormatter;
            return this;
        }

        /**
         * Set the stack trace formatter when {@link Logger} logging.
         *
         * @param stackTraceFormatter the stace trace formatter when {@link Logger} logging
         * @return the builder
         */
        public Builder stackTraceFormatter(StackTraceFormatter stackTraceFormatter) {
            this.stackTraceFormatter = stackTraceFormatter;
            return this;
        }

        /**
         * Set the border formatter when {@link Logger} logging.
         *
         * @param borderFormatter the border formatter when {@link Logger} logging
         * @return the builder
         */
        public Builder borderFormatter(BorderFormatter borderFormatter) {
            this.borderFormatter = borderFormatter;
            return this;
        }

        /**
         * Add a object formatter for specific class of object when {@link Logger} log an object.
         *
         * @param objectClass     the class of object
         * @param objectFormatter the object formatter to add
         * @param <T>             the type of object
         * @return the builder
         */
        public <T> Builder addObjectFormatter(Class<T> objectClass,
                                              ObjectFormatter<? super T> objectFormatter) {
            if (objectFormatters == null) {
                objectFormatters = new HashMap<>(5);
            }
            objectFormatters.put(objectClass, objectFormatter);
            return this;
        }

        /**
         * Set the printers used to print the log when {@link Logger} log.
         *
         * @param printers the printers used to print the log when {@link Logger} log
         * @return the builder
         */
        public Builder printers(Printer... printers) {
            if (printers.length == 0) {
                // Is there anybody want to reuse the Builder? It's not a good idea, but
                // anyway, in case you want to reuse a builder and do not want the custom
                // printers anymore, just do it.
                this.printer = null;
            } else if (printers.length == 1) {
                this.printer = printers[0];
            } else {
                this.printer = new PrinterSet(printers);
            }
            return this;
        }

        /**
         * Convenience of {@link #build()} and {@link Logger#v(Object)}.
         */
        public void v(Object object) {
            build().v(object);
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
         * Convenience of {@link #build()} and {@link Logger#d(Object)}.
         */
        public void d(Object object) {
            build().d(object);
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
         * Convenience of {@link #build()} and {@link Logger#i(Object)}.
         */
        public void i(Object object) {
            build().i(object);
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
         * Convenience of {@link #build()} and {@link Logger#w(Object)}.
         */
        public void w(Object object) {
            build().w(object);
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
         * Convenience of {@link #build()} and {@link Logger#e(Object)}.
         */
        public void e(Object object) {
            build().e(object);
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
         * Builds configured {@link Logger} object.
         *
         * @return the built configured {@link Logger} object
         */
        public Logger build() {
            return new Logger(this);
        }
    }
}
