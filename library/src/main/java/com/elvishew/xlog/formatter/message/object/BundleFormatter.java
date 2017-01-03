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

package com.elvishew.xlog.formatter.message.object;

import android.os.Bundle;

import com.elvishew.xlog.internal.util.ObjectToStringUtil;

/**
 * Format an Bundle object to a string.
 *
 * @since 1.4.0
 */
public class BundleFormatter implements ObjectFormatter<Bundle> {

  /**
   * Format an Bundle object to a string.
   *
   * @param data the Bundle object to format
   * @return the formatted string
   */
  @Override
  public String format(Bundle data) {
    return ObjectToStringUtil.bundleToString(data);
  }
}
