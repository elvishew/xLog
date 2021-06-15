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
import com.elvishew.xlog.internal.Platform;

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
      Platform.get().warn("JSON empty.");
      return "";
    }
    try {
      if (json.startsWith("{")) {
        JSONObject jsonObject = new JSONObject(json);
        formattedString = jsonObject.toString(JSON_INDENT);
      } else if (json.startsWith("[")) {
        JSONArray jsonArray = new JSONArray(json);
        formattedString = jsonArray.toString(JSON_INDENT);
      } else {
        Platform.get().warn("JSON should start with { or [");
        return json;
      }
    } catch (Exception e) {
      Platform.get().warn(e.getMessage());
      return json;
    }
    return formattedString;
  }
}
