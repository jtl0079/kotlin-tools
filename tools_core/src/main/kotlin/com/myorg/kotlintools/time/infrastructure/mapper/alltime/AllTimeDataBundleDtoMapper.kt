package com.myorg.kotlintools.time.infrastructure.mapper.alltime

import com.myorg.kotlintools.ValueOperator
import com.myorg.kotlintools.time.domain.model.alltime.AllTimeDataBundle
import com.myorg.kotlintools.time.domain.model.instant.InstantlyEntries
import com.myorg.kotlintools.time.infrastructure.dto.alltime.AllTimeDataBundleDto
import com.myorg.kotlintools.time.infrastructure.mapper.instant.toDomain
import com.myorg.kotlintools.time.infrastructure.mapper.instant.toDto
import com.myorg.kotlintools.valueOperatorOf


fun <K, V : Any> AllTimeDataBundle<K, V>.toDto(): AllTimeDataBundleDto<K, V> {
    return AllTimeDataBundleDto(
        entries = this.timeEntries.entries.map { it.toDto() }
    )
}


fun <K, V : Any> AllTimeDataBundleDto<K, V>.toDomain(
    valueOperator: ValueOperator<V>
): AllTimeDataBundle<K, V> {
    return AllTimeDataBundle(
        timeEntries = InstantlyEntries(
            entries = entries.map { it.toDomain() }.toMutableList(),
            valueOperator = valueOperator
        )
    )
}
