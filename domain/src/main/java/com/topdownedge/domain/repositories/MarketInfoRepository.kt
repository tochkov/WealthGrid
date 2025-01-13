package com.topdownedge.domain.repositories

import com.topdownedge.domain.entities.common.Ticker

interface MarketInfoRepository {

    suspend fun getIndexTickersList(indexCode: String): Result<List<Ticker>?>

    suspend fun getAllTickerList(exchangeCode: String): Result<List<Ticker>?>

}