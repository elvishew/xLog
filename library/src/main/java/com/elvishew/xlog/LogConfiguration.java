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

import com.elvishew.xlog.formatter.border.BorderFormatter;
import com.elvishew.xlog.formatter.message.json.JsonFormatter;
import com.elvishew.xlog.formatter.message.object.ObjectFormatter;
import com.elvishew.xlog.formatter.message.throwable.ThrowableFormatter;
import com.elvishew.xlog.formatter.message.xml.XmlFormatter;
import com.elvishew.xlog.formatter.stacktrace.StackTraceFormatter;
import com.elvishew.xlog.formatter.thread.ThreadFormatter;
import com.elvishew.xlog.interceptor.Interceptor;
import com.elvishew.xlog.internal.DefaultsFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The configuration used for logging, always attached to a {@link Logger}, will affect all logs
 * logged by the {@link Logger}.
 * <p>
 * Use the {@link Builder} to construct a {@link LogConfiguration} object.
 */
public class LogConfiguration {


  /**
   * The log level, the logs below of which would not be printed.
   */
  public final int logLevel;

  /**
   * The tag string.
   */
  public final String tag;

  /**
   * Whether we should log with thread info.
   */
  public final boolean withThread;

  /**
   * Whether we should log with stack trace.
   */
  public final boolean withStackTrace;

  /**
   * The origin of stack trace elements from which we should not log when logging with stack trace,
   * it can be a package name like "com.elvishew.xlog", a class name like "com.yourdomain.logWrapper",
   * or something else between package name and class name, like "com.yourdomain.".
   * <p>
   * It is mostly used when you are using a logger wrapper.
   *
   * @since 1.4.0
   */
  public final String stackTraceOrigin;

  /**
   * The number of stack trace elements we should log when logging with stack trace,
   * 0 if no limitation.
   */
  public final int stackTraceDepth;

  /**
   * Whether we should log with border.
   */
  public final boolean withBorder;

  /**
   * The JSON formatter used to format the JSON string when log a JSON string.
   */
  public final JsonFormatter jsonFormatter;

  /**
   * The XML formatter used to format the XML string when log a XML string.
   */
  public final XmlFormatter xmlFormatter;

  /**
   * The throwable formatter used to format the throwable when log a message with throwable.
   */
  public final ThrowableFormatter throwableFormatter;

  /**
   * The thread formatter used to format the thread when logging.
   */
  public final ThreadFormatter threadFormatter;

  /**
   * The stack trace formatter used to format the stack trace when logging.
   */
  public final StackTraceFormatter stackTraceFormatter;

  /**
   * The border formatter used to format the border when logging.
   */
  public final BorderFormatter borderFormatter;

  /**
   * The object formatters, used when logging an object.
   */
  private final Map<Class<?>, ObjectFormatter<?>> objectFormatters;

  /**
   * The interceptors, used to intercept the log when logging.
   *
   * @since 1.3.0
   */
  public final List<Interceptor> interceptors;

  /*package*/ LogConfiguration(final Builder builder) {
    logLevel = builder.logLevel;

    tag = builder.tag;

    withThread = builder.withThread;
    withStackTrace = builder.withStackTrace;
    stackTraceOrigin = builder.stackTraceOrigin;
    stackTraceDepth = builder.stackTraceDepth;
    withBorder = builder.withBorder;

    jsonFormatter = builder.jsonFormatter;
    xmlFormatter = builder.xmlFormatter;
    throwableFormatter = builder.throwableFormatter;
    threadFormatter = builder.threadFormatter;
    stackTraceFormatter = builder.stackTraceFormatter;
    borderFormatter = builder.borderFormatter;

    objectFormatters = builder.objectFormatters;

    interceptors = builder.interceptors;
  }

  /**
   * Get {@link ObjectFormatter} for specific object.
   *
   * @param object the object
   * @param <T>    the type of object
   * @return the object formatter for the object, or null if not found
   * @since 1.1.0
   */
  public <T> ObjectFormatter<? super T> getObjectFormatter(T object) {
    if (objectFormatters == null) {
      return null;
    }

    Class<? super T> clazz;
    Class<? super T> superClazz = (Class<? super T>) object.getClass();
    ObjectFormatter<? super T> formatter;
    do {
      clazz = superClazz;
      formatter = (ObjectFormatter<? super T>) objectFormatters.get(clazz);
      superClazz = clazz.getSuperclass();
    } while (formatter == null && superClazz != null);
    return formatter;
  }

  /**
   * Whether logs with specific level is loggable.
   *
   * @param level the specific level
   * @return true if loggable, false otherwise
   */
  /*package*/ boolean isLoggable(int level) {
    return level >= logLevel;
  }

  /**
   * Builder for {@link LogConfiguration}.
   */
  public static class Builder {

    private static final int DEFAULT_LOG_LEVEL = LogLevel.ALL;

    private static final String DEFAULT_TAG = "X-LOG";

    /**
     * The log level, the logs below of which would not be printed.
     */
    private int logLevel = DEFAULT_LOG_LEVEL;

    /**
     * The tag string used when log.
     */
    private String tag = DEFAULT_TAG;

    /**
     * Whether we should log with thread info.
     */
    private boolean withThread;

    /**
     * Whether we should log with stack trace.
     */
    private boolean withStackTrace;

    /**
     * The origin of stack trace elements from which we should NOT log when logging with stack trace,
     * it can be a package name like "com.elvishew.xlog", a class name like "com.yourdomain.logWrapper",
     * or something else between package name and class name, like "com.yourdomain.".
     * <p>
     * It is mostly used when you are using a logger wrapper.
     */
    private String stackTraceOrigin;

    /**
     * The number of stack trace elements we should log when logging with stack trace,
     * 0 if no limitation.
     */
    private int stackTraceDepth;

    /**
     * Whether we should log with border.
     */
    private boolean withBorder;

    /**
     * The JSON formatter used to format the JSON string when log a JSON string.
     */
    private JsonFormatter jsonFormatter;

    /**
     * The XML formatter used to format the XML string when log a XML string.
     */
    private XmlFormatter xmlFormatter;

    /**
     * The throwable formatter used to format the throwable when log a message with throwable.
     */
    private ThrowableFormatter throwableFormatter;

    /**
     * The thread formatter used to format the thread when logging.
     */
    private ThreadFormatter threadFormatter;

    /**
     * The stack trace formatter used to format the stack trace when logging.
     */
    private StackTraceFormatter stackTraceFormatter;

    /**
     * The border formatter used to format the border when logging.
     */
    private BorderFormatter borderFormatter;

    /**
     * The object formatters, used when logging an object.
     */
    private Map<Class<?>, ObjectFormatter<?>> objectFormatters;

    /**
     * The interceptors, used to intercept the log when logging.
     */
    private List<Interceptor> interceptors;

    /**
     * Construct a builder with all default configurations.
     */
    public Builder() {
    }

    /**
     * Construct a builder with all configurations from another {@link LogConfiguration}.
     *
     * @param logConfiguration the {@link LogConfiguration} to copy configurations from
     */
    public Builder(LogConfiguration logConfiguration) {
      logLevel = logConfiguration.logLevel;

      tag = logConfiguration.tag;

      withThread = logConfiguration.withThread;
      withStackTrace = logConfiguration.withStackTrace;
      stackTraceOrigin = logConfiguration.stackTraceOrigin;
      stackTraceDepth = logConfiguration.stackTraceDepth;
      withBorder = logConfiguration.withBorder;

      jsonFormatter = logConfiguration.jsonFormatter;
      xmlFormatter = logConfiguration.xmlFormatter;
      throwableFormatter = logConfiguration.throwableFormatter;
      threadFormatter = logConfiguration.threadFormatter;
      stackTraceFormatter = logConfiguration.stackTraceFormatter;
      borderFormatter = logConfiguration.borderFormatter;

      if (logConfiguration.objectFormatters != null) {
        objectFormatters = new HashMap<>(logConfiguration.objectFormatters);
      }

      if (logConfiguration.interceptors != null) {
        interceptors = new ArrayList<>(logConfiguration.interceptors);
      }
    }

    /**
     * Set the log level, the logs below of which would not be printed.
     *
     * @param logLevel the log level
     * @return the builder
     * @since 1.3.0
     */
    public Builder logLevel(int logLevel) {
      this.logLevel = logLevel;
      return this;
    }

    /**
     * Set the tag string used when log.
     *
     * @param tag the tag string used when log
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
      return this;
    }

    /**
     * Disable thread info.
     *
     * @return the builder
     */
    public Builder nt() {
      this.withThread = false;
      return this;
    }

    /**
     * Enable stack trace.
     *
     * @param depth the number of stack trace elements we should log, 0 if no limitation
     * @return the builder
     */
    public Builder st(int depth) {
      st(null, depth);
      return this;
    }

    /**
     * Enable stack trace.
     *
     * @param stackTraceOrigin the origin of stack trace elements from which we should NOT log when
     *                         logging with stack trace, it can be a package name like
     *                         "com.elvishew.xlog", a class name like "com.yourdomain.logWrapper",
     *                         or something else between package name and class name, like "com.yourdomain.".
     *                         It is mostly used when you are using a logger wrapper
     * @param depth            the number of stack trace elements we should log, 0 if no limitation
     * @return the builder
     * @since 1.4.0
     */
    public Builder st(String stackTraceOrigin, int depth) {
      this.withStackTrace = true;
      this.stackTraceOrigin = stackTraceOrigin;
      this.stackTraceDepth = depth;
      return this;
    }

    /**
     * Disable stack trace.
     *
     * @return the builder
     */
    public Builder nst() {
      this.withStackTrace = false;
      this.stackTraceOrigin = null;
      this.stackTraceDepth = 0;
      return this;
    }

    /**
     * Enable border.
     *
     * @return the builder
     */
    public Builder b() {
      this.withBorder = true;
      return this;
    }

    /**
     * Disable border.
     *
     * @return the builder
     */
    public Builder nb() {
      this.withBorder = false;
      return this;
    }

    /**
     * Set the JSON formatter used when log a JSON string.
     *
     * @param jsonFormatter the JSON formatter used when log a JSON string
     * @return the builder
     */
    public Builder jsonFormatter(JsonFormatter jsonFormatter) {
      this.jsonFormatter = jsonFormatter;
      return this;
    }

    /**
     * Set the XML formatter used when log a XML string.
     *
     * @param xmlFormatter the XML formatter used when log a XML string
     * @return the builder
     */
    public Builder xmlFormatter(XmlFormatter xmlFormatter) {
      this.xmlFormatter = xmlFormatter;
      return this;
    }

    /**
     * Set the throwable formatter used when log a message with throwable.
     *
     * @param throwableFormatter the throwable formatter used when log a message with throwable
     * @return the builder
     */
    public Builder throwableFormatter(ThrowableFormatter throwableFormatter) {
      this.throwableFormatter = throwableFormatter;
      return this;
    }

    /**
     * Set the thread formatter used when logging.
     *
     * @param threadFormatter the thread formatter used when logging
     * @return the builder
     */
    public Builder threadFormatter(ThreadFormatter threadFormatter) {
      this.threadFormatter = threadFormatter;
      return this;
    }

    /**
     * Set the stack trace formatter used when logging.
     *
     * @param stackTraceFormatter the stack trace formatter used when logging
     * @return the builder
     */
    public Builder stackTraceFormatter(StackTraceFormatter stackTraceFormatter) {
      this.stackTraceFormatter = stackTraceFormatter;
      return this;
    }

    /**
     * Set the border formatter used when logging.
     *
     * @param borderFormatter the border formatter used when logging
     * @return the builder
     */
    public Builder borderFormatter(BorderFormatter borderFormatter) {
      this.borderFormatter = borderFormatter;
      return this;
    }

    /**
     * Add a {@link ObjectFormatter} for specific class of object.
     *
     * @param objectClass     the class of object
     * @param objectFormatter the object formatter to add
     * @param <T>             the type of object
     * @return the builder
     * @since 1.1.0
     */
    public <T> Builder addObjectFormatter(Class<T> objectClass,
                                          ObjectFormatter<? super T> objectFormatter) {
      if (objectFormatters == null) {
        objectFormatters = new HashMap<>(DefaultsFactory.builtinObjectFormatters());
      }
      objectFormatters.put(objectClass, objectFormatter);
      return this;
    }

    /**
     * Copy all object formatters, only for internal usage.
     *
     * @param objectFormatters the object formatters to copy
     * @return the builder
     */
    /*package*/ Builder objectFormatters(Map<Class<?>, ObjectFormatter<?>> objectFormatters) {
      this.objectFormatters = objectFormatters;
      return this;
    }

    /**
     * Add an interceptor.
     *
     * @param interceptor the interceptor to add
     * @return the builder
     * @since 1.3.0
     */
    public Builder addInterceptor(Interceptor interceptor) {
      if (interceptors == null) {
        interceptors = new ArrayList<>();
      }
      interceptors.add(interceptor);
      return this;
    }

    /**
     * Copy all interceptors, only for internal usage.
     *
     * @param interceptors the interceptors to copy
     * @return the builder
     */
    /*package*/ Builder interceptors(List<Interceptor> interceptors) {
      this.interceptors = interceptors;
      return this;
    }

    /**
     * Builds configured {@link LogConfiguration} object.
     *
     * @return the built configured {@link LogConfiguration} object
     */
    public LogConfiguration build() {
      initEmptyFieldsWithDefaultValues();
      return new LogConfiguration(this);
    }

    private void initEmptyFieldsWithDefaultValues() {
      if (jsonFormatter == null) {
        jsonFormatter = DefaultsFactory.createJsonFormatter();
      }
      if (xmlFormatter == null) {
        xmlFormatter = DefaultsFactory.createXmlFormatter();
      }
      if (throwableFormatter == null) {
        throwableFormatter = DefaultsFactory.createThrowableFormatter();
      }
      if (threadFormatter == null) {
        threadFormatter = DefaultsFactory.createThreadFormatter();
      }
      if (stackTraceFormatter == null) {
        stackTraceFormatter = DefaultsFactory.createStackTraceFormatter();
      }
      if (borderFormatter == null) {
        borderFormatter = DefaultsFactory.createBorderFormatter();
      }
      if (objectFormatters == null) {
        objectFormatters = new HashMap<>(DefaultsFactory.builtinObjectFormatters());
      }
    }
  }
}
