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

import com.elvishew.xlog.internal.Platform;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * A simple implementation of {@link Writer}.
 * <p>
 * Subclass can override {@link #onNewFileCreated(File)} and do some initialization work to the new
 * file, such as calling {@link #appendLog(String)} to add a file header.
 *
 * @since 1.11.0
 */
public class SimpleWriter extends Writer {

  /**
   * The name of opened log file.
   */
  private String logFileName;

  /**
   * The opened log file.
   */
  private File logFile;

  private BufferedWriter bufferedWriter;

  @Override
  public boolean open(File file) {
    logFileName = file.getName();
    logFile = file;

    boolean isNewFile = false;

    // Create log file if not exists.
    if (!logFile.exists()) {
      try {
        File parent = logFile.getParentFile();
        if (!parent.exists()) {
          parent.mkdirs();
        }
        logFile.createNewFile();
        isNewFile = true;
      } catch (Exception e) {
        e.printStackTrace();
        close();
        return false;
      }
    }

    // Create buffered writer.
    try {
      bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));
      if (isNewFile) {
        onNewFileCreated(logFile);
      }
    } catch (Exception e) {
      e.printStackTrace();
      close();
      return false;
    }
    return true;
  }

  @Override
  public boolean isOpened() {
    return bufferedWriter != null && logFile.exists();
  }

  @Override
  public File getOpenedFile() {
    return logFile;
  }

  @Override
  public String getOpenedFileName() {
    return logFileName;
  }

  /**
   * Called after a log file is newly created.
   * <p>
   * You can do some initialization work to the new file, such as calling {@link #appendLog(String)}
   * to add a file header.
   * <p>
   * Called in worker thread.
   *
   * @param file the newly created log file
   */
  public void onNewFileCreated(File file) {
  }

  @Override
  public void appendLog(String log) {
    try {
      bufferedWriter.write(log);
      bufferedWriter.newLine();
      bufferedWriter.flush();
    } catch (Exception e) {
      Platform.get().warn("append log failed: " + e.getMessage());
    }
  }

  @Override
  public boolean close() {
    if (bufferedWriter != null) {
      try {
        bufferedWriter.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    bufferedWriter = null;
    logFileName = null;
    logFile = null;
    return true;
  }
}
