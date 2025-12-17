package com.myorg.kotlintools.time.domain.model.base

import com.myorg.kotlintools.ValueOperator

interface TimeEntriesBase<
        TEntry : TimeEntryBase<*, TTimestamp, TValue>,
        TTimestamp : Comparable<TTimestamp>,
        TValue : Any
        > {
    val entries: MutableList<TEntry>
    val valueOperator: ValueOperator<TValue>

    // --------------------------------
    // CREATE
    // --------------------------------
    fun add(entry: TEntry) = entries.add(entry)


    // --------------------------------
    // READ
    // --------------------------------

    fun sorted(): List<TEntry> = entries.sortedBy { it.timestamp }
    fun latest(): TEntry? = entries.maxByOrNull { it.timestamp }
    fun earliest(): TEntry? = entries.minByOrNull { it.timestamp }

    fun between(start: TTimestamp, end: TTimestamp): List<TEntry> =
        entries.filter { it.timestamp >= start && it.timestamp <= end }

    fun printAll() = entries.forEach { println(it) }

    // --------------------------------
    // UPDATE
    // --------------------------------


    fun sumToValue(entry: TEntry) {
        // 1. 找到第一个 key + timestamp 相同的 entry
        val index = entries.indexOfFirst {
            it.timestamp == entry.timestamp &&
                    it.key == entry.key
        }

        if (index >= 0) {
            val existing = entries[index]

            val newEntry = existing.withValue(
                valueOperator.add(existing.value, entry.value)
            ) as TEntry

            entries[index] = newEntry
        } else {
            entries.add(entry)
        }
    }


    // --------------------------------
    // DELETE
    // --------------------------------

    fun clear() = entries.clear()

    fun remove(entry: TEntry) = entries.remove(entry)

    fun removeIf(predicate: (TEntry) -> Boolean) =
        entries.removeIf(predicate)
}


// --------------------------------
// CREATE
// --------------------------------


// --------------------------------
// READ
// --------------------------------


// --------------------------------
// UPDATE
// --------------------------------


// --------------------------------
// DELETE
// --------------------------------