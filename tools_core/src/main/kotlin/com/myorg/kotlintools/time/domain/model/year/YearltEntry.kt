package com.myorg.kotlintools.time.domain.model.year

import com.myorg.kotlintools.time.domain.model.base.TimeEntryBase
import java.time.Year

data class YearlyEntry<TKey, TValue>(
    override val key: TKey,
    override val timestamp: Year,
    override val value: TValue
) : TimeEntryBase<TKey, Year, TValue>
{
    override fun withValue(newValue: TValue): TimeEntryBase<TKey, Year, TValue> {
        return copy(value = newValue)
    }
}
