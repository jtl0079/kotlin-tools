# kotlin-tools

一个可复用的 Kotlin / Android 工具库，可作为 **独立仓库** 使用，也可通过 **Git Submodule** 集成进多个 Android 项目。

---

## 📦 引入方式

### ✅ 方式一：作为 Git Submodule 引用（推荐）

在你的 Android 主项目根目录执行：

```bash
git submodule add https://github.com/你的账号/kotlin-tools.git libs/kotlin-tools
git submodule update --init --recursive
```

项目结构将变为：

```
YourProject
└── libs
    └── kotlin-tools
```

---

## 🔧 Android 工程配置步骤

### 1. 在 settings.gradle.kts 中注册模块

```kotlin
include(":libs:kotlin-tools")
```

---

### 2. 在 app/build.gradle.kts 中添加依赖

```kotlin
dependencies {
    implementation(project(":libs:kotlin-tools"))
}
```

Sync 项目后即可使用该库中的所有代码。

---

## 🧱 模块结构要求

kotlin-tools 必须是标准 Android Library 模块结构：

```
kotlin-tools
├── src/main/java/
│   └── com/yourorg/kotlintools/
│       └── YourTool.kt
├── build.gradle.kts
└── AndroidManifest.xml
```

---

## 🛠 示例 build.gradle.kts（kotlin-tools）

```kotlin
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.yourorg.kotlintools"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
    }
}
```

---

## ✅ 使用示例

### 在 kotlin-tools 中定义方法

```kotlin
package com.yourorg.kotlintools

fun helloTools(): String {
    return "Hello from kotlin-tools"
}
```

### 在主项目中调用

```kotlin
import com.yourorg.kotlintools.helloTools

val msg = helloTools()
```

---

## 🔄 更新子模块

进入主项目根目录执行：

```bash
git submodule update --remote --merge
```

进入主项目根目录执行：

```bash
git submodule foreach --recursive "git add .; git commit -m 'auto update submodule'; git push"
```


---

## 🧠 设计特点

* ✅ 独立 Git 仓库
* ✅ 可被多个项目共享
* ✅ 支持版本控制
* ✅ 工业级模块化架构

---

如需发布到 Maven 或 JitPack，可在此基础上进一步扩展发布配置。
