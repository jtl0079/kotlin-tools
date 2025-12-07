package com.myorg.kotlintools.time.domain.mapper

import com.myorg.kotlintools.time.domain.model.month.MonthlyEntry
import com.myorg.kotlintools.time.domain.model.year.YearlyEntry
import java.time.Year
import java.time.ZoneId


fun <TKey, TValue> MonthlyEntry<TKey, TValue>.toYearlyEntry(): YearlyEntry<TKey, TValue> {
    return YearlyEntry(
        key = key,
        value = value,
        timestamp = Year.of(timestamp.year)
    )
}