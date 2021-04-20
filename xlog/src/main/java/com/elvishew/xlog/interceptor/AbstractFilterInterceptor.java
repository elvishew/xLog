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

/**
 * An filter interceptor is used to filter some specific logs out, this filtered logs won't be
 * printed by any printer.
 *
 * @since 1.3.0
 */
public abstract class AbstractFilterInterceptor implements Interceptor {

  /**
   * {@inheritDoc}
   *
   * @param log the original log
   * @return the original log if it is acceptable, or null if it should be filtered out
   */
  @Override
  public LogItem intercept(LogItem log) {
    if (reject(log)) {
      // Filter this log out.
      return null;
    }
    return log;
  }

  /**
   * Whether specific log should be filtered out.
   *
   * @param log the specific log
   * @return true if the log should be filtered out, false otherwise
   */
  protected abstract boolean reject(LogItem log);
}
