package com.myorg.kotlintools.time.domain.model.month

import com.myorg.kotlintools.time.domain.model.base.TimeMapBase
import java.time.YearMonth



data class MonthlyMap<T>(
    override val timeMap: MutableMap<YearMonth, T> = mutableMapOf()
) : TimeMapBase<YearMonth, T> {

}
