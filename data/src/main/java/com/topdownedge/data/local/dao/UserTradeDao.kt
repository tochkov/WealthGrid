package com.topdownedge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.topdownedge.data.local.UserTradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserTradeDao {

    @Insert
    suspend fun insertTrade(trade: UserTradeEntity)

    @Query("SELECT * FROM user_trades")
    fun getAllTrades(): Flow<List<UserTradeEntity>>


}