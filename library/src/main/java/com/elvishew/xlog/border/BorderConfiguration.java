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

package com.elvishew.xlog.border;

/**
 * The border can surround the log for a better looking.
 * <p>
 * Use the {@link Builder} to construct a {@link BorderConfiguration} object.
 */
public class BorderConfiguration {

    public static final char DEFAULT_HORIZONTAL_BORDER_CHAR = '═';

    public static final char DEFAULT_VERTICAL_BORDER_CHAR = '║';

    public static final char DEFAULT_TOP_CONNER = '╔';

    public static final char DEFAULT_BOTTOM_CONNER = '╚';

    public static final int DEFAULT_BORDER_LENGTH = 100;

    public static final String DEFAULT_HORIZONTAL_BORDER =
            "═════════════════════════════════════════════════" +
                    "══════════════════════════════════════════════════";

    /**
     * Whether the border is enabled.
     */
    public final boolean isEnabled;

    /**
     * The horizontal border char.
     */
    public final char horizontalBorderChar;

    /**
     * The vertical border char.
     */
    public final char verticalBorderChar;

    /**
     * The length of the horizontal border.
     */
    public final int borderLength;

    /*package*/ BorderConfiguration(Builder builder) {
        this.isEnabled = builder.isEnabled;
        this.horizontalBorderChar = builder.horizontalBorderChar;
        this.verticalBorderChar = builder.verticalBorderChar;
        this.borderLength = builder.borderLength;
    }

    /**
     * Builder for {@link BorderConfiguration}.
     */
    public static class Builder {

        /**
         * Whether the border is enabled.
         */
        private boolean isEnabled = false;

        /**
         * The horizontal border char.
         */
        private char horizontalBorderChar = DEFAULT_HORIZONTAL_BORDER_CHAR;

        /**
         * The vertical border char.
         */
        private char verticalBorderChar = DEFAULT_VERTICAL_BORDER_CHAR;

        /**
         * The length of the horizontal border.
         */
        private int borderLength = DEFAULT_BORDER_LENGTH;

        /**
         * Set whether the border is enabled.
         *
         * @param isEnabled whether the border is enabled
         * @return the builder
         */
        public Builder enable(boolean isEnabled) {
            this.isEnabled = isEnabled;
            return this;
        }

        /**
         * Set the horizontal border char.
         *
         * @param horizontalBorderChar the horizontal border char
         * @return the builder
         */
        public Builder horizontalBorderChar(char horizontalBorderChar) {
            this.horizontalBorderChar = horizontalBorderChar;
            return this;
        }

        /**
         * Set the vertical border char.
         *
         * @param verticalBorderChar the vertical border char
         * @return the builder
         */
        public Builder verticalBorderChar(char verticalBorderChar) {
            this.verticalBorderChar = verticalBorderChar;
            return this;
        }

        /**
         * Set the horizontal border length.
         *
         * @param borderLength the horizontal border length
         * @return the builder
         */
        public Builder borderLength(int borderLength) {
            this.borderLength = borderLength;
            return this;
        }

        /**
         * Build configured {@link BorderConfiguration} object.
         *
         * @return the built configured {@link BorderConfiguration} object
         */
        public BorderConfiguration build() {
            return new BorderConfiguration(this);
        }
    }
}
