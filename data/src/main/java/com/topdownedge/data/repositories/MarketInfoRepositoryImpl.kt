package com.topdownedge.data.repositories

import com.topdownedge.data.local.dao.TickerDao
import com.topdownedge.data.mappers.toTicker
import com.topdownedge.data.mappers.toTickerEntity
import com.topdownedge.data.remote.EodhdFundamentalsApi
import com.topdownedge.data.remote.safeApiCall
import com.topdownedge.domain.entities.common.Ticker
import com.topdownedge.domain.repositories.MarketInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class MarketInfoRepositoryImpl
@Inject constructor(
    private val eodhdApi: EodhdFundamentalsApi,
    private val tickerDao: TickerDao
) : MarketInfoRepository {

    override fun getInitialSearchTickerList(fromCacheOnly: Boolean): Flow<Result<List<Ticker>?>> =
        flow {
            val initialData = tickerDao.getAllIndexTickers().firstOrNull()
            if (initialData != null) {
                emit(Result.success(initialData.map { it.toTicker() }))
                if (fromCacheOnly) {
                    return@flow
                }
            }

            val apiResult = safeApiCall { eodhdApi.getIndexConstituents() }
//            val apiResult = safeApiCall { eodhdApi.getExchangeSymbolsList() }
            if (apiResult.isFailure || apiResult.getOrNull() == null) {
                emit(Result.failure(apiResult.exceptionOrNull()!!))
            } else {
                val tickers = apiResult.getOrNull()!!.Components.values
//                val tickers = apiResult.getOrNull()!!
                tickerDao.upsertAllTickers(tickers.map { it.toTickerEntity() })
            }

            // Observe DB for any changes
            tickerDao.getAllIndexTickers()
                .collect { entities ->
                    emit(Result.success(entities.map { it.toTicker() }))
                }
        }.flowOn(Dispatchers.IO)

    override fun getTickersForSearch(searchQuery: String): Flow<Result<List<Ticker>?>> = flow {
        tickerDao.searchTickers(searchQuery)
            .collect { entities ->
                emit(Result.success(entities.map { it.toTicker() }))
            }
    }.flowOn(Dispatchers.IO)


    override suspend fun getAllTickerList(exchangeCode: String): Result<List<Ticker>?> {

        val response = eodhdApi.getExchangeSymbolsList()
        println(response.code)
        println(response.body())
        println(response.errorBody())

        return Result.success(emptyList())
    }


}