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

package com.elvishew.xlog.printer.file.clean;

import java.io.File;

/**
 * Limit the file life of a max time.
 */
public class TimeCleanStrategy implements CleanStrategy {

  private long maxTimeMillis;

  /**
   * Constructor.
   *
   * @param maxTimeMillis the max time the file can keep
   */
  public TimeCleanStrategy(long maxTimeMillis) {
    this.maxTimeMillis = maxTimeMillis;
  }

  @Override
  public boolean shouldClean(File file) {
    long currentTimeMillis = System.currentTimeMillis();
    long fileTimeMillis = file.lastModified();
    return (Math.abs(currentTimeMillis - fileTimeMillis) > maxTimeMillis);
  }
}
