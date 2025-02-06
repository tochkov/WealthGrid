package com.topdownedge.domain.entities.common

import java.time.LocalDate

data class PriceBar(
    val date: LocalDate,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val adjustedClose: Double,
    val volume: Long,
)

enum class BarInterval(val value: String) {
    DAY("d"), WEEK("w"), MONTH("m"), MIN_1(""), MIN_5(""), MIN_15(""), MIN_30(""), MIN_60(""), MIN_240("")
}

data class LastKnownPrice(
    val code: String,
    val timestamp: Long,
    val gmtoffset: Int,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Int,
    val previousClose: Double,
    val change: Double,
    val changePercentage: Double,
    val isFromCache: Boolean
)
