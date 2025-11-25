package com.myorg.kotlintools

import com.myorg.kotlintools.report.domain.model.ReportEntry
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 *
 * UI ，ViewModel，Service， Use Case ，Repository，Repository interface，DAO ，Domain
 *
 * 功能介绍
 * 1. 储存单个 reportData（key，value，精确时间）
 * 2. 计算不同 key 的不同年份和不同月份的数据总和（baseValue + sum（incrementalData））
 * 3. 使用 f2 获得数据总和后，把 baseValue = baseValue + sum（incrementalData，之后清除 incrementalData 以减少储存空间的使用
 *
 */

/**
 * 报表聚合结果（值对象）
 */
data class ReportSummary<T>(
    val key: String,
    val month: YearMonth,
    val baseValue: T,
    val incrementalValue: T,
    val totalValue: T,
    val incrementalCount: Int
)




/**
 * 月度聚合数据
 *
 * 采用"基础值 + 增量列表"模式：
 * - baseValue: 已结算的累计值（不可变快照）
 * - incremental: 待结算的增量数据
 *
 * 使用读写锁保证线程安全，支持高并发读取
 *
 * @param T 值类型
 * @property operator 值运算器
 */
class MonthlyBucket<T>(
    private val operator: ValueOperator<T>
)
{

    private val lock = ReentrantReadWriteLock()

    private var _baseValue: T = operator.zero()
    private val _incremental: MutableList<ReportEntry<T>> = mutableListOf()

    // -------------------- 读取操作 --------------------

    /** 获取基础值 */
    val baseValue: T
        get() = lock.read { _baseValue }

    /** 获取增量数据数量 */
    val incrementalCount: Int
        get() = lock.read { _incremental.size }

    /** 计算增量数据的总和 */
    fun getSumOfIncremental(): T = lock.read {
        _incremental.sumWith(operator) { it.value }
    }

    /** 计算总和（基础值 + 增量） */
    fun getSum(): T = lock.read {
        val incSum = _incremental.sumWith(operator) { it.value }
        operator.add(_baseValue, incSum)
    }

    /** 获取增量数据快照（用于调试或导出） */
    fun incrementalSnapshot(): List<ReportEntry<T>> = lock.read {
        _incremental.toList()
    }

    // -------------------- 写入操作 --------------------

    /** 添加增量数据 */
    fun addIncremental(entry: ReportEntry<T>) = lock.write {
        _incremental.add(entry)
    }

    /**
     * 结算：将增量合并到基础值并清空增量列表, baseValue = baseValue + incremental
     *
     * @return 本次结算的增量总和
     */
    fun settle(): T = lock.write {
        if (_incremental.isEmpty()) {
            return@write operator.zero()
        }
        val sum = _incremental.sumWith(operator) { it.value }
        _baseValue = operator.add(_baseValue, sum)
        _incremental.clear()
        sum
    }

    /** 清空所有数据（重置为零） */
    fun clear() = lock.write {
        _baseValue = operator.zero()
        _incremental.clear()
    }
}


/**
 * addIncremental 功能：添加原始数据
 * 1. 传入 (ReportEntry)
 * 2. 传入 (key: string, value: T, dateTime: Instant)
 *
 * getSumOfIncremental 功能：获取 Incremental 的总和
 * 1. 传入 (key: string, month: YearMonth), 获得该（key & month）对应的 Incremental 的总和
 *
 * getSum 功能：获取总和（base + incremental）
 * 1. 传入 (key: string, month: YearMonth)，获得某 key 在给定 YearMonth 的总和
 * 2. 传入 (key: string)，获取某 key 在所有月份的总和（Map<YearMonth, T> // map: YearMonth -> total）
 *
 * settle 功能：将 incrementalData 合并到 baseValue 并清空 incrementalData（PS. 节省空间）
 * 1. 无传入，将所有的 incrementalData 按 key 和 YearMonth 合并到 baseValue 并清空 incrementalData
 */
interface Report<T> {

    // -------------------- 写入 --------------------

    /** 添加数据条目 */
    fun addIncremental(entry: ReportEntry<T>)

    /** 添加数据（便捷方法） */
    fun addIncremental(key: String, value: T, timestamp: Instant)

    // -------------------- 查询 --------------------

    /** 获取所有 key */
    fun keys(): Set<String>

    /** 获取指定 key 的所有月份 */
    fun months(key: String): Set<YearMonth>

    /** 获取指定 key 和月份的增量总和（未结算部分|Incremental） */
    fun getIncrementalSum(key: String, month: YearMonth): T

    /** 获取指定 key 和月份的总和 (baseValue + Incremental) */
    fun getSum(key: String, month: YearMonth): T

    /** 获取指定 key 所有月份的总和 (baseValue + Incremental) */
    fun getEachYearMonthSumOfKey(key: String): Map<YearMonth, T>

    /** 获取指定 key 的全部月份总和 */
    fun getSumOfKey(key: String): T

    // -------------------- 结算与管理 --------------------

    /** 结算所有数据 */
    fun settle()

    /** 结算指定 key 的数据 */
    fun settle(key: String)

    /** 结算指定 key 和月份的数据 */
    fun settle(key: String, month: YearMonth)

    /** 清空所有数据 */
    fun clear()

    /** 移除指定 key 的所有数据 */
    fun remove(key: String): Boolean

    // -------------------- 统计 --------------------

    /** 获取统计信息 */
    fun stats(): ReportStats
}


/**
 * 报表统计信息
 */
data class ReportStats(
    val keyCount: Int,
    val bucketCount: Int,
    val totalIncrementalCount: Int
)

/**
 * 月度报表实现
 *
 * 特性：
 * - 线程安全：使用 ConcurrentHashMap + 读写锁
 * - 高并发读取：读操作不阻塞
 * - 内存友好：支持增量结算释放内存
 *
 * 使用示例：
 * ```
 * val report = MonthlyReport(IntOperator)
 *
 * // 添加数据
 * report.add("product-A", 100, Instant.now())
 * report.add("product-A", 50, Instant.now())
 *
 * // 查询
 * val total = report.sum("product-A", YearMonth.now())  // => 150
 *
 * // 结算（释放内存）
 * report.settle()
 * ```
 *
 * @param T 值类型
 * @param operator 值运算器
 * @param zone 时区（用于将 Instant 转换为 YearMonth）
 */
class MonthlyReport<T>(
    private val operator: ValueOperator<T>,
    private val zone: ZoneId = ZoneId.systemDefault()
) : Report<T> {

    // 数据结构: key -> (YearMonth -> MonthlyBucket)
    private val data = ConcurrentHashMap<String, ConcurrentHashMap<YearMonth, MonthlyBucket<T>>>()


    // -------------------- 内部辅助方法 --------------------

    /** 获取或创建 bucket */
    private fun bucketOf(key: String, month: YearMonth): MonthlyBucket<T> {
        return data
            .getOrPut(key) { ConcurrentHashMap() }
            .getOrPut(month) { MonthlyBucket(operator) }
    }

    /** 安全获取 bucket（可能为 null） */
    private fun bucketOrNull(key: String, month: YearMonth): MonthlyBucket<T>? {
        return data[key]?.get(month)
    }

    /** 从 Instant 提取 YearMonth */
    private fun Instant.toYearMonth(): YearMonth {
        return YearMonth.from(this.atZone(zone))
    }

    // -------------------- 写入操作 --------------------

    override fun addIncremental(entry: ReportEntry<T>) {
        val month = entry.timestamp.toYearMonth()
        bucketOf(entry.key, month).addIncremental(entry)
    }

    override fun addIncremental(key: String, value: T, timestamp: Instant) {
        addIncremental(ReportEntry(key, value, timestamp))
    }

    // -------------------- 查询操作 --------------------

    override fun keys(): Set<String> = data.keys.toSet()

    override fun months(key: String): Set<YearMonth> {
        return data[key]?.keys?.toSet() ?: emptySet()
    }

    override fun getIncrementalSum(key: String, month: YearMonth): T {
        return bucketOrNull(key, month)?.getSumOfIncremental() ?: operator.zero()
    }

    override fun getSum(key: String, month: YearMonth): T {
        return bucketOrNull(key, month)?.getSum() ?: operator.zero()
    }

    override fun getEachYearMonthSumOfKey(key: String): Map<YearMonth, T> {
        val monthMap = data[key] ?: return emptyMap()
        return monthMap.mapValues { (_, bucket) -> bucket.getSum() }
    }

    override fun getSumOfKey(key: String): T {
        val monthMap = data[key] ?: return operator.zero()
        var total = operator.zero()
        monthMap.values.forEach { bucket ->
            total = operator.add(total, bucket.getSum())
        }
        return total
    }

    // -------------------- 结算操作 --------------------

    override fun settle() {
        data.values.forEach { monthMap ->
            monthMap.values.forEach { bucket ->
                bucket.settle()
            }
        }
    }

    override fun settle(key: String) {
        data[key]?.values?.forEach { bucket ->
            bucket.settle()
        }
    }

    override fun settle(key: String, month: YearMonth) {
        bucketOrNull(key, month)?.settle()
    }

    // -------------------- 管理操作 --------------------

    override fun clear() {
        data.clear()
    }

    override fun remove(key: String): Boolean {
        return data.remove(key) != null
    }

    override fun stats(): ReportStats {
        var bucketCount = 0
        var incrementalCount = 0

        data.values.forEach { monthMap ->
            bucketCount += monthMap.size
            monthMap.values.forEach { bucket ->
                incrementalCount += bucket.incrementalCount
            }
        }

        return ReportStats(
            keyCount = data.size,
            bucketCount = bucketCount,
            totalIncrementalCount = incrementalCount
        )
    }
}

inline fun <reified T : Any> MonthlyReportOf(
    zone: ZoneId = ZoneId.systemDefault()
): MonthlyReport<T> {
    return MonthlyReport(
        operator = valueOperatorOf(),
        zone = zone
    )
}