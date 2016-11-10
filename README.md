# XLog
<img src='https://travis-ci.org/elvishew/xLog.svg?branch=master'/>

Convenient and flexible library for logging in android and java, can concurrently print the log to multiple target like Logcat, System.out and File, or even Server(or anywhere) if you like.

What XLog can do:
* XML and JSON formatted
* Thread information
* Stack trace information
* Save logs in file (configurable file naming and backup strategy)
* Good looking in Android Studio
* Easy to use, powerful in customization

## Dependency
```groovy
compile 'com.elvishew:xlog:1.0.0'
```

## Architecture
<img src='https://github.com/elvishew/XLog/blob/master/images/architecture.png'/>

## Preview
* Log with thread information, stack trace information and border
<img src='https://github.com/elvishew/XLog/blob/master/images/classic_log.png'/>
* Log files
<img src='https://github.com/elvishew/XLog/blob/master/images/log_files.png'/>

## Usage
### Initialization
#### Simple way
```java
XLog.init(LogLevel.ALL);
```
Or if you want to disable the log in release version
```
XLog.init(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE);
```

#### Advance way
```java
LogConfiguration config = new LogConfiguration.Builder()
    .tag("MY_TAG")                                         // Default: "X-LOG"
    .t()                                                   // Enable thread info, disabled by default
    .st(2)                                                 // Enable stack trace info with depth 2, disabled by default
    .b()                                                   // Enable border, disabled by default
    .jsonFormatter(new MyJsonFormatter())                  // Default: DefaultJsonFormatter
    .xmlFormatter(new MyXmlFormatter())                    // Default: DefaultXmlFormatter
    .throwableFormatter(new MyThrowableFormatter())        // Default: DefaultThrowableFormatter
    .threadFormatter(new MyThreadFormatter())              // Default: DefaultThreadFormatter
    .stackTraceFormatter(new MyStackTraceFormatter())      // Default: DefaultStackTraceFormatter
    .borderFormatter(new MyBoardFormatter())               // Default: DefaultBorderFormatter
    .build();

Printer androidPrinter = new AndroidPrinter();
Printer SystemPrinter = new SystemPrinter();
Printer filePrinter = new FilePrinter
    .Builder("/sdcard/xlog/")                              // The path to save log file
    .fileNameGenerator(new DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log")
    .backupStrategy(new MyBackupStrategy())                // Default: FileSizeBackupStrategy(1024 * 1024)
    .logFlattener(new MyLogFlattener())                    // Default: DefaultLogFlattener
    .build();

XLog.init(LogLevel.ALL,                                    // The log level, logs below this level won't be printed
    config,                                                // The log configuration, if not specified, will use new LogConfiguration.Builder().build()
    androidPrinter,                                        // Print the log using android.util.Log, if no printer is specified, AndroidPrinter will be used by default
    systemPrinter,                                         // Print the log using System.out.println, if not specified, will not be used
    filePrinter);                                          // Print the log to the file system, if not specified, will not be used
```
For android, a best place to do the initialization is [Application.onCreate()](http://developer.android.com/reference/android/app/Application.html#onCreate()).

### Global Usage
```java
XLog.d("Simple message")
XLog.d("My name is %s", "Elvis");
XLog.d("An exception caught", exception);
XLog.d(array);
XLog.json(unformattedJsonString);
XLog.xml(unformattedXmlString);
... // Other global usage
```
### Partial usage
Build a logger.
```java
Logger partial = XLog.tag("PARTIAL-LOG")
    ... // Other configs
    .build();
```
And use it partially, the logging methods is completely the same as that ones in [XLog].

```java
partial.d("Simple message 1");
partial.d("Simple message 2");
... // Other partial usage
```

### Log based Usage
Setup log based configs and log immediately, the logging methods is completely the same as that ones in [XLog].
```java
XLog.t()    // Enable thread into
    .st(3)  // Enable stack trace info with depth 3
    .b()    // Enable border
    ...     // Other configs
    .d("Simple message 1");

XLog.tag("TEMP-TAG")
    .st(0)  // Enable stack trace info without limitation
    ...     // Other configs
    .d("Simple message 2");

XLog.nt()   // Disable thread info
    .nst()  // Disable stack trace info
    .d("Simple message 3");

XLog.b().d("Simple message 4");

```

## Comparison
Let's imagine there are a JSON string and a XML string
```java
String jsonString = "{\"name\": \"Elvis\", \"age\": 18}";
String xmlString = "<team><member name="Elvis"/><member name="Leon"/></team>";
```

### [Android Log]
```java
Log.d(TAG, "Message");
Log.d(TAG, String.format("Message with argument: age=%s", 18));
Log.d(TAG, jsonString);
Log.d(TAG, xmlString);
Log.d(TAG, "Message with stack trace info", new Throwable());
```
<img src='https://github.com/elvishew/XLog/blob/master/images/comparison-android-log.png'/>

### XLog
```java
XLog.init(LogLevel.ALL);
XLog.d("Message");
XLog.d("Message with argument: age=%s", 18);
XLog.json(jsonString);
XLog.xml(xmlString);
XLog.st(5).d("Message with stack trace info");
```
<img src='https://github.com/elvishew/XLog/blob/master/images/comparison-xlog.png'/>

### XLog with border
```java
XLog.init(LogLevel.ALL, new LogConfiguration.Builder().b().build());
XLog.d("Message");
XLog.d("Message with argument: age=%s", 18);
XLog.json(jsonString);
XLog.xml(xmlString);
XLog.st(5).d("Message with stack trace info");
```
<img src='https://github.com/elvishew/XLog/blob/master/images/comparison-xlog-with-border.png'/>

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
#### Linux/Cygwin:
```shell
grep -rl "android.util.Log" <your-source-directory> | xargs sed -i "s/android.util.Log/com.elvishew.xlog.XLog.Log/g"
```

#### Mac
```shell
grep -rl "android.util.Log" <your-source-directory> | xargs sed -i "" "s/android.util.Log/com.elvishew.xlog.XLog.Log/g"
```

#### Android Studio
In 'Project' pane, switch to the 'Project Files' tab, then right-click on the your source directory.  
In the menu, click the 'Replace in Path...' option.  
In the dialog, fill the 'Text to find' with 'android.util.Log', and 'Replace with' with 'com.elvishew.xlog.XLog.Log', and click 'Find'.

## Thanks
Thanks to [Orhan Obut](https://github.com/orhanobut)'s [logger](https://github.com/orhanobut/logger), it give me many ideas of what a logger can do.
Thanks to [Serge Zaitsev](https://github.com/zserge)'s [log](https://github.com/zserge/log), it give me the thought of making XLog compatible with [Android Log].

## License
<pre>
Copyright 2016 Elvis Hew

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
[XLog]: https://github.com/elvishew/XLog/blob/master/library/src/main/java/com/elvishew/xlog/XLog.java
[Logger]: https://github.com/elvishew/XLog/blob/master/library/src/main/java/com/elvishew/xlog/Logger.java
