package com.myorg.kotlintools.time.domain.model.alltime

import com.myorg.kotlintools.time.domain.model.base.TimeDataBundleBase
import com.myorg.kotlintools.time.domain.model.base.TimeEntryBase
import com.myorg.kotlintools.time.domain.model.instant.InstantlyEntries
import com.myorg.kotlintools.time.domain.model.instant.InstantlyEntry
import java.time.Instant

data class AllTimeDataBundle<TKey, TValue>(
    override val timeEntries: InstantlyEntries<TKey, TValue> = InstantlyEntries(),
    override val keyTimeMap: KeyAllTimeMap<TKey, TValue> = KeyAllTimeMap()
) : TimeDataBundleBase<
        InstantlyEntry<TKey, TValue>,
        InstantlyEntries<TKey, TValue>,
        KeyAllTimeMap<TKey, TValue>
        >
{

}