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

package com.elvishew.xlog.printer;

import com.elvishew.xlog.AssertUtil;
import com.elvishew.xlog.LogItem;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.RandomUtil;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.XLogUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AndroidPrinterTest {

  List<LogItem> logContainer = new ArrayList<>();

  @Before
  public void setup() {
    XLogUtil.beforeTest();
    XLog.init(LogLevel.ALL, new AndroidPrinter() {
      @Override
      void printChunk(int logLevel, String tag, String msg) {
        logContainer.add(new LogItem(logLevel, tag, msg));
      }
    });
  }

  @Test
  public void testPrintShortMessage() throws Exception {
    String msg = "This is a short message";
    XLog.d(msg);
    assertEquals(1, logContainer.size());
    AssertUtil.assertHasLog(logContainer, msg);
  }

  @Test
  public void testPrint4kMessage() throws Exception {
    int length = AndroidPrinter.DEFAULT_MAX_CHUNK_SIZE;
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(RandomUtil.randomAsciiChar());
    }
    String msg = sb.toString();
    XLog.d(msg);
    assertEquals(1, logContainer.size());
    AssertUtil.assertHasLog(logContainer, msg);
  }

  @Test
  public void testPrintLongMessage() throws Exception {
    int messageChunkLength = AndroidPrinter.DEFAULT_MAX_CHUNK_SIZE;
    int length = (int) (3.6 * messageChunkLength);
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(RandomUtil.randomAsciiChar());
    }
    String msg = sb.toString();
    XLog.d(msg);
    assertEquals(4, logContainer.size());

    int start = 0;
    int end = 0;
    for (int i = 0; end < length; i++) {
      end = AndroidPrinter.adjustEnd(msg, start, Math.min(start + messageChunkLength, length));
      String chunk = msg.substring(start, end);
      AssertUtil.assertHasLog(logContainer, i, chunk);

      start = end;
    }
  }
}