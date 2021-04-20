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

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogItem;

/**
 * Interceptors are used to intercept every log after formatting message, thread info and
 * stack trace info, and before printing, normally we can modify or drop the log.
 * <p>
 * Remember interceptors are ordered, which means earlier added interceptor will get the log
 * first.
 * <p>
 * If any interceptor remove the log(by returning null when {@link #intercept(LogItem)}),
 * then the interceptors behind that one won't receive the log, and the log won't be printed at all.
 *
 * @see LogConfiguration.Builder#addInterceptor(Interceptor)
 * @see com.elvishew.xlog.XLog#addInterceptor(Interceptor)
 * @since 1.3.0
 */
public interface Interceptor {

  /**
   * Intercept the log.
   *
   * @param log the original log
   * @return the modified log, or null if the log should not be printed
   */
  LogItem intercept(LogItem log);
}
