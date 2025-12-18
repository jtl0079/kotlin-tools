package com.myorg.kotlintools.time.infrastructure.dto.instant

data class InstantlyEntryDto<TKey, TValue>(
    val key: TKey,
    val timestampMillis: Long,
    val value: TValue
)