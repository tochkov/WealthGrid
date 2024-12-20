package com.topdownedge.data.repositories

import com.topdownedge.data.remote.EodhdNewsApi
import com.topdownedge.data.remote.dto.toNewsArticle
import com.topdownedge.data.remote.safeApiCall
import com.topdownedge.domain.entities.NewsArticle
import com.topdownedge.domain.repositories.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl
@Inject constructor(private val newsApi: EodhdNewsApi) : NewsRepository {

    // TODO Consider how to cancel calls if user taps on different tab (General, AAPL, etc) while news is loading

    override suspend fun getGeneralNews(page: Int): Result<List<NewsArticle>?> {
        return safeApiCall {
            newsApi.getGeneralNews(
                EodhdNewsApi.ITEMS_PER_PAGE,
                page * EodhdNewsApi.ITEMS_PER_PAGE
            )
        }.map {
            it?.map { it.toNewsArticle() }
        }
    }

    override suspend fun getNewsForTicker(ticker: String, page: Int): Result<List<NewsArticle>?> {
        return safeApiCall {
            newsApi.getNewsForSymbol(
                ticker,
                EodhdNewsApi.ITEMS_PER_PAGE,
                page * EodhdNewsApi.ITEMS_PER_PAGE
            )
        }.map {
            it?.map { it.toNewsArticle() }
        }
    }

    override suspend fun getNewsForTopic(topic: String, page: Int): Result<List<NewsArticle>?> {
        return safeApiCall {
            newsApi.getNewsForTopic(
                topic,
                EodhdNewsApi.ITEMS_PER_PAGE,
                page * EodhdNewsApi.ITEMS_PER_PAGE
            )
        }.map {
            it?.map { it.toNewsArticle() }
        }
    }

}

