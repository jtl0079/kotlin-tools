package com.myorg.kotlintools.time.domain.model.alltime

import com.myorg.kotlintools.time.domain.model.month.MonthlyMap
import com.myorg.kotlintools.time.domain.model.year.YearlyMap

data class AllTimeMapGroup<TValue>(
    val monthlyMap: MonthlyMap<TValue> ,
    val yearlyMap: YearlyMap<TValue>
) {

}