package com.myorg.kotlintools.report.domain.repository

import com.myorg.kotlintools.report.*
import com.myorg.kotlintools.report.domain.model.ReportEntry
import java.time.YearMonth

// ============================================================================
// Repository Interface - 仓储接口（在 Domain 层定义，Repository 层实现）
// ============================================================================

/**
 * 报表仓储接口
 *
 * 职责：
 * - 定义数据存取契约
 * - 不包含业务逻辑
 * - 只关注 CRUD 操作
 *
 * 注意：这个接口应该放在 domain 包下，实现放在 repository 包下
 */
interface ReportRepository<T> : ReportCreateRepository<T>
{

    // -------------------- 命令操作（Command） --------------------
    // CRUD
    fun save(entry: ReportEntry<T>)
    fun saveBatch(entries: List<ReportEntry<T>>)
    fun settle(key: String, month: YearMonth)
    fun settleAll()
    fun clear()
    fun remove(key: String): Boolean

    // -------------------- 查询操作（Query） --------------------

    fun findKeys(): Set<String>
    fun findMonths(key: String): Set<YearMonth>
    fun findBaseValue(key: String, month: YearMonth): T
    fun findIncrementalValue(key: String, month: YearMonth): T
    fun findTotalValue(key: String, month: YearMonth): T
    fun findAllMonthlyValues(key: String): Map<YearMonth, T>
    fun findIncrementalEntries(key: String, month: YearMonth): List<ReportEntry<T>>
}
