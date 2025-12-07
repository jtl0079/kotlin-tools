package com.myorg.kotlintools.time.domain.model.alltime

import com.myorg.kotlintools.time.domain.model.base.KeyTimeMapBase

data class KeyAllTimeMap<TKey, TValue>(
    override val keyTimeMap: MutableMap<TKey, AllTimeMapGroup<TValue>>
) : KeyTimeMapBase<TKey, AllTimeMapGroup<TValue>> {

}