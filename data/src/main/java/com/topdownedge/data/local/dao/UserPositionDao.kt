package com.topdownedge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.topdownedge.data.local.UserPositionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPositionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    @Upsert
    suspend fun upsertPosition(position: UserPositionEntity)

    @Update
    suspend fun updatePositions(positions: List<UserPositionEntity>)

    @Query("SELECT * FROM user_positions WHERE symbol = :symbol")
    fun getUserPositionForSymbol(symbol: String): Flow<UserPositionEntity>

    @Query("SELECT * FROM user_positions ORDER BY totalInvested DESC")
    fun getAllUserPositions(): Flow<List<UserPositionEntity>>
}