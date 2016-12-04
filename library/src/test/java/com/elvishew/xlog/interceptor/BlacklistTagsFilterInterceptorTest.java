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

package com.elvishew.xlog.interceptor;

import com.elvishew.xlog.LogItem;
import com.elvishew.xlog.LogLevel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BlacklistTagsFilterInterceptorTest {

  private BlacklistTagsFilterInterceptor interceptor;

  @Before
  public void setup() {
    interceptor = new BlacklistTagsFilterInterceptor("abc", "def");
  }

  @Test
  public void testBlacklist() throws Exception {
    assertTagRejected("abc");
    assertTagRejected("def");

    assertTagAccepted("");
    assertTagAccepted("ab");
    assertTagAccepted("abcd");
    assertTagAccepted("bcd");
    assertTagAccepted("abcdef");
    assertTagAccepted("defg");
    assertTagAccepted("ef");
  }

  private void assertTagAccepted(String tag) {
    LogItem log = new LogItem(LogLevel.DEBUG, tag, "Message");
    assertNotNull("Tag " + log.tag + " should be accepted", interceptor.intercept(log));
  }

  private void assertTagRejected(String tag) {
    LogItem log = new LogItem(LogLevel.DEBUG, tag, "Message");
    assertNull("Tag " + log.tag + " should be rejected", interceptor.intercept(log));
  }
}