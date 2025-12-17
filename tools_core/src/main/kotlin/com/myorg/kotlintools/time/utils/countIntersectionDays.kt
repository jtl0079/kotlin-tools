package com.myorg.kotlintools.time.utils


import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

fun countIntersectionDays(
    periodMainStartInstant: Instant,
    periodMainEndInstant: Instant,
    periodSubStartInstant: Instant,
    periodSubEndInstant: Instant,
    zoneId: ZoneId = ZoneId.systemDefault()
): Long {
    /**
     * 计算 periodSub 在 periodMain 内的天数（inclusive）
     */

    // 1️⃣ Instant → LocalDate（统一时区）
    val mainStart: LocalDate = periodMainStartInstant.atZone(zoneId).toLocalDate()
    val mainEnd: LocalDate   = periodMainEndInstant.atZone(zoneId).toLocalDate()

    val subStart: LocalDate  = periodSubStartInstant.atZone(zoneId).toLocalDate()
    val subEnd: LocalDate    = periodSubEndInstant.atZone(zoneId).toLocalDate()

    return countIntersectionDays(
        periodMainStart = mainStart,
        periodMainEnd = mainEnd,
        periodSubStart = subStart,
        periodSubEnd = subEnd
    )
}

fun countIntersectionDays(
    periodMainStart: LocalDate,
    periodMainEnd: LocalDate,
    periodSubStart: LocalDate,
    periodSubEnd: LocalDate
): Long {
    /**
     * 计算 periodSub 在 periodMain 内的天数（inclusive）
     */

    // 1️⃣ 求交集区间
    val effectiveStart = maxOf(periodMainStart, periodSubStart)
    val effectiveEnd   = minOf(periodMainEnd, periodSubEnd)

    // 2️⃣ 无交集
    if (effectiveStart.isAfter(effectiveEnd)) return 0

    // 3️⃣ inclusive 天数
    return ChronoUnit.DAYS.between(effectiveStart, effectiveEnd) + 1
}



fun ld(date: String): Instant =
    LocalDate.parse(date).atStartOfDay(ZoneId.of("UTC")).toInstant()


fun main() {
    print("[Start Here]\n\n")


    val result = countIntersectionDays(
        periodMainStartInstant = ld("2025-11-01"),
        periodMainEndInstant   = ld("2025-11-30"),
        periodSubStartInstant  = ld("2025-02-01"),
        periodSubEndInstant    = ld("2025-12-10"),
    )

    print(result)

    print("\n[End Here]\n\n")
}