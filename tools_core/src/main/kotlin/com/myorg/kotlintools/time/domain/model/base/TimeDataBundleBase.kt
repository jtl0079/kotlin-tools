package com.myorg.kotlintools.time.domain.model.base

interface TimeDataBundleBase<
        TEntry : TimeEntryBase<*, *, *>,
        TEntries : TimeEntriesBase<TEntry, *>,
        TKeyTimeMap : KeyTimeMapBase<*, *>
        >
{
    val timeEntries: TEntries
    val keyTimeMap: TKeyTimeMap


    // --------------------------------
    // CREATE
    // --------------------------------

    fun addEntry(entry: TEntry) {
        timeEntries.add(entry)
        //keyTimeMap.updateFromEntry(entry)
    }



    // --------------------------------
    // READ
    // --------------------------------


    // --------------------------------
    // UPDATE
    // --------------------------------


    // --------------------------------
    // DELETE
    // --------------------------------
}

