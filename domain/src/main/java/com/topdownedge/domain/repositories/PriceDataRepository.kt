package com.topdownedge.domain.repositories

import com.topdownedge.domain.entities.common.LastKnownPrice
import com.topdownedge.domain.entities.common.PriceBar
import com.topdownedge.domain.entities.common.TickerWithPriceWrapper
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PriceDataRepository {

    fun getHistoricalDailyPrices(
        symbol: String,
        fromDate: LocalDate? = null,
        toDate: LocalDate? = null,
    ): Flow<Result<List<PriceBar>?>>

    fun getMarketTickersWithPrice(): Flow<Result<TickerWithPriceWrapper>>

    suspend fun getLastKnownPriceData(tickerCodes: List<String>): Flow<Result<Map<String, LastKnownPrice>?>>
}