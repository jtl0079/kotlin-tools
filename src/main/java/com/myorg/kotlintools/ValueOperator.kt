package com.myorg.kotlintools

import java.math.BigDecimal


// -----------------------------
// 1. 可加值的抽象（解耦 T 的"加"操作）
// -----------------------------
interface ValueOperator<T> {
    fun zero(): T
    fun add(a: T, b: T): T
}

/** 常用实现 */
object IntOperator : ValueOperator<Int> {
    override fun zero() = 0
    override fun add(a: Int, b: Int) = a + b
}
object LongOperator : ValueOperator<Long> {
    override fun zero() = 0L
    override fun add(a: Long, b: Long) = a + b
}
object DoubleOperator : ValueOperator<Double> {
    override fun zero() = 0.0
    override fun add(a: Double, b: Double) = a + b
}
class BigDecimalOperator(private val scale: Int = 2) : ValueOperator<BigDecimal> {
    override fun zero() = BigDecimal.ZERO.setScale(scale)
    override fun add(a: BigDecimal, b: BigDecimal) = a.add(b).setScale(scale, BigDecimal.ROUND_HALF_UP)
}
