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

package com.elvishew.xlog.formatter.stacktrace;

import com.elvishew.xlog.internal.SystemCompat;

/**
 * Formatted stack trace looks like:
 * <br>	├ com.elvishew.xlog.SampleClassC.sampleMethodC(SampleClassC.java:200)
 * <br>	├ com.elvishew.xlog.SampleClassB.sampleMethodB(SampleClassB.java:100)
 * <br>	└ com.elvishew.xlog.SampleClassA.sampleMethodA(SampleClassA.java:50)
 */
public class DefaultStackTraceFormatter implements StackTraceFormatter {

  @Override
  public String format(StackTraceElement[] stackTrace) {
    StringBuilder sb = new StringBuilder(256);
    if (stackTrace == null || stackTrace.length == 0) {
      return null;
    } else if (stackTrace.length == 1) {
      return "\t─ " + stackTrace[0].toString();
    } else {
      for (int i = 0, N = stackTrace.length; i < N; i++) {
        if (i != N - 1) {
          sb.append("\t├ ");
          sb.append(stackTrace[i].toString());
          sb.append(SystemCompat.lineSeparator);
        } else {
          sb.append("\t└ ");
          sb.append(stackTrace[i].toString());
        }
      }
      return sb.toString();
    }
  }
}
