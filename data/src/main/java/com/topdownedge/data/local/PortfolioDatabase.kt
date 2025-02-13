package com.topdownedge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.topdownedge.data.local.dao.UserPositionDao
import com.topdownedge.data.local.dao.UserTradeDao

@Database(
    entities = [
        UserTradeEntity::class,
        UserPositionEntity::class,
    ],
    version = 5,
)
@TypeConverters(TypeConverterz::class)
abstract class PortfolioDatabase: RoomDatabase() {

    abstract fun userTradeDao(): UserTradeDao

    abstract fun userPositionDao(): UserPositionDao
}