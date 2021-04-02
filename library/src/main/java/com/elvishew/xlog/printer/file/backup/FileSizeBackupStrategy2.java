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

package com.elvishew.xlog.printer.file.backup;

import java.io.File;

/**
 * Limit the file size of a max length.
 *
 * @since 1.9.0
 */
public class FileSizeBackupStrategy2 extends AbstractBackupStrategy {

  private long maxSize;

  private int maxBackupIndex;

  /**
   * Constructor.
   *
   * @param maxSize        the max size the file can reach
   * @param maxBackupIndex the max backup index, or {@link #NO_LIMIT}, see {@link #getMaxBackupIndex()}
   */
  public FileSizeBackupStrategy2(long maxSize, int maxBackupIndex) {
    this.maxSize = maxSize;
    this.maxBackupIndex = maxBackupIndex;
  }

  @Override
  public boolean shouldBackup(File file) {
    return file.length() > maxSize;
  }

  @Override
  public int getMaxBackupIndex() {
    return maxBackupIndex;
  }
}
