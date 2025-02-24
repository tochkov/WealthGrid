package com.topdownedge.data.repositories

import com.topdownedge.data.local.dao.TickerDao
import com.topdownedge.data.mappers.toTicker
import com.topdownedge.data.mappers.toTickerEntity
import com.topdownedge.data.mappers.toTickers
import com.topdownedge.data.remote.EodhdFundamentalsApi
import com.topdownedge.data.remote.safeApiCall
import com.topdownedge.domain.entities.common.Ticker
import com.topdownedge.domain.repositories.MarketInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class MarketInfoRepositoryImpl
@Inject constructor(
    private val fundamentalsApi: EodhdFundamentalsApi,
    private val tickerDao: TickerDao,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
) : MarketInfoRepository {

    override fun getInitialSearchTickerList(fromCacheOnly: Boolean): Flow<Result<List<Ticker>?>> =
        flow {
            val initialData = tickerDao.getAllIndexTickers().firstOrNull()
            if (initialData != null && initialData.isNotEmpty()) {
                emit(Result.success(initialData.map { it.toTicker() }))
                if (fromCacheOnly) {
                    return@flow
                }
            }

            val apiResult = safeApiCall { fundamentalsApi.getIndexConstituents() }
            if (apiResult.isFailure || apiResult.getOrNull() == null) {
                emit(Result.failure(apiResult.exceptionOrNull()!!))
            } else {
                val tickers = apiResult.getOrNull()!!.Components.values
                tickerDao.upsertAllTickers(tickers.map { it.toTickerEntity() })
            }

            // Observe DB for any changes
            tickerDao.getAllIndexTickers()
                .map { it.map { it.toTicker() } }
                .map { Result.success(it) }
                .collect { emit(it) }

        }.flowOn(ioDispatcher)

    override fun getTickersForSearch(searchQuery: String): Flow<Result<List<Ticker>?>> = flow {
        tickerDao.searchTickers(searchQuery)
            .map { it.toTickers() }
            .map { Result.success(it) }
            .collect { emit(it) }
    }.flowOn(ioDispatcher)

    override suspend fun getAllTickerList(exchangeCode: String): Result<List<Ticker>?> {

        val response = fundamentalsApi.getExchangeSymbolsList()
        println(response.code)
        println(response.body())
        println(response.errorBody())

        return Result.success(emptyList())
    }


}