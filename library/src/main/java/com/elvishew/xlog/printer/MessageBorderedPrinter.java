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

import com.elvishew.xlog.System;
import com.elvishew.xlog.border.BorderConfiguration;

/**
 * {@link Printer} with message bordered.
 */
public abstract class MessageBorderedPrinter extends MessageFormattedPrinter {

    private final BorderConfiguration borderConfiguration;

    /**
     * Constructor.
     *
     * @param borderConfiguration the border configuration
     */
    public MessageBorderedPrinter(BorderConfiguration borderConfiguration) {
        this.borderConfiguration = borderConfiguration;
    }

    @Override
    protected final void onPrintFormattedMessage(int logLevel, String tag, String msg) {
        onPrintBorderedMessage(logLevel, tag, borderMsgIfNecessary(msg));
    }

    /**
     * Print a log with bordered message.
     *
     * @param logLevel the log level of the printing log
     * @param tag      the tag string
     * @param msg      the bordered message you would like to log
     */
    protected abstract void onPrintBorderedMessage(int logLevel, String tag, String msg);

    private String borderMsgIfNecessary(String msg) {
        if (borderConfiguration.isEnabled) {
            // Build horizontal border.
            String topHorizontalBorder = null;
            String bottomHorizontalBorder = null;
            if (borderConfiguration.horizontalBorderChar == BorderConfiguration.DEFAULT_HORIZONTAL_BORDER_CHAR) {
                if (borderConfiguration.verticalBorderChar == BorderConfiguration.DEFAULT_VERTICAL_BORDER_CHAR) {
                    if (borderConfiguration.borderLength == BorderConfiguration.DEFAULT_BORDER_LENGTH) {
                        topHorizontalBorder = BorderConfiguration.DEFAULT_TOP_CONNER + BorderConfiguration.DEFAULT_HORIZONTAL_BORDER;
                        bottomHorizontalBorder = BorderConfiguration.DEFAULT_BOTTOM_CONNER + BorderConfiguration.DEFAULT_HORIZONTAL_BORDER;
                    } else {
                        String horizontalBorder = buildHorizontalBorder(
                                borderConfiguration.horizontalBorderChar,
                                borderConfiguration.borderLength);
                        topHorizontalBorder = BorderConfiguration.DEFAULT_TOP_CONNER + horizontalBorder;
                        bottomHorizontalBorder = BorderConfiguration.DEFAULT_BOTTOM_CONNER + horizontalBorder;
                    }
                }
            }
            if (topHorizontalBorder == null) {
                topHorizontalBorder = buildHorizontalBorder(
                        borderConfiguration.horizontalBorderChar,
                        borderConfiguration.borderLength);
                bottomHorizontalBorder = topHorizontalBorder;
            }

            // Insert vertical borders.
            StringBuilder msgWithVerticalBorderBuilder = new StringBuilder(msg.length() + 10);
            String[] lines = msg.split(System.lineSeparator);
            for (String line : lines) {
                msgWithVerticalBorderBuilder.append(borderConfiguration.verticalBorderChar).append(line).append(System.lineSeparator);
            }
            String msgWithVerticalBorder = msgWithVerticalBorderBuilder.toString();

            // Append horizontal borders.
            StringBuilder messageBuilder = new StringBuilder(
                    msgWithVerticalBorder.length() + 2 * (borderConfiguration.borderLength + 1));
            messageBuilder
                    .append(topHorizontalBorder).append(System.lineSeparator)
                    .append(msgWithVerticalBorder)
                    .append(bottomHorizontalBorder);
            msg = messageBuilder.toString();
        }
        return msg;
    }

    private String buildHorizontalBorder(char horizontalBorderChar, int borderLength) {
        StringBuilder horizontalBorderBuilder = new StringBuilder(borderLength);
        for (int i = borderLength; i > 0; i--) {
            horizontalBorderBuilder.append(horizontalBorderChar);
        }
        return horizontalBorderBuilder.toString();
    }
}
