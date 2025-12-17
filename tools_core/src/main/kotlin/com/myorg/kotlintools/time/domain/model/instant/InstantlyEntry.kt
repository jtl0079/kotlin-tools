package com.myorg.kotlintools.time.domain.model.instant

import com.myorg.kotlintools.time.domain.model.base.TimeEntryBase
import java.time.Instant

data class InstantlyEntry<TKey, TValue>(
    override val key: TKey,
    override val timestamp: Instant,
    override val value: TValue
) : TimeEntryBase<TKey, Instant, TValue>{

    override fun withValue(newValue: TValue): TimeEntryBase<TKey, Instant, TValue> {
        return copy(value = newValue)
    }
}


