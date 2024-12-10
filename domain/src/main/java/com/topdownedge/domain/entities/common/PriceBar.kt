package com.topdownedge.domain.entities.common

data class PriceBar(
    val date: String,
    val interval: String, // Daily, 30 min, 15 min
    val open: String,
    val high: String,
    val low: String,
    val close: String,
    val adjClose: String,
    val volume: String,
)
