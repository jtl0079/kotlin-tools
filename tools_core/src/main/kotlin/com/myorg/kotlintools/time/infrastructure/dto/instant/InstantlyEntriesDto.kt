package com.myorg.kotlintools.time.infrastructure.dto.instant

data class InstantlyEntriesDto<TKey, TValue>(
    val entries: List<InstantlyEntryDto<TKey, TValue>>
    // DTO 不携带 ValueOperator 因为那是 domain 行为
)