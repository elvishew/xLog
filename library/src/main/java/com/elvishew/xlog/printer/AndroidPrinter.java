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

package com.elvishew.xlog.printer;

/**
 * Log {@link Printer} using {@link android.util.Log}.
 */
public class AndroidPrinter implements Printer {

  /**
   * Generally, android has a default length limit of 4096 for single log, but
   * some device(like HUAWEI) has its own shorter limit, so we just use 2048
   * and wish it could run well in all devices.
   */
  static final int DEFAULT_MAX_CHUNK_SIZE = 2048;

  private int maxChunkSize;

  /**
   * Constructor.
   * <p>
   * If single message is too long, it will be separated to several chunks automatically, the max
   * size of each chunk default to be {@value #DEFAULT_MAX_CHUNK_SIZE}, you can specify the
   * maxChunkSize using {@link #AndroidPrinter(int)}.
   */
  public AndroidPrinter() {
    this(DEFAULT_MAX_CHUNK_SIZE);
  }

  /**
   * Constructor.
   *
   * @param maxChunkSize the max size of each chunk. If the message is too long, it will be
   *                     separated to several chunks automatically
   * @since 1.4.1
   */
  public AndroidPrinter(int maxChunkSize) {
    this.maxChunkSize = maxChunkSize;
  }

  @Override
  public void println(int logLevel, String tag, String msg) {
    if (msg.length() <= maxChunkSize) {
      printChunk(logLevel, tag, msg);
      return;
    }

    int msgLength = msg.length();
    int start = 0;
    int end;
    while (start < msgLength) {
      end = adjustEnd(msg, start, Math.min(start + maxChunkSize, msgLength));
      printChunk(logLevel, tag, msg.substring(start, end));

      start = end;
    }
  }

  /**
   * Move the end to the nearest line separator('\n') (if exist).
   */
  static int adjustEnd(String msg, int start, int originEnd) {
    if (originEnd == msg.length()) {
      // Already end of message.
      return originEnd;
    }
    if (msg.charAt(originEnd) == '\n') {
      // Already prior to '\n'.
      return originEnd;
    }
    // Search back for '\n'.
    int last = originEnd - 1;
    while (start < last) {
      if (msg.charAt(last) == '\n') {
        return last + 1;
      }
      last--;
    }
    return originEnd;
  }

  /**
   * Print single chunk of log in new line.
   *
   * @param logLevel the level of log
   * @param tag      the tag of log
   * @param msg      the msg of log
   */
  void printChunk(int logLevel, String tag, String msg) {
    android.util.Log.println(logLevel, tag, msg);
  }
}
