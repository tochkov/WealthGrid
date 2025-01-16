package com.topdownedge.domain.repositories

import com.topdownedge.domain.entities.common.Ticker
import kotlinx.coroutines.flow.Flow

interface MarketInfoRepository {

    /**
     * TODO - in the future this will return recently observed by the user tickers - possibly extract to UseCase
     */
    fun getInitialSearchTickerList(): Flow<Result<List<Ticker>?>>

    suspend fun getAllTickerList(exchangeCode: String): Result<List<Ticker>?>

}