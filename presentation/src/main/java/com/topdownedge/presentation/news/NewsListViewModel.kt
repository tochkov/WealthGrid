package com.topdownedge.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topdownedge.domain.entities.NewsArticle
import com.topdownedge.domain.entities.NewsCategory
import com.topdownedge.domain.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

data class NewsListUiState(
    var news: List<NewsArticle> = emptyList(),
    var isLoading: Boolean = false,
    var isRefreshing: Boolean = false,
    var hasMoreToLoad: Boolean = true,
    var paginationError: Boolean = false,
    var errorMassage: String = "error",
    var selectedCategory: NewsCategory = NewsCategory.General,
)

@HiltViewModel
class NewsListViewModel
@Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {

    // https://stackoverflow.com/questions/10282939/how-to-get-favicons-url-from-a-generic-webpage-in-javascript?answertab=scoredesc#tab-top
    // - https://t3.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=https://cnbc.com&size=64
    // - https://webutility.io/favicon-extractor

    val newsState: StateFlow<NewsListUiState>
        field = MutableStateFlow(NewsListUiState())

    private var currentCategory: NewsCategory? = null
    private lateinit var currentPaginator: JustPaginator<NewsArticle>
    private lateinit var currentNewsList: ArrayList<NewsArticle>
    private val categoriesMap =
        mutableMapOf<NewsCategory, Pair<JustPaginator<NewsArticle>, ArrayList<NewsArticle>>>()

    init {
        onCategoryChange(NewsCategory.General)
    }

    private var fetchJob: Job? = null

    fun refreshCategory() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            currentPaginator.refreshToInitial()
        }
    }

    fun loadNextPage() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            currentPaginator.loadNextPage()
        }
    }

    fun onCategoryChange(category: NewsCategory) {
        if (currentCategory == category) {
            return
        }
        currentCategory = category

        fetchJob?.cancel()

        currentPaginator = getCurrentPaginator(category)
        currentNewsList = getCurrentNewsList(category)

        newsState.update {
            it.copy(
                selectedCategory = currentCategory!!,
                news = currentNewsList
            )
        }

        if (currentNewsList.isEmpty()) {
            loadNextPage()
        }
    }

    private fun getCurrentPaginator(category: NewsCategory): JustPaginator<NewsArticle> {
        if (!categoriesMap.containsKey(category)) {
            val newsList = arrayListOf<NewsArticle>()
            val paginator = createPaginator(newsList) { page ->
                newsRepository.getNews(category, page)
            }
            categoriesMap[category] = Pair(paginator, newsList)
        }

        return categoriesMap[category]!!.first
    }

    private fun getCurrentNewsList(category: NewsCategory): ArrayList<NewsArticle> {
        return categoriesMap[category]!!.second
    }

    private fun createPaginator(
        articleList: ArrayList<NewsArticle>,
        requestPage: suspend (pageToLoad: Int) -> Result<List<NewsArticle>?>
    ): JustPaginator<NewsArticle> {

        return JustPaginator<NewsArticle>(
            requestPage = requestPage,
            onLoadingUpdated = { isLoading ->
                newsState.update { it.copy(isLoading = isLoading) }
            },
            onSuccessNextPage = { newItems ->

                articleList.addAll(newItems)

                newsState.update {
                    it.copy(
                        news = articleList,
                        hasMoreToLoad = newItems.isNotEmpty(),
                        paginationError = false
                    )
                }
            },
            onFailureNextPage = { e ->
                if (e !is CancellationException) {
                    newsState.update {
                        it.copy(
                            paginationError = true,
                            errorMassage = e?.message ?: it.errorMassage
                        )
                    }
                }
            },
            onRefreshingUpdated = { isRefreshing ->
                newsState.update { it.copy(isRefreshing = isRefreshing) }
            },
            onSuccessRefresh = { newItems ->

                articleList.clear()
                articleList.addAll(newItems)

                newsState.update {
                    it.copy(
                        news = articleList,
                        hasMoreToLoad = newItems.isNotEmpty(),
                        paginationError = false,
                    )
                }
            },
            onFailureRefresh = { e ->
                if (articleList.isEmpty() && e !is CancellationException) {
                    newsState.update {
                        it.copy(
                            paginationError = true,
                            errorMassage = e?.message ?: it.errorMassage
                        )
                    }
                }
            }
        )

    }

}