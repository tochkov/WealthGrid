package com.topdownedge.data.repositories

import com.topdownedge.data.remote.EodhdFundamentalsApi
import com.topdownedge.data.remote.dto.toTicker
import com.topdownedge.data.remote.safeApiCall
import com.topdownedge.domain.entities.common.Ticker
import com.topdownedge.domain.repositories.MarketInfoRepository
import javax.inject.Inject


class MarketInfoRepositoryImpl
@Inject constructor(private val eodhdApi: EodhdFundamentalsApi) : MarketInfoRepository {

    override suspend fun getIndexTickersList(indexCode: String): Result<List<Ticker>?> {

        val apiResult = safeApiCall { eodhdApi.getIndexConstituents() }

        if (apiResult.isFailure || apiResult.getOrNull() == null) {
            return Result.failure(apiResult.exceptionOrNull()!!)
        } else {
            val tickers = apiResult.getOrNull()!!.Components.values
            val sortedTickers = tickers.sortedByDescending { it.Weight }
            return Result.success(sortedTickers.map { it.toTicker() })
        }
    }

    override suspend fun getAllTickerList(exchangeCode: String): Result<List<Ticker>?> {

        val response = eodhdApi.getExchangeSymbolsList()
        println(response.code)
        println(response.body())
        println(response.errorBody())

        return Result.success(emptyList())
    }


}