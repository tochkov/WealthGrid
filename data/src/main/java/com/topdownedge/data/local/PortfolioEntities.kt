package com.topdownedge.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "user_trades",
    indices = [
        Index(value = ["symbol"]),
        Index(value = ["dateTraded"])
    ]
)
data class UserTradeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tickerCode: String,
    val tickerExchange: String,
    val symbol: String = "$tickerCode.$tickerExchange",
    val tickerName: String,
    val dateSubmitted: LocalDate,
    val dateTraded: LocalDate,
    val price: Double,
    val shares: Double,
    val isBuy: Boolean,
)

@Entity(
    tableName = "user_positions",
    indices = [
        Index(value = ["symbol"], unique = true),
    ]
)
data class UserPositionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tickerCode: String,
    val tickerExchange: String,
    val symbol: String = "$tickerCode.$tickerExchange",
    val tickerName: String,

    val firstTradeDate: LocalDate,
    val lastTradeDate: LocalDate,

    val averagePrice: Double,
    val sharesQuantity: Double,
    val totalInvested: Double,

    val lastKnownPrice: Double,

    )