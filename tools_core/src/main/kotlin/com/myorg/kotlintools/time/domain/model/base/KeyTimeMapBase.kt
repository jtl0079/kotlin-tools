package com.myorg.kotlintools.time.domain.model.base

interface KeyTimeMapBase <TKey, TTimeMapGroup>{
    val keyTimeMap: MutableMap<TKey, TTimeMapGroup>


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

    fun setMapValue(entry: TimeEntryBase<*, *, *>)



    // --------------------------------
    // DELETE
    // --------------------------------

    fun clear() = keyTimeMap.clear()



}