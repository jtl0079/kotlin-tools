package com.myorg.kotlintools.time.domain.model.year


import com.myorg.kotlintools.time.domain.model.base.TimeMapBase
import java.time.Year
import java.time.YearMonth



data class YearlyMap<T>(
    override val timeMap: MutableMap<Year, T>
) : TimeMapBase<Year, T> {

}
