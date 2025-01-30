package com.topdownedge.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.topdownedge.data.local.UserPositionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPositionDao {

    @Upsert
    suspend fun upsertPosition(position: UserPositionEntity)

    @Query("SELECT * FROM user_positions WHERE symbol = :symbol")
    fun getUserPositionForSymbol(symbol: String): Flow<UserPositionEntity>

    @Query("SELECT * FROM user_positions ORDER BY totalInvested DESC")
    fun getAllUserPositions(): Flow<List<UserPositionEntity>>
}