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

import com.elvishew.xlog.border.BorderConfiguration;
import com.elvishew.xlog.formatter.message.json.JsonFormatter;
import com.elvishew.xlog.formatter.message.method.MethodFormatter;
import com.elvishew.xlog.formatter.message.method.MethodInfo;
import com.elvishew.xlog.formatter.message.xml.XmlFormatter;
import com.elvishew.xlog.printer.MessageBorderedPrinter;
import com.elvishew.xlog.printer.Printer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.elvishew.xlog.LogLevel.DEBUG;
import static com.elvishew.xlog.LogLevel.INFO;
import static com.elvishew.xlog.LogLevel.VERBOSE;
import static org.junit.Assert.assertTrue;

public class XLogTest {

    private static final String MESSAGE = "message";

    private static final String DEFAULT_TAG = "XLOG";

    private static final String CUSTOM_TAG = "custom_tag";

    private static final String CUSTOM_PRINTER_MSG_PREFIX = "message from custom printer - ";

    private SimpleLogPrinter logPrinter;

    private List<LogItem> logsContainer = new ArrayList<>();

    @Before
    public void setup() {
        logPrinter = new SimpleLogPrinter(logsContainer);
        XLog.init(VERBOSE,
                new LogConfiguration.Builder().tag(DEFAULT_TAG).build(),
                logPrinter);
    }

    @Test
    public void infoSimple() {
        XLog.i(MESSAGE);
        assertLog(INFO, DEFAULT_TAG, MESSAGE);
    }

    @Test
    public void infoCustomTag() {
        XLog.tag(CUSTOM_TAG).i(MESSAGE);
        assertLog(INFO, CUSTOM_TAG, MESSAGE);
    }

    @Test
    public void infoCustomPrinter() {
        XLog.printers(
                new SimpleLogPrinter(logsContainer) {

                    @Override
                    protected LogItem onPrint(int logLevel, String tag, String msg) {
                        return super.onPrint(logLevel, tag, CUSTOM_PRINTER_MSG_PREFIX + msg);
                    }
                })
                .i(MESSAGE);
        assertLog(INFO, DEFAULT_TAG, CUSTOM_PRINTER_MSG_PREFIX + MESSAGE);
    }

    @Test
    public void jsonCustomFormatter() {
        XLog.jsonFormatter(
                new JsonFormatter() {

                    @Override
                    public String format(String data) {
                        return "This is a json string: " + data;
                    }
                })
                .json("{name=xlog}");
        assertLog(DEBUG, DEFAULT_TAG, "This is a json string: {name=xlog}");
    }

    @Test
    public void xmlCustomFormatter() {
        XLog.xmlFormatter(
                new XmlFormatter() {

                    @Override
                    public String format(String data) {
                        return "This is a xml string: " + data;
                    }
                })
                .xml("<note name=\"xlog\">");
        assertLog(DEBUG, DEFAULT_TAG, "This is a xml string: <note name=\"xlog\">");
    }

    @Test
    public void methodCustomFormatter() {
        XLog.methodFormatter(
                new MethodFormatter() {

                    @Override
                    public String format(MethodInfo data) {
                        return data.stackTraceElements[0].getMethodName();
                    }
                })
                .method();
        assertLog(DEBUG, DEFAULT_TAG, "methodCustomFormatter");
    }

    @Test
    public void border() {
        BorderConfiguration border = new BorderConfiguration.Builder()
                .enable(true)
                .horizontalBorderChar('*')
                .verticalBorderChar('*')
                .borderLength(10)
                .build();
        Printer printer = new MessageBorderedPrinter(border) {

            @Override
            protected void onPrintBorderedMessage(int logLevel, String tag, String msg) {
                logsContainer.add(new LogItem(logLevel, tag, msg));
            }
        };
        XLog.printers(printer).i("Message with a border");
        assertLog(INFO, DEFAULT_TAG,
                "**********" + System.lineSeparator +
                        "*Message with a border" + System.lineSeparator +
                        "**********");
    }

    private void assertLog(int logLevel, String tag, String msg) {
        assertLog(new LogItem(logLevel, tag, msg));
    }

    private void assertLog(LogItem target) {
        boolean result = false;
        for (LogItem log : logsContainer) {
            if ((log.logLevel == target.logLevel)
                    && log.tag.equals(target.tag)
                    && log.msg.equals(target.msg)) {
                result = true;
            }
        }
        assertTrue("Missing log: " + target, result);
    }
}