package com.topdownedge.data.repositories

import com.topdownedge.data.local.dao.UserPositionDao
import com.topdownedge.data.local.dao.UserTradeDao
import com.topdownedge.data.mappers.toUserPosition
import com.topdownedge.data.mappers.toUserPositionEntity
import com.topdownedge.data.mappers.toUserPositionList
import com.topdownedge.data.mappers.toUserTradeEntity
import com.topdownedge.data.mappers.toUserTradeList
import com.topdownedge.domain.entities.UserPosition
import com.topdownedge.domain.entities.UserTrade
import com.topdownedge.domain.repositories.UserPortfolioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPortfolioRepositoryImpl @Inject constructor(
    private val userTradeDao: UserTradeDao,
    private val userPositionDao: UserPositionDao,
) : UserPortfolioRepository {

    override fun getAllUserTrades(): Flow<List<UserTrade>> =
        userTradeDao.getAllUserTrades()
            .map { it.toUserTradeList() }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)

    override fun getUserTradesForSymbol(symbol: String): Flow<List<UserTrade>>  =
        userTradeDao.getUserTradesForSymbol(symbol)
            .map { it.toUserTradeList() }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)

    override suspend fun submitUserTrade(trade: UserTrade) {
        userTradeDao.insertUserTrade(trade.toUserTradeEntity())
        val existingTrades = getUserTradesForSymbol(trade.symbol).first()
        val position = constructPositionFromTrades(existingTrades)
        userPositionDao.upsertPosition(position.toUserPositionEntity())
    }

    private fun constructPositionFromTrades(trades: List<UserTrade>): UserPosition {
        val firstTrade = trades.first()
        val lastTrade = trades.last()
        val averagePrice = trades.sumOf { it.price * it.shares } / trades.sumOf { it.shares }
        val sharesQuantity = trades.sumOf { it.shares }
        return UserPosition(
            tickerCode = firstTrade.tickerCode,
            tickerExchange = firstTrade.tickerExchange,
            tickerName = firstTrade.tickerName,
            firstTradeDate = firstTrade.dateTraded,
            lastTradeDate = lastTrade.dateTraded,
            averagePrice = averagePrice,
            sharesQuantity = sharesQuantity,
        )
    }

    override fun getUserPositionForSymbol(symbol: String) =
        userPositionDao.getUserPositionForSymbol(symbol)
            .map { it?.toUserPosition() }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)

    override fun getAllUserPositions(): Flow<List<UserPosition>> =
        userPositionDao.getAllUserPositions()
            .map { it.toUserPositionList() }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)


}