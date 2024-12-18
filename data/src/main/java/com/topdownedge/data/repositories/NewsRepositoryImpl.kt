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


    override fun getNews(): Flow<Result<List<NewsArticle>?>> {

        return flow {

            val newsResult =
                safeApiCall {
                    newsApi.getNewsForSymbol("SPY.US")
//                    newsApi.getNewsForTopic("european regulatory news")
                }.map {
                    it?.map { it.toNewsArticle() }
                }
            emit(newsResult)
        }
    }

}

