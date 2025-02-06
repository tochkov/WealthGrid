package com.topdownedge.data.mappers

import com.topdownedge.data.local.LastKnownPriceEntity
import com.topdownedge.data.local.PriceBarEntity
import com.topdownedge.data.remote.dto.LastKnownPriceDto
import com.topdownedge.data.remote.dto.PriceBarDto
import com.topdownedge.domain.entities.common.LastKnownPrice
import com.topdownedge.domain.entities.common.PriceBar
import java.time.LocalDate


//fun PriceBar.toEntity(symbol: String) = PriceBarEntity(
//    symbol = symbol,
//    date = date,
//    open = open,
//    high = high,
//    low = low,
//    close = close,
//    adjustedClose = adjustedClose,
//    volume = volume
//)
//
//fun List<PriceBar>.toEntities(symbol: String) = map { it.toEntity(symbol) }

fun PriceBarDto.toEntity(symbol: String) = PriceBarEntity(
    symbol = symbol,
    dateTimestamp = LocalDate.parse(date).toEpochDay(),
    open = open,
    high = high,
    low = low,
    close = close,
    adjustedClose = adjustedClose,
    volume = volume
)

fun List<PriceBarDto>.toEntities(symbol: String) = map { it.toEntity(symbol) }

fun PriceBarEntity.toPriceBar() = PriceBar(
    date = LocalDate.ofEpochDay(dateTimestamp),
    open = open,
    high = high,
    low = low,
    close = close,
    adjustedClose = adjustedClose,
    volume = volume
)

fun List<PriceBarEntity>.toPriceBars() = map { it.toPriceBar() }

fun LastKnownPriceDto.toLastKnownPriceDomain(isFromCache: Boolean) = LastKnownPrice(
    code = code.split(".US")[0],
    timestamp = timestamp,
    gmtoffset = gmtoffset,
    open = open,
    high = high,
    low = low,
    close = close,
    volume = volume,
    previousClose = previousClose,
    change = change,
    changePercentage = changePercentage,
    isFromCache = isFromCache
)

fun List<LastKnownPriceDto>.toLastKnownPricesDomain(isFromCache: Boolean) = map { it.toLastKnownPriceDomain(isFromCache) }

fun LastKnownPriceEntity.toLastKnownPrice(isFromCache: Boolean) = LastKnownPrice(
    code = code,
    timestamp = timestamp,
    gmtoffset = gmtoffset,
    open = open,
    high = high,
    low = low,
    close = close,
    volume = volume,
    previousClose = previousClose,
    change = change,
    changePercentage = changePercentage,
    isFromCache = isFromCache
)

fun List<LastKnownPriceEntity>.toLastKnownPrices(isFromCache: Boolean) = map { it.toLastKnownPrice(isFromCache) }

fun LastKnownPriceDto.toLastKnownPriceEntity() = LastKnownPriceEntity(
    code = code.split(".US")[0],
    timestamp = timestamp,
    gmtoffset = gmtoffset,
    open = open,
    high = high,
    low = low,
    close = close,
    volume = volume,
    previousClose = previousClose,
    change = change,
    changePercentage = changePercentage,
)

fun List<LastKnownPriceDto>.toLastKnownPriceEntities() = map { it.toLastKnownPriceEntity() }