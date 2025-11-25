package com.myorg.kotlintools.report.domain.model

import com.myorg.kotlintools.ValueOperator
import com.myorg.kotlintools.report.domain.mapper.toMonthlyReportEntry
import java.time.YearMonth

class MonthlyReportMap<T>(
    private val operator: ValueOperator<T>
) {

    private val data: MutableMap<YearMonth, T> = mutableMapOf()

    fun get(): MutableMap<YearMonth, T> = data

    fun addValue(monthlyReportEntry: MonthlyReportEntry<T>) {
        val month = monthlyReportEntry.yearMonthStamp
        val value = monthlyReportEntry.value

        data[month] = if (data.containsKey(month)) {
            operator.add(data[month]!!, value)
        } else {
            value
        }
    }

    fun addValue(reportEntry: ReportEntry<T>) {
        addValue(reportEntry.toMonthlyReportEntry())
    }
}