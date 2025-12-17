```
time/
├── domain/
│   ├── model/           # 实体（纯数据 + 核心行为）
│   ├── mapper/          # 纯数据结构转换（不涉及业务规则）
│   ├── repository/      # 仓库接口（契约）
│   └── service/         # 纯领域服务（跨实体逻辑）
│
├── usecase/             # 所有业务场景逻辑
│   ├── AddReportUseCase.kt
│   ├── UpdateReportUseCase.kt
│   ├── DeleteReportUseCase.kt
│   └── GetReportUseCase.kt
│
├── infrastructure/
│   ├── repository/      # Repository 实现
│   ├── datasource/      # Local / Remote 实现
│   └── config/          # Lock / Thread / Network / Storage Config
│
├── presentation/
│   ├── viewmodel/
│   ├── ui/
│   └── navigation/
│
└── else/

```