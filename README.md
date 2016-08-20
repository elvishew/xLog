[简体中文](https://github.com/elvishew/XLog/blob/master/README_ZH.md)
# XLog
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
compile 'com.elvishew:xlog:0.1.1'
```

## Usage
### Initialization
#### Simple way
```java
XLog.init(LogLevel.ALL);
```

#### Advance way
```java
XLog.init(LogLevel.ALL,
        new LogConfiguration                                             // If LogConfiguration not specified, will use new LogConfiguration.Builder().build()
                .Builder()                                               // The log configuration used when logging
                .tag("MY_TAG")                                           // Default: "XLOG"
                .t()                                                     // Enable thread info, disabled by default
                .st(1)                                                   // Enable stack trace info, disabled by default
                .b()                                                     // Enable border, disabled by default
                .jsonFormatter(new DefaultJsonFormatter())               // Default: DefaultJsonFormatter
                .xmlFormatter(new DefaultXmlFormatter())                 // Default: DefaultXmlFormatter
                .throwableFormatter(new DefaultThrowableFormatter())     // Default: DefaultThrowableFormatter
                .threadFormatter(new DefaultThreadFormatter())           // Default: DefaultThreadFormatter
                .stackTraceFormatter(new DefaultStackTraceFormatter())   // Default: DefaultStackTraceFormatter
                .borderFormatter(new DefaultBoardFormatter())            // Default: DefaultBorderFormatter
                .build(),
        new AndroidPrinter(),                                            // Print the log using android.util.Log, if no printer is specified, AndroidPrinter will be used by default
        new SystemPrinter(),                                             // Print the log using System.out.println, if not specified, will not be used
        new FilePrinter                                                  // Print the log to the file system, if not specified, will not be used
                .Builder("/sdcard/xlog/")                                // The path to save log file
                .fileNameGenerator(new DateFileNameGenerator())          // Default: ChangelessFileNameGenerator("log")
                .backupStrategy(new FileSizeBackupStrategy(1024 * 1024)) // Default: FileSizeBackupStrategy(1024 * 1024)
                .logFlattener(new DefaultLogFlattener())                 // Default: DefaultLogFlattener
                .build()
```
For android, a best place to do the initialization is [Application.onCreate()](http://developer.android.com/reference/android/app/Application.html#onCreate()).

### Global Usage
```java
// Logging a LogLevel.INFO log
XLog.v(String, Object...);
XLog.v(String);
XLog.v(String, Throwable);

// Logging a LogLevel.DEBUG log
XLog.d(String, Object...);
XLog.d(String);
XLog.d(String, Throwable);

// Logging a LogLevel.INFO log
XLog.i(String, Object...);
XLog.i(String);
XLog.i(String, Throwable);

// Logging a LogLevel.WARN log
XLog.w(String, Object...);
XLog.w(String);
XLog.w(String, Throwable);

// Log a LogLevel.ERROR log
XLog.e(String, Object...);
XLog.e(String);
XLog.e(String, Throwable);

// Logging a JSON string
XLog.json(String);

// Logging a XML string
XLog.xml(String);
```

### Custom Usage
#### 1. Start a customization
Call any one of
```java
XLog.tag(String);
XLog.t();
XLog.nt();
XLog.st(int);
XLog.nst();
XLog.b();
XLog.nb();
XLog.jsonFormatter(JsonFormatter);
XLog.xmlFormatter(XmlFormatter);
XLog.throwableFormatter(ThrowableFormatter);
XLog.threadFormatter(ThreadFormatter);
XLog.stackTraceFormatter(StackTraceFormatter);
XLog.borderFormatter(BorderFormatter);
XLog.printers(Printer...);
```
to create a [Logger].Builder object.

#### 2. Further customization
Continue to customize other fields of the [Logger].Builder object.
```java
builer.tag(String);
builer.t();
builer.nt();
builer.st(int);
builer.nst();
builer.b();
builer.nb();
builer.jsonFormatter(JsonFormatter);
builer.xmlFormatter(XmlFormatter);
builer.throwableFormatter(ThrowableFormatter);
builer.threadFormatter(ThreadFormatter);
builer.stackTraceFormatter(StackTraceFormatter);
builer.borderFormatter(BorderFormatter);
builer.printers(Printer...);
```

#### 3. Build a customized Logger
Call the
```java
builder.build();
```
of the Logger.Builder objetct and then a [Logger] object is built.

#### 4. Start to log.
The logging methods of a [Logger] is completely same as that ones in [XLog].  
**As a convenience, you can ignore the step 3, just call the logging methods of [Logger].Builder object, it will automatically build a [Logger] object and call the target logging method, that means the built [Logger] object is kind of one-off.**  
All logging methods are list in **Global Usage**.

## Comparison
Let's imagine there are a JSON string and a XML string
```java
String jsonString = "{name:Elvis, age: 18}";
String xmlString = "<Person name=\"Elvis\" age=\"18\" />";
```

### [Android Log]
```java
Log.d(TAG, "The message");
Log.d(TAG, String.format("The message with argument: age=%s", 18));
Log.d(TAG, formatJson(jsonString));
Log.d(TAG, formatXml(xmlString));
Log.d(TAG, "testAndroidLog(" + arg1 + ", " + arg2 + ", " + arg3 + ")");
Log.d(TAG, "Here's the call stack", new Throwable());
```
<img src='https://github.com/elvishew/XLog/blob/master/images/comparison-android-log.png'/>

### XLog
```java
XLog.d("The message");
XLog.d("The message with argument: age=%s", 18);
XLog.json(jsonString);
XLog.xml(xmlString);
```
<img src='https://github.com/elvishew/XLog/blob/master/images/comparison-xlog.png'/>

### XLog with border
```java
Logger logger = XLog.b().build();
logger.d("The message");
logger.d("The message with argument: age=%s", 18);
logger.json(jsonString);
logger.xml(xmlString);
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
Thanks to [Orhan Obut](https://github.com/orhanobut)'s [logger](https://github.com/orhanobut/logger), it give me many ideas of what a logger should look like.  
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
