package com.myorg.kotlintools.time.domain.model.instant

import com.myorg.kotlintools.time.domain.model.base.TimeEntriesBase

data class InstantlyEntries<TKey, TValue>(
    override val entries: MutableList<InstantlyEntry<TKey, TValue>> = mutableListOf()
) : TimeEntriesBase<InstantlyEntry<TKey, TValue>> {

}