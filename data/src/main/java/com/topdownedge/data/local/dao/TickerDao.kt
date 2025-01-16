package com.topdownedge.data.local.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import com.topdownedge.domain.entities.common.Ticker
import kotlinx.coroutines.flow.Flow

@Entity(
    indices = [Index(
        value = ["code", "exchange"],
        unique = true
    )]
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

    @Upsert
    suspend fun upsertAllTickers(tickers: List<TickerEntity>)

//    @Query("SELECT * FROM TickerEntity WHERE code = :code AND exchange = :exchange")
//    suspend fun getTickerByCodeAndExchange(code: String, exchange: String): TickerEntity?

    @Query("SELECT * FROM tickerentity ORDER BY index_weight DESC")
    fun getAllIndexTickers(): Flow<List<TickerEntity>>




}

fun TickerEntity.toTicker(): Ticker{
    return Ticker(
        code = code,
        name = name,
        exchange = exchange,
        type = type ?: "",
        indexWeight = index_weight
    )
}

fun Ticker.toTickerEntity(): TickerEntity{
    return TickerEntity(
        code = code,
        name = name,
        exchange = exchange,
        type = type,
        index_weight = indexWeight
    )

}