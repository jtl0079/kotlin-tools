package com.myorg.kotlintools.report.domain.mapper

import com.myorg.kotlintools.report.domain.model.MonthlyReportEntry
import com.myorg.kotlintools.report.domain.model.YearlyReportEntry
import java.time.Year

fun <T> MonthlyReportEntry<T>.toYearlyReportEntry(): YearlyReportEntry<T> {
    return YearlyReportEntry(
        key = key,
        value = value,
        yearStamp = Year.of(yearMonthStamp.year)
    )
}
