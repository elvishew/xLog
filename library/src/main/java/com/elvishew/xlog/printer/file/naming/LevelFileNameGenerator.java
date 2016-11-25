/*
 * Copyright 2015 Elvis Hew
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

package com.elvishew.xlog.printer.file.naming;

import com.elvishew.xlog.LogLevel;

/**
 * Generate file name according to the log level, different levels lead to different file names.
 */
public class LevelFileNameGenerator implements FileNameGenerator {

  @Override
  public boolean isFileNameChangeable() {
    return true;
  }

  /**
   * Generate a file name which represent a specific log level.
   */
  @Override
  public String generateFileName(int logLevel, long timestamp) {
    return LogLevel.getLevelName(logLevel);
  }
}
