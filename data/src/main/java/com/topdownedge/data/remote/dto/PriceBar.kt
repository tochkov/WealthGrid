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