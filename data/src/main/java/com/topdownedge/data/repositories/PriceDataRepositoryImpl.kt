package com.topdownedge.data.repositories

import com.topdownedge.data.local.dao.PriceBarDao
import com.topdownedge.data.mappers.toEntities
import com.topdownedge.data.mappers.toPriceBars
import com.topdownedge.data.remote.EodhdPriceDataApi
import com.topdownedge.data.remote.safeApiCall
import com.topdownedge.domain.entities.common.PriceBar
import com.topdownedge.domain.fmt
import com.topdownedge.domain.repositories.PriceDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import javax.inject.Inject

class PriceDataRepositoryImpl
@Inject constructor(
    private val eodhdApi: EodhdPriceDataApi,
    private val priceBarDao: PriceBarDao,
) : PriceDataRepository {

    override fun getHistoricalDailyPrices(
        symbol: String,
        fromDate: LocalDate?,
        toDate: LocalDate?,
    ): Flow<Result<List<PriceBar>?>> = flow {

        // First, try to get cached data from local database
        val initialData = priceBarDao.observePriceBarsForSymbol(
            symbol,
            fromDate?.toEpochDay() ?: 0,
        ).firstOrNull()
        var lastDateWithPrice: LocalDate? = null

        // If we have cached data, emit it immediately and note the last date
        if (initialData?.isEmpty() == false) {
            lastDateWithPrice = LocalDate.ofEpochDay(initialData.last().dateTimestamp)
            emit(Result.success(initialData.toPriceBars()))
        }

        // Check if we need to fetch new data (no cache or cache is outdated)
        if (lastDateWithPrice == null || lastDateWithPrice < LocalDate.now()) {
            // Fetch new data from API, starting from the day after our last cached date
            val apiResult = safeApiCall {
                eodhdApi.getHistoricalPrices(
                    symbol = symbol,
                    fromDate = lastDateWithPrice?.plusDays(1)?.fmt()
                )
            }

            // Handle API errors
            if (apiResult.isFailure) {
                emit(Result.failure(apiResult.exceptionOrNull()!!))
            } else {
                // Store new price data in local database
                val priceBars = apiResult.getOrNull()!!
                priceBarDao.upsertPriceBars(priceBars.toEntities(symbol))
            }
        }

        // Observe and emit all future updates from the local database
        priceBarDao.observePriceBarsForSymbol(
            symbol,
            fromDate?.toEpochDay() ?: 0,
        ).collect { entities ->
            if (entities.isNotEmpty()) {
                emit(Result.success(entities.toPriceBars()))
            }
        }

    }.flowOn(Dispatchers.IO)


}