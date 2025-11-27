package com.myorg.kotlintools.report


import com.myorg.kotlintools.ReportStats
import com.myorg.kotlintools.ReportSummary
import com.myorg.kotlintools.ValueOperator
import com.myorg.kotlintools.report.domain.model.ReportEntry
import com.myorg.kotlintools.report.domain.repository.ReportRepository
import java.time.Instant
import java.time.YearMonth

// ============================================================================
// Use Cases - 用例（应用业务逻辑）
// ============================================================================

/**
 * 用例基类
 *
 * 职责：
 * - 协调 Repository 和 Domain Service
 * - 包含应用层的业务逻辑
 * - 不包含存储逻辑
 */
abstract class UseCase

// ============================================================================
// 1. 添加报表数据用例
// ============================================================================

/**
 * 添加单条报表数据
 *
 * 业务规则：
 * - 验证 key 不为空
 * - 验证时间戳不在未来
 */
class AddReportEntryUseCase<T>(
    private val repository: ReportRepository<T>
) : UseCase() {

    fun execute(key: String, value: T, timestamp: Instant) {
        require(key.isNotBlank()) { "Report key cannot be blank" }
        require(timestamp <= Instant.now()) { "Timestamp cannot be in the future" }

        val entry = ReportEntry(key, value, timestamp)
        repository.save(entry)
    }

    fun execute(entry: ReportEntry<T>) {
        require(entry.timestamp <= Instant.now()) { "Timestamp cannot be in the future" }
        repository.save(entry)
    }
}

/**
 * 批量添加报表数据
 */
class AddReportEntriesBatchUseCase<T>(
    private val repository: ReportRepository<T>
) : UseCase() {

    fun execute(entries: List<ReportEntry<T>>) {
        require(entries.isNotEmpty()) { "Entries list cannot be empty" }

        // 业务规则：验证所有条目
        entries.forEach { entry ->
            require(entry.key.isNotBlank()) { "Report key cannot be blank" }
            require(entry.timestamp <= Instant.now()) { "Timestamp cannot be in the future" }
        }

        repository.saveBatch(entries)
    }
}


// ============================================================================
// 2. 查询报表数据用例
// ============================================================================

/**
 * 查询报表汇总信息
 *
 * 业务逻辑：
 * - 组合 baseValue 和 incrementalValue
 * - 计算增量数据条数
 */
class GetReportSummaryUseCase<T>(
    private val repository: ReportRepository<T>
) : UseCase() {

    fun execute(key: String, month: YearMonth): ReportSummary<T> {
        val baseValue = repository.findBaseValue(key, month)
        val incrementalValue = repository.findIncrementalValue(key, month)
        val totalValue = repository.findTotalValue(key, month)
        val entries = repository.findIncrementalEntries(key, month)

        return ReportSummary(
            key = key,
            month = month,
            baseValue = baseValue,
            incrementalValue = incrementalValue,
            totalValue = totalValue,
            incrementalCount = entries.size
        )
    }
}

/**
 * 查询指定 key 的所有月份汇总
 */
class GetMonthlyReportUseCase<T>(
    private val repository: ReportRepository<T>,
    private val operator: ValueOperator<T>
) : UseCase() {

    /**
     * 获取指定 key 的月度报表
     *
     * @return Map<YearMonth, T> 按月份排序
     */
    fun execute(key: String): Map<YearMonth, T> {
        return repository.findAllMonthlyValues(key)
            .toSortedMap()
    }


}

/**
 * 查询所有 keys
 */
class GetAllKeysUseCase<T>(
    private val repository: ReportRepository<T>
) : UseCase() {

    fun execute(): Set<String> = repository.findKeys()
}



// ============================================================================
// 3. 结算报表数据用例
// ============================================================================

/**
 * 结算策略接口
 */
interface SettlementStrategy {
    fun shouldSettle(incrementalCount: Int, ageInDays: Long): Boolean
}

/**
 * 默认结算策略：增量数据超过阈值或超过指定天数
 */
class DefaultSettlementStrategy(
    private val incrementalThreshold: Int = 1000,
    private val maxAgeInDays: Long = 30
) : SettlementStrategy {

    override fun shouldSettle(incrementalCount: Int, ageInDays: Long): Boolean {
        return incrementalCount >= incrementalThreshold || ageInDays >= maxAgeInDays
    }
}

/**
 * 智能结算用例
 *
 * 业务逻辑：
 * - 根据策略决定是否结算
 * - 记录结算日志
 */
class SettleReportUseCase<T>(
    private val repository: ReportRepository<T>,
    private val strategy: SettlementStrategy = DefaultSettlementStrategy()
) : UseCase() {

    /**
     * 结算指定 key 和月份
     */
    fun execute(key: String, month: YearMonth) {
        repository.settle(key, month)
    }

    /**
     * 结算指定 key 的所有月份
     */
    fun execute(key: String) {
        val months = repository.findMonths(key)
        months.forEach { month ->
            repository.settle(key, month)
        }
    }

    /**
     * 结算所有数据
     */
    fun executeAll() {
        repository.settleAll()
    }

    /**
     * 智能结算：根据策略决定
     *
     * @return 被结算的 key 列表
     */
    fun executeSmartSettlement(): List<String> {
        val settledKeys = mutableListOf<String>()
        val allKeys = repository.findKeys()

        allKeys.forEach { key ->
            val months = repository.findMonths(key)
            months.forEach { month ->
                val entries = repository.findIncrementalEntries(key, month)

                // 这里简化处理，实际应计算数据的年龄
                if (strategy.shouldSettle(entries.size, 0)) {
                    repository.settle(key, month)
                    settledKeys.add("$key:$month")
                }
            }
        }

        return settledKeys
    }
}


// ============================================================================
// 4. 导出报表数据用例
// ============================================================================

/**
 * 导出格式
 */
sealed class ExportFormat {
    object CSV : ExportFormat()
    object JSON : ExportFormat()
    data class Custom(val formatter: (ReportEntry<*>) -> String) : ExportFormat()
}

/**
 * 导出报表数据
 *
 * 业务逻辑：
 * - 按月份排序
 * - 格式化输出
 */
class ExportReportUseCase<T>(
    private val repository: ReportRepository<T>
) : UseCase() {

    /**
     * 导出指定 key 的月度数据
     */
    fun execute(key: String): List<Pair<YearMonth, T>> {
        return repository.findAllMonthlyValues(key)
            .toList()
            .sortedBy { it.first }
    }

    /**
     * 导出指定 key 和月份的原始数据
     */
    fun executeRawData(key: String, month: YearMonth): List<ReportEntry<T>> {
        return repository.findIncrementalEntries(key, month)
    }

    /**
     * 导出所有 keys 的汇总数据
     */
    fun executeAllKeys(): Map<String, Map<YearMonth, T>> {
        val allKeys = repository.findKeys()
        return allKeys.associateWith { key ->
            repository.findAllMonthlyValues(key)
        }
    }
}


// ============================================================================
// 5. 数据清理用例
// ============================================================================

/**
 * 清理报表数据
 *
 * 业务逻辑：
 * - 清理旧数据
 * - 删除指定 key
 */
class CleanReportUseCase<T>(
    private val repository: ReportRepository<T>
) : UseCase() {

    /**
     * 删除指定 key 的所有数据
     */
    fun execute(key: String): Boolean {
        return repository.remove(key)
    }

    /**
     * 清空所有数据
     */
    fun executeAll() {
        repository.clear()
    }

    /**
     * 清理空的 key（没有任何数据的 key）
     *
     * @return 被清理的 key 数量
     */
    fun executeEmptyKeys(): Int {
        val allKeys = repository.findKeys()
        var cleanedCount = 0

        allKeys.forEach { key ->
            val months = repository.findMonths(key)
            if (months.isEmpty()) {
                repository.remove(key)
                cleanedCount++
            }
        }

        return cleanedCount
    }
}