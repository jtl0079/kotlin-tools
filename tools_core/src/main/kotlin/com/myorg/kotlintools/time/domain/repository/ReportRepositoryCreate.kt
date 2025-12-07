package com.myorg.kotlintools.time.domain.repository

interface ReportRepositoryCreate<T> {
    fun addReport(report: T)

}