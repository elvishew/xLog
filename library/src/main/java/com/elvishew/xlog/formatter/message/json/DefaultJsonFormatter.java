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

package com.elvishew.xlog.formatter.message.json;

import com.elvishew.xlog.formatter.FormatException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Simply format the JSON using {@link JSONObject} and {@link JSONArray}.
 */
public class DefaultJsonFormatter implements JsonFormatter {

  private static final int JSON_INDENT = 4;

  @Override
  public String format(String json) {
    String formattedString = null;
    if (json == null || json.trim().length() == 0) {
      throw new FormatException("JSON empty.");
    }
    try {
      if (json.startsWith("{")) {
        JSONObject jsonObject = new JSONObject(json);
        formattedString = jsonObject.toString(JSON_INDENT);
      } else if (json.startsWith("[")) {
        JSONArray jsonArray = new JSONArray(json);
        formattedString = jsonArray.toString(JSON_INDENT);
      } else {
        throw new FormatException("JSON should start with { or [, but found " + json);
      }
    } catch (Exception e) {
      throw new FormatException("Parse JSON error. JSON string:" + json, e);
    }
    return formattedString;
  }
}
