package com.myorg.kotlintools.report.domain.model

import com.myorg.kotlintools.ValueOperator
import java.time.Year




class Report<T> (
    private val operator: ValueOperator<T>
)
{
    private val reportEntries = ReportEntries<T>(operator)
    private val reportBuckets: MutableMap<String, ReportBucket<T>> = mutableMapOf()

    fun settle(){


    }
}