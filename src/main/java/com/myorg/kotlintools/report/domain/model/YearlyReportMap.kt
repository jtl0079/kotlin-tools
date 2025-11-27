package com.myorg.kotlintools.report.domain.model

import com.myorg.kotlintools.ValueOperator
import com.myorg.kotlintools.report.domain.mapper.toMonthlyReportEntry
import java.time.Year
import java.time.YearMonth

class YearlyReportMap<T>(
    private val operator: ValueOperator<T>

) {

    private val data: MutableMap<Year, T> = mutableMapOf()

    fun get(): MutableMap<Year, T> = data

    fun clear() = data.clear()

    fun addValue(monthlyMap: Map<YearMonth, T>) {
        monthlyMap.forEach { (yearMonth, value) ->
            val year = Year.of(yearMonth.year)
            data[year] = if (data.containsKey(year)) {
                operator.add(data[year]!!, value)
            } else {
                value
            }
        }
    }

    fun setValue(monthlyMap: Map<YearMonth, T>) {
        monthlyMap.forEach { (yearMonth, value) ->
            val year = Year.of(yearMonth.year)
            data[year] = if (data.containsKey(year)) {
                operator.add(data[year]!!, value)
            } else {
                value
            }
        }
    }

    fun setValue(monthlyReportMap: MonthlyReportMap<T>){
        setValue(monthlyReportMap)
    }
}