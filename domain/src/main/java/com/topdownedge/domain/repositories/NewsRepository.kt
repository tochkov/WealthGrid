package com.topdownedge.domain.repositories

import com.topdownedge.domain.entities.NewsArticle
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getGeneralNews(page: Int): Flow<Result<List<NewsArticle>?>>

    fun getNewsForTicker(ticker: String, page: Int): Flow<Result<List<NewsArticle>?>>

    fun getNewsForTopic(topic: String, page: Int): Flow<Result<List<NewsArticle>?>>


}