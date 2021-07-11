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

package com.elvishew.xlog.printer.file.writer;

import java.io.File;

/**
 * A writer is used to write log into log file.
 * <p>
 * Used in worker thread.
 *
 * @since 1.11.0
 */
public abstract class Writer {

  /**
   * Open a specific log file for future writing, if it doesn't exist yet, just create it.
   *
   * @param file the specific log file, may not exist
   * @return true if the log file is successfully opened, false otherwise
   */
  public abstract boolean open(File file);

  /**
   * Whether a log file is successfully opened in previous {@link #open(File)}.
   *
   * @return true if log file is opened, false otherwise
   */
  public abstract boolean isOpened();

  /**
   * Get the opened log file.
   *
   * @return the opened log file, or null if log file not opened
   */
  public abstract File getOpenedFile();

  /**
   * Get the name of opened log file.
   *
   * @return the name of opened log file, or null if log file not opened
   */
  public abstract String getOpenedFileName();

  /**
   * Append the log to the end of the opened log file, normally an extra line separator is needed.
   *
   * @param log the log to append
   */
  public abstract void appendLog(String log);

  /**
   * Make sure the opened log file is closed, normally called before switching the log file.
   *
   * @return true if the log file is successfully closed, false otherwise
   */
  public abstract boolean close();
}
