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

package com.elvishew.xlog.internal.printer.file.backup;

import com.elvishew.xlog.printer.file.backup.BackupStrategy2;

import java.io.File;

public class BackupUtil {

  /**
   * Shift existed backups if needed, and backup the logging file.
   *
   * @param loggingFile    the logging file
   * @param backupStrategy the strategy should be use when backing up
   */
  public static void backup(File loggingFile, BackupStrategy2 backupStrategy) {
    String loggingFileName = loggingFile.getName();
    String path = loggingFile.getParent();
    File backupFile;
    File nextBackupFile;
    int maxBackupIndex = backupStrategy.getMaxBackupIndex();
    if (maxBackupIndex > 0) {
      backupFile = new File(path, backupStrategy.getBackupFileName(loggingFileName, maxBackupIndex));
      if (backupFile.exists()) {
        backupFile.delete();
      }
      for (int i = maxBackupIndex - 1; i > 0; i--) {
        backupFile = new File(path, backupStrategy.getBackupFileName(loggingFileName, i));
        if (backupFile.exists()) {
          nextBackupFile = new File(path, backupStrategy.getBackupFileName(loggingFileName, i + 1));
          backupFile.renameTo(nextBackupFile);
        }
      }
      nextBackupFile = new File(path, backupStrategy.getBackupFileName(loggingFileName, 1));
      loggingFile.renameTo(nextBackupFile);
    } else if (maxBackupIndex == BackupStrategy2.NO_LIMIT) {
      for (int i = 1; i < Integer.MAX_VALUE; i++) {
        nextBackupFile = new File(path, backupStrategy.getBackupFileName(loggingFileName, i));
        if (!nextBackupFile.exists()) {
          loggingFile.renameTo(nextBackupFile);
          break;
        }
      }
    } else {
      // Illegal maxBackIndex, could not come here.
    }
  }

  /**
   * Check if a {@link BackupStrategy2} is valid, will throw a exception if invalid.
   *
   * @param backupStrategy the backup strategy to be verify
   */
  public static void verifyBackupStrategy(BackupStrategy2 backupStrategy) {
    int maxBackupIndex = backupStrategy.getMaxBackupIndex();
    if (maxBackupIndex < 0) {
      throw new IllegalArgumentException("Max backup index should not be less than 0");
    } else if (maxBackupIndex == Integer.MAX_VALUE) {
      throw new IllegalArgumentException("Max backup index too big: " + maxBackupIndex);
    }
  }
}
