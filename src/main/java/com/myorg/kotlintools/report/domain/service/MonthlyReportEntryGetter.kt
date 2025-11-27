package com.myorg.kotlintools.report.domain.service

import com.myorg.kotlintools.report.domain.model.MonthlyReportEntry
import com.myorg.kotlintools.report.domain.model.ReportEntry
import java.time.YearMonth

/**
 * 查询
 * key --> stamp --> value
 *
 * 储存
 * entry -> map -> bucket -> obj (report)
 * 传入 ReportEntry 后，每个不同的 stamp 对应一种 bucket，这样可以添加不同的 bucket 满足不同需求的 report 时间线，
 *
 */

/**
 * ReportEntry (key:value)+(绝对时间-->Instant)：
 * - 单条 Entry 每个都是一个主体
 *
 * MonthlyEntry (key:value)+(时间记录范围:年月:YearMonth)
 * YearlyEntry (key:value)+(时间记录范围:年)
 */

/**
 *
 */
interface MonthlyReportEntryGetter<T> {
    fun getMonthlyReportEntry(key: String, yearMonth: YearMonth): MonthlyReportEntry<T>
}



/**
 * 强制子类实现返回 MonthlyEntry 类型的函数
 * */
interface MonthlyEntriesGetter{   // GroupBy 年月
    fun getMonthlyEntry()
}

interface DailyEntriesGetter{ // GroupBy 年月日
    fun getDailyEntry()

}