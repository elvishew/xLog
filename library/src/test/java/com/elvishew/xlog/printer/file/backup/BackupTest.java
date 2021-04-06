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

import com.elvishew.xlog.internal.printer.file.backup.BackupStrategyWrapper;
import com.elvishew.xlog.internal.printer.file.backup.BackupUtil;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class BackupTest {

  String logPath = "build/test/log";

  String logFileName = "log";

  @Before
  public void setup() throws IOException {
    // Clean log folder.
    File folder = new File(logPath);
    File[] files = folder.listFiles();
    if (files != null) {
      for (File file : files) {
        file.delete();
      }
    }

    // Ensure folder exist.
    if (!folder.exists()) {
      folder.mkdirs();
    }
  }

  @Test
  public void testBackupOld() throws Exception {
    BackupStrategy2 backupStrategy = new BackupStrategyWrapper(new BackupStrategy() {
      @Override
      public boolean shouldBackup(File file) {
        return true;
      }
    });

    File logFile = new File(logPath, logFileName);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(1, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(1, backupStrategy);
  }

  @Test
  public void testBackupMaxIndex1() throws Exception {
    BackupStrategy2 backupStrategy = new AbstractBackupStrategy() {
      @Override
      public int getMaxBackupIndex() {
        return 1;
      }

      @Override
      public boolean shouldBackup(File file) {
        return true;
      }
    };

    File logFile = new File(logPath, logFileName);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(1, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(1, backupStrategy);
  }

  @Test
  public void testBackupMaxIndex2() throws Exception {
    BackupStrategy2 backupStrategy = new AbstractBackupStrategy() {
      @Override
      public int getMaxBackupIndex() {
        return 2;
      }

      @Override
      public boolean shouldBackup(File file) {
        return true;
      }
    };

    File logFile = new File(logPath, logFileName);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(1, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(2, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(2, backupStrategy);
  }

  @Test
  public void testBackupMaxIndex5() throws Exception {
    BackupStrategy2 backupStrategy = new AbstractBackupStrategy() {
      @Override
      public int getMaxBackupIndex() {
        return 5;
      }

      @Override
      public boolean shouldBackup(File file) {
        return true;
      }
    };

    File logFile = new File(logPath, logFileName);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(1, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(2, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(3, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(4, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(5, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(5, backupStrategy);
  }

  @Test
  public void testBackupMaxIndex5WithMissingFile() throws Exception {
    BackupStrategy2 backupStrategy = new AbstractBackupStrategy() {
      @Override
      public int getMaxBackupIndex() {
        return 5;
      }

      @Override
      public boolean shouldBackup(File file) {
        return true;
      }
    };

    File logFile = new File(logPath, logFileName);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(1, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(2, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(3, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(4, backupStrategy);

    File file2 = new File(logPath, backupStrategy.getBackupFileName(logFileName, 2));
    file2.delete();

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFilesCount(4);
    assertFileExists(backupStrategy, 1);
    assertFileExists(backupStrategy, 2);
    assertFileExists(backupStrategy, 4);
    assertFileExists(backupStrategy, 5);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFilesCount(4);
    assertFileExists(backupStrategy, 1);
    assertFileExists(backupStrategy, 2);
    assertFileExists(backupStrategy, 3);
    assertFileExists(backupStrategy, 5);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFilesCount(4);
    assertFileExists(backupStrategy, 1);
    assertFileExists(backupStrategy, 2);
    assertFileExists(backupStrategy, 3);
    assertFileExists(backupStrategy, 4);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFilesCount(5);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFilesCount(5);
  }

  @Test
  public void testBackupMaxIndexNoLimit() throws Exception {
    BackupStrategy2 backupStrategy = new AbstractBackupStrategy() {
      @Override
      public int getMaxBackupIndex() {
        return NO_LIMIT;
      }

      @Override
      public boolean shouldBackup(File file) {
        return true;
      }
    };

    File logFile = new File(logPath, logFileName);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(1, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(2, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(3, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(4, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(5, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(6, backupStrategy);
  }

  @Test
  public void testBackupMaxIndexNoLimitWithMissingFile() throws Exception {
    BackupStrategy2 backupStrategy = new AbstractBackupStrategy() {
      @Override
      public int getMaxBackupIndex() {
        return NO_LIMIT;
      }

      @Override
      public boolean shouldBackup(File file) {
        return true;
      }
    };

    File logFile = new File(logPath, logFileName);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(1, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(2, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(3, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(4, backupStrategy);

    File file2 = new File(logPath, backupStrategy.getBackupFileName(logFileName, 2));
    file2.delete();

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(4, backupStrategy);

    File file1 = new File(logPath, backupStrategy.getBackupFileName(logFileName, 1));
    file1.delete();
    File file3 = new File(logPath, backupStrategy.getBackupFileName(logFileName, 3));
    file3.delete();

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFilesCount(3);
    assertFileExists(backupStrategy, 1);
    assertFileExists(backupStrategy, 2);
    assertFileExists(backupStrategy, 4);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFiles(4, backupStrategy);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFilesCount(5);

    logFile.createNewFile();
    BackupUtil.backup(logFile, backupStrategy);
    assertFilesCount(6);
  }

  private void assertFiles(int fileCount, BackupStrategy2 backupStrategy) {
    assertFilesCount(fileCount);
    for (int i = 1; i <= fileCount; i++) {
      assertFileExists(backupStrategy, i);
    }
  }

  private void assertFilesCount(int filesCount) {
    File folder = new File(logPath);
    File[] files = folder.listFiles();
    assert files != null;
    assertEquals(filesCount, files.length);
  }

  private void assertFileExists(BackupStrategy2 backupStrategy, int index) {
    File file = new File(logPath, backupStrategy.getBackupFileName(logFileName, index));
    assert file.exists();
  }
}