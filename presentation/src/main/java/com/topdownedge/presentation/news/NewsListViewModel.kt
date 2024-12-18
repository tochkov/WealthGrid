package com.topdownedge.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topdownedge.domain.entities.NewsArticle
import com.topdownedge.domain.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel
@Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {

    // https://stackoverflow.com/questions/10282939/how-to-get-favicons-url-from-a-generic-webpage-in-javascript?answertab=scoredesc#tab-top
    // - https://t3.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=https://cnbc.com&size=64
    // - https://webutility.io/favicon-extractor

        // maybe do this in a generic style UIState<T>
    data class UIState(
        var isLoading: Boolean = false,
        var news: List<NewsArticle> = emptyList(),
        var isError: Boolean = false,
        var errorMassage: String = "error",
    )

    private val _newsState = MutableStateFlow(UIState(isLoading = true))
    val newsState = _newsState.asStateFlow()

    init {
        getNewzzz()
    }

    fun getNewzzz() {
        viewModelScope.launch {
            newsRepository.getNews().collect { value ->

                if (value.isSuccess) {
                    _newsState.update {
                        it.copy(
                            isLoading = false,
                            news = value.getOrNull() ?: emptyList()
                        )
                    }
                } else {
                    _newsState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            errorMassage = value.exceptionOrNull()?.message ?: "error"
                        )
                    }
                }

            }
        }


    }

}