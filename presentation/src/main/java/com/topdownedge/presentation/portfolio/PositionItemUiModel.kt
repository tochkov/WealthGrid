package com.topdownedge.presentation.portfolio

import androidx.compose.ui.graphics.Color
import com.topdownedge.domain.entities.UserPosition
import com.topdownedge.domain.fmtPercent
import com.topdownedge.domain.fmtPrice
import com.topdownedge.domain.fmtMoney
import com.topdownedge.domain.fmtMoney2
import ir.ehsannarmani.compose_charts.models.Pie

data class PositionItemUiModel(
    val tickerCode: String,
    val tickerExchange: String,
    val tickerName: String,

    val averagePrice: Double,
    val sharesQuantity: Double,
    val totalInvested: Double = averagePrice * sharesQuantity,

    val currentPrice: Double?,
    val previousDayClose: Double?,
    var percentageOfPortfolio: Double? = 23.45,
    val isCurrentPriceFromCache: Boolean,

    val positionColor: Color
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

    fun currentValueDouble(): Double {
        if (currentPrice == null) return 0.0
        return currentPrice * sharesQuantity
    }

    fun totalPNL(): String {
        if (currentPrice == null) return ""
        val totalGain = (currentPrice - averagePrice) * sharesQuantity
        return if (totalGain >= 0) {
            "+${totalGain.fmtMoney2()}"
        } else {
            totalGain.fmtMoney2()
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




fun List<PositionItemUiModel>.toPieList() = mapIndexed { index, position ->
    Pie(
        label = position.tickerCode,
        data = position.currentValueDouble(),
        color = position.positionColor,
    )
}

fun List<PositionItemUiModel>.populatePctOfPortfolio() = also { positions ->
    val total = positions.sumOf { it.currentValueDouble() }
    positions.map { it.percentageOfPortfolio = (it.currentValueDouble() / total) * 100 }
}



fun UserPosition.toPositionItemUiModel(isFromCache: Boolean, positionColor: Color) = PositionItemUiModel(
    tickerCode = tickerCode,
    tickerExchange = tickerExchange,
    tickerName = tickerName,
    averagePrice = averagePrice,
    sharesQuantity = sharesQuantity,
    currentPrice = currentPrice,
    previousDayClose = currentPrice,
    percentageOfPortfolio = percentageOfPortfolio,
    isCurrentPriceFromCache = isFromCache,
    positionColor = positionColor
)

