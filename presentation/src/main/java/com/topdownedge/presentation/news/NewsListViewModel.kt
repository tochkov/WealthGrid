package com.topdownedge.presentation.news

import android.R.attr.value
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topdownedge.domain.Resource
import com.topdownedge.domain.entities.NewsArticle
import com.topdownedge.domain.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel
@Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {

    // https://stackoverflow.com/questions/10282939/how-to-get-favicons-url-from-a-generic-webpage-in-javascript?answertab=scoredesc#tab-top
    // - https://t3.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=https://cnbc.com&size=64
    // - https://webutility.io/favicon-extractor

    private val _newsState = MutableStateFlow<Resource<List<NewsArticle>?>>(Resource.Loading(true))
    val newsState = _newsState.asStateFlow()

    fun getNewzzz() {
        viewModelScope.launch {
            newsRepository.getNews().collect { value ->
                when (value) {
                    is Resource.Success -> {
                        _newsState.value = value
                        Log.d("XXX", "VM Success: ${value.data?.size ?: -1}")
                    }
                    is Resource.Error -> {
                        _newsState.value = value
                        Log.d("XXX", "VM Err: ${value.message}")
                    }
                    is Resource.Loading -> {
                        _newsState.value = value

                        Log.d("XXX", "VM load: ${value.isLoading}")
                    }
                }
            }
        }
    }

}