package com.topdownedge.data.di

import android.app.Application
import androidx.room.Room
import com.topdownedge.data.local.MarketInfoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    @Singleton
    fun provideMarketInfoDatabase(appContext: Application): MarketInfoDatabase {
        return Room.databaseBuilder(
            appContext,
            MarketInfoDatabase::class.java,
            "market_info"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTickerDao(db: MarketInfoDatabase) = db.tickerDao()


}