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

import android.app.Application;

import com.elvishew.xlog.formatter.message.json.JsonFormatter;
import com.elvishew.xlog.formatter.message.method.MethodFormatter;
import com.elvishew.xlog.formatter.message.throwable.ThrowableFormatter;
import com.elvishew.xlog.formatter.message.xml.XmlFormatter;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.util.StackTraceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A log tool which can be used in android or java, the most important feature is it can print the
 * logs to multiple place in the same time, such as android shell, terminal and file system, you can
 * even print the log to the remote server if you want, all of these can be done just within one
 * calling.
 * <p/>
 * <b>How to use in a general way:</b>
 * <p/>
 * <b>1. Initial the log system.</b>
 * <br/>Using one of {@link XLog#init(int)}, {@link XLog#init(int, LogConfiguration)} and
 * {@link XLog#init(int, LogConfiguration, Printer...)}, that will setup a {@link LogConfiguration}
 * for a global usage.
 * If you want to use a customized configuration instead of the global one to log something, you can
 * start a customization logging.
 * <p/>
 * <br/>For android, a best place to do the initialization is {@link Application#onCreate()}.
 * <p/>
 * <b>2. Start to log.</b>
 * <br/>{@link #v(String, Object...)}, {@link #v(String)} and {@link #v(String, Throwable)} are for
 * logging a {@link LogLevel#INFO} log.
 * <br/>{@link #d(String, Object...)}, {@link #d(String)} and {@link #d(String, Throwable)} are for
 * logging a {@link LogLevel#DEBUG} log.
 * <br/>{@link #i(String, Object...)}, {@link #i(String)} and {@link #i(String, Throwable)} are for
 * logging a {@link LogLevel#INFO} log.
 * <br/>{@link #w(String, Object...)}, {@link #w(String)} and {@link #w(String, Throwable)} are for
 * logging a {@link LogLevel#WARN} log.
 * <br/>{@link #e(String, Object...)}, {@link #e(String)} and {@link #e(String, Throwable)} are for
 * logging a {@link LogLevel#ERROR} log.
 * <br/>{@link #json(String)} is for logging a {@link LogLevel#JSON} log.
 * <br/>{@link #xml(String)} is for logging a {@link LogLevel#XML} log.
 * <br/>{@link #method(Object...)} is for logging a {@link LogLevel#METHOD} log.
 * <br/>{@link #stack()}, {@link #stack(String, Object...)} and {@link #stack(String)} are for
 * logging a calling stack.
 * <p/>
 * <b>How to use in a dynamically customizing way after initializing the log system:</b>
 * <p/>
 * <b>1. Start a customization.</b>
 * <br/>Call any of {@link #tag(String)}, {@link #jsonFormatter(JsonFormatter)},
 * {@link #xmlFormatter(XmlFormatter)}, {@link #methodFormatter(MethodFormatter)},
 * {@link #throwableFormatter(ThrowableFormatter)} and {@link #printers(Printer...)}, it will return
 * a {@link Logger.Builder} object.
 * <p/>
 * <b>2. Finish the customization.</b>
 * <br/>Continue to setup other fields of the returned {@link Logger.Builder}.
 * <p/>
 * <b>3. Build a dynamically generated {@link Logger}.</b>
 * <br/>Call the {@link Logger.Builder#build()} of the returned {@link Logger.Builder}.
 * <p/>
 * <b>4. Start to log.</b>
 * <br/>The logging methods of a {@link Logger} is completely same as that ones in {@link XLog}.
 * <br/>As a convenience, you can ignore the step 3, just call the logging methods of
 * {@link Logger.Builder#build()}, it will automatically build a {@link Logger} and call the target
 * logging method.
 * <p/>
 * <b>Compatibility:</b>
 * <p/>
 * In order to be compatible with {@link android.util.Log}, all the methods of
 * {@link android.util.Log} are supported here.
 * See:
 * <br/>{@link Log#v(String, String)}, {@link Log#v(String, String, Throwable)}
 * <br/>{@link Log#d(String, String)}, {@link Log#d(String, String, Throwable)}
 * <br/>{@link Log#i(String, String)}, {@link Log#i(String, String, Throwable)}
 * <br/>{@link Log#w(String, String)}, {@link Log#w(String, String, Throwable)}
 * <br/>{@link Log#wtf(String, String)}, {@link Log#wtf(String, String, Throwable)}
 * <br/>{@link Log#e(String, String)}, {@link Log#e(String, String, Throwable)}
 * <br/>{@link Log#println(int, String, String)}
 * <br/>{@link Log#isLoggable(String, int)}
 * <br/>{@link Log#getStackTraceString(Throwable)}
 * <p/>
 */
public class XLog {

    /**
     * Global log configuration.
     */
    static LogConfiguration sLogConfiguration;

    /**
     * Global log printers.
     */
    static List<Printer> sPrinters = new ArrayList<Printer>();

    /**
     * Global log level, below of which would not be log.
     */
    static int sLogLevel = LogLevel.ALL;

    /**
     * Thread safe logger array.
     */
    private static ThreadLocal<Logger> sLocalLogger = new ThreadLocal<Logger>() {

        @Override
        protected Logger initialValue() {
            return new Logger(sLogConfiguration, sPrinters);
        }
    };

    private static boolean sIsInitialized;

    /**
     * Prevent instance.
     */
    private XLog() {
    }

    /**
     * Initialize log system, should be called only once.
     */
    public static void init(int logLevel) {
        init(logLevel, new LogConfiguration.Builder().build(), new AndroidPrinter());
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @param logConfiguration the log configuration
     */
    public static void init(int logLevel, LogConfiguration logConfiguration) {
        init(logLevel, logConfiguration, new AndroidPrinter());
    }

    /**
     * Initialize log system, should be called only once.
     */
    public static void init(int logLevel, Printer... printers) {
        init(logLevel, new LogConfiguration.Builder().build(), printers);
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @param logConfiguration the log configuration
     */
    public static void init(int logLevel, LogConfiguration logConfiguration, Printer... printers) {
        if (sIsInitialized) {
            throw new IllegalStateException(
                    "XLog is already initialized, do not initialize again.");
        }
        sLogLevel = logLevel;
        sLogConfiguration = logConfiguration;
        sPrinters.addAll(Arrays.asList(printers));
    }

    /**
     * Start to customize a {@link Logger} and set the tag.
     *
     * @param tag the tag to customize
     * @return the {@link Logger.Builder} to build the a {@link Logger}
     */
    public static Logger.Builder tag(String tag) {
        return new Logger.Builder().tag(tag);
    }

    /**
     * Start to customize a {@link Logger} and set the {@link JsonFormatter}.
     *
     * @param jsonFormatter the {@link JsonFormatter} to customize
     * @return the {@link Logger.Builder} to build the a {@link Logger}
     */
    public static Logger.Builder jsonFormatter(JsonFormatter jsonFormatter) {
        return new Logger.Builder().jsonFormatter(jsonFormatter);
    }

    /**
     * Start to customize a {@link Logger} and set the {@link XmlFormatter}.
     *
     * @param xmlFormatter the {@link XmlFormatter} to customize
     * @return the {@link Logger.Builder} to build the a {@link Logger}
     */
    public static Logger.Builder xmlFormatter(XmlFormatter xmlFormatter) {
        return new Logger.Builder().xmlFormatter(xmlFormatter);
    }

    /**
     * Start to customize a {@link Logger} and set the {@link MethodFormatter}.
     *
     * @param methodFormatter the {@link MethodFormatter} to customize
     * @return the {@link Logger.Builder} to build the a {@link Logger}
     */
    public static Logger.Builder methodFormatter(MethodFormatter methodFormatter) {
        return new Logger.Builder().methodFormatter(methodFormatter);
    }

    /**
     * Start to customize a {@link Logger} and set the {@link ThrowableFormatter}.
     *
     * @param throwableFormatter the {@link ThrowableFormatter} to customize
     * @return the {@link Logger.Builder} to build the a {@link Logger}
     */
    public static Logger.Builder throwableFormatter(ThrowableFormatter throwableFormatter) {
        return new Logger.Builder().throwableFormatter(throwableFormatter);
    }

    /**
     * Start to customize a {@link Logger} and set the {@link Printer} array.
     *
     * @param printers the {@link Printer} array to customize
     * @return the {@link Logger.Builder} to build the a {@link Logger}
     */
    public static Logger.Builder printers(Printer... printers) {
        return new Logger.Builder().printers(printers);
    }

    /**
     * Log a message with level {@link LogLevel#VERBOSE}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public static void v(String format, Object... args) {
        getLogger().v(format, args);
    }

    /**
     * Log a message with level {@link LogLevel#VERBOSE}.
     *
     * @param msg the message to log
     */
    public static void v(String msg) {
        getLogger().v(msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#VERBOSE}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void v(String msg, Throwable tr) {
        getLogger().v(msg, tr);
    }

    /**
     * Log a message with level {@link LogLevel#DEBUG}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public static void d(String format, Object... args) {
        getLogger().d(format, args);
    }

    /**
     * Log a message with level {@link LogLevel#DEBUG}.
     *
     * @param msg the message to log
     */
    public static void d(String msg) {
        getLogger().d(msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#DEBUG}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void d(String msg, Throwable tr) {
        getLogger().d(msg, tr);
    }

    /**
     * Log a message with level {@link LogLevel#INFO}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public static void i(String format, Object... args) {
        getLogger().i(format, args);
    }

    /**
     * Log a message with level {@link LogLevel#INFO}.
     *
     * @param msg the message to log
     */
    public static void i(String msg) {
        getLogger().i(msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#INFO}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void i(String msg, Throwable tr) {
        getLogger().i(msg, tr);
    }

    /**
     * Log a message with level {@link LogLevel#WARN}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public static void w(String format, Object... args) {
        getLogger().w(format, args);
    }

    /**
     * Log a message with level {@link LogLevel#WARN}.
     *
     * @param msg the message to log
     */
    public static void w(String msg) {
        getLogger().w(msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#WARN}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void w(String msg, Throwable tr) {
        getLogger().w(msg, tr);
    }

    /**
     * Log a message with level {@link LogLevel#ERROR}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public static void e(String format, Object... args) {
        getLogger().e(format, args);
    }

    /**
     * Log a message with level {@link LogLevel#ERROR}.
     *
     * @param msg the message to log
     */
    public static void e(String msg) {
        getLogger().e(msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#ERROR}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void e(String msg, Throwable tr) {
        getLogger().e(msg, tr);
    }

    /**
     * Log a JSON string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param json the JSON string to log
     */
    public static void json(String json) {
        getLogger().json(json);
    }

    /**
     * Log a XML string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param xml the XML string to log
     */
    public static void xml(String xml) {
        getLogger().xml(xml);
    }

    /**
     * Log a method, with level {@link LogLevel#DEBUG} by default.
     *
     * @param arguments the arguments of the method to log
     */
    public static void method(Object... arguments) {
        getLogger().method(1, arguments);
    }

    /**
     * Log a stack trace, with level {@link LogLevel#DEBUG} by default.
     */
    public static void stack() {
        getLogger().stack("", 1);
    }

    /**
     * Log a stack trace, with level {@link LogLevel#DEBUG} by default.
     *
     * @param format the format of the extra message to log
     * @param args   the arguments of the extra message to log
     */
    public static void stack(String format, Object... args) {
        getLogger().stack(String.format(format, args), 1);
    }

    /**
     * Log a stack trace, with level {@link LogLevel#DEBUG} by default.
     *
     * @param msg the extra message to log
     */
    public static void stack(String msg) {
        getLogger().stack(msg, 1);
    }

    private static Logger getLogger() {
        return sLocalLogger.get();
    }

    /**
     * Compatible class with {@link android.util.Log}.
     *
     * @deprecated please use {@link XLog} instead
     */
    public static class Log {

        /**
         * @deprecated compatible with {@link android.util.Log#v(String, String)}
         */
        public static void v(String tag, String msg) {
            tag(tag).build().v(msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#v(String, String, Throwable)}
         */
        public static void v(String tag, String msg, Throwable tr) {
            tag(tag).build().v(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#d(String, String)}
         */
        public static void d(String tag, String msg) {
            tag(tag).build().d(msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#d(String, String, Throwable)}
         */
        public static void d(String tag, String msg, Throwable tr) {
            tag(tag).build().d(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#i(String, String)}
         */
        public static void i(String tag, String msg) {
            tag(tag).build().i(msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#i(String, String, Throwable)}
         */
        public static void i(String tag, String msg, Throwable tr) {
            tag(tag).build().i(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#w(String, String)}
         */
        public static void w(String tag, String msg) {
            tag(tag).build().w(msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#w(String, String, Throwable)}
         */
        public static void w(String tag, String msg, Throwable tr) {
            tag(tag).build().w(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#w(String, Throwable)}
         */
        public static void w(String tag, Throwable tr) {
            tag(tag).build().w("", tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#e(String, String)}
         */
        public static void e(String tag, String msg) {
            tag(tag).build().e(msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#e(String, String, Throwable)}
         */
        public static void e(String tag, String msg, Throwable tr) {
            tag(tag).build().e(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#wtf(String, String)}
         */
        public static void wtf(String tag, String msg) {
            e(tag, msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#wtf(String, Throwable)}
         */
        public static void wtf(String tag, Throwable tr) {
            wtf(tag, "", tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#wtf(String, String, Throwable)}
         */
        public static void wtf(String tag, String msg, Throwable tr) {
            e(tag, msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#println(int, String, String)}
         */
        public static void println(int logLevel, String tag, String msg) {
            tag(tag).build().println(logLevel, msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#isLoggable(String, int)}
         */
        public static boolean isLoggable(String tag, int level) {
            return level >= sLogLevel;
        }

        /**
         * @deprecated compatible with {@link android.util.Log#getStackTraceString(Throwable)}
         */
        public static String getStackTraceString(Throwable tr) {
            return StackTraceUtil.getStackTraceString(tr);
        }
    }
}
