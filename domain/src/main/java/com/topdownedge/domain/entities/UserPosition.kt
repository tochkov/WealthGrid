package com.topdownedge.domain.entities

import java.time.LocalDate


data class UserPosition(
    val tickerCode: String,
    val tickerExchange: String,
    val symbol: String = "$tickerCode.$tickerExchange",

    val firstTradeDate: LocalDate,
    val lastTradeDate: LocalDate,

    val averagePrice: Double,
    val sharesQuantity: Double,
    val totalInvested: Double = averagePrice * sharesQuantity,

    var currentPrice: Double = Double.MIN_VALUE,
    var percentageOfPortfolio: Double = Double.MIN_VALUE

) {

    fun currentValue(): Double = currentPrice * sharesQuantity
    fun totalPNL(): Double = currentValue() - totalInvested
    fun totalPNLPercent(): Double = (totalPNL() / totalInvested) * 100
}