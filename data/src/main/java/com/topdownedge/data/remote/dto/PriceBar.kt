package com.topdownedge.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceBarDto(
    @SerialName("date")
    val date: String,

    @SerialName("open")
    val open: Double,

    @SerialName("high")
    val high: Double,

    @SerialName("low")
    val low: Double,

    @SerialName("close")
    val close: Double,

    @SerialName("adjusted_close")
    val adjustedClose: Double,

    @SerialName("volume")
    val volume: Long
)


@Serializable
data class LastKnownPriceDto(
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
    @SerialName("change_p")
    val changePercentage: Double
)