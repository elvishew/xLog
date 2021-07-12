# XLog

![](https://travis-ci.org/elvishew/xLog.svg?branch=master)

[简体中文](https://github.com/elvishew/xLog/blob/master/README_ZH.md)

Lightweight and pretty, powerful and flexible logger for android and java, can print the log to Logcat, Console and Files, or anywhere if you like.

## Logcat Output

![](https://github.com/elvishew/XLog/blob/master/images/logcat-output.png)

## Quick Start

Dependency

```groovy
implementation 'com.elvishew:xlog:1.10.1'
```

Initialization

```java
XLog.init(LogLevel.ALL);
```

Logging

```java
XLog.d("hello xlog");
```

## Logging

Log simple message.

```java
XLog.d(message);
```

Log a message with throwable, usually use when exception is thrown.

```java
XLog.e(message, throwable);
```

Format string is also supported, so you don't have to append so many strings and variables by `+`.

```java
XLog.d("Hello %s, I am %d", "Elvis", 20);
```

Unformatted JSON and XML string will be formatted automatically.

```java
XLog.json(JSON_CONTENT);
XLog.xml(XML_CONTENT);
```

All `Collection`s and `Map`s data are supported.

```java
XLog.d(array);
XLog.d(list);
XLog.d(map);
```

If needed, you can also dump `Intent` and `Bundle` object directly.

```java
XLog.d(intent);
XLog.d(bundle);
```

In fact, you can dump any type of object if you want. You can specify an `ObjectFormatter` for specific type, otherwise `toString()` will be used when converting the object to a string.

```java
XLog.d(object);
```

Note: `v/d/i/w/e` are optional, `v` for `VERBOSE`, `d` for `DEBUG`, `i` for `INFO`, `w` for `WARNING` and `e` for `ERROR`.

## Config

`xLog` is very flexible, almost every component is configurable.

When initialization, there are a simple way

```java
XLog.init(LogLevel.ALL);
```

and advance way.

```java
LogConfiguration config = new LogConfiguration.Builder()
    .logLevel(BuildConfig.DEBUG ? LogLevel.ALL             // Specify log level, logs below this level won't be printed, default: LogLevel.ALL
        : LogLevel.NONE)
    .tag("MY_TAG")                                         // Specify TAG, default: "X-LOG"
    .enableThreadInfo()                                    // Enable thread info, disabled by default
    .enableStackTrace(2)                                   // Enable stack trace info with depth 2, disabled by default
    .enableBorder()                                        // Enable border, disabled by default
    .jsonFormatter(new MyJsonFormatter())                  // Default: DefaultJsonFormatter
    .xmlFormatter(new MyXmlFormatter())                    // Default: DefaultXmlFormatter
    .throwableFormatter(new MyThrowableFormatter())        // Default: DefaultThrowableFormatter
    .threadFormatter(new MyThreadFormatter())              // Default: DefaultThreadFormatter
    .stackTraceFormatter(new MyStackTraceFormatter())      // Default: DefaultStackTraceFormatter
    .borderFormatter(new MyBoardFormatter())               // Default: DefaultBorderFormatter
    .addObjectFormatter(AnyClass.class,                    // Add formatter for specific class of object
        new AnyClassObjectFormatter())                     // Use Object.toString() by default
    .addInterceptor(new BlacklistTagsFilterInterceptor(    // Add blacklist tags filter
        "blacklist1", "blacklist2", "blacklist3"))
    .addInterceptor(new MyInterceptor())                   // Add other log interceptor
    .build();

Printer androidPrinter = new AndroidPrinter(true);         // Printer that print the log using android.util.Log
Printer consolePrinter = new ConsolePrinter();             // Printer that print the log to console using System.out
Printer filePrinter = new FilePrinter                      // Printer that print(save) the log to file
    .Builder("<path-to-logs-dir>")                         // Specify the directory path of log file(s)
    .fileNameGenerator(new DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log")
    .backupStrategy(new NeverBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
    .cleanStrategy(new FileLastModifiedCleanStrategy(MAX_TIME))     // Default: NeverCleanStrategy()
    .flattener(new MyFlattener())                          // Default: DefaultFlattener
    .writer(new MyWriter())                                // Default: SimpleWriter
    .build();

XLog.init(                                                 // Initialize XLog
    config,                                                // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build()
    androidPrinter,                                        // Specify printers, if no printer is specified, AndroidPrinter(for Android)/ConsolePrinter(for java) will be used.
    consolePrinter,
    filePrinter);
```

After initialization, a global `Logger` with the global config is created, all logging via `XLog` will pass to this global `Logger`.

Besides, you can create unlimmited number of `Logger` with different configs:

* Base on global `Logger`, change tag to `"TAG-A"`.

```java
Logger logger = XLog.tag("TAG-A")
                    ... // other overrides
                    .build();
logger.d("Message with custom tag");
```

* Base on global `Logger`, enable border and thread info.

```java
Logger logger = XLog.enableBorder()
                    .enableThread()
                    ... // other overrides
                    .build();
logger.d("Message with thread info and border");
```

you can also log with one-time-use config:

```java
XLog.tag("TAG-A").d("Message with custom tag");
XLog.enableBorder().enableThread().d("Message with thread info and border");
```

## Print to anywhere

With one logging statement

```java
XLog.d("hello xlog");
```
you can print the `"hello xlog"` to

* Logcat (with `AndroidPrinter`)

* File (with `FilePrinter`)

and anywhere you like.

Just implement the `Printer` interface, and specify it when initializing

```java
XLog.init(config, printer1, printer2...printerN);
```

or when creating a non-global `Logger`

```java
Logger logger = XLog.printer(printer1, printer2...printerN)
                    .build();
```

or when one-time-use logging

```java
XLog.printer(printer1, printer2...printerN).d("Message with one-time-use printers");
```

## Save logs to file

To save logs to file, you need to create a `FilePrinter`

```java
Printer filePrinter = new FilePrinter                      // Printer that print(save) the log to file
    .Builder("<path-to-logs-dir>")                         // Specify the directory path of log file(s)
    .fileNameGenerator(new DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log")
    .backupStrategy(new NeverBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
    .cleanStrategy(new FileLastModifiedCleanStrategy(MAX_TIME))     // Default: NeverCleanStrategy()
    .flattener(new MyFlattener())                          // Default: DefaultFlattener
    .build();
```

and add the `FilePrinter` to XLog when initializing

```java
XLog.init(config, filePrinter);
```

or when creating an non-global `Logger`

```java
Logger logger = XLog.printer(filePrinter)
                    ... // other overrides
                    .build();
```

or when one-time-use logging

```java
XLog.printer(filePrinter).d("Message with one-time-use printers");
```

### Save third party logs

You can config `LibCat` after initializing `XLog`.

```java
LibCat.config(true, filePrinter);
```

Then, the logs logged by third party modules/libraries(within same app) will be saved to file too.

Go to [LibCat] for more details.

### Custom file name

You can specify the file name directly, or categorize the logs to different files by some rules.

* Use `ChangelessFileNameGenerator`, you can specify a changeless file name.

```
logs-dir
└──log
```

* Use `LevelFileNameGenerator`, it will categorize logs by levels automatically.

```
logs-dir
├──VERBOSE
├──DEBUG
├──INFO
├──WARN
└──ERROR
```

* Use `DateFileNameGenerator`, it will categorize logs by date automatically.

```
logs-dir
├──2020-01-01
├──2020-01-02
├──2020-01-03
└──2020-01-04
```

* Implement `FileNameGenerator` directly, make the file name generating rules by yourself.

```
logs-dir
├──2020-01-01-<hash1>.log
├──2020-01-01-<hash2>.log
├──2020-01-03-<hash>.log
└──2020-01-05-<hash>.log
```

By default, a `ChangelessFileNameGenerator` with log file name `log` is used.


### Custom log format

Log elements(date, time, level and message) should be flattened to a single string before being printed to the file, you need a `Flattener` to do that.

We have defined a `PatternFlattener`, which may satisfy most of you. All you need to do is just passing a pattern with parameters to the flattener.

Supported parameters:

|Parameter|Represents|
|:---:|---|
|{d}|Date in default date format "yyyy-MM-dd HH:mm:ss.SSS"|
|{d format}|Date in specific date format|
|{l}|Short name of log level. e.g: V/D/I|
|{L}|Long name of log level. e.g: VERBOSE/DEBUG/INFO|
|{t}|Tag of log|
|{m}|Message of log|

Imagine there is a log, with `DEBUG` level, `"my_tag"` tag and `"Simple message"` message, the flattened log would be as below.

|Pattern|Flattened log|
|:---:|---|
|{d} {l}/{t}: {m}|2016-11-30 13:00:00.000 D/my_tag: Simple message|
|{d yyyy-MM-dd HH:mm:ss.SSS} {l}/{t}: {m}|2016-11-30 13:00:00.000 D/my_tag: Simple message|
|{d yyyy/MM/dd HH:mm:ss} {l}\|{t}: {m}|2016/11/30 13:00:00 D\|my_tag: Simple message|
|{d yy/MM/dd HH:mm:ss} {l}\|{t}: {m}|16/11/30 13:00:00 D\|my_tag: Simple message|
|{d MM/dd HH:mm} {l}-{t}-{m}|11/30 13:00 D-my_tag-Simple message|

If you don't even want to construct a pattern, `ClassicFlattener` is for you. It is a `PatternFlattener` with a default pattern `{d} {l}/{t}: {m}`.

By default, `FilePrinter` use a `DefaultFlattener`, which just simply concat the timestamp and message together. You may don't like it, so please remember to specify your own `Flattener`, maybe a `ClassicFlattener`.

### Auto backup

Every single log file may grow to an unexpected size, a `AbstractBackupStrategy2` allow you to start a new file at some point, and change the old file name with `.bak.n`(n is the backup index) suffix.

```
logs-dir
├──log
├──log.bak.1
├──log.bak.2
├──log.bak.3
├──...
└──log.bak.n
```

If you don't like the `.bak.n` suffix, you can use `BackupStrategy2` directly to specify the backup file name.

Mostly, you just want to start a new file when the log file reach a specified max-size, so `FileSizeBackupStrategy2` is presented for you.

By default, `FileSizeBackupStrategy(1024*1024)` is used, which will auto backup the log file when it reach a size of 1M. Besides, there will only be one logging file and one backup file in the same time, that means you can save at most only 2M logs.

So, if you want to save more logs, and more backup files, please use `FileSizeBackupStrategy2` instead, this allow you keeping multiple backup files in the same time.

### Auto clean

If you use a changeable `FileNameGenerator`, there would be more than one log files in the logs folder, and gets more and more as time goes on. Besides, if you use a backup strategy not limiting the max backup index, that would also make numbers of log files out of control. To prevent running out of disk space, you need a `CleanStrategy`.

Typically, you can use a `FileLastModifiedCleanStrategy`, which will delete log files that have not been modified for a period of time(e.g., a week) during initialization.

By default, `NeverCleanStrategy` is used, which will never do any cleaning.

### Compress log files

Just call

```java
LogUtil.compress("<path-to-logs-dir>", "<path-to-zip-file>");
```

a zip file will be created and the entire log folder will be compressed and written to it, so you can easily collect the user logs for issue-debug.

Note: the origianl log files will not be deleted.

## Intercept and filter log

Before each log being printed, you have a chance to modify or filter out the log, by using an `Interceptor`.

We have already predefined some `Interceptor` for you, e.g. `WhitelistTagsFilterInterceptor` only allows the logs of specified tags to be printed, and `BlacklistTagsFilterInterceptor` is used to filter out(not print) logs of specified tags.

You can specify multiple `Interceptor`s for a single `Logger`, these `Interceptor`s will be given the opportunity to modify or filter out logs in the order in which they were added. Once a log is filtered out by an `Interceptor`, subsequent `Interceptor`s will no longer get this log.

## Format object

When logging an object

```java
XLog.d(object);
```

the `toString` of the object will be called by default.

Sometimes, the `toString` implementation of the object is not quite what you want, so you need an `ObjectFormatter` to define how this type of object should be converted to a string when logging.

On the android platform, we predefine `IntentFormatter` and `BundleFormatter` for `Intent` and `Bundle` class.

You can implement and add your own `ObjectFormatter` for any class.

Please note, `ObjectFormatter`s only work when logging an object.

## Similar libraries

* [logger](https://github.com/orhanobut/logger)
* [KLog](https://github.com/ZhaoKaiQiang/KLog)

Compare with other logger libraries:

* Well documented
* So flexible that you can easily customize or enhance it

## Compatibility

In order to be compatible with [Android Log], all the methods of [Android Log] are supported here.

See the Log class defined in [XLog].

```java
Log.v(String, String);
Log.v(String, String, Throwable);
Log.d(String, String);
Log.d(String, String, Throwable);
Log.i(String, String);
Log.i(String, String, Throwable);
Log.w(String, String);
Log.w(String, String, Throwable);
Log.wtf(String, String);
Log.wtf(String, String, Throwable);
Log.e(String, String);
Log.e(String, String, Throwable);
Log.println(int, String, String);
Log.isLoggable(String, int);
Log.getStackTraceString(Throwable);
```

### Migration

If you have a big project using the [Android Log], and it is a hard work to change all usage of [Android Log] to [XLog], then you can use the compatible API, simply replace all 'android.util.Log' to 'com.elvishew.xlog.XLog.Log'.  
(**For a better performance, you should think about not using the compatible API.**)

#### Linux/Cygwin

```shell
grep -rl "android.util.Log" <your-source-directory> | xargs sed -i "s/android.util.Log/com.elvishew.xlog.XLog.Log/g"
```

#### Mac

```shell
grep -rl "android.util.Log" <your-source-directory> | xargs sed -i "" "s/android.util.Log/com.elvishew.xlog.XLog.Log/g"
```

#### Android Studio

1. In 'Project' pane, switch to the 'Project Files' tab, then right-click on the your source directory.
2. In the menu, click the 'Replace in Path...' option.
3. In the dialog, fill the 'Text to find' with 'android.util.Log', and 'Replace with' with 'com.elvishew.xlog.XLog.Log', and click 'Find'.

Optionally, instead of replacing all 'android.util.Log', you can just use [LibCat] to intercept all logs logged by `android.util.Log` and redirect them to `XLog`'s `Printer`.

## [Issues](https://github.com/elvishew/xLog/issues)

If you meet any problem when using XLog, or have any suggestion, please feel free to create an issue.  
Before creating an issue, please check if there is an existed one.

## Thanks

Thanks to [Orhan Obut](https://github.com/orhanobut)'s [logger](https://github.com/orhanobut/logger), it give us many ideas of what a logger can do.

Thanks to [Serge Zaitsev](https://github.com/zserge)'s [log](https://github.com/zserge/log), it give us the thought of making `xLog` compatible with [Android Log].

## License

<pre>
Copyright 2015-2021 Elvis Hew

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>

[Android Log]: http://developer.android.com/reference/android/util/Log.html
[XLog]: https://github.com/elvishew/xLog/blob/master/xlog/src/main/java/com/elvishew/xlog/XLog.java
[Logger]: https://github.com/elvishew/xLog/blob/master/xlog/src/main/java/com/elvishew/xlog/Logger.java
[LibCat]: https://github.com/elvishew/xLog/blob/master/xlog-libcat/README.md
