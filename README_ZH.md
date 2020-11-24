# XLog
![](https://travis-ci.org/elvishew/xLog.svg?branch=master)

轻量、美观强大、可扩展的 Android 和 Java 日志库，可同时将日志打印在如 Logcat、Console 和文件中。如果你愿意，你可以将日志打印到任何地方。

## Logcat 输出

![](https://github.com/elvishew/XLog/blob/master/images/logcat-output.png)

## 快速开始

依赖

```groovy
implementation 'com.elvishew:xlog:1.7.1'
```

初始化

```java
XLog.init(LogLevel.ALL);
```

打印日志

```java
XLog.d("hello xlog");
```

## 英文文档：[English](https://github.com/elvishew/XLog/blob/master/README.md)

## 中文详解请参考：[XLog 详解及源码分析](https://www.jianshu.com/p/15ff181cc2f8)

