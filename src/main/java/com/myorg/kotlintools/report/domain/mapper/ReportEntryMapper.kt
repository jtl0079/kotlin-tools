package com.myorg.kotlintools.report.domain.mapper

import com.myorg.kotlintools.report.domain.model.MonthlyReportEntry
import com.myorg.kotlintools.report.domain.model.ReportEntry
import com.myorg.kotlintools.report.domain.model.YearlyReportEntry
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId

/**
 * ReportEntry<T>  ->  monthlyReportEntry<T>
 * ReportEntry<T>  ->  YearlyReportEntry<T>
 */

fun <T> ReportEntry<T>.toMonthlyReportEntry(): MonthlyReportEntry<T> {
    return MonthlyReportEntry(
        key = key,
        value = value,
        yearMonthStamp = YearMonth.from(timestamp.atZone(ZoneId.systemDefault()))
    )
}


fun <T> ReportEntry<T>.toYearlyReportEntry(): YearlyReportEntry<T> {
    return YearlyReportEntry(
        key = key,
        value = value,
        yearStamp = Year.from(timestamp.atZone(ZoneId.systemDefault()))
    )
}