package com.topdownedge.domain.repositories

import com.topdownedge.domain.entities.UserPosition
import com.topdownedge.domain.entities.UserTrade
import kotlinx.coroutines.flow.Flow

interface UserPortfolioRepository {

    fun getAllUserTrades(): Flow<List<UserTrade>>

    fun getUserTradesForSymbol(symbol: String): Flow<List<UserTrade>>

    suspend fun submitUserTrade(trade: UserTrade)

    fun getUserPositionForSymbol(symbol: String): Flow<UserPosition?>

    fun getAllUserPositions(): Flow<List<UserPosition>>

}