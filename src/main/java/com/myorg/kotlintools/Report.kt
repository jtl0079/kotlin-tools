package com.myorg.kotlintools

import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class ReportData<T>(
    val key: String,
    val value: T,
    val dateTime: Instant,
)

interface ReportImpl<T> {
    fun add(key: String, date: LocalDate, value: T)
    fun get(key: String): Map<String, List<T>>
}

data class MonthlyReportData<T>(
    val incrementalData: MutableList<ReportData<T>>,
    val baseValue:T,
)


class MonthlyReport<T>  {

    // key -> month(yyyy-MM) -> 数据
    private val Data: MutableMap<String, MutableMap<String, MonthlyReportData<T>>> = mutableMapOf()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM")

    /**
     *
     * 功能介绍
     * 1. 储存单个 reportData（key，value，准确时间）
     * 2. 计算不同 key 的不同年份和不同月份的数据总和（baseValue + sum（incrementalData））
     * 3. 使用 f2 获得数据总和后，把 baseValue = baseValue + sum（incrementalData，之后清除 incrementalData 以减少储存空间的使用
     *
    */


}