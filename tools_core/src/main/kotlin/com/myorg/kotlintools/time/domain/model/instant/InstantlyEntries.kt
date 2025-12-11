package com.myorg.kotlintools.time.domain.model.instant

import com.myorg.kotlintools.time.domain.model.base.TimeEntriesBase
import java.time.Instant

data class InstantlyEntries<TKey, TValue>(
    override val entries: MutableList<InstantlyEntry<TKey, TValue>> = mutableListOf()
) : TimeEntriesBase<InstantlyEntry<TKey, TValue>, Instant> {

}


fun main(){
    print("[here start]\n")
    val entries = InstantlyEntries<String, Int>()

    entries.add(InstantlyEntry("key1", Instant.now(), 1))
    print(entries.toString())
}
