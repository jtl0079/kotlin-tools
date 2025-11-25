package com.myorg.kotlintools.report.domain.model

import java.time.Year
import java.time.YearMonth

/**
 * 原始报表数据条目
 *
 * @param T 值类型
 * @property key 数据分类键（如：产品ID、部门代码）
 * @property value 数据值
 * @property yearStamp 数据产生的年
 */
data class YearlyReportEntry<T>(
    val key: String,
    val value: T,
    val yearStamp: Year
)