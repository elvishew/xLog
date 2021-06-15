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

package com.elvishew.xlog.internal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.elvishew.xlog.formatter.message.object.BundleFormatter;
import com.elvishew.xlog.formatter.message.object.IntentFormatter;
import com.elvishew.xlog.formatter.message.object.ObjectFormatter;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.ConsolePrinter;
import com.elvishew.xlog.printer.Printer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Platform {

  private static final Platform PLATFORM = findPlatform();

  public static Platform get() {
    return PLATFORM;
  }

  @SuppressLint("NewApi")
  String lineSeparator() {
    return System.lineSeparator();
  }

  Printer defaultPrinter() {
    return new ConsolePrinter();
  }

  Map<Class<?>, ObjectFormatter<?>> builtinObjectFormatters() {
    return Collections.emptyMap();
  }

  public void warn(String msg) {
    System.out.println(msg);
  }

  public void error(String msg) {
    System.out.println(msg);
  }

  private static Platform findPlatform() {
    try {
      Class.forName("android.os.Build");
      if (Build.VERSION.SDK_INT != 0) {
        return new Android();
      }
    } catch (ClassNotFoundException ignored) {
    }
    return new Platform();
  }

  static class Android extends Platform {

    private static final Map<Class<?>, ObjectFormatter<?>> BUILTIN_OBJECT_FORMATTERS;

    static {
      Map<Class<?>, ObjectFormatter<?>> objectFormatters = new HashMap<>();
      objectFormatters.put(Bundle.class, new BundleFormatter());
      objectFormatters.put(Intent.class, new IntentFormatter());
      BUILTIN_OBJECT_FORMATTERS = Collections.unmodifiableMap(objectFormatters);
    }

    @Override
    String lineSeparator() {
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return "\n";
      }
      return System.lineSeparator();
    }

    @Override
    Printer defaultPrinter() {
      return new AndroidPrinter();
    }

    @Override
    Map<Class<?>, ObjectFormatter<?>> builtinObjectFormatters() {
      return BUILTIN_OBJECT_FORMATTERS;
    }

    @Override
    public void warn(String msg) {
      android.util.Log.w("XLog", msg);
    }

    @Override
    public void error(String msg) {
      android.util.Log.e("XLog", msg);
    }
  }
}
