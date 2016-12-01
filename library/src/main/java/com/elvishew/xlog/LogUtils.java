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

import static com.elvishew.xlog.XLog.assertInitialization;

/**
 * Utilities for convenience.
 *
 * @since 1.2.0
 */
public class LogUtils {

  /**
   * Format a JSON string using default JSON formatter.
   *
   * @param json the JSON string to format
   * @return the formatted string
   */
  public static String formatJson(String json) {
    assertInitialization();
    return XLog.sLogConfiguration.jsonFormatter.format(json);
  }

  /**
   * Format an XML string using default XML formatter.
   *
   * @param xml the XML string to format
   * @return the formatted string
   */
  public static String formatXml(String xml) {
    assertInitialization();
    return XLog.sLogConfiguration.xmlFormatter.format(xml);
  }

  /**
   * Format a throwable using default throwable formatter.
   *
   * @param throwable the throwable to format
   * @return the formatted string
   */
  public static String formatThrowable(Throwable throwable) {
    assertInitialization();
    return XLog.sLogConfiguration.throwableFormatter.format(throwable);
  }

  /**
   * Format a thread using default thread formatter.
   *
   * @param thread the thread to format
   * @return the formatted string
   */
  public static String formatThread(Thread thread) {
    assertInitialization();
    return XLog.sLogConfiguration.threadFormatter.format(thread);
  }

  /**
   * Format a stack trace using default stack trace formatter.
   *
   * @param stackTrace the stack trace to format
   * @return the formatted string
   */
  public static String formatStackTrace(StackTraceElement[] stackTrace) {
    assertInitialization();
    return XLog.sLogConfiguration.stackTraceFormatter.format(stackTrace);
  }

  /**
   * Add border to string segments using default border formatter.
   *
   * @param segments the string segments to add border to
   * @return the bordered string segments
   */
  public static String addBorder(String[] segments) {
    assertInitialization();
    return XLog.sLogConfiguration.borderFormatter.format(segments);
  }
}
