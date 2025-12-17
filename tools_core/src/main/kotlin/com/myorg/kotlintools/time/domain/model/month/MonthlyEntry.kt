package com.myorg.kotlintools.time.domain.model.month

import com.myorg.kotlintools.time.domain.model.base.TimeEntryBase
import java.time.YearMonth

data class MonthlyEntry<TKey, TValue>(
    override val key: TKey,
    override val timestamp: YearMonth,
    override val value: TValue
) : TimeEntryBase<TKey, YearMonth, TValue>
{
    override fun withValue(newValue: TValue): TimeEntryBase<TKey, YearMonth, TValue> {
        return copy(value = newValue)
    }
}
