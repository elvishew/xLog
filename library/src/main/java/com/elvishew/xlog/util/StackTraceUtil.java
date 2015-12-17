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

package com.elvishew.xlog.util;

import com.elvishew.xlog.System;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

public class StackTraceUtil {

    /**
     * Get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new FastPrintWriter(sw, false, 256);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static String getCallStackTraceString(StackTraceElement[] callStack, int ignoredStackTraceDepth) {
        StringBuilder sb = new StringBuilder(256);
        if (callStack != null) {
            for (int i = ignoredStackTraceDepth; i < callStack.length; i++) {
                sb.append("\tat ");
                sb.append(callStack[i].toString());
                sb.append(System.lineSeparator);
            }
        }
        return sb.toString();
    }
}
