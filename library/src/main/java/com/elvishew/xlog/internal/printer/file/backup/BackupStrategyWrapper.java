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

import com.elvishew.xlog.printer.file.backup.BackupStrategy;
import com.elvishew.xlog.printer.file.backup.BackupStrategy2;

import java.io.File;

/**
 * Wrap a {@link BackupStrategy} to fit the {@link BackupStrategy2} interface, and perform like
 * a {@link BackupStrategy} with the old logic before v1.9.0.
 */
public class BackupStrategyWrapper implements BackupStrategy2 {

  private BackupStrategy backupStrategy;

  public BackupStrategyWrapper(BackupStrategy backupStrategy) {
    this.backupStrategy = backupStrategy;
  }

  @Override
  public int getMaxBackupIndex() {
    return 1;
  }

  @Override
  public String getBackupFileName(String fileName, int backupIndex) {
    return fileName + ".bak";
  }

  @Override
  public boolean shouldBackup(File file) {
    return backupStrategy.shouldBackup(file);
  }
}
