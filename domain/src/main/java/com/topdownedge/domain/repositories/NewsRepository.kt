package com.topdownedge.domain.repositories

import com.topdownedge.domain.Resource
import com.topdownedge.domain.entities.NewsArticle
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(): Flow<Resource<List<NewsArticle>?>>


}