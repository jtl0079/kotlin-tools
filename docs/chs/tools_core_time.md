# FileManager
## 文件夹路径
```
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
```

## 路径展开
```
│           └── time
│               ├── domain/
│               │   ├── mapper/          # 纯数据结构转换（不涉及业务规则）
│               │   │
│               │   ├── model/           # 实体（纯数据 + 核心行为）
│               │   │   ├── base/
│               │   │   │   ├── TimeEntryBase.kt            # .*Entry\.kt   , entry template 
│               │   │   │   ├── TimeEntriesBase.kt          # .*Entries\.kt , entries template 
│               │   │   │   ├── TimeMapBase.kt              # .*Map\.kt     , Map (time, value) template 
│               │   │   │   ├── TimeMapGroupBase.kt         # .*MapGroup\.kt, MapGroup tempate
│               │   │   │   ├── KeyTimeMapBase.kt           # Key.*Map\.kt  , Map (key, time, value) template
│               │   │   │   └──
│               │   │   ├── instant/
│               │   │   │   ├── InstantlyEntry.kt       # 单条数据
│               │   │   │   ├── InstantlyEntries.kt     
│               │   │   │   └──
│               │   │   ├── month/
│               │   │   │   ├── MonthlyEntry.kt         # 单条数据
│               │   │   │   ├── MonthlyMap.kt           # 时间键，映射值
│               │   │   │   └──
│               │   │   ├── year/
│               │   │   │   ├── YearlyEntry.kt         # 单条数据
│               │   │   │   ├── YearlyMap.kt           # 时间键，映射值
│               │   │   │   └──
│               │   │   ├── alltime/
│               │   │   │   ├── AllTimeMapGroup.kt         # 集合所有 timeMap
│               │   │   │   ├── KeyAllTimeMap.kt           # key, 时间键，value
│               │   │   │   └──
│               │   │   └── 
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
```

# 设计
## var 设计


| Model template type   | 衍生类 naming |     |
| --- | --- | --- |
| 1. TimeEntryBase      | .*Entry       |
| 2. TimeEntriesBase    | .*Entries     |  |
| 3. TimeMapBase        | .*Map         |  |
| 4. TimeMapGroupBase   | .*MapGroup    |  |
| 5. KeyTimeMapBase     | Key.*Map      |  |
| 6. TimeDataBundleBase | .*DataBundle  |  |
```

TimeDataBundleBase<
    TimeEntriesBase<TimeEntryBase<TKey, TTime, TValue>>,
    KeyTimeMapBase<TKey, TValue>
    >
├── TimeEntriesBase<TimeEntryBase<TKey, TTime, TValue>>
├── KeyTimeMapBase<TKey, TValue>


TimeEntriesBase<TimeEntryBase<TKey, TTime, TValue>>
├── entryies: MutableList<TimeEntryBase<TKey, TTime, TValue>>
│   ├── TimeEntryBase<TKey, TTime, TValue>
│   │   ├── key: TKey
│   │   ├── timestamp: TTime
│   │   ├── value: TValue
│   │   └──
│   


KeyTimeMapBase<TKey, TimeMapGroupBase<TValue>>
├── keyTimeMap: MutableMap<TKey, TimeMapGroupBase<TValue>>
│   ├── TKey
│   ├── TimeMapGroupBase<TValue>
│   │   ├── timeMap0: TimeMapBase<T*, TValue>
│   │   ├── timeMap1: TimeMapBase<T*, TValue>
│   │   │   ├── TimeMapBase<T*, TValue>
│   │   │   │   ├── timeMap: MutableMap<T*, TValue>
│   │   │   │   └──
│   




```


