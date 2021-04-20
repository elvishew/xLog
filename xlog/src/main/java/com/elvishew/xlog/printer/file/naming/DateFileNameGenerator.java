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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Generate file name according to the timestamp, different dates will lead to different file names.
 */
public class DateFileNameGenerator implements FileNameGenerator {

  ThreadLocal<SimpleDateFormat> mLocalDateFormat = new ThreadLocal<SimpleDateFormat>() {

    @Override
    protected SimpleDateFormat initialValue() {
      return new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    }
  };

  @Override
  public boolean isFileNameChangeable() {
    return true;
  }

  /**
   * Generate a file name which represent a specific date.
   */
  @Override
  public String generateFileName(int logLevel, long timestamp) {
    SimpleDateFormat sdf = mLocalDateFormat.get();
    sdf.setTimeZone(TimeZone.getDefault());
    return sdf.format(new Date(timestamp));
  }
}
