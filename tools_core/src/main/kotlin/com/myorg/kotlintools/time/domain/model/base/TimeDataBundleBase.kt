package com.myorg.kotlintools.time.domain.model.base

interface TimeDataBundleBase<
        TRawTimeEntries: TimeEntriesBase<*>,
        TKeyTimeMapGroup: KeyTimeMapBase<*, *>
        >
{
}