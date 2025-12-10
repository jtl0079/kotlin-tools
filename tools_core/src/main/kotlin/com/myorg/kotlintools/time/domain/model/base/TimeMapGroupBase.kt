package com.myorg.kotlintools.time.domain.model.base

interface TimeMapGroupBase<
        TValue,
        TMap : TimeMapBase<*, TValue>
        > {

    fun sumValue(entry: TimeEntryBase<*, *, *>)
}


