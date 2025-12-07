package com.myorg.kotlintools.time.domain.model.alltime

import com.myorg.kotlintools.time.domain.model.base.TimeDataBundleBase
import com.myorg.kotlintools.time.domain.model.instant.InstantlyEntries

data class AllTimeDataBundle<TKey, TValue>(
    override val timeEntries: InstantlyEntries<TKey, TValue> = InstantlyEntries(),
    override val keyTimeMap: KeyAllTimeMap<TKey, TValue> = KeyAllTimeMap()
) : TimeDataBundleBase<
        InstantlyEntries<TKey, TValue>,
        KeyAllTimeMap<TKey, TValue>
        >
{

}