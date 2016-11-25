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

package com.elvishew.xlog.printer;

/**
 * Log {@link Printer} which should print the log to remote server.
 * <p>
 * This is just a empty implementation telling you that you can do
 * such thing, you can override {@link #println(int, String, String)} )} and sending the log by your
 * implementation.
 */
public class RemotePrinter implements Printer {

  @Override
  public void println(int logLevel, String tag, String msg) {
    // TODO: Send the log to your server.
  }
}
