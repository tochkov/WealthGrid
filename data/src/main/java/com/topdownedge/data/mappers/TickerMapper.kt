package com.topdownedge.data.mappers

import com.topdownedge.data.local.TickerEntity
import com.topdownedge.data.remote.dto.IndexConstituentDto
import com.topdownedge.data.remote.dto.SymbolItemDto
import com.topdownedge.domain.entities.common.Ticker


fun IndexConstituentDto.toTickerEntity() = TickerEntity(
    code = Code,
    name = Name,
    exchange = Exchange,
    indexWeight = Weight
)

fun TickerEntity.toTicker(): Ticker {
    return Ticker(
        code = code,
        name = name,
        exchange = exchange,
        type = type ?: "",
        indexWeight = indexWeight
    )
}
