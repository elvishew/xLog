/*
 * Copyright 2021 Elvis Hew
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

package com.elvishew.xlog.libcat.internal;

import com.elvishew.xlog.printer.Printer;

/**
 * Intercept the origin log logged by {@link android.util.Log}, and pass it to specific {@link Printer}.
 */
public class Cat {

  public static boolean keepOriginLog = true;

  public static Printer output;

  private Cat() {
  }

  public static int v(String tag, String msg) {
    return println(android.util.Log.VERBOSE, tag, msg);
  }

  public static int v(String tag, String msg, Throwable tr) {
    return println(android.util.Log.VERBOSE, tag, msg + '\n' + android.util.Log.getStackTraceString(tr));
  }

  public static int d(String tag, String msg) {
    return println(android.util.Log.DEBUG, tag, msg);
  }

  public static int d(String tag, String msg, Throwable tr) {
    return println(android.util.Log.DEBUG, tag, msg + '\n' + android.util.Log.getStackTraceString(tr));
  }

  public static int i(String tag, String msg) {
    return println(android.util.Log.INFO, tag, msg);
  }

  public static int i(String tag, String msg, Throwable tr) {
    return println(android.util.Log.INFO, tag, msg + '\n' + android.util.Log.getStackTraceString(tr));
  }

  public static int w(String tag, String msg) {
    return println(android.util.Log.WARN, tag, msg);
  }

  public static int w(String tag, String msg, Throwable tr) {
    return println(android.util.Log.WARN, tag, msg + '\n' + android.util.Log.getStackTraceString(tr));
  }

  public static int w(String tag, Throwable tr) {
    return println(android.util.Log.WARN, tag, android.util.Log.getStackTraceString(tr));
  }

  public static int e(String tag, String msg) {
    return println(android.util.Log.ERROR, tag, msg);
  }

  public static int e(String tag, String msg, Throwable tr) {
    return println(android.util.Log.ERROR, tag, msg + '\n' + android.util.Log.getStackTraceString(tr));
  }

  public static int println(int priority, String tag, String msg) {
    int ret = 0;
    if (keepOriginLog) {
      ret = android.util.Log.println(priority, tag, msg);
    }
    if (output != null) {
      output.println(priority, tag, msg);
    }
    return ret;
  }
}
