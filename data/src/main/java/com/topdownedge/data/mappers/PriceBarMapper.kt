package com.topdownedge.data.mappers

import android.system.Os.close
import com.topdownedge.data.local.PriceBarEntity
import com.topdownedge.data.local.dao.PriceBarDao
import com.topdownedge.data.remote.dto.PriceBarDto
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