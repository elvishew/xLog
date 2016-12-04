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

package com.elvishew.xlog.interceptor;

import com.elvishew.xlog.LogItem;

import java.util.Arrays;

/**
 * Filter out the logs with a tag that is NOT in the whitelist.
 *
 * @since 1.3.0
 */
public class WhitelistTagsFilterInterceptor extends AbstractFilterInterceptor {

  private Iterable<String> whitelistTags;

  /**
   * Constructor
   *
   * @param whitelistTags the whitelist tags, the logs with a tag that is NOT in the whitelist
   *                      will be filtered out
   */
  public WhitelistTagsFilterInterceptor(String... whitelistTags) {
    this(Arrays.asList(whitelistTags));
  }

  /**
   * Constructor
   *
   * @param whitelistTags the whitelist tags, the logs with a tag that is NOT in the whitelist
   *                      will be filtered out
   */
  public WhitelistTagsFilterInterceptor(Iterable<String> whitelistTags) {
    if (whitelistTags == null) {
      throw new NullPointerException();
    }
    this.whitelistTags = whitelistTags;
  }

  /**
   * {@inheritDoc}
   *
   * @return true if the tag of the log is NOT in the whitelist, false otherwise
   */
  @Override
  protected boolean reject(LogItem log) {
    if (whitelistTags != null) {
      for (String enabledTag : whitelistTags) {
        if (log.tag.equals(enabledTag)) {
          return false;
        }
      }
    }
    return true;
  }
}
