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

package com.elvishew.xlog.internal.util;

import com.elvishew.xlog.XLog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * Utility related with stack trace.
 */
public class StackTraceUtil {

  private static final String XLOG_STACK_TRACE_ORIGIN;

  static {
    // Let's start from xlog library.
    String xlogClassName = XLog.class.getName();
    XLOG_STACK_TRACE_ORIGIN = xlogClassName.substring(0, xlogClassName.lastIndexOf('.') + 1);
  }

  /**
   * Get a loggable stack trace from a Throwable
   *
   * @param tr An exception to log
   */
  public static String getStackTraceString(Throwable tr) {
    if (tr == null) {
      return "";
    }

    // This is to reduce the amount of log spew that apps do in the non-error
    // condition of the network being unavailable.
    Throwable t = tr;
    while (t != null) {
      if (t instanceof UnknownHostException) {
        return "";
      }
      t = t.getCause();
    }

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    tr.printStackTrace(pw);
    pw.flush();
    return sw.toString();
  }

  /**
   * Get the real stack trace and then crop it with a max depth.
   *
   * @param stackTrace the full stack trace
   * @param maxDepth   the max depth of real stack trace that will be cropped, 0 means no limitation
   * @return the cropped real stack trace
   */
  public static StackTraceElement[] getCroppedRealStackTrack(StackTraceElement[] stackTrace,
                                                             String stackTraceOrigin,
                                                             int maxDepth) {
    return cropStackTrace(getRealStackTrack(stackTrace, stackTraceOrigin), maxDepth);
  }

  /**
   * Get the real stack trace, all elements that come from XLog library would be dropped.
   *
   * @param stackTrace the full stack trace
   * @return the real stack trace, all elements come from system and library user
   */
  private static StackTraceElement[] getRealStackTrack(StackTraceElement[] stackTrace,
                                                       String stackTraceOrigin) {
    int ignoreDepth = 0;
    int allDepth = stackTrace.length;
    String className;
    for (int i = allDepth - 1; i >= 0; i--) {
      className = stackTrace[i].getClassName();
      if (className.startsWith(XLOG_STACK_TRACE_ORIGIN)
          || (stackTraceOrigin != null && className.startsWith(stackTraceOrigin))) {
        ignoreDepth = i + 1;
        break;
      }
    }
    int realDepth = allDepth - ignoreDepth;
    StackTraceElement[] realStack = new StackTraceElement[realDepth];
    System.arraycopy(stackTrace, ignoreDepth, realStack, 0, realDepth);
    return realStack;
  }

  /**
   * Crop the stack trace with a max depth.
   *
   * @param callStack the original stack trace
   * @param maxDepth  the max depth of real stack trace that will be cropped,
   *                  0 means no limitation
   * @return the cropped stack trace
   */
  private static StackTraceElement[] cropStackTrace(StackTraceElement[] callStack,
                                                    int maxDepth) {
    int realDepth = callStack.length;
    if (maxDepth > 0) {
      realDepth = Math.min(maxDepth, realDepth);
    }
    StackTraceElement[] realStack = new StackTraceElement[realDepth];
    System.arraycopy(callStack, 0, realStack, 0, realDepth);
    return realStack;
  }
}
