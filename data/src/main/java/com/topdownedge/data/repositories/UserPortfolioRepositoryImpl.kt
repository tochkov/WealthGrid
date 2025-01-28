package com.topdownedge.data.repositories

import com.topdownedge.data.local.dao.PriceBarDao
import com.topdownedge.data.local.dao.UserTradeDao
import com.topdownedge.data.mappers.toUserTrade
import com.topdownedge.data.mappers.toUserTradeEntity
import com.topdownedge.data.mappers.toUserTradeList
import com.topdownedge.data.remote.EodhdPriceDataApi
import com.topdownedge.domain.entities.UserTrade
import com.topdownedge.domain.repositories.UserPortfolioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPortfolioRepositoryImpl @Inject constructor(
    private val userTradeDao: UserTradeDao,
) : UserPortfolioRepository {

    override fun getUserTrades(): Flow<List<UserTrade>> =
        userTradeDao.getAllTrades()
            .map { it.toUserTradeList() }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)

    override suspend fun addUserTrade(trade: UserTrade) {

        userTradeDao.insertTrade(trade.toUserTradeEntity())
        // TODO: Update POSITIONS
    }
}