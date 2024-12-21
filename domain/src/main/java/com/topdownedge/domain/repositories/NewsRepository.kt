package com.topdownedge.domain.repositories

import com.topdownedge.domain.entities.NewsArticle
import com.topdownedge.domain.entities.NewsCategory

interface NewsRepository {

    suspend fun getGeneralNews(page: Int): Result<List<NewsArticle>?>

    suspend fun getNewsForTicker(ticker: String, page: Int): Result<List<NewsArticle>?>

    suspend fun getNewsForTopic(topic: String, page: Int): Result<List<NewsArticle>?>

    suspend fun getNews(category: NewsCategory, page: Int): Result<List<NewsArticle>?>


}