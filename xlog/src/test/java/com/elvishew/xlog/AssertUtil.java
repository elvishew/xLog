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

import java.util.List;

import static org.junit.Assert.assertTrue;

public class AssertUtil {

  public static void assertHasLog(List<LogItem> logsContainer, LogItem target) {
    boolean result = false;
    for (LogItem log : logsContainer) {
      if ((log.level == target.level)
          && log.tag.equals(target.tag)
          && log.msg.equals(target.msg)) {
        result = true;
      }
    }
    assertTrue("Missing log: " + target, result);
  }

  public static void assertHasLog(List<LogItem> logsContainer, String msg) {
    boolean result = false;
    for (LogItem log : logsContainer) {
      if (log.msg.equals(msg)) {
        result = true;
      }
    }
    assertTrue("Missing log: " + msg, result);
  }

  public static void assertHasLog(List<LogItem> logsContainer, int position, String msg) {
    boolean result = false;
    if (logsContainer.size() > position
        && logsContainer.get(position) != null
        && logsContainer.get(position).msg.equals(msg)) {
      result = true;
    }
    assertTrue("Missing log: " + msg, result);
  }

  public static void assertNoLog(List<LogItem> logsContainer) {
    boolean result = logsContainer.size() == 0;
    assertTrue("Unexpected log found", result);
  }
}
