package com.myorg.kotlintools.report.domain.model

import com.myorg.kotlintools.ValueOperator
import java.time.Year


class ReportBucket<T> (
    private val operator: ValueOperator<T>
){
    private val monthly = MonthlyReportMap<T>(operator)
    private val yearly:MutableMap<Year, T> = mutableMapOf()

    fun addValue(reportEntries :ReportEntries<T>){
        reportEntries.get().forEach{ reportEntry ->
            monthly.addValue(reportEntry)
        }


    }
}

class Report<T> (
    private val operator: ValueOperator<T>
)
{
    private val reportEntries = ReportEntries<T>(operator)
    private val reportBuckets: MutableMap<String, ReportBucket<T>> = mutableMapOf()

    fun settle(){


    }
}