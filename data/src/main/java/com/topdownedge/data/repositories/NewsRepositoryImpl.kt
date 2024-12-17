package com.topdownedge.data.repositories

import com.topdownedge.data.remote.EodhdNewsApi
import com.topdownedge.data.remote.dto.NewsArticleDto
import com.topdownedge.data.remote.dto.toNewsArticle
import com.topdownedge.data.remote.ktorRequestToResult
import com.topdownedge.domain.Resource
import com.topdownedge.domain.entities.NewsArticle
import com.topdownedge.domain.repositories.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl
@Inject constructor(private val newsApi: EodhdNewsApi) : NewsRepository {


    override fun getNews(): Flow<Resource<List<NewsArticle>?>> {

        return flow {

            emit(Resource.loading(true))

            val newsResultDtos: Resource<List<NewsArticleDto>?> =
//                ktorRequestToResult { newsApi.getNewsForSymbol("AAPL.US") }
                ktorRequestToResult { newsApi.getNewsForTopic("european regulatory news") }
            val newsResult: Resource<List<NewsArticle>?> =
                newsResultDtos.mapTo { it?.map { it.toNewsArticle() } }
            emit(newsResult)
            emit(Resource.loading(false))
        }
    }

}

