package com.topdownedge.presentation.news

/**
 * Handles paginated data loading with support for both pagination and refresh operations.
 *
 * @param Item The type of items being paginated
 * @property requestPage Function that loads a page of items given a page number
 * @property onLoadingUpdated Callback invoked when pagination loading state changes
 * @property onSuccessNextPage Callback invoked when a page is successfully loaded
 * @property onFailureNextPage Callback invoked when page loading fails
 * @property onRefreshingUpdated Callback invoked when refresh loading state changes
 * @property onSuccessRefresh Callback invoked when refresh operation succeeds
 * @property onFailureRefresh Callback invoked when refresh operation fails
 * @property initialPage Starting page number for pagination, defaults to 0
 */
class JustPaginator<Item>(
    private val requestPage: suspend (pageToLoad: Int) -> Result<List<Item>?>,
    private val onLoadingUpdated: (isLoading: Boolean) -> Unit,
    private val onSuccessNextPage: (items: List<Item>) -> Unit,
    private val onFailureNextPage: (e: Throwable?) -> Unit,
    private val onRefreshingUpdated: (isRefreshing: Boolean) -> Unit = {},
    private val onSuccessRefresh: (items: List<Item>) -> Unit = {},
    private val onFailureRefresh: (e: Throwable?) -> Unit = {},
    val initialPage: Int = 0
) {


    var currentPage = initialPage
        private set
    private var isLoading = false
    private var isRefreshing = false

    /**
     * Loads the next page of items if not currently loading.
     * Updates loading state and triggers appropriate callbacks.
     */
    suspend fun loadNextPage() {
        if (isRefreshing || isLoading) {
            return
        }
        isLoading = true
        onLoadingUpdated(true)

        val result = requestPage(currentPage)

        if (result.isSuccess) {
            onSuccessNextPage(result.getOrNull() ?: emptyList())
            isLoading = false
            onLoadingUpdated(false)
            currentPage++
        } else {
            onFailureNextPage(result.exceptionOrNull())
            isLoading = false
            onLoadingUpdated(false)
        }
    }

    /**
     * Refreshes the data by resetting to the initial page and loading it.
     * Updates refreshing state and triggers appropriate callbacks.
     */
    suspend fun refreshToInitial() {
        if (isRefreshing) {
            return
        }
        isRefreshing = true
        onRefreshingUpdated(true)

        currentPage = initialPage

        val result = requestPage(currentPage)

        if (result.isSuccess) {
            onSuccessRefresh(result.getOrNull() ?: emptyList())
            isRefreshing = false
            onRefreshingUpdated(false)
            currentPage++
        } else {
            onFailureRefresh(result.exceptionOrNull())
            isRefreshing = false
            onRefreshingUpdated(false)
        }
    }
}