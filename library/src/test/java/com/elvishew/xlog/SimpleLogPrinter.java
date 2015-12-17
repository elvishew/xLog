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

package com.elvishew.xlog;

import com.elvishew.xlog.printer.MessageFormattedPrinter;

import java.util.ArrayList;
import java.util.List;

public class SimpleLogPrinter extends MessageFormattedPrinter {

    private List<LogItem> logsContainers = new ArrayList<>();

    public SimpleLogPrinter(List<LogItem> logsContainer) {
        this.logsContainers = logsContainer;
    }

    @Override
    protected void onPrintFormattedMessage(int logLevel, String tag, String msg) {
        LogItem log = onPrint(logLevel, tag, msg);
        afterPrint(log);
    }

    protected LogItem onPrint(int logLevel, String tag, String msg) {
        return new LogItem(logLevel, tag, msg);
    }

    private void afterPrint(LogItem log) {
        logsContainers.add(log);
    }
}
