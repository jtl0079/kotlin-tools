package com.myorg.kotlintools.time.domain.model.base

import java.time.YearMonth

interface TimeMapBase<TTime, TValue> {
    val timeMap: MutableMap<TTime, TValue>

    // --------------------------------
    // READ
    // --------------------------------


    fun printAll() = timeMap.forEach { println(it) }


    // --------------------------------
    // UPDATE
    // --------------------------------
    fun sumValue(time: TTime, value: TValue) {
        val oldValue = timeMap[time]

        val newValue = when {
            oldValue == null -> value
            oldValue is Number && value is Number ->
                (oldValue.toDouble() + value.toDouble()) as TValue
            else -> throw IllegalArgumentException("TValue must be numeric")
        }

        timeMap[time] = newValue
    }

    fun setValue(time: TTime, value: TValue) {
        timeMap[time] = value
    }
}

