package com.topdownedge.data.local.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity(
    indices = [
        Index(value = ["code", "exchange"], unique = true),
        Index(value = ["name"])
    ]
)
data class TickerEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: String,
    val name: String,
    val exchange: String,
    var type: String? = null,
    var index_weight: Double? = null,
)
@Dao
interface TickerDao {

    @Query(
        """
        SELECT * FROM tickerentity
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
            index_weight DESC -- Secondary sort by index_weight
        LIMIT 100
    """
    )
    fun searchTickers(queryString: String): Flow<List<TickerEntity>>



    @Upsert
    suspend fun upsertAllTickers(tickers: List<TickerEntity>)


    @Query("""
        SELECT * FROM tickerentity
        ORDER BY index_weight DESC
        LIMIT 100
        """)
    fun getAllIndexTickers(): Flow<List<TickerEntity>>

}
