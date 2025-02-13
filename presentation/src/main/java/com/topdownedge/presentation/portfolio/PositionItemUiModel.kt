package com.topdownedge.presentation.portfolio

import com.topdownedge.domain.entities.UserPosition
import com.topdownedge.domain.fmtPercent
import com.topdownedge.domain.fmtPrice
import com.topdownedge.domain.fmtMoney

data class PositionItemUiModel(
    val tickerCode: String,
    val tickerExchange: String,
    val tickerName: String,

    val averagePrice: Double,
    val sharesQuantity: Double,
    private val totalInvested: Double = averagePrice * sharesQuantity,

    val currentPrice: Double?,
    val previousDayClose: Double?,
    val percentageOfPortfolio: Double? = 23.45,
    val isCurrentPriceFromCache: Boolean
) {
    fun averagePrice(): String = averagePrice.fmtPrice()
    fun sharesQuantity(): String = sharesQuantity.fmtPrice()
    fun currentPrice(): String = currentPrice.fmtPrice()

    fun percentageOfPortfolio(): String = percentageOfPortfolio.fmtPercent()

    fun totalInvested(): String = totalInvested.fmtMoney()

    fun currentValue(): String {
        if (currentPrice == null) return ""
        return (currentPrice * sharesQuantity).fmtMoney()
    }

    fun totalPNL(): String {
        if (currentPrice == null) return ""
        val totalGain = (currentPrice - averagePrice) * sharesQuantity
        return if (totalGain >= 0) {
            "+${totalGain.fmtMoney()}"
        } else {
            totalGain.fmtMoney()
        }
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

    fun totalPNLPercentDouble(): Double?{
        if (currentPrice == null) return null
        return ((currentPrice - averagePrice) / averagePrice) * 100
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
    tickerName = tickerName,
    averagePrice = averagePrice,
    sharesQuantity = sharesQuantity,
    currentPrice = currentPrice,
    previousDayClose = currentPrice,
    percentageOfPortfolio = percentageOfPortfolio,
    isCurrentPriceFromCache = isFromCache
)

