package com.topdownedge.presentation.news

/**
 * Handles paginated data loading.
 *
 * @param Item The type of items being paginated
 * @property requestPage Function that loads a page of items
 * @property onLoadingUpdated Callback for loading state changes
 * @property onSuccess Callback for successful page loads
 * @property onFailure Callback for loading failures
 * @property initialPage Starting page number
 */
class JustPaginator<Item>(
    private val requestPage: suspend (pageToLoad: Int) -> Result<List<Item>?>,
    private val onLoadingUpdated: (isLoading: Boolean) -> Unit,
    private val onSuccess: (items: List<Item>) -> Unit,
    private val onFailure: (e: Throwable?) -> Unit,
    private val initialPage: Int = 0
) {

    var currentPage = initialPage
        private set
    private var isLoading = false

    /**
     * Loads the next page of items if not currently loading.
     * Updates loading state and triggers appropriate callbacks.
     */
    suspend fun loadNextPage() {
        if (isLoading) {
            return
        }
        isLoading = true
        onLoadingUpdated(true)

        val result = requestPage(currentPage)

        if (result.isSuccess) {
            onSuccess(result.getOrNull() ?: emptyList())
            isLoading = false
            onLoadingUpdated(false)
            currentPage++
        } else {
            onFailure(result.exceptionOrNull())
            isLoading = false
            onLoadingUpdated(false)
        }
    }

    /**
     * Resets the paginator to its initial state.
     */
    suspend fun reset() {
        currentPage = initialPage
        isLoading = false
    }
}