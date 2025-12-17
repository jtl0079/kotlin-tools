package com.myorg.kotlintools.time.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

fun countPeriodInclusiveDays(
    start: LocalDate,
    end: LocalDate
): Long {
    /**
     * Returns the number of days in this period (inclusive).
     *
     * If start > end, returns 0.
     */

    if (start.isAfter(end)) return 0

    return ChronoUnit.DAYS.between(start, end) + 1
}




fun countPeriodInclusiveDays(
    startInstant: Instant,
    endInstant: Instant,
    zoneId: ZoneId = ZoneId.systemDefault()
): Long {
    val start = startInstant.atZone(zoneId).toLocalDate()
    val end = endInstant.atZone(zoneId).toLocalDate()

    return countPeriodInclusiveDays(start, end)
}


fun main() {
    print("[Start Here]\n\n")

    println(
        countPeriodInclusiveDays(
            LocalDate.of(2025, 12, 1),
            LocalDate.of(2026, 1, 1)
        )
    )
}