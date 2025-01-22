package com.topdownedge.domain.repositories

import com.topdownedge.domain.entities.common.BarInterval
import com.topdownedge.domain.entities.common.PriceBar
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PriceDataRepository {

    fun getHistoricalPrices(
        symbol: String,
        fromDate: LocalDate? = null,
        toDate: LocalDate? = null,
        interval: BarInterval = BarInterval.DAY
    ): Flow<Result<List<PriceBar>>>
}