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

package com.elvishew.xlog.printer;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.formatter.message.method.MethodInfo;

/**
 * Represents a group of Printers that should used to print the single log in the same time.
 */
public class PrinterSet implements Printer {

    private Printer[] printers;

    /**
     * Constructor, pass printers in and will use all these printers to print the same log.
     *
     * @param printers the printers used to print the same log
     */
    public PrinterSet(Printer... printers) {
        this.printers = printers;
    }

    @Override
    public void println(int logLevel, LogConfiguration logConfiguration, String msg) {
        for (Printer printer : printers) {
            printer.println(logLevel, logConfiguration, msg);
        }
    }

    @Override
    public void println(int logLevel, LogConfiguration logConfiguration, String msg, Throwable tr) {
        for (Printer printer : printers) {
            printer.println(logLevel, logConfiguration, msg, tr);
        }
    }

    @Override
    public void json(LogConfiguration logConfiguration, String json) {
        for (Printer printer : printers) {
            printer.json(logConfiguration, json);
        }
    }

    @Override
    public void xml(LogConfiguration logConfiguration, String xml) {
        for (Printer printer : printers) {
            printer.xml(logConfiguration, xml);
        }
    }

    @Override
    public void method(LogConfiguration logConfiguration, MethodInfo methodInfo) {
        for (Printer printer : printers) {
            printer.method(logConfiguration, methodInfo);
        }
    }
}
