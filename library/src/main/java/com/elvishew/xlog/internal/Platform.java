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
import android.os.Build;

import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.ConsolePrinter;
import com.elvishew.xlog.printer.Printer;

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

  public void warn(String msg) {
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
    public void warn(String msg) {
      android.util.Log.w("XLog", msg);
    }
  }
}
