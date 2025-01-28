package com.topdownedge.domain.repositories

import com.topdownedge.domain.entities.UserTrade
import kotlinx.coroutines.flow.Flow

interface UserPortfolioRepository {


    fun getUserTrades(): Flow<List<UserTrade>>

    suspend fun addUserTrade(trade: UserTrade)

}