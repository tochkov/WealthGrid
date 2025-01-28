package com.topdownedge.data.mappers

import com.topdownedge.data.local.UserTradeEntity
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
