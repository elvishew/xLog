[English](https://github.com/elvishew/XLog/blob/master/README.md)
# XLog
方便可扩展的 Android 和 java 库，可同时在多个通道打印日志，如 Logcat、System.out 和文件。如果你愿意，甚至可以打印到远程服务器（或其他任何地方）。

## 依赖
```groovy
repositories {
    jcenter() // mavenCentral() would work, too
}
dependencies {
  compile 'com.elvishew:xlog:0.1.1'
}
```

## 用法
### 初始化
#### 简单方式
```java
XLog.init(LogLevel.ALL);
```

#### 高级方式
```java
XLog.init(LogLevel.ALL,
        new LogConfiguration                                             // 如果没有指定 LogConfiguration，会默认使用 new LogConfiguration.Builder().build()
                .Builder()                                               // 打印日志时会用到的配置
                .tag("MY_TAG")                                           // 默认: "XLOG"
                .jsonFormatter(new DefaultJsonFormatter())               // 默认: DefaultJsonFormatter
                .xmlFormatter(new DefaultXmlFormatter())                 // 默认: DefaultXmlFormatter
                .methodFormatter(new DefaultMethodFormatter())           // 默认: DefaultMethodFormatter
                .throwableFormatter(new DefaultThrowableFormatter())     // 默认: DefaultThrowableFormatter
                .build(),
        new AndroidPrinter(                                              // 通过 android.util.Log 打印 log。如果没有指定任何 Printer，会默认使用 AndroidPrinter
                new BorderConfiguration                                  // 如果没有指定 BorderConfiguration，会默认使用 new BorderConfiguration.Builder().enable(false).build()
                        .Builder()                                       // 用来装饰日志消息的边框配置
                        .enable(true)                                    // 默认: false
                        .horizontalBorderChar('═')                       // 默认: '═'
                        .verticalBorderChar('║')                         // 默认: '║'
                        .borderLength(100)                               // 默认: 100
                        .build()
        ),
        new SystemPrinter(),                                             // 通过 System.out.println 打印日志。如果没有指定，则不会使用
        new FilePrinter                                                  // 打印日志到文件。如果没有指定，则不会使用
                .Builder("/sdcard/xlog/")                                // 保存日志文件的路径
                .fileNameGenerator(new DateFileNameGenerator())          // 默认: ChangelessFileNameGenerator("log")
                .backupStrategy(new FileSizeBackupStrategy(1024 * 1024)) // 默认: FileSizeBackupStrategy(1024 * 1024)
                .logFormatter(new DefaultLogFormatter())                 // 默认: DefaultLogFormatter
                .build()
```
对于 android，做初始化的最佳地方是 [Application.onCreate()](http://developer.android.com/reference/android/app/Application.html#onCreate())。

### 全局用法
```java
// 打印一个 LogLevel.INFO 级别的日志
XLog.v(String, Object...);
XLog.v(String);
XLog.v(String, Throwable);

// 打印一个 LogLevel.DEBUG 级别的日志
XLog.d(String, Object...);
XLog.d(String);
XLog.d(String, Throwable);

// 打印一个 LogLevel.INFO 级别的日志
XLog.i(String, Object...);
XLog.i(String);
XLog.i(String, Throwable);

// 打印一个 LogLevel.WARN 级别的日志
XLog.w(String, Object...);
XLog.w(String);
XLog.w(String, Throwable);

// 打印一个 LogLevel.ERROR 级别的日志
XLog.e(String, Object...);
XLog.e(String);
XLog.e(String, Throwable);

// 打印一个 JSON 字符串
XLog.json(String);

// 打印一个 XML 字符串
XLog.xml(String);

// 打印一个方法
XLog.method(Object...);

// 打印一个调用栈
XLog.stack();
XLog.stack(String, Object...);
XLog.stack(String);
```

### 定制用法
#### 1. 开始一个定制
调用
```java
XLog.tag(String);
XLog.jsonFormatter(JsonFormatter);
XLog.xmlFormatter(XmlFormatter);
XLog.methodFormatter(MethodFormatter);
XLog.throwableFormatter(ThrowableFormatter);
XLog.printers(Printer...);
```
中的任何一个方法以创建一个 [Logger].Builder 对象。

#### 2. 更多定制
继续定制 [Logger].Builder 对象的其他参数。
```java
builer.tag(String);
builer.jsonFormatter(JsonFormatter);
builer.xmlFormatter(XmlFormatter);
builer.methodFormatter(MethodFormatter);
builer.throwableFormatter(ThrowableFormatter);
builer.printers(Printer...);
```

#### 3. 生成一个定制后的 Logger
调用 Logger.Builder 对象的
```java
builder.build();
```
方法以生成一个 [Logger] 对象。

#### 4. 开始打印日志.
[Logger] 类里的所有打印日志相关方法都跟 [XLog] 类里的一模一样  
**为了更便利，你可以忽略第 3 步，直接调用 [Logger].Builder 对象的打印日志相关方法，这会生成一个 [Logger] 对象并自动调用它里面的相应方法，也就是说这个生成的 [Logger] 对象是“一次性”的。**  
所有打印日志相关方法都在 **全局使用** 中列出来了。

## 比较
让我们设想有一个 JSON 字符串和一个 XML 字符串：
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
XLog.method(arg1, arg2, arg3);
XLog.stack("Here's the call stack");
```
<img src='https://github.com/elvishew/XLog/blob/master/images/comparison-xlog.png'/>

### 带边框的 XLog
```java
Logger logger = XLog.printers(
        new AndroidPrinter(
                new BorderConfiguration
                        .Builder()
                        .enable(true)
                        .build()))
        .build();    
logger.d("The message");
logger.d("The message with argument: age=%s", 18);
logger.json(jsonString);
logger.xml(xmlString);
logger.method(arg1, arg2, arg3);
logger.stack("Here's the call stack");
```
<img src='https://github.com/elvishew/XLog/blob/master/images/comparison-xlog-with-border.png'/>

## 兼容性
为了兼容 [Android Log]，XLog 支持 [Android Log] 里的所有方法。  
请看 [XLog] 里的 Log 类。
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
### 迁移
如果你有一个大工程正在用 [Android Log]，并且很难将所有对 [Android Log] 的使用都换成 [XLog]，那么你可以使用兼容 API，简单地将所有 'android.util.Log' 替换成 'com.elvishew.xlog.XLog.Log'.  
(**为了更好的性能，尽量不要使用兼容 API**)
#### Linux/Cygwin:
```shell
grep -rl "android.util.Log" <your-source-directory> | xargs sed -i "s/android.util.Log/com.elvishew.xlog.XLog.Log/g"
```

#### Mac
```shell
grep -rl "android.util.Log" <your-source-directory> | xargs sed -i "" "s/android.util.Log/com.elvishew.xlog.XLog.Log/g"
```

#### Android Studio
在 'Project' 窗口里，切换到 'Project Files' 标签，然后右键点击你的源码目录。  
在出现的菜单里，点击 'Replace in Path...' 选项。  
在弹出的对话框里，'Text to find' 区域填上 'android.util.Log'，'Replace with' 区域填个 'com.elvishew.xlog.XLog.Log'，然后点击 'Find'。

## 感谢
感谢 [Orhan Obut](https://github.com/orhanobut) 的 [logger](https://github.com/orhanobut/logger) 给了我很多“应该做成怎样”的思考。  
感谢 [Serge Zaitsev](https://github.com/zserge) 的 [log](https://github.com/zserge/log)，让我想到要兼容 [Android Log]。

## 许可
<pre>
Copyright 2015 Elvis Hew

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
