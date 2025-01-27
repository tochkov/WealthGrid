package com.topdownedge.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.topdownedge.data.local.PriceBarEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PriceBarDao {

    @Upsert
    suspend fun upsertPriceBars(priceBars: List<PriceBarEntity>)

    @Query("""
        SELECT * FROM price_bars_daily 
        WHERE symbol = :symbol 
        AND dateTimestamp >= :startDate 
        AND (:endDate IS NULL OR dateTimestamp <= :endDate)
        ORDER BY dateTimestamp ASC
    """)
    fun observePriceBarsForSymbol(
        symbol: String,
        startDate: Long,
        endDate: Long? = null
    ): Flow<List<PriceBarEntity>>

    @Query("""
        SELECT MAX(dateTimestamp) 
        FROM price_bars_daily 
        WHERE symbol = :symbol
    """)
    suspend fun getLastDateForSymbol(symbol: String): LocalDate?

}