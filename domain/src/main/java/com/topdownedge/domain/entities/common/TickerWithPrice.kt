package com.topdownedge.domain.entities.common



data class TickerWithPriceWrapper(
    val tickers: List<TickerWithPrice>,
    val isFromCache: Boolean
)

data class TickerWithPrice(
    val code: String,
    val name: String,
    val exchange: String,
    val lastPrice: Double?,
    val previousClose: Double?,
    val changePercentage: Double?,
)