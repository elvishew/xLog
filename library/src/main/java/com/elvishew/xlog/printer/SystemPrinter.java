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

import com.elvishew.xlog.formatter.DefaultFormatterFactory;
import com.elvishew.xlog.formatter.log.LogFormatter;

public class SystemPrinter extends MessageFormattedPrinter {

    /**
     * The log formatter when print a log.
     */
    private LogFormatter logFormatter;

    /**
     * Constructor.
     */
    public SystemPrinter() {
        this.logFormatter = DefaultFormatterFactory.createLogFormatter();
    }

    /**
     * Constructor.
     *
     * @param logFormatter the log formatter when print a log
     */
    public SystemPrinter(LogFormatter logFormatter) {
        this.logFormatter = logFormatter;
    }

    @Override
    protected void onPrintFormattedMessage(int logLevel, String tag, String msg) {
        String formattedLog = logFormatter.format(logLevel, tag, msg, System.currentTimeMillis());
        System.out.println(formattedLog);
    }
}
