package com.topdownedge.domain.repositories

import kotlinx.coroutines.flow.Flow

interface  LivePricesRepository {

    fun observeLivePricesConnection(): Flow<Map<String, Double>>

    fun closeLivePricesConnection()

    fun subscribeToLivePrices(tickers: List<String>)

    fun unsubscribeFromLivePrices(tickers: List<String>)



}