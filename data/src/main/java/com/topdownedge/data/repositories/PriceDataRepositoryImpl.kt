package com.topdownedge.data.repositories

import android.util.Log
import com.topdownedge.data.local.dao.TickerDao
import com.topdownedge.data.remote.EodhdFundamentalsApi
import com.topdownedge.data.remote.EodhdPriceDataApi
import com.topdownedge.data.remote.safeApiCall
import com.topdownedge.domain.entities.common.BarInterval
import com.topdownedge.domain.entities.common.PriceBar
import com.topdownedge.domain.repositories.PriceDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class PriceDataRepositoryImpl
@Inject constructor(
    private val eodhdApi: EodhdPriceDataApi,
    private val tickerDao: TickerDao
) : PriceDataRepository {
    override fun getHistoricalPrices(
        symbol: String,
        fromDate: LocalDate?,
        toDate: LocalDate?,
        interval: BarInterval
    ): Flow<Result<List<PriceBar>>> = flow {

        val fromDateStr = fromDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val toDateStr = toDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        Log.e("XXX", "interval: ${interval.value}" )
        val apiResult = safeApiCall {
            eodhdApi.getHistoricalPrices(
                symbol,
                fromDateStr,
                toDateStr,
                interval.value
            )
        }

        if(apiResult.isSuccess) {
            Log.e("XXX", "apiResult.isSuccess")
            Log.e("XXX", apiResult.getOrNull().toString())
//            emit(Result.success(apiResult.getOrNull()!!.map { it.toPriceBar() }))
        } else {
            Log.e("XXX", "apiResult.FAILURE")
//            emit(Result.failure(apiResult.exceptionOrNull()!!))


        }
    }
}