/*
 * Copyright 2016 Elvis Hew
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

package com.elvishew.xlog;

/**
 * Represent a single log going to be printed.
 *
 * @since 1.3.0
 */
public class LogItem {

  /**
   * Level of the log.
   *
   * @see LogLevel
   */
  public int level;

  /**
   * The tag, should not be null.
   */
  public String tag;

  /**
   * The formatted message, should not be null.
   */
  public String msg;

  /**
   * The formatted thread info, null if thread info is disabled.
   *
   * @see LogConfiguration.Builder#t()
   * @see LogConfiguration.Builder#nt()
   */
  public String threadInfo;

  /**
   * The formatted stack trace info, null if stack trace info is disabled.
   *
   * @see LogConfiguration.Builder#st(int)
   * @see LogConfiguration.Builder#nst()
   */
  public String stackTraceInfo;

  public LogItem(int level, String tag, String msg) {
    this.level = level;
    this.tag = tag;
    this.msg = msg;
  }

  public LogItem(int level, String tag, String threadInfo, String stackTraceInfo, String msg) {
    this.level = level;
    this.tag = tag;
    this.threadInfo = threadInfo;
    this.stackTraceInfo = stackTraceInfo;
    this.msg = msg;
  }
}
