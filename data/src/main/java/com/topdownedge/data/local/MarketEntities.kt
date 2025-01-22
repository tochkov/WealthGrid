package com.topdownedge.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(
    tableName = "stock_tickers",
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
    var indexWeight: Double? = null,
)

@Entity(
    tableName = "price_bars_daily",
    indices = [
        Index(value = ["symbol", "dateTimestamp"], unique = true),
        Index(value = ["symbol"]),
        Index(value = ["dateTimestamp"])
    ]
)
data class PriceBarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val symbol: String,
    val dateTimestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val adjustedClose: Double,
    val volume: Long
)