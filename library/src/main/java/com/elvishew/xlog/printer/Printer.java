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

import com.elvishew.xlog.printer.file.FilePrinter;

/**
 * A printer is used for printing the log to somewhere, like android shell, terminal
 * or file system.
 * <p>
 * There are 4 main implementation of Printer.
 * <br>{@link AndroidPrinter}, print log to android shell terminal.
 * <br>{@link ConsolePrinter}, print log to console via System.out.
 * <br>{@link FilePrinter}, print log to file system.
 * <br>{@link RemotePrinter}, print log to remote server, this is empty implementation yet.
 */
public interface Printer {

  /**
   * Print log in new line.
   *
   * @param logLevel the level of log
   * @param tag      the tag of log
   * @param msg      the msg of log
   */
  void println(int logLevel, String tag, String msg);
}
