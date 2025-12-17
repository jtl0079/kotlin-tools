package com.myorg.kotlintools.time.domain.model.instant

import com.myorg.kotlintools.ValueOperator
import com.myorg.kotlintools.time.domain.model.base.TimeEntriesBase
import com.myorg.kotlintools.valueOperatorOf
import java.time.Instant

data class InstantlyEntries<TKey, TValue: Any>(
    override val entries: MutableList<InstantlyEntry<TKey, TValue>> = mutableListOf(),
    override val valueOperator: ValueOperator<TValue>
) : TimeEntriesBase<
        InstantlyEntry<TKey, TValue>,
        Instant,
        TValue
        > {


}


fun main(){
    print("[here start]\n")
    val entries = InstantlyEntries<String, Int>(
        valueOperator = valueOperatorOf<Int>()
    )

    print(entries.toString())
}
