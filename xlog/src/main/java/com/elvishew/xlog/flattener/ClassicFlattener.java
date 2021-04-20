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

package com.elvishew.xlog.flattener;

import com.elvishew.xlog.LogLevel;

/**
 * The classic flattener, flatten the log with pattern "{@value #DEFAULT_PATTERN}".
 * <p>
 * Imagine there is a log, with {@link LogLevel#DEBUG} level, "my_tag" tag and "Simple message"
 * message, the flattened log would be: "2016-11-30 13:00:00.000 D/my_tag: Simple message"
 *
 * @since 1.3.0
 */
public class ClassicFlattener extends PatternFlattener {

  private static final String DEFAULT_PATTERN = "{d} {l}/{t}: {m}";

  public ClassicFlattener() {
    super(DEFAULT_PATTERN);
  }
}
