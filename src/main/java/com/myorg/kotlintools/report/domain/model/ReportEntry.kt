package com.myorg.kotlintools.report.domain.model

import java.time.Instant

/**
 * 原始报表数据条目
 *
 * @param T 值类型
 * @property key 数据分类键（如：产品ID、部门代码）
 * @property value 数据值
 * @property timestamp 数据产生的精确时间
 */
data class ReportEntry<T>(
    val key: String,
    val value: T,
    val timestamp: Instant
)

