package com.topdownedge.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.topdownedge.data.local.dao.PriceBarDao
import com.topdownedge.data.local.dao.TickerDao

@Database(
    entities = [
        TickerEntity::class,
        PriceBarEntity::class
    ],
    version = 5,
//    autoMigrations = [
//        AutoMigration(
//            from = 4, to = 5,
//            spec = MarketDatabase.DeleteDate::class
//        )
//    ]
)
@TypeConverters(TypeConverterz::class)
abstract class MarketDatabase : RoomDatabase() {

    abstract fun tickerDao(): TickerDao

    abstract fun priceBarDao(): PriceBarDao

//    @RenameColumn(
//        tableName = "price_bars_daily",
//        fromColumnName = "dateTimeStamp",
//        toColumnName = "dateTimestamp"
//    )
//    class RenameTimestamp : AutoMigrationSpec
//
//    @DeleteColumn(
//        tableName = "price_bars_daily",
//        columnName = "dateTimeStamp"
//    )
//    class DeleteDate : AutoMigrationSpec

}