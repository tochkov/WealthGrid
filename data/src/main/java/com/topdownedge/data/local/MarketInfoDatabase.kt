package com.topdownedge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.topdownedge.data.local.dao.TickerDao
import com.topdownedge.data.local.dao.TickerEntity

@Database(
    entities = [TickerEntity::class],
    version = 1
)
abstract class MarketInfoDatabase: RoomDatabase() {

    abstract fun tickerDao(): TickerDao



}