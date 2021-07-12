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

package com.elvishew.xlogsample;

import android.app.Application;
import android.os.Build;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.ClassicFlattener;
import com.elvishew.xlog.interceptor.BlacklistTagsFilterInterceptor;
import com.elvishew.xlog.libcat.LibCat;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
import com.elvishew.xlog.printer.file.writer.SimpleWriter;

import java.io.File;

public class XLogSampleApplication extends Application {

  public static Printer globalFilePrinter;

  private static final long MAX_TIME = 1000 * 60 * 60 * 24 * 2; // two days

  @Override
  public void onCreate() {
    super.onCreate();

    initXlog();
  }

  /**
   * Initialize XLog.
   */
  private void initXlog() {
    LogConfiguration config = new LogConfiguration.Builder()
        .logLevel(BuildConfig.DEBUG ? LogLevel.ALL             // Specify log level, logs below this level won't be printed, default: LogLevel.ALL
            : LogLevel.NONE)
        .tag(getString(R.string.global_tag))                   // Specify TAG, default: "X-LOG"
        // .enableThreadInfo()                                 // Enable thread info, disabled by default
        // .enableStackTrace(2)                                // Enable stack trace info with depth 2, disabled by default
        // .enableBorder()                                     // Enable border, disabled by default
        // .jsonFormatter(new MyJsonFormatter())               // Default: DefaultJsonFormatter
        // .xmlFormatter(new MyXmlFormatter())                 // Default: DefaultXmlFormatter
        // .throwableFormatter(new MyThrowableFormatter())     // Default: DefaultThrowableFormatter
        // .threadFormatter(new MyThreadFormatter())           // Default: DefaultThreadFormatter
        // .stackTraceFormatter(new MyStackTraceFormatter())   // Default: DefaultStackTraceFormatter
        // .borderFormatter(new MyBoardFormatter())            // Default: DefaultBorderFormatter
        // .addObjectFormatter(AnyClass.class,                 // Add formatter for specific class of object
        //     new AnyClassObjectFormatter())                  // Use Object.toString() by default
        .addInterceptor(new BlacklistTagsFilterInterceptor(    // Add blacklist tags filter
            "blacklist1", "blacklist2", "blacklist3"))
        // .addInterceptor(new WhitelistTagsFilterInterceptor( // Add whitelist tags filter
        //     "whitelist1", "whitelist2", "whitelist3"))
        // .addInterceptor(new MyInterceptor())                // Add a log interceptor
        .build();

    Printer androidPrinter = new AndroidPrinter();             // Printer that print the log using android.util.Log
    Printer filePrinter = new FilePrinter                      // Printer that print the log to the file system
        .Builder(new File(getExternalCacheDir().getAbsolutePath(), "log").getPath())       // Specify the path to save log file
        .fileNameGenerator(new DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log")
        // .backupStrategy(new MyBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
        // .cleanStrategy(new FileLastModifiedCleanStrategy(MAX_TIME))     // Default: NeverCleanStrategy()
        .flattener(new ClassicFlattener())                     // Default: DefaultFlattener
        .writer(new SimpleWriter() {                           // Default: SimpleWriter
          @Override
          public void onNewFileCreated(File file) {
            super.onNewFileCreated(file);
            final String header = "\n>>>>>>>>>>>>>>>> File Header >>>>>>>>>>>>>>>>" +
                    "\nDevice Manufacturer: " + Build.MANUFACTURER +
                    "\nDevice Model       : " + Build.MODEL +
                    "\nAndroid Version    : " + Build.VERSION.RELEASE +
                    "\nAndroid SDK        : " + Build.VERSION.SDK_INT +
                    "\nApp VersionName    : " + BuildConfig.VERSION_NAME +
                    "\nApp VersionCode    : " + BuildConfig.VERSION_CODE +
                    "\n<<<<<<<<<<<<<<<< File Header <<<<<<<<<<<<<<<<\n\n";
            appendLog(header);
          }
        })
        .build();

    XLog.init(                                                 // Initialize XLog
        config,                                                // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build()
        androidPrinter,                                        // Specify printers, if no printer is specified, AndroidPrinter(for Android)/ConsolePrinter(for java) will be used.
        filePrinter);

    // For future usage: partial usage in MainActivity.
    globalFilePrinter = filePrinter;

    // Intercept all logs(including logs logged by third party modules/libraries) and print them to file.
    LibCat.config(true, filePrinter);
  }
}
