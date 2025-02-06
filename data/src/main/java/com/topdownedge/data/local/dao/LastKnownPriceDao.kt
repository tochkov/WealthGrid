package com.topdownedge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.topdownedge.data.local.LastKnownPriceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LastKnownPriceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReplaceLastKnownPrices(lastKnownPrices: List<LastKnownPriceEntity>)

    @Query("SELECT * FROM last_known_prices WHERE code IN (:symbols)")
    fun observeLastKnownPrices(symbols: List<String>): Flow<List<LastKnownPriceEntity>>

}