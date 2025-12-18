package com.myorg.kotlintools.time.infrastructure.mapper.instant

import com.myorg.kotlintools.time.domain.model.instant.InstantlyEntry
import com.myorg.kotlintools.time.infrastructure.dto.instant.InstantlyEntryDto
import java.time.Instant


fun <K, V : Any> InstantlyEntryDto<K, V>.toDomain(): InstantlyEntry<K, V> {
    return InstantlyEntry(
        key = key,
        timestamp = Instant.ofEpochMilli(timestampMillis),
        value = value
    )
}

fun <K, V> InstantlyEntry<K, V>.toDto(): InstantlyEntryDto<K, V> {
    return InstantlyEntryDto(
        key = key,
        timestampMillis = timestamp.toEpochMilli(),
        value = value
    )
}