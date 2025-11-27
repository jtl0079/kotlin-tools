package com.myorg.kotlintools.report.domain.model

import com.myorg.kotlintools.ValueOperator

/** 默认的 report bucket
 * 默认储存项目的所有实现的 report
 */
class ReportBucket<T> (
    private val operator: ValueOperator<T>
){
    private val monthly = MonthlyReportMap<T>(operator)
    private val yearly = YearlyReportMap<T>(operator)

    fun addValue(reportEntries :ReportEntries<T>){
        reportEntries.get().forEach{ reportEntry ->
            monthly.addValue(reportEntry)
        }

        yearly.setValue(monthly.get())
    }
    fun setValue(reportEntries :ReportEntries<T>){
        monthly.setValue(reportEntries)
        yearly.setValue(monthly)
    }
}