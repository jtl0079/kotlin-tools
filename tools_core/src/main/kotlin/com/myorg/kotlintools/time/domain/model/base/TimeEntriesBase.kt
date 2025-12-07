package com.myorg.kotlintools.time.domain.model.base

interface TimeEntriesBase<TEntry : TimeEntryBase<*, *, *>> {
    val entries: List<TEntry>
}
