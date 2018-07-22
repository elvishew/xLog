# XLog
![](https://travis-ci.org/elvishew/xLog.svg?branch=master)

[English](https://github.com/elvishew/XLog/blob/master/README.md)

简单、美观、强大、可扩展的 Android 和 Java 日志库，可同时在多个通道打印日志，如 Logcat、Console 和文件。如果你愿意，甚至可以打印到远程服务器（或其他任何地方）。

XLog 能干什么:
* 全局配置（TAG，各种格式化器...）或基于单条日志的配置
* 支持打印任意对象以及可自定义的对象格式化器
* 支持打印数组
* 支持打印无限长的日志（没有 4K 字符的限制）
* XML 和 JSON 格式化输出
* 线程信息（线程名等，可自定义）
* 调用栈信息（可配置的调用栈深度，调用栈信息包括类名、方法名文件名和行号）
* 支持日志拦截器
* 保存日志文件（文件名和自动备份策略可灵活配置）
* 在 Android Studio 中的日志样式美观
* 简单易用，扩展性高

与其他日志库的不同:
* 优美的源代码，良好的文档
* 扩展性高，可轻松扩展和强化功能
* 轻量级，零依赖

## 依赖
```groovy
compile 'com.elvishew:xlog:1.4.1'
```

## 预览
* 带线程信息、调用栈信息和边框的日志
![](https://github.com/elvishew/XLog/blob/master/images/classic_log.png)
* 格式化后的网络 API 请求
![](https://github.com/elvishew/XLog/blob/master/images/restful_request.png)
* 格式化后的网络 API 响应
![](https://github.com/elvishew/XLog/blob/master/images/restful_response.png)
* 日志文件
![](https://github.com/elvishew/XLog/blob/master/images/log_files.png)

## 架构
![](https://github.com/elvishew/XLog/blob/master/images/architecture.png)

## 用法
### 初始化
#### 简单方式
```java
XLog.init(LogLevel.ALL);
```
或者如果你想要在正式版中禁止打日志
```
XLog.init(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE);
```

#### 高级方式
```java
LogConfiguration config = new LogConfiguration.Builder()
    .logLevel(BuildConfig.DEBUG ? LogLevel.ALL             // 指定日志级别，低于该级别的日志将不会被打印，默认为 LogLevel.ALL
        : LogLevel.NONE)
    .tag("MY_TAG")                                         // 指定 TAG，默认为 "X-LOG"
    .t()                                                   // 允许打印线程信息，默认禁止
    .st(2)                                                 // 允许打印深度为2的调用栈信息，默认禁止
    .b()                                                   // 允许打印日志边框，默认禁止
    .jsonFormatter(new MyJsonFormatter())                  // 指定 JSON 格式化器，默认为 DefaultJsonFormatter
    .xmlFormatter(new MyXmlFormatter())                    // 指定 XML 格式化器，默认为 DefaultXmlFormatter
    .throwableFormatter(new MyThrowableFormatter())        // 指定可抛出异常格式化器，默认为 DefaultThrowableFormatter
    .threadFormatter(new MyThreadFormatter())              // 指定线程信息格式化器，默认为 DefaultThreadFormatter
    .stackTraceFormatter(new MyStackTraceFormatter())      // 指定调用栈信息格式化器，默认为 DefaultStackTraceFormatter
    .borderFormatter(new MyBoardFormatter())               // 指定边框格式化器，默认为 DefaultBorderFormatter
    .addObjectFormatter(AnyClass.class,                    // 为指定类添加格式化器
        new AnyClassObjectFormatter())                     // 默认使用 Object.toString()
    .addInterceptor(new BlacklistTagsFilterInterceptor(    // 添加黑名单 TAG 过滤器
        "blacklist1", "blacklist2", "blacklist3"))
    .addInterceptor(new MyInterceptor())                   // 添加一个日志拦截器
    .build();

Printer androidPrinter = new AndroidPrinter();             // 通过 android.util.Log 打印日志的打印器
Printer consolePrinter = new ConsolePrinter();             // 通过 System.out 打印日志到控制台的打印器
Printer filePrinter = new FilePrinter                      // 打印日志到文件的打印器
    .Builder("/sdcard/xlog/")                              // 指定保存日志文件的路径
    .fileNameGenerator(new DateFileNameGenerator())        // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
    .backupStrategy(new NeverBackupStrategy()              // 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)
    .logFlattener(new MyFlattener())                       // 指定日志平铺器，默认为 DefaultFlattener
    .build();

XLog.init(                                                 // 初始化 XLog
    config,                                                // 指定日志配置，如果不指定，会默认使用 new LogConfiguration.Builder().build()
    androidPrinter,                                        // 添加任意多的打印器。如果没有添加任何打印器，会默认使用 AndroidPrinter(Android)/ConsolePrinter(java)
    consolePrinter,
    filePrinter);
```
对于 android，做初始化的最佳地方是 [Application.onCreate()](http://developer.android.com/reference/android/app/Application.html#onCreate())。

### 全局用法
```java
XLog.d("Simple message")
XLog.d("My name is %s", "Elvis");
XLog.d("An exception caught", exception);
XLog.d(object);
XLog.d(array);
XLog.json(unformattedJsonString);
XLog.xml(unformattedXmlString);
... // 其他全局使用
```
### 局部用法
创建一个 [Logger]。
```java
Logger partial = XLog.tag("PARTIAL-LOG")
    ... // 其他配置
    .build();
```
然后对该 [Logger] 进行局部范围的使用，所有打印日志的相关方法都跟 [XLog] 类里的一模一样。
```java
partial.d("Simple message 1");
partial.d("Simple message 2");
... // 其他局部使用
```

### 基于单条日志的用法
进行基于单条日志的配置，然后就可以直接打印日志了，所有打印日志的相关方法都跟 [XLog] 类里的一模一样。
```java
XLog.t()    // 允许打印线程信息
    .st(3)  // 允许打印深度为3的调用栈信息
    .b()    // 允许打印日志边框
    ...     // 其他配置
    .d("Simple message 1");

XLog.tag("TEMP-TAG")
    .st(0)  // 允许打印不限深度的调用栈信息
    ...     // 其他配置
    .d("Simple message 2");

XLog.nt()   // 禁止打印线程信息
    .nst()  // 禁止打印调用栈信息
    .d("Simple message 3");

XLog.b().d("Simple message 4");
```

## 比较
让我们设想有一个 JSON 字符串和一个 XML 字符串：
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
![](https://github.com/elvishew/XLog/blob/master/images/comparison-android-log.png)

### XLog
```java
XLog.init(LogLevel.ALL);
XLog.d("Message");
XLog.d("Message with argument: age=%s", 18);
XLog.json(jsonString);
XLog.xml(xmlString);
XLog.st(5).d("Message with stack trace info");
```
![](https://github.com/elvishew/XLog/blob/master/images/comparison-xlog.png)

### 带边框的 XLog
```java
XLog.init(LogLevel.ALL, new LogConfiguration.Builder().b().build());
XLog.d("Message");
XLog.d("Message with argument: age=%s", 18);
XLog.json(jsonString);
XLog.xml(xmlString);
XLog.st(5).d("Message with stack trace info");
```
![](https://github.com/elvishew/XLog/blob/master/images/comparison-xlog-with-border.png)

## 类似的库
* [logger](https://github.com/orhanobut/logger)
* [KLog](https://github.com/ZhaoKaiQiang/KLog)

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

## 待开发
* [x] 打印任意对象: XLog.d(Object) （v1.1.0 开始支持）
* [x] 支持日志拦截器（类似于 [okhttp] 的 [Interceptor]，这里用来拦截日志）（v1.3.0 开始支持）
* [x] 添加 tags 过滤器 (白名单过滤和黑名单过滤)（v1.3.0 开始支持）
* [x] 添加 PatternLogFlattener（主要用在向文件打印日志时），如: 使用模式 "{d yyyy-MM-dd hh:mm:ss.SSS} {l}/{t}: {m}"，平铺后的日志将会是 "2016-10-30 13:00:00,000 W/my_tag: Simple message" (v1.3.0 开始支持)
* [x] 打印日志到文件时，采用异步方式（v1.3.0 开始支持）
* [x] Logger 粒度的日志级别控制，取代当前的全局控制（v1.3.0 开始支持）
* [x] 为 Bundle 和 Intent 对象添加默认的格式化器（v1.4.0 开始支持）
* [x] 导出日志文件为 .zip（v1.4.0 开始支持）

## [版本](https://github.com/elvishew/xLog/releases)
最新版本：1.4.1 [Change log](https://github.com/elvishew/xLog/releases/tag/1.4.1)

## [问题](https://github.com/elvishew/xLog/issues)
如果你在使用过程中遇到任何问题或者有任何建议，请创建一个 Issue。

注意：麻烦使用英文提问和回复，方便其他国家的用户看懂并从中受益，谢谢。

## 感谢
感谢 [Orhan Obut](https://github.com/orhanobut) 的 [logger](https://github.com/orhanobut/logger)，它让我知道一个日志库能做哪些事情。

感谢 [Serge Zaitsev](https://github.com/zserge) 的 [log](https://github.com/zserge/log)，让我想到要兼容 [Android Log]。

## 许可
<pre>
Copyright 2018 Elvis Hew

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

## 关于我
* 微博：[http://weibo.com/elvishew](http://weibo.com/elvishew)
* 个人博客：[http://blog.elvishew.com](http://blog.elvishew.com)
* 简书：[http://www.jianshu.com/users/bc6537653220](http://www.jianshu.com/users/bc6537653220)

[Android Log]: http://developer.android.com/reference/android/util/Log.html
[XLog]: https://github.com/elvishew/XLog/blob/master/library/src/main/java/com/elvishew/xlog/XLog.java
[Logger]: https://github.com/elvishew/XLog/blob/master/library/src/main/java/com/elvishew/xlog/Logger.java
[okhttp]: https://github.com/square/okhttp
[Interceptor]: https://github.com/square/okhttp/blob/master/okhttp/src/main/java/okhttp3/Interceptor.java