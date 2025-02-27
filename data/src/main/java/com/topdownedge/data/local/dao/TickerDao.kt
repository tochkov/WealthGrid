package com.topdownedge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.topdownedge.data.local.TickerEntity
import com.topdownedge.domain.entities.common.TickerWithPrice
import kotlinx.coroutines.flow.Flow

const val RESULTS_LIMIT = 15

@Dao
interface TickerDao {

    @Query(
        """
        SELECT * FROM stock_tickers
        WHERE code LIKE :queryString || '%'  
        OR code LIKE '%' || :queryString || '%' 
        OR name LIKE :queryString || '%'  
        OR name LIKE '% ' || :queryString || '%'  
        ORDER BY
            CASE
                WHEN UPPER(code) = UPPER(:queryString) THEN 1 -- Exact code match (highest priority)
                WHEN code LIKE :queryString || '%' THEN 2 -- Code starts with queryString
                WHEN name LIKE :queryString || '%' THEN 3 -- Name starts with queryString
                WHEN name LIKE '% ' || :queryString || '%' THEN 4 -- Name contains a word starting with queryString
                WHEN code LIKE '%' || :queryString || '%' THEN 5 -- Code contains queryString
                ELSE 6 -- Everything else (lowest priority)
            END,
            indexWeight DESC -- Secondary sort by index_weight
        LIMIT $RESULTS_LIMIT
    """
    )
    fun searchTickers(queryString: String): Flow<List<TickerEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    @Upsert
    suspend fun upsertAllTickers(tickers: List<TickerEntity>)


    @Query(
        """
        SELECT * FROM stock_tickers
        ORDER BY indexWeight DESC
        LIMIT $RESULTS_LIMIT
        """
    )
    fun getAllIndexTickers(): Flow<List<TickerEntity>>


    @Query("""
    SELECT 
        t.code,
        t.name,
        t.exchange,
        p.close as lastPrice,
        p.previousClose,
        p.changePercentage
    FROM stock_tickers t
    LEFT JOIN last_known_prices p ON t.code = p.code
    ORDER BY t.indexWeight DESC
    LIMIT $RESULTS_LIMIT
""")
    fun getTopTickersWithPrices(): Flow<List<TickerWithPrice>>



}
