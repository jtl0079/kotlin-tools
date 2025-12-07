package com.myorg.kotlintools.time.domain.model.base

/**
 * 基础时间条目 (key + timestamp + value  )
 */
interface TimeEntryBase<TKey, Timestamp, TValue> {
    val key: TKey
    val timestamp: Timestamp
    val value: TValue
}


