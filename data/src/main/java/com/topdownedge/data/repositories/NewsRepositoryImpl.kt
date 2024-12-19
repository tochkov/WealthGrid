package com.topdownedge.data.repositories

import com.topdownedge.data.remote.EodhdNewsApi
import com.topdownedge.data.remote.dto.toNewsArticle
import com.topdownedge.data.remote.safeApiCall
import com.topdownedge.domain.entities.NewsArticle
import com.topdownedge.domain.repositories.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl
@Inject constructor(private val newsApi: EodhdNewsApi) : NewsRepository {

    override fun getGeneralNews(page: Int): Flow<Result<List<NewsArticle>?>> {

        return flow {
            val newsResult =
                safeApiCall {
                    newsApi.getGeneralNews(
                        EodhdNewsApi.ITEMS_PER_PAGE,
                        page * EodhdNewsApi.ITEMS_PER_PAGE
                    )
                }.map {
                    it?.map { it.toNewsArticle() }
                }
            emit(newsResult)
        }
    }


    override fun getNewsForTicker(ticker: String, page: Int): Flow<Result<List<NewsArticle>?>> {

        return flow {
            val newsResult =
                safeApiCall {
                    newsApi.getNewsForSymbol(
                        ticker,
                        EodhdNewsApi.ITEMS_PER_PAGE,
                        page * EodhdNewsApi.ITEMS_PER_PAGE
                    )
                }.map {
                    it?.map { it.toNewsArticle() }
                }
            emit(newsResult)
        }
    }

    override fun getNewsForTopic(
        topic: String,
        page: Int
    ): Flow<Result<List<NewsArticle>?>> {

        return flow {
            val newsResult =
                safeApiCall {
                    newsApi.getNewsForTopic(
                        topic,
                        EodhdNewsApi.ITEMS_PER_PAGE,
                        page * EodhdNewsApi.ITEMS_PER_PAGE
                    )
                }.map {
                    it?.map { it.toNewsArticle() }
                }
            emit(newsResult)
        }
    }

}

