package com.topdownedge.data.mappers

import com.topdownedge.data.local.UserPositionEntity
import com.topdownedge.data.local.UserTradeEntity
import com.topdownedge.domain.entities.UserPosition
import com.topdownedge.domain.entities.UserTrade


fun UserTradeEntity.toUserTrade() = UserTrade(
    tickerCode = tickerCode,
    tickerExchange = tickerExchange,
    dateSubmitted = dateSubmitted,
    dateTraded = dateTraded,
    price = price,
    shares = shares,
    isBuy = isBuy,
)

fun List<UserTradeEntity>.toUserTradeList() = map { it.toUserTrade() }

fun UserTrade.toUserTradeEntity() = UserTradeEntity(
    tickerCode = tickerCode,
    tickerExchange = tickerExchange,
    dateSubmitted = dateSubmitted,
    dateTraded = dateTraded,
    price = price,
    shares = shares,
    isBuy = isBuy,
)

fun List<UserPositionEntity>.toUserPositionList() = map { it.toUserPosition() }

fun UserPositionEntity.toUserPosition() = UserPosition(
    tickerCode = tickerCode,
    tickerExchange = tickerExchange,
    firstTradeDate = firstTradeDate,
    lastTradeDate = lastTradeDate,
    averagePrice = averagePrice,
    sharesQuantity = sharesQuantity,
    totalInvested = totalInvested,
    currentPrice = currentPrice,
    percentageOfPortfolio = percentageOfPortfolio,
)

fun UserPosition.toUserPositionEntity() = UserPositionEntity(
    tickerCode = tickerCode,
    tickerExchange = tickerExchange,
    firstTradeDate = firstTradeDate,
    lastTradeDate = lastTradeDate,
    averagePrice = averagePrice,
    sharesQuantity = sharesQuantity,
    totalInvested = totalInvested,
    currentPrice = currentPrice,
    currentValue = currentValue(),
    totalPNL = totalPNL(),
    totalPNLPercent = totalPNLPercent(),
    percentageOfPortfolio = percentageOfPortfolio,
)


