package com.myorg.kotlintools.time.domain.model.base

interface TimeMapGroupBase<
        TValue,
        TMap : TimeMapBase<*, TValue>
        > {

    // --------------------------------
    // CREATE
    // --------------------------------


    // --------------------------------
    // READ
    // --------------------------------

    // --------------------------------
    // UPDATE
    // --------------------------------

    fun sumToValue(entry: TimeEntryBase<*, *, *>)

    fun setValue(entry: TimeEntryBase<*, *, *>)

}


