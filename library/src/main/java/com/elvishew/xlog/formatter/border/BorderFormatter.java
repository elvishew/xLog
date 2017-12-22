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

package com.elvishew.xlog.formatter.border;

import com.elvishew.xlog.formatter.Formatter;

/**
 * The border formatter used to wrap string segments with borders when logging.
 * <p>
 * e.g:
 * <br>
 * <br>╔════════════════════════════════════════════════════════════════════════════
 * <br>║Thread: main
 * <br>╟────────────────────────────────────────────────────────────────────────────
 * <br>║	├ com.elvishew.xlog.SampleClassB.sampleMethodB(SampleClassB.java:100)
 * <br>║	└ com.elvishew.xlog.SampleClassA.sampleMethodA(SampleClassA.java:50)
 * <br>╟────────────────────────────────────────────────────────────────────────────
 * <br>║Here is a simple message
 * <br>╚════════════════════════════════════════════════════════════════════════════
 */
public interface BorderFormatter extends Formatter<String[]> {
}
