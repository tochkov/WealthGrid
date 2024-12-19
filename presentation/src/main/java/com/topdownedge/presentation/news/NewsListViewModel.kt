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
        var news: List<NewsArticle> = emptyList(),
        var isLoading: Boolean = false,
        var hasMoreToLoad: Boolean = true,
        var isError: Boolean = false,
        var errorMassage: String = "error",
    )

    var ticker = "AAPL.US"

    // TODO why do I need stateFLow not just state after the pagination implementation?
    private val _newsState = MutableStateFlow(UIState(isLoading = true))
    val newsState = _newsState.asStateFlow()

    private val paginator: FlowPaginator<NewsArticle> = FlowPaginator<NewsArticle>(
        requestPage = { page ->
            newsRepository.getGeneralNews(page)
//            newsRepository.getNewsForTicker(ticker, page)
        },
        onLoadingUpdated = { isLoading ->
            _newsState.update {
                it.copy(isLoading = isLoading)
            }
        },
        onSuccess = { items ->
            _newsState.update {
                it.copy(
//                    news = (it.news + items).distinctBy { article -> article.url }
                    news = it.news + items,
                    hasMoreToLoad = items.isNotEmpty(),
                    isError = false

                )
            }
        },
        onFailure = { e ->
            _newsState.update {
                it.copy(
                    isError = true,
                    errorMassage = e?.message ?: "error"
                )
            }
        }
    )

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        viewModelScope.launch {
            paginator.loadNextPage()
        }
    }

}