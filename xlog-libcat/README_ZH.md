# LibCat

[English](https://github.com/elvishew/XLog/blob/master/xlog-libcat/README.md)

拦截所有在 APP 代码里通过 `android.util.Log` 直接打印的日志，并把这些日志重定向到指定的 `Printer`。

大多数情况下，`LibCat` 被用来拦截第三方模块/库的日志，并通过指定 `FilePrinter`，把这些日志保存到文件中。

关于 `Printer`，请到 [XLog] 了解更多信息。

## 快速开始

在 build.gradle 添加

```groovy
apply plugin: 'android-aspectjx'

android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.10'
    }
}

aspectjx {
    // 如果你的项目中使用了 kotlin，确保 exclude `kotlin`，不然编译会报 zip file is empty
    exclude 'kotlin'

    // 添加 'exclude' 你不想拦截日志的包/类
    exclude 'androidx.appcompat'
    exclude 'android.support'

    // 或者：添加 'include' 你要拦截日志的包/类
}

dependencies {
    implementation 'com.elvishew:xlog-libcat:1.0.0'
}
```

在初始化 APP 时进行配置

```java
LibCat.config(true, printer);
```

这样, 此后通过 `android.util.Log` 打印的所有日志将被重定向到 `printer`。

## 示例

* 在 `logcat` 和 `printer` 中都有日志

```java
LibCat.config(true, printer);
```

* 只在 `logcat` 有日志 (和没有使用 `LibCat` 时一样的现象)

```java
LibCat.config(true, null);
```

* 只在 `printer` 有日志

```java
LibCat.config(false, printer);
```

* 日志彻底消失

```java
LibCat.config(false, null);
```

## 参考

[AspectJ]

[AspectJX]

## 注意
在编译阶段，[AspectJ] 会移除所有对 `android.util.Log` 的调用，并替换成 `LibCat` 的相关逻辑。除了编译出的字节码外，所有源码不会被改变。

## License

<pre>
Copyright 2021 Elvis Hew

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

[AspectJ]: https://www.eclipse.org/aspectj/
[AspectJX]: https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx
[Printer]: https://github.com/elvishew/XLog/blob/master/xlog/src/main/java/com/elvishew/xlog/printer/Printer.java
[XLog]: https://github.com/elvishew/xLog/blob/master/README_ZH.md
