package com.topdownedge.data.di

import com.topdownedge.data.repositories.MarketInfoRepositoryImpl
import com.topdownedge.data.repositories.NewsRepositoryImpl
import com.topdownedge.data.repositories.TokenRepositoryImpl
import com.topdownedge.domain.repositories.MarketInfoRepository
import com.topdownedge.domain.repositories.NewsRepository
import com.topdownedge.domain.repositories.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface  RepositoryModule {

    @Binds
    fun bindsTokenRepository(impl: TokenRepositoryImpl): TokenRepository

    @Binds
    fun bindsNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    @Binds
    fun bindsMarketInfoRepository(impl: MarketInfoRepositoryImpl): MarketInfoRepository

}