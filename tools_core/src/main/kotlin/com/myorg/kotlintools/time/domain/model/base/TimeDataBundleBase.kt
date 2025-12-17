package com.myorg.kotlintools.time.domain.model.base

interface TimeDataBundleBase<
        TKey,
        TTimestamp : Comparable<TTimestamp>,
        TValue : Any,
        TEntry : TimeEntryBase<TKey, TTimestamp, TValue>,
        TEntries : TimeEntriesBase<TEntry, TTimestamp, TValue>,
        TKeyTimeMap : KeyTimeMapBase<TKey, *>
        >
{
    val timeEntries: TEntries
    val keyTimeMap: TKeyTimeMap


    // --------------------------------
    // CREATE
    // --------------------------------

    // --------------------------------
    // CREATE ENTRIES ONLY
    // --------------------------------

    fun addEntry(entry: TEntry) {
        timeEntries.add(entry)
    }



    // --------------------------------
    // READ
    // --------------------------------



    // --------------------------------
    // UPDATE
    // --------------------------------

    // --------------------------------
    // UPDATE BOTH
    // --------------------------------

    fun sumValue(entry: TEntry) {
        /**
         * timeEntries 里找对应的 entry （匹配 key 和 timestamp 第一个匹配上的，没有则插入 entry）
         *
         * keyTimeMap 里找对应的 value（匹配 key 和 timestamp 理论上 map 只会一个值，没有则插入 entry）
         * */
        timeEntries.sumToValue(entry)
        keyTimeMap.sumToValue(entry)

    }

    // --------------------------------
    // UPDATE ENTRIES ONLY
    // --------------------------------

    // --------------------------------
    // UPDATE MAP ONLY
    // --------------------------------
    fun sumMapValue(entry: TimeEntryBase<*, *, *>){
        keyTimeMap.sumToValue(entry)
    }

    fun syncMapByEntries(){
        /** Map 的本质是 groupBy entries 的数据
         * 所以遍历 entries 然后根据相同的 key && timestamp 更新 map
         * */

        // 1. 先清空 map（以 entries 为 source of truth）
        keyTimeMap.clear()

        // 2. 重新根据 entries 累加
        timeEntries.entries.forEach { entry ->
            keyTimeMap.sumToValue(entry)
        }
    }



    // --------------------------------
    // DELETE
    // --------------------------------



    fun clear() {
        timeEntries.clear()
        keyTimeMap.clear()
    }
}

