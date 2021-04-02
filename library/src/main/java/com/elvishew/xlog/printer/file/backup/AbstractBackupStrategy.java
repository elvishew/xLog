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

/**
 * An abstract {@link BackupStrategy2}, simply append '.bak.n' to the end of normal file name when
 * naming a backup with index n.
 * <p>
 * Developers can simply extend this class when defining their own {@link BackupStrategy2}.
 *
 * @since 1.9.0
 */
public abstract class AbstractBackupStrategy implements BackupStrategy2 {

  @Override
  public String getBackupFileName(String fileName, int backupIndex) {
    return fileName + ".bak." + backupIndex;
  }
}
