package com.myorg.kotlintools.time.domain.model.base

/**
 * 基础时间条目 (key + timestamp + value  )
 */
interface TimeEntryBase<
        TKey,
        TTimestamp : Comparable<TTimestamp>,
        TValue
        > {
    val key: TKey
    val timestamp: TTimestamp
    val value: TValue
}





