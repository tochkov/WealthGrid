package com.topdownedge.presentation.market

import com.topdownedge.domain.entities.common.LastKnownPrice
import com.topdownedge.domain.fmtPercent
import com.topdownedge.domain.fmtPrice
import com.topdownedge.presentation.common.getLogoUrl


val SPY: MarketItemUiModel = MarketItemUiModel(
    tickerCode = "SPY",
    tickerName = "S&P 500",
    tickerExchange = "US",
)
val QQQ: MarketItemUiModel = MarketItemUiModel(
    tickerCode = "QQQ",
    tickerName = "Nasdaq 100",
    tickerExchange = "US",
)
val IWM: MarketItemUiModel = MarketItemUiModel(
    tickerCode = "IWM",
    tickerName = "Russell 2000",
    tickerExchange = "US",
)

data class MarketItemUiModel(
    val tickerCode: String,
    val tickerName: String,
    val tickerExchange: String,
    val logoUrl: String = getLogoUrl(tickerCode),
    val currentPrice: Double? = null,
    val previousClose: Double? = null,
    val isFromCache: Boolean = true
) {
    fun getCurrentPrice() = currentPrice.fmtPrice()
    fun getPercentGain(): String {
        if (currentPrice == null || previousClose == null) return ""
        val percentGain = (currentPrice - previousClose) / previousClose * 100
        return if (percentGain >= 0) {
            "+${percentGain.fmtPercent()}"
        } else {
            percentGain.fmtPercent()
        }
    }

}

fun MarketItemUiModel.updatePrice(newPrice: LastKnownPrice?) = copy(
    currentPrice = newPrice?.close ?: currentPrice,
    previousClose = newPrice?.previousClose ?: previousClose,
    isFromCache = newPrice?.isFromCache ?: isFromCache
)