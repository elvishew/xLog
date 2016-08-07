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

import android.annotation.TargetApi;
import android.os.Build;

/**
 * System environment.
 */
public class SystemCompat {

    /**
     * The line separator of system.
     */
    public static String lineSeparator = lineSeparator();

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String lineSeparator() {
        try { // No need to detect whether current platform is android, just do it with catching.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return System.lineSeparator();
            } else {
                return "\n";
            }
        } catch (Exception e) {
            return "\n";
        }
    }
}
