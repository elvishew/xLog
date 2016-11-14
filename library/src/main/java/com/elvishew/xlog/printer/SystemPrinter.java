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

import com.elvishew.xlog.DefaultsFactory;
import com.elvishew.xlog.printer.flattener.LogFlattener;

/**
 * Log {@link Printer} using {@code System.out.println(String)}.
 */
public class SystemPrinter implements Printer {

    /**
     * The log flattener when print a log.
     */
    private LogFlattener logFlattener;

    /**
     * Constructor.
     */
    public SystemPrinter() {
        this.logFlattener = DefaultsFactory.createLogFlattener();
    }

    /**
     * Constructor.
     *
     * @param logFlattener the log flattener when print a log
     */
    public SystemPrinter(LogFlattener logFlattener) {
        this.logFlattener = logFlattener;
    }

    @Override
    public void println(int logLevel, String tag, String msg) {
        String flattenedLog = logFlattener.flatten(logLevel, tag, msg).toString();
        System.out.println(flattenedLog);
    }
}
