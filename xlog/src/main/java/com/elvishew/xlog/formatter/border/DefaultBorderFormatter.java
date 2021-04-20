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

package com.elvishew.xlog.formatter.border;

import com.elvishew.xlog.internal.SystemCompat;

/**
 * String segments wrapped with borders look like:
 * <br>╔════════════════════════════════════════════════════════════════════════════
 * <br>║String segment 1
 * <br>╟────────────────────────────────────────────────────────────────────────────
 * <br>║String segment 2
 * <br>╟────────────────────────────────────────────────────────────────────────────
 * <br>║String segment 3
 * <br>╚════════════════════════════════════════════════════════════════════════════
 */
public class DefaultBorderFormatter implements BorderFormatter {

  private static final char VERTICAL_BORDER_CHAR = '║';

  // Length: 100.
  private static final String TOP_HORIZONTAL_BORDER =
      "╔═════════════════════════════════════════════════" +
          "══════════════════════════════════════════════════";

  // Length: 99.
  private static final String DIVIDER_HORIZONTAL_BORDER =
      "╟─────────────────────────────────────────────────" +
          "──────────────────────────────────────────────────";

  // Length: 100.
  private static final String BOTTOM_HORIZONTAL_BORDER =
      "╚═════════════════════════════════════════════════" +
          "══════════════════════════════════════════════════";

  @Override
  public String format(String[] segments) {
    if (segments == null || segments.length == 0) {
      return "";
    }

    String[] nonNullSegments = new String[segments.length];
    int nonNullCount = 0;
    for (String segment : segments) {
      if (segment != null) {
        nonNullSegments[nonNullCount++] = segment;
      }
    }
    if (nonNullCount == 0) {
      return "";
    }

    StringBuilder msgBuilder = new StringBuilder();
    msgBuilder.append(TOP_HORIZONTAL_BORDER).append(SystemCompat.lineSeparator);
    for (int i = 0; i < nonNullCount; i++) {
      msgBuilder.append(appendVerticalBorder(nonNullSegments[i]));
      if (i != nonNullCount - 1) {
        msgBuilder.append(SystemCompat.lineSeparator).append(DIVIDER_HORIZONTAL_BORDER)
            .append(SystemCompat.lineSeparator);
      } else {
        msgBuilder.append(SystemCompat.lineSeparator).append(BOTTOM_HORIZONTAL_BORDER);
      }
    }
    return msgBuilder.toString();
  }

  /**
   * Add {@value #VERTICAL_BORDER_CHAR} to each line of msg.
   *
   * @param msg the message to add border
   * @return the message with {@value #VERTICAL_BORDER_CHAR} in the start of each line
   */
  private static String appendVerticalBorder(String msg) {
    StringBuilder borderedMsgBuilder = new StringBuilder(msg.length() + 10);
    String[] lines = msg.split(SystemCompat.lineSeparator);
    for (int i = 0, N = lines.length; i < N; i++) {
      if (i != 0) {
        borderedMsgBuilder.append(SystemCompat.lineSeparator);
      }
      String line = lines[i];
      borderedMsgBuilder.append(VERTICAL_BORDER_CHAR).append(line);
    }
    return borderedMsgBuilder.toString();
  }
}
