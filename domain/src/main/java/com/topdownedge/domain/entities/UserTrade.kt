package com.topdownedge.domain.entities

import java.time.LocalDate


data class UserTrade(
    val tickerCode: String,
    val tickerExchange: String,
    val dateSubmitted: LocalDate,
    val dateTraded: LocalDate,
    val price: Double,
    val shares: Double,
    val isBuy: Boolean,
)

// date, type:buy/sell, amount, stock:Stock
