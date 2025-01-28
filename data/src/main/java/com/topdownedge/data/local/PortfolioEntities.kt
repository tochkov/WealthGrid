package com.topdownedge.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "user_trades",
    indices = [
        Index(value = ["tickerCode", "tickerExchange"]),
        Index(value = ["dateTraded"])
    ]
)
data class UserTradeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tickerCode: String,
    val tickerExchange: String,
    val dateSubmitted: LocalDate,
    val dateTraded: LocalDate,
    val price: Double,
    val shares: Double,
    val isBuy: Boolean = true,
)