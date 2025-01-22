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
        ORDER BY dateTimestamp ASC
    """)
    fun observePriceBarsForSymbol(symbol: String): Flow<List<PriceBarEntity>>

    @Query("""
        SELECT MAX(dateTimestamp) 
        FROM price_bars_daily 
        WHERE symbol = :symbol
        LIMIT 100
    """)
    suspend fun getLastDateForSymbol(symbol: String): LocalDate?

//    @Query("""
//        SELECT MIN(date)
//        FROM price_bars
//        WHERE symbol = :symbol
//    """)
//    suspend fun getFirstDateForSymbol(symbol: String): LocalDate?
//
//    @Query("DELETE FROM price_bars WHERE symbol = :symbol")
//    suspend fun clearPriceDataForSymbol(symbol: String)
}