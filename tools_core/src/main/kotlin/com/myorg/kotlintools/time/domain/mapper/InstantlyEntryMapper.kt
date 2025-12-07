package com.myorg.kotlintools.time.domain.mapper


import com.myorg.kotlintools.time.domain.model.instant.InstantlyEntry
import com.myorg.kotlintools.time.domain.model.month.MonthlyEntry
import com.myorg.kotlintools.time.domain.model.year.YearlyEntry
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId

fun <TKey, TValue> InstantlyEntry<TKey, TValue>.toMonthlyEntry(
    zone: ZoneId = ZoneId.systemDefault()
): MonthlyEntry<TKey, TValue> {
    val ym = YearMonth.from(timestamp.atZone(zone))
    return MonthlyEntry(
        key = key,
        value = value,
        timestamp = ym
    )
}


fun <TKey, TValue> InstantlyEntry<TKey, TValue>.toYearlyEntry(
    zone: ZoneId = ZoneId.systemDefault()
): YearlyEntry<TKey, TValue> {
    val year = Year.of(timestamp.atZone(zone).year)
    return YearlyEntry(
        key = key,
        value = value,
        timestamp = year
    )
}