package com.topdownedge.data.di

import android.app.Application
import androidx.room.Room
import com.topdownedge.data.local.MarketDatabase
import com.topdownedge.data.local.PortfolioDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

//    val MIGRATION_1_2 = object : Migration(1, 2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("CREATE INDEX IF NOT EXISTS `index_TickerEntity_name` ON `TickerEntity` (`name`)")
//        }
//    }

    @Provides
    @Singleton
    fun provideMarketDatabase(appContext: Application): MarketDatabase {
        return Room.databaseBuilder(
            appContext,
            MarketDatabase::class.java,
            "market_info"
        )
//            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTickerDao(db: MarketDatabase) = db.tickerDao()

    @Provides
    @Singleton
    fun providePriceBarDao(db: MarketDatabase) = db.priceBarDao()

    @Provides
    @Singleton
    fun provideLastKnownPriceDao(db: MarketDatabase) = db.lastKnownPriceDao()



    @Provides
    @Singleton
    fun providePortfolioDatabase(appContext: Application): PortfolioDatabase {
        return Room.databaseBuilder(
            appContext,
            PortfolioDatabase::class.java,
            "user_portfolio"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserTradeDao(db: PortfolioDatabase) = db.userTradeDao()

    @Provides
    @Singleton
    fun provideUserPositionDao(db: PortfolioDatabase) = db.userPositionDao()



}