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

import com.elvishew.xlog.formatter.DefaultFormatterFactory;
import com.elvishew.xlog.formatter.message.json.JsonFormatter;
import com.elvishew.xlog.formatter.message.method.MethodFormatter;
import com.elvishew.xlog.formatter.message.throwable.ThrowableFormatter;
import com.elvishew.xlog.formatter.message.xml.XmlFormatter;

/**
 * The configuration used for logging, will be passed to every {@link com.elvishew.xlog.printer.Printer}
 * when trying to print any log, the {@link com.elvishew.xlog.printer.Printer} should respect the
 * configuration when printing the log.
 * <p/>
 * Use the {@link Builder} to construct a {@link LogConfiguration} object.
 */
public class LogConfiguration {

    /**
     * The tag string.
     */
    public final String tag;

    /**
     * The JSON formatter used to format the JSON string when log a JSON string.
     */
    public final JsonFormatter jsonFormatter;

    /**
     * The XML formatter used to format the XML string when log a XML string.
     */
    public final XmlFormatter xmlFormatter;

    /**
     * The method formatter used to format the method when log a method.
     */
    public final MethodFormatter methodFormatter;

    /**
     * The throwable formatter used to format the throwable when log a message with throwable.
     */
    public final ThrowableFormatter throwableFormatter;

    /*package*/ LogConfiguration(final Builder builder) {
        tag = builder.tag;
        jsonFormatter = builder.jsonFormatter;
        xmlFormatter = builder.xmlFormatter;
        methodFormatter = builder.methodFormatter;
        throwableFormatter = builder.throwableFormatter;
    }

    /**
     * Builder for {@link LogConfiguration}.
     */
    public static class Builder {

        private static final String DEFAULT_TAG = "XLog";

        /**
         * The tag string used when log.
         */
        private String tag = DEFAULT_TAG;

        /**
         * The JSON formatter used to format the JSON string when log a JSON string.
         */
        private JsonFormatter jsonFormatter;

        /**
         * The XML formatter used to format the XML string when log a XML string.
         */
        private XmlFormatter xmlFormatter;

        /**
         * The method formatter used to format the method when log a method.
         */
        private MethodFormatter methodFormatter;

        /**
         * The throwable formatter used to format the throwable when log a message with throwable.
         */
        private ThrowableFormatter throwableFormatter;

        /**
         * Construct a builder with all default configurations.
         */
        public Builder() {
        }

        /**
         * Construct a builder with all configurations from another {@link LogConfiguration}.
         *
         * @param logConfiguration the {@link LogConfiguration} to copy configurations from
         */
        public Builder(LogConfiguration logConfiguration) {
            tag = logConfiguration.tag;
            jsonFormatter = logConfiguration.jsonFormatter;
            xmlFormatter = logConfiguration.xmlFormatter;
            methodFormatter = logConfiguration.methodFormatter;
            throwableFormatter = logConfiguration.throwableFormatter;
        }

        /**
         * Set the tag string used when log.
         *
         * @param tag the tag string used when log
         * @return the builder
         */
        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        /**
         * Set the JSON formatter used when log a JSON string.
         *
         * @param jsonFormatter the JSON formatter used when log a JSON string
         * @return the builder
         */
        public Builder jsonFormatter(JsonFormatter jsonFormatter) {
            this.jsonFormatter = jsonFormatter;
            return this;
        }

        /**
         * Set the XML formatter used when log a XML string.
         *
         * @param xmlFormatter the XML formatter used when log a XML string
         * @return the builder
         */
        public Builder xmlFormatter(XmlFormatter xmlFormatter) {
            this.xmlFormatter = xmlFormatter;
            return this;
        }

        /**
         * Set the method formatter used when log a method.
         *
         * @param methodFormatter the method formatter used when log a method
         * @return the builder
         */
        public Builder methodFormatter(MethodFormatter methodFormatter) {
            this.methodFormatter = methodFormatter;
            return this;
        }

        /**
         * Set the throwable formatter used when log a message with throwable.
         *
         * @param throwableFormatter the throwable formatter used when log a message with throwable
         * @return the builder
         */
        public Builder throwableFormatter(ThrowableFormatter throwableFormatter) {
            this.throwableFormatter = throwableFormatter;
            return this;
        }

        /**
         * Builds configured {@link LogConfiguration} object.
         *
         * @return the built configured {@link LogConfiguration} object
         */
        public LogConfiguration build() {
            initEmptyFieldsWithDefaultValues();
            return new LogConfiguration(this);
        }

        private void initEmptyFieldsWithDefaultValues() {
            if (jsonFormatter == null) {
                jsonFormatter = DefaultFormatterFactory.createJsonFormatter();
            }
            if (xmlFormatter == null) {
                xmlFormatter = DefaultFormatterFactory.createXmlFormatter();
            }
            if (methodFormatter == null) {
                methodFormatter = DefaultFormatterFactory.createMethodFormatter();
            }
            if (throwableFormatter == null) {
                throwableFormatter = DefaultFormatterFactory.createThrowableFormatter();
            }
        }
    }
}
