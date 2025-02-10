package com.topdownedge.data.repositories

import com.topdownedge.data.local.dao.LastKnownPriceDao
import com.topdownedge.data.local.dao.PriceBarDao
import com.topdownedge.data.local.dao.RESULTS_LIMIT
import com.topdownedge.data.local.dao.TickerDao
import com.topdownedge.data.mappers.toEntities
import com.topdownedge.data.mappers.toLastKnownPriceEntities
import com.topdownedge.data.mappers.toLastKnownPrices
import com.topdownedge.data.mappers.toLastKnownPricesDomain
import com.topdownedge.data.mappers.toPriceBars
import com.topdownedge.data.mappers.toTickerEntity
import com.topdownedge.data.remote.EodhdFundamentalsApi
import com.topdownedge.data.remote.EodhdPriceDataApi
import com.topdownedge.data.remote.safeApiCall
import com.topdownedge.domain.entities.common.LastKnownPrice
import com.topdownedge.domain.entities.common.PriceBar
import com.topdownedge.domain.entities.common.TickerWithPriceWrapper
import com.topdownedge.domain.fmt
import com.topdownedge.domain.repositories.PriceDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PriceDataRepositoryImpl
@Inject constructor(
    private val fundamentalsApi: EodhdFundamentalsApi,
    private val priceDataApi: EodhdPriceDataApi,
    private val tickerDao: TickerDao,
    private val priceBarDao: PriceBarDao,
    private val lastKnownPriceDao: LastKnownPriceDao,
) : PriceDataRepository {

    private val ioDispatcher: CoroutineContext = Dispatchers.IO

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
                priceDataApi.getHistoricalPrices(
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

    }.flowOn(ioDispatcher)

    override fun getMarketTickersWithPrice() = flow {

        val initialData = tickerDao.getTopTickersWithPrices().firstOrNull()
        if (initialData?.isNotEmpty() == true) {
            emit(Result.success(TickerWithPriceWrapper(initialData, true)))
        }

        val apiResultTickers = safeApiCall { fundamentalsApi.getIndexConstituents() }
        if (apiResultTickers.isFailure || apiResultTickers.getOrNull() == null) {
//            emit(Result.failure(apiResultTickers.exceptionOrNull()!!))
        } else {
            val tickersRaw = apiResultTickers.getOrNull()!!.Components.values
            val tickers = tickersRaw.sortedByDescending { it.Weight }.map { it.toTickerEntity() }
            tickerDao.upsertAllTickers(tickers)

            val apiResultPrices = safeApiCall {
                priceDataApi.getRealTimePrices(
                    tickers.first().code,
                    tickers.subList(0, RESULTS_LIMIT).map { it.code }.joinToString()
                )
            }

            if (apiResultPrices.isFailure) {
//                emit(Result.failure(apiResultPrices.exceptionOrNull()!!))
            } else {
                lastKnownPriceDao.insertReplaceLastKnownPrices(
                    apiResultPrices.getOrNull()!!.toLastKnownPriceEntities()
                )
            }
        }

        tickerDao.getTopTickersWithPrices()
            .map { Result.success(TickerWithPriceWrapper(it, false)) }
            .collect { emit(it) }

    }.flowOn(ioDispatcher)


    override fun getLastKnownPriceData(tickerSymbols: List<String>): Flow<Result<Map<String, LastKnownPrice>?>> =
        flow {
            if (tickerSymbols.isEmpty()) {
                throw IllegalArgumentException("Tickers list muss have at least one ticker")
            }
            val initialData = lastKnownPriceDao.observeLastKnownPrices(tickerSymbols).firstOrNull()

            if (initialData?.isNotEmpty() == true) {
                val tickersMap = initialData.toLastKnownPrices(true).associateBy { it.code }
                emit(Result.success(tickersMap))
            }

            val apiResultPrices = safeApiCall {
                priceDataApi.getRealTimePrices(tickerSymbols.first(), tickerSymbols.joinToString())
            }

            if (apiResultPrices.isFailure) {
//            emit(Result.failure(apiResultPrices.exceptionOrNull()!!))
            } else {
                lastKnownPriceDao.insertReplaceLastKnownPrices(
                    apiResultPrices.getOrNull()!!.toLastKnownPriceEntities()
                )
                val tickerMap = apiResultPrices.getOrNull()!!.toLastKnownPricesDomain(false)
                    .associateBy { it.code }
                emit(Result.success(tickerMap))

            }

//            lastKnownPriceDao.observeLastKnownPrices(tickerSymbols)
//                .map { it.map { it.toLastKnownPrice(false) }.associateBy { it.code } }
//                .map { Result.success(it) }
//                .collect { emit(it) }


        }.flowOn(ioDispatcher)


}