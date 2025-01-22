package com.topdownedge.domain.entities.common

data class PriceBar(
    val date: String,
    val open: String,
    val high: String,
    val low: String,
    val close: String,
    val adjClose: String,
    val volume: String,
)

enum class BarInterval(val value: String) {
    DAY("d"), WEEK("w"), MONTH("m"), MIN_1(""), MIN_5(""), MIN_15(""), MIN_30(""), MIN_60(""), MIN_240("")
}
