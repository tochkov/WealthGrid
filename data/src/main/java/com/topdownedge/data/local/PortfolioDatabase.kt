package com.topdownedge.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.topdownedge.data.local.dao.UserTradeDao

@Database(
    entities = [
        UserTradeEntity::class,
    ],
    version = 2,
//        autoMigrations = [
//        AutoMigration(
//            from = 1, to = 2,
//        )
//    ]
)
@TypeConverters(TypeConverterz::class)
abstract class PortfolioDatabase: RoomDatabase() {

    abstract fun userTradeDao(): UserTradeDao
}