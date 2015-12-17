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

package com.elvishew.xlog.formatter.log;

/**
 * The log formatter used to format the final log string according to the log level, tag,
 * message and timestamp.
 */
public interface LogFormatter {

    /**
     * Format the log.
     *
     * @param logLevel  the level of log
     * @param tag       the tag of log
     * @param message   the message of log
     * @param timestamp the timestamp when the logging happen
     * @return the formatted final log string
     */
    String format(int logLevel, String tag, String message, long timestamp);
}
