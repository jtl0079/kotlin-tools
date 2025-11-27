package com.myorg.kotlintools.report



import com.myorg.kotlintools.ReportStats
import com.myorg.kotlintools.ValueOperator
import com.myorg.kotlintools.report.domain.model.ReportEntry
import com.myorg.kotlintools.report.domain.repository.ReportRepository
import com.myorg.kotlintools.sumWith
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write


// ============================================================================
// Storage Implementation - 存储单元（内部实现细节）
// ============================================================================

/**
 * 月度存储桶（内部类，不对外暴露）
 *
 * 职责：
 * - 管理单个月份的数据
 * - 线程安全保证
 * - 内存管理（结算）
 */
internal class MonthlyBucket<T>(
    private val operator: ValueOperator<T>
) {
    private val lock = ReentrantReadWriteLock()
    private var _baseValue: T = operator.zero()
    private val _incremental: MutableList<ReportEntry<T>> = mutableListOf()

    val baseValue: T
        get() = lock.read { _baseValue }

    val incrementalCount: Int
        get() = lock.read { _incremental.size }

    fun addIncremental(entry: ReportEntry<T>) = lock.write {
        _incremental.add(entry)
    }

    fun getIncrementalSum(): T = lock.read {
        _incremental.sumWith(operator) { it.value }
    }

    fun getTotalValue(): T = lock.read {
        val incSum = _incremental.sumWith(operator) { it.value }
        operator.add(_baseValue, incSum)
    }

    fun getIncrementalEntries(): List<ReportEntry<T>> = lock.read {
        _incremental.toList()
    }

    fun settle(): T = lock.write {
        if (_incremental.isEmpty()) return@write operator.zero()
        val sum = _incremental.sumWith(operator) { it.value }
        _baseValue = operator.add(_baseValue, sum)
        _incremental.clear()
        sum
    }

    fun clear() = lock.write {
        _baseValue = operator.zero()
        _incremental.clear()
    }
}


// ============================================================================
// Repository Implementation - 仓储实现
// ============================================================================


/**
 * 月度报表仓储实现
 *
 * 职责：
 * - 实现 ReportRepository 接口
 * - 管理数据存储结构
 * - 不包含业务逻辑（业务逻辑在 Use Case 层）
 */
class InMemoryReportRepository<T>(
    private val operator: ValueOperator<T>,
    private val zone: ZoneId = ZoneId.systemDefault()
) : ReportRepository<T> {

    // 数据结构: key -> (YearMonth -> MonthlyBucket)
    private val storage =
        ConcurrentHashMap<String, ConcurrentHashMap<YearMonth, MonthlyBucket<T>>>()

    // -------------------- 内部辅助方法 --------------------

    private fun bucketOf(key: String, month: YearMonth): MonthlyBucket<T> {
        return storage
            .getOrPut(key) { ConcurrentHashMap() }
            .getOrPut(month) { MonthlyBucket(operator) }
    }

    private fun bucketOrNull(key: String, month: YearMonth): MonthlyBucket<T>? {
        return storage[key]?.get(month)
    }

    private fun Instant.toYearMonth(): YearMonth {
        return YearMonth.from(this.atZone(zone))
    }

    // -------------------- 命令操作 --------------------

    override fun save(entry: ReportEntry<T>) {
        val month = entry.timestamp.toYearMonth()
        bucketOf(entry.key, month).addIncremental(entry)
    }

    override fun saveBatch(entries: List<ReportEntry<T>>) {
        entries.forEach { save(it) }
    }

    override fun settle(key: String, month: YearMonth) {
        bucketOrNull(key, month)?.settle()
    }

    override fun settleAll() {
        storage.values.forEach { monthMap ->
            monthMap.values.forEach { bucket ->
                bucket.settle()
            }
        }
    }

    override fun clear() {
        storage.clear()
    }

    override fun remove(key: String): Boolean {
        return storage.remove(key) != null
    }

    // -------------------- 查询操作 --------------------

    override fun findKeys(): Set<String> = storage.keys.toSet()

    override fun findMonths(key: String): Set<YearMonth> {
        return storage[key]?.keys?.toSet() ?: emptySet()
    }

    override fun findBaseValue(key: String, month: YearMonth): T {
        return bucketOrNull(key, month)?.baseValue ?: operator.zero()
    }

    override fun findIncrementalValue(key: String, month: YearMonth): T {
        return bucketOrNull(key, month)?.getIncrementalSum() ?: operator.zero()
    }

    override fun findTotalValue(key: String, month: YearMonth): T {
        return bucketOrNull(key, month)?.getTotalValue() ?: operator.zero()
    }

    override fun findAllMonthlyValues(key: String): Map<YearMonth, T> {
        val monthMap = storage[key] ?: return emptyMap()
        return monthMap.mapValues { (_, bucket) -> bucket.getTotalValue() }
    }

    override fun findIncrementalEntries(key: String, month: YearMonth): List<ReportEntry<T>> {
        return bucketOrNull(key, month)?.getIncrementalEntries() ?: emptyList()
    }

    fun stats(): ReportStats {
        var bucketCount = 0
        var incrementalCount = 0

        storage.values.forEach { monthMap ->
            bucketCount += monthMap.size
            monthMap.values.forEach { bucket ->
                incrementalCount += bucket.incrementalCount
            }
        }

        return ReportStats(
            keyCount = storage.size,
            bucketCount = bucketCount,
            totalIncrementalCount = incrementalCount
        )
    }
}
