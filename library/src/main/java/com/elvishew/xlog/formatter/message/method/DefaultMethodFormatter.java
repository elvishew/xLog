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

package com.elvishew.xlog.formatter.message.method;

/**
 * Simply join the method name, source file and line number together, and arguments if provided.
 */
public class DefaultMethodFormatter implements MethodFormatter {

    @Override
    public String format(MethodInfo data) {
        StringBuilder sb = new StringBuilder(80);
        StackTraceElement callSource = data.stackTraceElements[0];
        sb.append(getSimpleClassName(callSource.getClassName()))
                .append(".")
                .append(callSource.getMethodName())
                .append(" (")
                .append(callSource.getFileName())
                .append(":")
                .append(callSource.getLineNumber())
                .append(") ");
        int paramCount = data.arguments.length;
        if (paramCount != 0) {
            sb.append('(');
            if (paramCount == 1) {
                sb.append(data.arguments[0]);
            } else if (paramCount > 1) {
                sb.append(data.arguments[0]);
                for (int i = 1; i < paramCount; i++) {
                    sb.append(", ").append(data.arguments[i]);
                }
            }
            sb.append(')');
        }
        return sb.toString();
    }

    private String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }
}
