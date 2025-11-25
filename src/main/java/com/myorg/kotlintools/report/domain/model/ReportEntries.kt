package com.myorg.kotlintools.report.domain.model

import com.myorg.kotlintools.ValueOperator
import com.myorg.kotlintools.report.domain.service.MonthlyReportEntryGetter
import com.myorg.kotlintools.sumWith
import java.time.YearMonth
import java.time.ZoneId


class ReportEntries<T>(
    private val operator: ValueOperator<T>
) : MonthlyReportEntryGetter<T> {
    private val incremental: MutableList<ReportEntry<T>> = mutableListOf()

    fun get(): List<ReportEntry<T>> = incremental

    override fun getMonthlyReportEntry(
        key: String,
        yearMonth: YearMonth
    ): MonthlyReportEntry<T> {

        val zone = ZoneId.systemDefault()

        val filtered = incremental.filter {
            it.key == key &&
                    YearMonth.from(it.timestamp.atZone(zone)) == yearMonth
        }

        val total = filtered.sumWith(operator) { it.value } // <-- selector: (ReportEntry<T>) -> T

        return MonthlyReportEntry(
            key = key,
            value = total,
            yearMonthStamp = yearMonth
        )
    }


}