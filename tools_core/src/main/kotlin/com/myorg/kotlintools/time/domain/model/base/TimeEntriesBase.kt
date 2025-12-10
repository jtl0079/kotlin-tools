package com.myorg.kotlintools.time.domain.model.base

interface TimeEntriesBase<
        TEntry : TimeEntryBase<*, TTimestamp, *>,
        TTimestamp: Comparable<TTimestamp>
        > {
    val entries: MutableList<TEntry>

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

    // --------------------------------
    // UPDATE
    // --------------------------------


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