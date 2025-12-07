# to user
## kotlin-tools 文件夹架构介绍
```
project/                                    # 顶层仓库根目录
├── README.md                               # 项目说明（如何构建、如何贡献、模块说明） # 必备
├── LICENSE                                 # 许可证文件（MIT/Apache 等）   
├── build.gradle.kts               
├── CMakeLists.txt                          # 顶层 CMake：定义项目名、option、子目录、安装规则
│
├── tools_core/                            # ⭐Core Kotlin 工具集
│   ├── build.gradle.kts
│   └── src/
│       └── main/kotlin/com/myorg/kotlintools/
│           ├── math
│           └── time
│               ├── domain/
│               │   ├── model/           # 实体（纯数据 + 核心行为）
│               │   ├── mapper/          # 纯数据结构转换（不涉及业务规则）
│               │   ├── repository/      # 仓库接口（契约）
│               │   └── service/         # 纯领域服务（跨实体逻辑）
│               │
│               ├── usecase/             # 所有业务场景逻辑
│               ├── infrastructure/
│               │   ├── repository/      # Repository 实现
│               │   ├── datasource/      # Local / Remote 实现
│               │   └── config/          # Lock / Thread / Network / Storage Config
│               │
│               └── common/ 
│
├── tools_android/                         # ⭐Android 特化模块（ViewModel/UI/Android API）
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/myorg/kotlintools/android
│           ├── hardware/
│           │   └── vibrator/
│           ├── ui/
│           │   ├── selection/
│           │   └── color/
│           └── features/
│               └── clock/
│                   ├── presentation/
│                   │   ├── viewmodel/
│                   │   ├── ui/
│                   │   └── navigation/
│                   ├── platform/                   # Android 特定：Storage/Thread/Network
│                   │
│                   └── else/                      
│               
├── tools_cpp/                             # ⭐若需要 CMake + JNI（可选）
│   ├── CMakeLists.txt
│   ├── include/
│   ├── src/
│   └── build.gradle.kts (带 externalNativeBuild)
│
├── cmake/                                 # CMake 模块（FindXXX.cmake）
│   └── Modules/
│
├── docs/                                  # 文档
│   ├── architecture.md
│   ├── modules.md
│   └── development.md
│
└── third_party/

```
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

或

include(":kotlin-tools")
project(":kotlin-tools").projectDir = file("libs/kotlin-tools")

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

## 🔄 更新模块

进入主项目根目录执行：（上传子模块）

```bash
git submodule update --remote --merge
```

进入主项目根目录执行：（上传子模块和主模块）：

```bash
git submodule foreach --recursive "git add .; git commit -m 'auto update submodule'; git push"
git add .
git commit -m "update all"
git push
```


---

## 🧠 设计特点

* ✅ 独立 Git 仓库
* ✅ 可被多个项目共享
* ✅ 支持版本控制
* ✅ 工业级模块化架构

---

如需发布到 Maven 或 JitPack，可在此基础上进一步扩展发布配置。
