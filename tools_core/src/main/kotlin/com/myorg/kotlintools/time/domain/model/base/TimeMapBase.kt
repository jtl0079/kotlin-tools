package com.myorg.kotlintools.time.domain.model.base

import java.time.YearMonth

interface TimeMapBase<TTime, TValue> {
    val timeMap: MutableMap<TTime, TValue>
}

