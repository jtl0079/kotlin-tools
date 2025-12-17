package com.myorg.kotlintools.time.domain.model.alltime

import com.myorg.kotlintools.time.domain.model.base.KeyTimeMapBase
import com.myorg.kotlintools.time.domain.model.base.TimeEntryBase
import com.myorg.kotlintools.time.domain.model.month.MonthlyMap
import com.myorg.kotlintools.time.domain.model.year.YearlyMap

data class KeyAllTimeMap<TKey, TValue>(
    override val keyTimeMap: MutableMap<TKey, AllTimeMapGroup<TValue>> = mutableMapOf()
) : KeyTimeMapBase<TKey, AllTimeMapGroup<TValue>> {

    // --------------------------------
    // CREATE
    // --------------------------------


    // --------------------------------
    // READ
    // --------------------------------

    fun getMonthlyValue(entry: TimeEntryBase<*, *, *>) : TValue? = keyTimeMap[entry.key]?.getMonthlyValue(entry)

    fun getYearlyValue(entry: TimeEntryBase<*, *, *>) : TValue? = keyTimeMap[entry.key]?.getYearlyValue(entry)


    // --------------------------------
    // UPDATE
    // --------------------------------
    override fun sumToValue(entry: TimeEntryBase<*, *, *>) {
        @Suppress("UNCHECKED_CAST")
        val key = entry.key as TKey

        // 取得或建立对应该 key 的 AllTimeMapGroup
        val group = keyTimeMap.getOrPut(key) {
            AllTimeMapGroup(
                monthlyMap = MonthlyMap(),
                yearlyMap = YearlyMap(mutableMapOf())
            )
        }

        // 委托给 AllTimeMapGroup 做时间分组与累加
        group.sumToValue(entry)
    }

    override fun setMapValue(entry: TimeEntryBase<*, *, *>) {

        val key = entry.key as TKey

        // 取得或建立对应该 key 的 AllTimeMapGroup
        val group = keyTimeMap.getOrPut(key) {
            AllTimeMapGroup(
                monthlyMap = MonthlyMap(),
                yearlyMap = YearlyMap(mutableMapOf())
            )
        }


        group.sumToValue(entry)


    }



}



