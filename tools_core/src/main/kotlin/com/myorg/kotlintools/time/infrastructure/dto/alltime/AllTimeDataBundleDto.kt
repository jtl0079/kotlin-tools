package com.myorg.kotlintools.time.infrastructure.dto.alltime

import com.myorg.kotlintools.time.domain.model.alltime.AllTimeDataBundle
import com.myorg.kotlintools.time.domain.model.instant.InstantlyEntries
import com.myorg.kotlintools.time.domain.model.instant.InstantlyEntry
import com.myorg.kotlintools.time.infrastructure.dto.instant.InstantlyEntryDto
import com.myorg.kotlintools.time.infrastructure.mapper.alltime.toDto
import com.myorg.kotlintools.valueOperatorOf
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.collections.component1
import kotlin.collections.component2

data class AllTimeDataBundleDto<TKey, TValue>(
    val entries: List<InstantlyEntryDto<TKey, TValue>>,
    //val keyTimeMap: Map<TKey, KeyAllTimeMapDto<TKey, TValue>>
)


fun main() {
    print("[Here start]\n")
    val bundle =
        AllTimeDataBundle<String, Double>(
            timeEntries = InstantlyEntries(valueOperator = valueOperatorOf())
        )


    bundle.addEntry(
        entry = InstantlyEntry(
            key = "key 1",
            timestamp = LocalDateTime.of(2025, 12, 16, 21, 30, 0)
                .atZone(ZoneId.of("Asia/Kuala_Lumpur"))
                .toInstant(),
            value = 1.0

        )
    )
    bundle.addEntry(
        entry = InstantlyEntry(
            key = "key 1",
            timestamp = LocalDateTime.of(2025, 12, 16, 21, 30, 0)
                .atZone(ZoneId.of("Asia/Kuala_Lumpur"))
                .toInstant(),
            value = 1.2

        )
    )
    bundle.addEntry(
        entry = InstantlyEntry(
            key = "key 1",
            timestamp = LocalDateTime.of(2026, 11, 16, 21, 30, 0)
                .atZone(ZoneId.of("Asia/Kuala_Lumpur"))
                .toInstant(),
            value = 1.0

        )
    )
    bundle.addEntry(
        entry = InstantlyEntry(
            key = "key 2",
            timestamp = LocalDateTime.of(2025, 12, 17, 21, 30, 0)
                .atZone(ZoneId.of("Asia/Kuala_Lumpur"))
                .toInstant(),
            value = 2.0

        )
    )


    val dto = bundle.toDto()

    println(dto)
    print("[Here End]\n")
}

