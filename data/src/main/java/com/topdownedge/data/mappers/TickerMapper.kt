package com.topdownedge.data.mappers

import com.topdownedge.data.local.TickerEntity
import com.topdownedge.data.remote.dto.IndexConstituentDto
import com.topdownedge.data.remote.dto.SymbolItemDto
import com.topdownedge.domain.entities.common.Ticker


fun SymbolItemDto.toTicker() = Ticker(
    code = Code,
    name = Name,
    country = Country,
    exchange = Exchange,
    currency = Currency,
    type = Type,
)

fun SymbolItemDto.toTickerEntity() = TickerEntity(
    code = Code,
    name = Name,
    exchange = Exchange,
    type = Type,
)

fun IndexConstituentDto.toTicker() = Ticker(
    code = Code,
    name = Name,
    exchange = Exchange,
    sector = Sector,
    industry = Industry,
    indexWeight = Weight,
)

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

fun Ticker.toTickerEntity(): TickerEntity {
    return TickerEntity(
        code = code,
        name = name,
        exchange = exchange,
        type = type,
        indexWeight = indexWeight
    )

}