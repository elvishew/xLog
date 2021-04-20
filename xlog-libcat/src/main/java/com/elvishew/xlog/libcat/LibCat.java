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

package com.elvishew.xlog.libcat;

import com.elvishew.xlog.libcat.internal.Cat;
import com.elvishew.xlog.printer.Printer;

/**
 * Intercept the origin log logged by {@link android.util.Log}, and pass it to specific {@link Printer}.
 * <p>
 * Call {@link #config(boolean, Printer)} to config LibCat when initializing app.
 * <p>
 * Please note that LibCat only work after you apply the 'android-aspectjx' plugin in your app's 'build.gradle'.
 */
public class LibCat {

  /**
   * Config LibCat.
   *
   * @param keepOriginLog whether the origin log logged by {@link android.util.Log} should be kept,
   *                      which means you can still see them in 'logcat', default to be true
   * @param output        specify a {@link Printer} to print the intercepted logs, can be null if
   *                      there is no need to print the logs to other place
   */
  public static void config(boolean keepOriginLog, Printer output) {
    Cat.keepOriginLog = keepOriginLog;
    Cat.output = output;
  }
}
