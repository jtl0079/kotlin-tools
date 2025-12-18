# To User

---

## 📦 引入方式

### 方式一：作为 Git Submodule 引用（推荐）

在你的 Android 主项目根目录执行：

```bash
git submodule add https://github.com/你的账号/kotlin-tools.git libs/kotlin-tools
git submodule update --init --recursive
```

项目结构应变为：

```
YourProject
└── libs
    └── kotlin-tools
```

---

## 🧩 模块说明

* **tools_core**：与平台无关的 Kotlin 工具模块（纯 Kotlin / JVM）
* **tools_android**：Android 平台相关的工具与扩展，依赖 Android SDK

---

## 🔧 Android 工程配置步骤

### 1. 在 settings.gradle.kts 中注册模块

> 以下两种方式 **任选其一**，请勿同时使用。

#### 方式 A：按实际目录结构显式注册（推荐）

```kotlin
include(":libs:kotlin-tools:tools_core")
include(":libs:kotlin-tools:tools_android")
```

#### 方式 B：将 kotlin-tools 作为统一根模块注册

```kotlin
include(":kotlin-tools")
project(":kotlin-tools").projectDir = file("libs/kotlin-tools")

include(":kotlin-tools:tools_core")
include(":kotlin-tools:tools_android")
```

---

### 2. 在 app/build.gradle.kts 中添加依赖

```kotlin
dependencies {
    implementation(project(":kotlin-tools:tools_core"))
    implementation(project(":kotlin-tools:tools_android"))
}
```

Sync 项目后即可使用该库中的所有代码。

---

# To Editor

## 🔄 更新模块

### 拉取子模块远端更新

在主项目根目录执行：

```bash
git submodule update --remote --merge
```

---

### 提交并推送子模块及主模块改动

在主项目根目录执行：

```bash
git submodule foreach --recursive "git add .; git commit -m 'auto update submodule'; git push"

git add .
git commit -m "update all"
git push
```

---

## ⚠️ 注意事项

* 子模块的提交与主项目提交是 **两个独立的 Git 历史**
* 修改子模块代码后，必须先在子模块中提交并推送
* 主项目中记录的只是子模块的 **commit 引用**
