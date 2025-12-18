# To User

---

## 📦 Integration

### Option 1: Add as a Git Submodule (Recommended)

Run the following commands in the root directory of your Android project:

```bash
git submodule add https://github.com/your-account/kotlin-tools.git libs/kotlin-tools
git submodule update --init --recursive
```

Your project structure should look like this:

```
YourProject
└── libs
    └── kotlin-tools
```

---

## 🧩 Module Overview

* **tools_core**: Platform-independent Kotlin utilities (pure Kotlin / JVM)
* **tools_android**: Android-specific utilities and extensions, depends on Android SDK

---

## 🔧 Android Project Configuration

### 1. Register modules in `settings.gradle.kts`

> Choose **one** of the following approaches. **Do not use both at the same time.**

#### Option A: Register modules by actual directory structure (Recommended)

```kotlin
include(":libs:kotlin-tools:tools_core")
include(":libs:kotlin-tools:tools_android")
```

#### Option B: Register `kotlin-tools` as a unified root module

```kotlin
include(":kotlin-tools")
project(":kotlin-tools").projectDir = file("libs/kotlin-tools")

include(":kotlin-tools:tools_core")
include(":kotlin-tools:tools_android")
```

---

### 2. Add dependencies in `app/build.gradle.kts`

```kotlin
dependencies {
    implementation(project(":kotlin-tools:tools_core"))
    implementation(project(":kotlin-tools:tools_android"))
}
```

Sync the project and the library will be ready to use.

---

# To Editor

## 🔄 Updating the Submodule

### Pull latest changes from the submodule remote

Run the following command in the root directory of the main project:

```bash
git submodule update --remote --merge
```

---

### Commit and push changes for both submodule and main project

Run the following commands in the root directory of the main project:

```bash
git submodule foreach --recursive "git add .; git commit -m 'auto update submodule'; git push"

git add .
git commit -m "update all"
git push
```

---

## ⚠️ Notes

* The submodule and the main project have **separate Git histories**
* After modifying submodule code, you must commit and push changes **inside the submodule first**
* The main project only records a **commit reference** to the submodule
