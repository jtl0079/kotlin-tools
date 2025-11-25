package com.myorg.kotlintools

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass


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
object FloatOperator : ValueOperator<Float> {
    override fun zero(): Float = 0.0f
    override fun add(a: Float, b: Float): Float = a + b
}
object DoubleOperator : ValueOperator<Double> {
    override fun zero() = 0.0
    override fun add(a: Double, b: Double) = a + b
}


data class BigDecimalConfig(
    val scale: Int = 2,
    val roundingMode: RoundingMode = RoundingMode.HALF_UP
) {
    companion object {
        /** 默认配置：2 位小数，四舍五入 */
        val DEFAULT = BigDecimalConfig()

        /** 货币配置：2 位小数，银行家舍入法 */
        val CURRENCY = BigDecimalConfig(scale = 2, roundingMode = RoundingMode.HALF_EVEN)

        /** 高精度配置：8 位小数 */
        val HIGH_PRECISION = BigDecimalConfig(scale = 8)
    }
}

class BigDecimalOperator(
    private val config: BigDecimalConfig = BigDecimalConfig.DEFAULT
) : ValueOperator<BigDecimal> {

    // 缓存零值，避免重复创建对象
    private val cachedZero: BigDecimal = BigDecimal.ZERO.setScale(config.scale, config.roundingMode)

    override fun zero(): BigDecimal = cachedZero

    override fun add(a: BigDecimal, b: BigDecimal): BigDecimal =
        a.add(b).setScale(config.scale, config.roundingMode)

    // 便捷构造函数
    constructor(scale: Int) : this(BigDecimalConfig(scale = scale))
    constructor(scale: Int, roundingMode: RoundingMode) : this(BigDecimalConfig(scale, roundingMode))
}


/**
 * ValueOperator 工厂
 *
 * 提供运算器的注册、获取功能，支持运行时扩展。
 *
 * 设计说明：
 *   - 使用 ConcurrentHashMap 保证线程安全
 *   - 支持自定义类型注册，便于扩展
 *   - 内置常用基础类型
 */
object ValueOperatorFactory {

    // 类型 -> 运算器供应商的映射
    // 使用供应商（lambda）而非实例，支持有状态的运算器（如 BigDecimalOperator）
    private val registry = ConcurrentHashMap<KClass<*>, () -> ValueOperator<*>>()

    init {
        // 注册内置类型
        register(Int::class) { IntOperator }
        register(Long::class) { LongOperator }
        register(Double::class) { DoubleOperator }
        register(Float::class) { FloatOperator }
        register(BigDecimal::class) { BigDecimalOperator() }
    }

    /**
     * 注册自定义类型的运算器
     *
     * @param clazz 值类型的 KClass
     * @param supplier 运算器供应商
     *
     * 示例：
     * ```
     * data class Money(val cents: Long)
     *
     * object MoneyOperator : ValueOperator<Money> {
     *     override fun zero() = Money(0)
     *     override fun add(a: Money, b: Money) = Money(a.cents + b.cents)
     * }
     *
     * ValueOperatorFactory.register(Money::class) { MoneyOperator }
     * ```
     */
    fun <T : Any> register(clazz: KClass<T>, supplier: () -> ValueOperator<T>) {
        registry[clazz] = supplier
    }

    /**
     * 获取指定类型的运算器
     *
     * @param clazz 值类型的 KClass
     * @return 对应的运算器
     * @throws IllegalArgumentException 如果类型未注册
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(clazz: KClass<T>): ValueOperator<T> {
        val supplier = registry[clazz]
            ?: throw IllegalArgumentException(
                "No ValueOperator registered for ${clazz.simpleName}. " +
                        "Use ValueOperatorFactory.register() to add support."
            )
        return supplier() as ValueOperator<T>
    }

    /**
     * 检查类型是否已注册
     */
    fun <T : Any> isRegistered(clazz: KClass<T>): Boolean = registry.containsKey(clazz)

    /**
     * 获取所有已注册的类型
     */
    fun registeredTypes(): Set<KClass<*>> = registry.keys.toSet()
}


/**
 * 通过 reified 泛型获取默认运算器
 *
 * 示例：
 * ```
 * val intOp = valueOperatorOf<Int>()
 * val sum = intOp.add(1, 2)  // => 3
 * ```
 */
inline fun <reified T : Any> valueOperatorOf(): ValueOperator<T> =
    ValueOperatorFactory.get(T::class)

/**
 * 使用运算器对集合求和
 *
 * 示例：
 * ```
 * val numbers = listOf(1, 2, 3, 4, 5)
 * val sum = numbers.sumWith(IntOperator)  // => 15
 * ```
 */
fun <T> Iterable<T>.sumWith(operator: ValueOperator<T>): T =
    fold(operator.zero()) { acc, value -> operator.add(acc, value) }

/**
 * 使用运算器对序列求和（惰性计算）
 */
fun <T> Sequence<T>.sumWith(operator: ValueOperator<T>): T =
    fold(operator.zero()) { acc, value -> operator.add(acc, value) }



/** 内联求和辅助函数，避免创建中间集合 */
inline fun <T, E> List<E>.sumWith(
    operator: ValueOperator<T>,
    selector: (E) -> T
): T {
    var acc = operator.zero()
    for (e in this) {
        acc = operator.add(acc, selector(e))
    }
    return acc
}