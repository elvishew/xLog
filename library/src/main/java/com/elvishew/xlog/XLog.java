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

import com.elvishew.xlog.formatter.border.BorderFormatter;
import com.elvishew.xlog.formatter.message.json.JsonFormatter;
import com.elvishew.xlog.formatter.message.object.ObjectFormatter;
import com.elvishew.xlog.formatter.message.throwable.ThrowableFormatter;
import com.elvishew.xlog.formatter.message.xml.XmlFormatter;
import com.elvishew.xlog.formatter.stacktrace.StackTraceFormatter;
import com.elvishew.xlog.formatter.thread.ThreadFormatter;
import com.elvishew.xlog.interceptor.Interceptor;
import com.elvishew.xlog.internal.DefaultsFactory;
import com.elvishew.xlog.internal.Platform;
import com.elvishew.xlog.internal.util.StackTraceUtil;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.PrinterSet;

/**
 * A log tool which can be used in android or java, the most important feature is it can print the
 * logs to multiple place in the same time, such as android shell, console and file, you can
 * even print the log to the remote server if you want, all of these can be done just within one
 * calling.
 * <br>Also, XLog is very flexible, almost every component is replaceable.
 * <p>
 * <b>How to use in a general way:</b>
 * <p>
 * <b>1. Initial the log system.</b>
 * <br>Using one of
 * <br>{@link XLog#init()}
 * <br>{@link XLog#init(int)},
 * <br>{@link XLog#init(LogConfiguration)}
 * <br>{@link XLog#init(Printer...)},
 * <br>{@link XLog#init(int, Printer...)},
 * <br>{@link XLog#init(LogConfiguration, Printer...)},
 * <br>that will setup a {@link Logger} for a global usage.
 * If you want to use a customized configuration instead of the global one to log something, you can
 * start a customization logging.
 * <p>
 * For android, a best place to do the initialization is {@link Application#onCreate()}.
 * <p>
 * <b>2. Start to log.</b>
 * <br>{@link #v(String, Object...)}, {@link #v(String)} and {@link #v(String, Throwable)} are for
 * logging a {@link LogLevel#INFO} message.
 * <br>{@link #d(String, Object...)}, {@link #d(String)} and {@link #d(String, Throwable)} are for
 * logging a {@link LogLevel#DEBUG} message.
 * <br>{@link #i(String, Object...)}, {@link #i(String)} and {@link #i(String, Throwable)} are for
 * logging a {@link LogLevel#INFO} message.
 * <br>{@link #w(String, Object...)}, {@link #w(String)} and {@link #w(String, Throwable)} are for
 * logging a {@link LogLevel#WARN} message.
 * <br>{@link #e(String, Object...)}, {@link #e(String)} and {@link #e(String, Throwable)} are for
 * logging a {@link LogLevel#ERROR} message.
 * <br>{@link #log(int, String, Object...)}, {@link #log(int, String)} and
 * {@link #log(int, String, Throwable)} are for logging a specific level message.
 * <br>{@link #json(String)} is for logging a {@link LogLevel#DEBUG} JSON string.
 * <br>{@link #xml(String)} is for logging a {@link LogLevel#DEBUG} XML string.
 * <br> Also, you can directly log any object with specific log level, like {@link #v(Object)},
 * and any object array with specific log level, like {@link #v(Object[])}.
 * <p>
 * <b>How to use in a dynamically customizing way after initializing the log system:</b>
 * <p>
 * <b>1. Start a customization.</b>
 * <br>Call any of
 * <br>{@link #logLevel(int)}
 * <br>{@link #tag(String)},
 * <br>{@link #t()},
 * <br>{@link #nt()},
 * <br>{@link #st(int)},
 * <br>{@link #nst()},
 * <br>{@link #b()},
 * <br>{@link #nb()},
 * <br>{@link #jsonFormatter(JsonFormatter)},
 * <br>{@link #xmlFormatter(XmlFormatter)},
 * <br>{@link #threadFormatter(ThreadFormatter)},
 * <br>{@link #stackTraceFormatter(StackTraceFormatter)},
 * <br>{@link #throwableFormatter(ThrowableFormatter)}
 * <br>{@link #borderFormatter(BorderFormatter)}
 * <br>{@link #addObjectFormatter(Class, ObjectFormatter)}
 * <br>{@link #addInterceptor(Interceptor)}
 * <br>{@link #printers(Printer...)},
 * <br>it will return a {@link Logger.Builder} object.
 * <p>
 * <b>2. Finish the customization.</b>
 * <br>Continue to setup other fields of the returned {@link Logger.Builder}.
 * <p>
 * <b>3. Build a dynamically generated {@link Logger}.</b>
 * <br>Call the {@link Logger.Builder#build()} of the returned {@link Logger.Builder}.
 * <p>
 * <b>4. Start to log.</b>
 * <br>The logging methods of a {@link Logger} is completely same as that ones in {@link XLog}.
 * <br>As a convenience, you can ignore the step 3, just call the logging methods of
 * {@link Logger.Builder}, it will automatically build a {@link Logger} and call the target
 * logging method.
 * <p>
 * <b>Compatibility:</b>
 * <p>
 * In order to be compatible with {@link android.util.Log}, all the methods of
 * {@link android.util.Log} are supported here.
 * See:
 * <br>{@link Log#v(String, String)}, {@link Log#v(String, String, Throwable)}
 * <br>{@link Log#d(String, String)}, {@link Log#d(String, String, Throwable)}
 * <br>{@link Log#i(String, String)}, {@link Log#i(String, String, Throwable)}
 * <br>{@link Log#w(String, String)}, {@link Log#w(String, String, Throwable)}
 * <br>{@link Log#wtf(String, String)}, {@link Log#wtf(String, String, Throwable)}
 * <br>{@link Log#e(String, String)}, {@link Log#e(String, String, Throwable)}
 * <br>{@link Log#println(int, String, String)}
 * <br>{@link Log#isLoggable(String, int)}
 * <br>{@link Log#getStackTraceString(Throwable)}
 * <p>
 */
public class XLog {

  /**
   * Global logger for all direct logging via {@link XLog}.
   */
  private static Logger sLogger;

  /**
   * Global log configuration.
   */
  static LogConfiguration sLogConfiguration;

  /**
   * Global log printer.
   */
  static Printer sPrinter;

  static boolean sIsInitialized;

  /**
   * Prevent instance.
   */
  private XLog() {
  }

  /**
   * Initialize log system, should be called only once.
   *
   * @since 1.3.0
   */
  public static void init() {
    init(new LogConfiguration.Builder().build(), DefaultsFactory.createPrinter());
  }

  /**
   * Initialize log system, should be called only once.
   *
   * @param logLevel the log level, logs with a lower level than which would not be printed
   */
  public static void init(int logLevel) {
    init(new LogConfiguration.Builder().logLevel(logLevel).build(),
        DefaultsFactory.createPrinter());
  }

  /**
   * Initialize log system, should be called only once.
   *
   * @param logLevel         the log level, logs with a lower level than which would not be printed
   * @param logConfiguration the log configuration
   * @deprecated the log level is part of log configuration now, use {@link #init(LogConfiguration)}
   * instead, since 1.3.0
   */
  @Deprecated
  public static void init(int logLevel, LogConfiguration logConfiguration) {
    init(new LogConfiguration.Builder(logConfiguration).logLevel(logLevel).build());
  }

  /**
   * Initialize log system, should be called only once.
   *
   * @param logConfiguration the log configuration
   * @since 1.3.0
   */
  public static void init(LogConfiguration logConfiguration) {
    init(logConfiguration, DefaultsFactory.createPrinter());
  }

  /**
   * Initialize log system, should be called only once.
   *
   * @param printers the printers, each log would be printed by all of the printers
   * @since 1.3.0
   */
  public static void init(Printer... printers) {
    init(new LogConfiguration.Builder().build(), printers);
  }

  /**
   * Initialize log system, should be called only once.
   *
   * @param logLevel the log level, logs with a lower level than which would not be printed
   * @param printers the printers, each log would be printed by all of the printers
   */
  public static void init(int logLevel, Printer... printers) {
    init(new LogConfiguration.Builder().logLevel(logLevel).build(), printers);
  }

  /**
   * Initialize log system, should be called only once.
   *
   * @param logLevel         the log level, logs with a lower level than which would not be printed
   * @param logConfiguration the log configuration
   * @param printers         the printers, each log would be printed by all of the printers
   * @deprecated the log level is part of log configuration now,
   * use {@link #init(LogConfiguration, Printer...)} instead, since 1.3.0
   */
  @Deprecated
  public static void init(int logLevel, LogConfiguration logConfiguration, Printer... printers) {
    init(new LogConfiguration.Builder(logConfiguration).logLevel(logLevel).build(), printers);
  }

  /**
   * Initialize log system, should be called only once.
   *
   * @param logConfiguration the log configuration
   * @param printers         the printers, each log would be printed by all of the printers
   * @since 1.3.0
   */
  public static void init(LogConfiguration logConfiguration, Printer... printers) {
    if (sIsInitialized) {
      Platform.get().warn("XLog is already initialized, do not initialize again");
    }
    sIsInitialized = true;

    if (logConfiguration == null) {
      throw new IllegalArgumentException("Please specify a LogConfiguration");
    }
    sLogConfiguration = logConfiguration;

    sPrinter = new PrinterSet(printers);

    sLogger = new Logger(sLogConfiguration, sPrinter);
  }

  /**
   * Throw an IllegalStateException if not initialized.
   */
  static void assertInitialization() {
    if (!sIsInitialized) {
      throw new IllegalStateException("Do you forget to initialize XLog?");
    }
  }

  /**
   * Start to customize a {@link Logger} and set the log level.
   *
   * @param logLevel the log level to customize
   * @return the {@link Logger.Builder} to build the {@link Logger}
   * @since 1.3.0
   */
  public static Logger.Builder logLevel(int logLevel) {
    return new Logger.Builder().logLevel(logLevel);
  }

  /**
   * Start to customize a {@link Logger} and set the tag.
   *
   * @param tag the tag to customize
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder tag(String tag) {
    return new Logger.Builder().tag(tag);
  }

  /**
   * Start to customize a {@link Logger} and enable thread info.
   *
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder t() {
    return new Logger.Builder().t();
  }

  /**
   * Start to customize a {@link Logger} and disable thread info.
   *
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder nt() {
    return new Logger.Builder().nt();
  }

  /**
   * Start to customize a {@link Logger} and enable stack trace.
   *
   * @param depth the number of stack trace elements we should log, 0 if no limitation
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder st(int depth) {
    return new Logger.Builder().st(depth);
  }

  /**
   * Start to customize a {@link Logger} and enable stack trace.
   *
   * @param stackTraceOrigin the origin of stack trace elements from which we should NOT log,
   *                         it can be a package name like "com.elvishew.xlog", a class name
   *                         like "com.yourdomain.logWrapper", or something else between
   *                         package name and class name, like "com.yourdomain.".
   *                         It is mostly used when you are using a logger wrapper
   * @param depth            the number of stack trace elements we should log, 0 if no limitation
   * @return the {@link Logger.Builder} to build the {@link Logger}
   * @since 1.4.0
   */
  public static Logger.Builder st(String stackTraceOrigin, int depth) {
    return new Logger.Builder().st(stackTraceOrigin, depth);
  }

  /**
   * Start to customize a {@link Logger} and disable stack trace.
   *
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder nst() {
    return new Logger.Builder().nst();
  }

  /**
   * Start to customize a {@link Logger} and enable border.
   *
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder b() {
    return new Logger.Builder().b();
  }

  /**
   * Start to customize a {@link Logger} and disable border.
   *
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder nb() {
    return new Logger.Builder().nb();
  }

  /**
   * Start to customize a {@link Logger} and set the {@link JsonFormatter}.
   *
   * @param jsonFormatter the {@link JsonFormatter} to customize
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder jsonFormatter(JsonFormatter jsonFormatter) {
    return new Logger.Builder().jsonFormatter(jsonFormatter);
  }

  /**
   * Start to customize a {@link Logger} and set the {@link XmlFormatter}.
   *
   * @param xmlFormatter the {@link XmlFormatter} to customize
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder xmlFormatter(XmlFormatter xmlFormatter) {
    return new Logger.Builder().xmlFormatter(xmlFormatter);
  }

  /**
   * Start to customize a {@link Logger} and set the {@link ThrowableFormatter}.
   *
   * @param throwableFormatter the {@link ThrowableFormatter} to customize
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder throwableFormatter(ThrowableFormatter throwableFormatter) {
    return new Logger.Builder().throwableFormatter(throwableFormatter);
  }

  /**
   * Start to customize a {@link Logger} and set the {@link ThreadFormatter}.
   *
   * @param threadFormatter the {@link ThreadFormatter} to customize
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder threadFormatter(ThreadFormatter threadFormatter) {
    return new Logger.Builder().threadFormatter(threadFormatter);
  }

  /**
   * Start to customize a {@link Logger} and set the {@link StackTraceFormatter}.
   *
   * @param stackTraceFormatter the {@link StackTraceFormatter} to customize
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder stackTraceFormatter(StackTraceFormatter stackTraceFormatter) {
    return new Logger.Builder().stackTraceFormatter(stackTraceFormatter);
  }

  /**
   * Start to customize a {@link Logger} and set the {@link BorderFormatter}.
   *
   * @param borderFormatter the {@link BorderFormatter} to customize
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder borderFormatter(BorderFormatter borderFormatter) {
    return new Logger.Builder().borderFormatter(borderFormatter);
  }

  /**
   * Start to customize a {@link Logger} and add an object formatter for specific class of object.
   *
   * @param objectClass     the class of object
   * @param objectFormatter the object formatter to add
   * @param <T>             the type of object
   * @return the {@link Logger.Builder} to build the {@link Logger}
   * @since 1.1.0
   */
  public static <T> Logger.Builder addObjectFormatter(Class<T> objectClass,
                                                      ObjectFormatter<? super T> objectFormatter) {
    return new Logger.Builder().addObjectFormatter(objectClass, objectFormatter);
  }

  /**
   * Start to customize a {@link Logger} and add an interceptor.
   *
   * @param interceptor the interceptor to add
   * @return the {@link Logger.Builder} to build the {@link Logger}
   * @since 1.3.0
   */
  public static Logger.Builder addInterceptor(Interceptor interceptor) {
    return new Logger.Builder().addInterceptor(interceptor);
  }

  /**
   * Start to customize a {@link Logger} and set the {@link Printer} array.
   *
   * @param printers the {@link Printer} array to customize
   * @return the {@link Logger.Builder} to build the {@link Logger}
   */
  public static Logger.Builder printers(Printer... printers) {
    return new Logger.Builder().printers(printers);
  }

  /**
   * Log an object with level {@link LogLevel#VERBOSE}.
   *
   * @param object the object to log
   * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
   * @since 1.1.0
   */
  public static void v(Object object) {
    assertInitialization();
    sLogger.v(object);
  }

  /**
   * Log an array with level {@link LogLevel#VERBOSE}.
   *
   * @param array the array to log
   */
  public static void v(Object[] array) {
    assertInitialization();
    sLogger.v(array);
  }

  /**
   * Log a message with level {@link LogLevel#VERBOSE}.
   *
   * @param format the format of the message to log
   * @param args   the arguments of the message to log
   */
  public static void v(String format, Object... args) {
    assertInitialization();
    sLogger.v(format, args);
  }

  /**
   * Log a message with level {@link LogLevel#VERBOSE}.
   *
   * @param msg the message to log
   */
  public static void v(String msg) {
    assertInitialization();
    sLogger.v(msg);
  }

  /**
   * Log a message and a throwable with level {@link LogLevel#VERBOSE}.
   *
   * @param msg the message to log
   * @param tr  the throwable to be log
   */
  public static void v(String msg, Throwable tr) {
    assertInitialization();
    sLogger.v(msg, tr);
  }

  /**
   * Log an object with level {@link LogLevel#DEBUG}.
   *
   * @param object the object to log
   * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
   * @since 1.1.0
   */
  public static void d(Object object) {
    assertInitialization();
    sLogger.d(object);
  }

  /**
   * Log an array with level {@link LogLevel#DEBUG}.
   *
   * @param array the array to log
   */
  public static void d(Object[] array) {
    assertInitialization();
    sLogger.d(array);
  }

  /**
   * Log a message with level {@link LogLevel#DEBUG}.
   *
   * @param format the format of the message to log
   * @param args   the arguments of the message to log
   */
  public static void d(String format, Object... args) {
    assertInitialization();
    sLogger.d(format, args);
  }

  /**
   * Log a message with level {@link LogLevel#DEBUG}.
   *
   * @param msg the message to log
   */
  public static void d(String msg) {
    assertInitialization();
    sLogger.d(msg);
  }

  /**
   * Log a message and a throwable with level {@link LogLevel#DEBUG}.
   *
   * @param msg the message to log
   * @param tr  the throwable to be log
   */
  public static void d(String msg, Throwable tr) {
    assertInitialization();
    sLogger.d(msg, tr);
  }

  /**
   * Log an object with level {@link LogLevel#INFO}.
   *
   * @param object the object to log
   * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
   * @since 1.1.0
   */
  public static void i(Object object) {
    assertInitialization();
    sLogger.i(object);
  }

  /**
   * Log an array with level {@link LogLevel#INFO}.
   *
   * @param array the array to log
   */
  public static void i(Object[] array) {
    assertInitialization();
    sLogger.i(array);
  }

  /**
   * Log a message with level {@link LogLevel#INFO}.
   *
   * @param format the format of the message to log
   * @param args   the arguments of the message to log
   */
  public static void i(String format, Object... args) {
    assertInitialization();
    sLogger.i(format, args);
  }

  /**
   * Log a message with level {@link LogLevel#INFO}.
   *
   * @param msg the message to log
   */
  public static void i(String msg) {
    assertInitialization();
    sLogger.i(msg);
  }

  /**
   * Log a message and a throwable with level {@link LogLevel#INFO}.
   *
   * @param msg the message to log
   * @param tr  the throwable to be log
   */
  public static void i(String msg, Throwable tr) {
    assertInitialization();
    sLogger.i(msg, tr);
  }

  /**
   * Log an object with level {@link LogLevel#WARN}.
   *
   * @param object the object to log
   * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
   * @since 1.1.0
   */
  public static void w(Object object) {
    assertInitialization();
    sLogger.w(object);
  }

  /**
   * Log an array with level {@link LogLevel#WARN}.
   *
   * @param array the array to log
   */
  public static void w(Object[] array) {
    assertInitialization();
    sLogger.w(array);
  }

  /**
   * Log a message with level {@link LogLevel#WARN}.
   *
   * @param format the format of the message to log
   * @param args   the arguments of the message to log
   */
  public static void w(String format, Object... args) {
    assertInitialization();
    sLogger.w(format, args);
  }

  /**
   * Log a message with level {@link LogLevel#WARN}.
   *
   * @param msg the message to log
   */
  public static void w(String msg) {
    assertInitialization();
    sLogger.w(msg);
  }

  /**
   * Log a message and a throwable with level {@link LogLevel#WARN}.
   *
   * @param msg the message to log
   * @param tr  the throwable to be log
   */
  public static void w(String msg, Throwable tr) {
    assertInitialization();
    sLogger.w(msg, tr);
  }

  /**
   * Log an object with level {@link LogLevel#ERROR}.
   *
   * @param object the object to log
   * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
   * @since 1.1.0
   */
  public static void e(Object object) {
    assertInitialization();
    sLogger.e(object);
  }

  /**
   * Log an array with level {@link LogLevel#ERROR}.
   *
   * @param array the array to log
   */
  public static void e(Object[] array) {
    assertInitialization();
    sLogger.e(array);
  }

  /**
   * Log a message with level {@link LogLevel#ERROR}.
   *
   * @param format the format of the message to log
   * @param args   the arguments of the message to log
   */
  public static void e(String format, Object... args) {
    assertInitialization();
    sLogger.e(format, args);
  }

  /**
   * Log a message with level {@link LogLevel#ERROR}.
   *
   * @param msg the message to log
   */
  public static void e(String msg) {
    assertInitialization();
    sLogger.e(msg);
  }

  /**
   * Log a message and a throwable with level {@link LogLevel#ERROR}.
   *
   * @param msg the message to log
   * @param tr  the throwable to be log
   */
  public static void e(String msg, Throwable tr) {
    assertInitialization();
    sLogger.e(msg, tr);
  }

  /**
   * Log an object with specific log level.
   *
   * @param logLevel the specific log level
   * @param object   the object to log
   * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
   * @since 1.4.0
   */
  public static void log(int logLevel, Object object) {
    assertInitialization();
    sLogger.log(logLevel, object);
  }

  /**
   * Log an array with specific log level.
   *
   * @param logLevel the specific log level
   * @param array    the array to log
   * @since 1.4.0
   */
  public static void log(int logLevel, Object[] array) {
    assertInitialization();
    sLogger.log(logLevel, array);
  }

  /**
   * Log a message with specific log level.
   *
   * @param logLevel the specific log level
   * @param format   the format of the message to log
   * @param args     the arguments of the message to log
   * @since 1.4.0
   */
  public static void log(int logLevel, String format, Object... args) {
    assertInitialization();
    sLogger.log(logLevel, format, args);
  }

  /**
   * Log a message with specific log level.
   *
   * @param logLevel the specific log level
   * @param msg      the message to log
   * @since 1.4.0
   */
  public static void log(int logLevel, String msg) {
    assertInitialization();
    sLogger.log(logLevel, msg);
  }

  /**
   * Log a message and a throwable with specific log level.
   *
   * @param logLevel the specific log level
   * @param msg      the message to log
   * @param tr       the throwable to be log
   * @since 1.4.0
   */
  public static void log(int logLevel, String msg, Throwable tr) {
    assertInitialization();
    sLogger.log(logLevel, msg, tr);
  }

  /**
   * Log a JSON string, with level {@link LogLevel#DEBUG} by default.
   *
   * @param json the JSON string to log
   */
  public static void json(String json) {
    assertInitialization();
    sLogger.json(json);
  }

  /**
   * Log a XML string, with level {@link LogLevel#DEBUG} by default.
   *
   * @param xml the XML string to log
   */
  public static void xml(String xml) {
    assertInitialization();
    sLogger.xml(xml);
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
      return sLogConfiguration.isLoggable(level);
    }

    /**
     * @deprecated compatible with {@link android.util.Log#getStackTraceString(Throwable)}
     */
    public static String getStackTraceString(Throwable tr) {
      return StackTraceUtil.getStackTraceString(tr);
    }
  }
}
