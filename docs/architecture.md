# kotlin-tools 文件夹架构介绍
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
│               ├── utils/
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

# kotlin-tools 项目模板
