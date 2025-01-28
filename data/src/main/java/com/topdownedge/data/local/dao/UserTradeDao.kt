package com.topdownedge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.topdownedge.data.local.UserTradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserTradeDao {

    @Insert
    suspend fun insertUserTrade(trade: UserTradeEntity)

    @Query("""
        SELECT * FROM user_trades 
        ORDER BY dateTraded DESC
        """)
    fun getAllUserTrades(): Flow<List<UserTradeEntity>>

    @Query("""
        SELECT * FROM user_trades 
        WHERE symbol = :symbol
        """)
    fun getUserTradesForSymbol(symbol: String): Flow<List<UserTradeEntity>>


}