package com.topdownedge.presentation.news

import androidx.lifecycle.ViewModel
import com.topdownedge.domain.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel
@Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {

}