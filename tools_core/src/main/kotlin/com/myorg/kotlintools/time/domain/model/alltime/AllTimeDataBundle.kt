package com.myorg.kotlintools.time.domain.model.alltime

import com.myorg.kotlintools.time.domain.model.base.TimeDataBundleBase
import com.myorg.kotlintools.time.domain.model.base.TimeEntryBase
import com.myorg.kotlintools.time.domain.model.instant.InstantlyEntries
import com.myorg.kotlintools.time.domain.model.instant.InstantlyEntry
import com.myorg.kotlintools.valueOperatorOf
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class AllTimeDataBundle<TKey, TValue : Any>(
    override val timeEntries: InstantlyEntries<TKey, TValue> ,
    override val keyTimeMap: KeyAllTimeMap<TKey, TValue> = KeyAllTimeMap()
) : TimeDataBundleBase<
        TKey,
        Instant,
        TValue,
        InstantlyEntry<TKey, TValue>,
        InstantlyEntries<TKey, TValue>,
        KeyAllTimeMap<TKey, TValue>
        > {

    fun getMonthlyValue(entry: TimeEntryBase<*, *, *>): TValue? = keyTimeMap.getMonthlyValue(entry)

    fun getYearlyValue(entry: TimeEntryBase<*, *, *>): TValue? = keyTimeMap.getYearlyValue(entry)


}


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


    bundle.syncMapByEntries()

    print("[DATA] \n")
    print("[Entries] \n")
    bundle.timeEntries.printAll()
    print("\n\n")

    print("[Map] \n")
    bundle.keyTimeMap.keyTimeMap.entries.forEach { (key, group) ->
        print("${key} \n")
        group.monthlyMap.printAll()
        group.yearlyMap.printAll()
    }
    print("\n\n")

    print("[STATISTIC] \n")
    bundle.keyTimeMap.keyTimeMap.entries.forEach { (key, group) ->


    }

    print("[Here End]\n")
}

