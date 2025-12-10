package com.myorg.kotlintools.time.domain.model.alltime

import com.myorg.kotlintools.time.domain.model.base.TimeEntryBase
import com.myorg.kotlintools.time.domain.model.base.TimeMapGroupBase
import com.myorg.kotlintools.time.domain.model.month.MonthlyMap
import com.myorg.kotlintools.time.domain.model.year.YearlyMap
import java.time.Instant
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId

data class AllTimeMapGroup<TValue>(
    val monthlyMap: MonthlyMap<TValue>,
    val yearlyMap: YearlyMap<TValue>
) : TimeMapGroupBase<TValue, MonthlyMap<TValue>> {

    override fun sumValue(entry: TimeEntryBase<*, *, *>) {
        val timestamp = entry.timestamp as Instant
        val value = entry.value as TValue


        val zoned = timestamp.atZone(ZoneId.systemDefault())
        val ym = YearMonth.from(zoned)
        val year = zoned.year

        // 写入 monthly
        monthlyMap.sumValue(ym, value)

        // 写入 yearly
        yearlyMap.sumValue(Year.of(year), value)
    }
}