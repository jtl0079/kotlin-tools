package com.myorg.kotlintools.time.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit


fun countPeriodDurationDays(
    start: LocalDate,
    end: LocalDate
): Long {
    /**
     * Returns the Duration of days in this period (inclusive).
     *
     * If start > end, returns 0.
     */

    if (start.isAfter(end)) return 0

    return ChronoUnit.DAYS.between(start, end)
}

fun countPeriodDurationDays(
    startInstant: Instant,
    endInstant: Instant,
    zoneId: ZoneId = ZoneId.systemDefault()
): Long {
    val start = startInstant.atZone(zoneId).toLocalDate()
    val end = endInstant.atZone(zoneId).toLocalDate()

    return countPeriodInclusiveDays(start, end)
}