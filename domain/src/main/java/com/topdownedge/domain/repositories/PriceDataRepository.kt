package com.topdownedge.domain.repositories

import com.topdownedge.domain.entities.common.PriceBar
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PriceDataRepository {

    fun getHistoricalDailyPrices(
        symbol: String,
        fromDate: LocalDate? = null,
        toDate: LocalDate? = null,
    ): Flow<Result<List<PriceBar>?>>
}