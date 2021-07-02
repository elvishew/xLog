# LibCat

[简体中文](https://github.com/elvishew/XLog/blob/master/xlog-libcat/README_ZH.md)

Intercept the logs directly logged by `android.util.Log` within whole app's code, and redirect the logs to specified `Printer`.

Mostly, `LibCat` is used to intercept the logs from third party modules/libraries, and save the logs to the log file, by specifying a `FilePrinter`.

About `Printer`s, see more in [XLog].

## Quick Start

Add in build.gradle

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
    // if you use kotlin in your project make sure to exclude `kotlin`,
    // otherwise a build error `zip file is empty` will be thrown
    exclude 'kotlin'

    // add 'exclude' packages/classes that you don't want to intercept the logs from
    exclude 'androidx.appcompat'
    exclude 'android.support'

    // or add 'include' packages/classes that you want to intercept the logs from
}

dependencies {
    implementation 'com.elvishew:xlog-libcat:1.0.0'
}
```

Config when initializing app

```java
LibCat.config(true, printer);
```

Then, all future logs logged by `android.util.Log` will be redirected to `printer`.

## Examples

* Logs in `logcat` and `printer`

```java
LibCat.config(true, printer);
```

* Logs in `logcat` only (exactly like the situation before using `LibCat`)

```java
LibCat.config(true, null);
```

* Logs in `printer` only

```java
LibCat.config(false, printer);
```

* Logs disappear totally

```java
LibCat.config(false, null);
```

## References

[AspectJ]

[AspectJX]

## Attention
During compiling app, [AspectJ] will remove all callings of `android.util.Log` and replace with `LibCat` logic. No source code will be changed, but only bytecode.

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
[XLog]: https://github.com/elvishew/xLog/blob/master/README.md
