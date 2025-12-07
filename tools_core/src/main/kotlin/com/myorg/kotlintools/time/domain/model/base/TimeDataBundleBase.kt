package com.myorg.kotlintools.time.domain.model.base

interface TimeDataBundleBase<
        TTimeEntries: TimeEntriesBase<*>,
        TKeyTimeMap: KeyTimeMapBase<*, *>
        >
{
    val timeEntries: TTimeEntries
    val keyTimeMap: TKeyTimeMap
}

