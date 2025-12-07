package com.myorg.kotlintools.report.domain.repository

interface ReportRepositoryCreate<T> {
    fun addReport(report: T)

}