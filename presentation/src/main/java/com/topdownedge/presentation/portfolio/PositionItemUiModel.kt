package com.topdownedge.presentation.portfolio

import com.topdownedge.domain.entities.UserPosition
import com.topdownedge.domain.fmtPercent
import com.topdownedge.domain.fmtPrice

data class PositionItemUiModel(
    val tickerCode: String,
    val tickerExchange: String,

    val averagePrice: Double,
    val sharesQuantity: Double,
    val totalInvested: Double = averagePrice * sharesQuantity,

    val currentPrice: Double?,
    val previousDayClose: Double?,
    val percentageOfPortfolio: Double?,
    val isCurrentPriceFromCache: Boolean
) {
    fun averagePrice(): String = averagePrice.fmtPrice()
    fun sharesQuantity(): String = sharesQuantity.fmtPrice()
    fun currentPrice(): String = currentPrice.fmtPrice()
    fun percentageOfPortfolio(): String = percentageOfPortfolio.fmtPrice()

    fun currentValue(): String {
        if (currentPrice == null) return ""
        return (currentPrice * sharesQuantity).fmtPrice()
    }

    fun totalPNL(): String {
        if (currentPrice == null) return ""
        return ((currentPrice - averagePrice) * totalInvested).fmtPrice()
    }

    fun totalPNLPercent(): String{
        if (currentPrice == null) return ""
        val percentGain = ((currentPrice - averagePrice) / averagePrice) * 100
        return if(percentGain >= 0) {
            "+${percentGain.fmtPercent()}"
        } else {
            percentGain.fmtPercent()
        }
    }

    fun dailyPercentageGain(): String {
        if (currentPrice == null || previousDayClose == null) return ""
        val percentGain = ((currentPrice - previousDayClose) / previousDayClose) * 100
        return if(percentGain >= 0) {
            "+${percentGain.fmtPercent()}"
        } else {
            percentGain.fmtPercent()
        }
    }
}

fun UserPosition.toPositionItemUiModel(isFromCache: Boolean) = PositionItemUiModel(
    tickerCode = tickerCode,
    tickerExchange = tickerExchange,
    averagePrice = averagePrice,
    sharesQuantity = sharesQuantity,
    currentPrice = currentPrice,
    previousDayClose = currentPrice,
    percentageOfPortfolio = percentageOfPortfolio,
    isCurrentPriceFromCache = isFromCache
)

