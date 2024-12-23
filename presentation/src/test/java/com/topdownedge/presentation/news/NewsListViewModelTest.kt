package com.topdownedge.presentation.news

import com.google.common.truth.Truth.assertThat
import com.topdownedge.domain.entities.NewsArticle
import com.topdownedge.domain.entities.NewsCategory
import com.topdownedge.domain.repositories.NewsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val newsRepository = mockk<NewsRepository>()
    private lateinit var viewModel: NewsListViewModel

    private val ARTICLES_GENERAL = createTestArticles("General News ")
    private val ARTICLES_TICKER = createTestArticles("Ticker News ")
    private val tickerCategory = NewsCategory.Ticker("TEST")

    private val defaultUiState = NewsListUiState(
        news = emptyList(),
        isLoading = false,
        isRefreshing = false,
        hasMoreToLoad = true,
        paginationError = false,
        errorMassage = "",
        selectedCategory = NewsCategory.General,
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        setupRepositoryMocks()
        viewModel = NewsListViewModel(newsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should match default values`() = runBlocking {
        assertThat(viewModel.newsState.value).isEqualTo(defaultUiState)
    }

    @Test
    fun `repository error should update error state`() = runBlocking {
        val errorMessage = "Network error"
        coEvery {
            newsRepository.getNews(NewsCategory.General, 0)
        } returns Result.failure(Exception(errorMessage))

        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.newsState.value).isEqualTo(
            defaultUiState.copy(
                paginationError = true,
                errorMassage = errorMessage
            )
        )
    }

    @Test
    fun `loading state should be handled correctly during operations`() = runBlocking {
        coEvery {
            newsRepository.getNews(NewsCategory.General, 0)
        } coAnswers {
            delay(100)
            Result.success(ARTICLES_GENERAL.take(PAGE_SIZE))
        }
        println("XXX - initial test")
        viewModel.loadNextPage()
        testDispatcher.scheduler.advanceTimeBy(50)
        println("XXX - assert1")
        assertThat(viewModel.newsState.value.isLoading).isTrue()

        testDispatcher.scheduler.advanceTimeBy(100)
        println("XXX - assert2")
        assertThat(viewModel.newsState.value.isLoading).isFalse()
    }

    @Test
    fun `loading initial page should update state correctly`() = runBlocking {
        // When initial load starts
        testDispatcher.scheduler.runCurrent()
        assertThat(viewModel.newsState.value).isEqualTo(
            defaultUiState.copy(isLoading = true)
        )

        // When initial load completes
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.newsState.value).isEqualTo(
            defaultUiState.copy(news = ARTICLES_GENERAL.take(PAGE_SIZE))
        )
        assertThat(viewModel.currentPaginator.pageToLoad).isEqualTo(1)
    }

    @Test
    fun `loading subsequent pages should append items correctly`() = runBlocking {
        // Load first page
        testDispatcher.scheduler.advanceUntilIdle()

        // Load second page
        viewModel.loadNextPage()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.newsState.value).isEqualTo(
            defaultUiState.copy(news = ARTICLES_GENERAL.take(PAGE_SIZE * 2))
        )
        assertThat(viewModel.currentPaginator.pageToLoad).isEqualTo(2)
    }

    @Test
    fun `empty page response should disable further loading`() = runBlocking {
        // Load all available pages
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.loadNextPage()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.loadNextPage()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.newsState.value).isEqualTo(
            defaultUiState.copy(
                news = ARTICLES_GENERAL.take(PAGE_SIZE * 2),
                hasMoreToLoad = false
            )
        )
    }

    @Test
    fun `duplicate articles in subsequent pages should be filtered out`() = runBlocking {
        // Setup repository to return overlapping articles
        coEvery {
            newsRepository.getNews(NewsCategory.General, 1)
        } returns Result.success(ARTICLES_GENERAL.drop(8).take(PAGE_SIZE))

        // Load pages
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.loadNextPage()
        testDispatcher.scheduler.advanceUntilIdle()

        val resultNews = viewModel.newsState.value.news
        assertThat(resultNews.distinct()).isEqualTo(resultNews)
        assertThat(resultNews).isEqualTo(ARTICLES_GENERAL.take(18))
    }

    @Test
    fun `refresh should clear existing data and reload first page`() = runBlocking {
        // Load initial data
        testDispatcher.scheduler.advanceUntilIdle()
        // Load next page
        viewModel.loadNextPage()
        testDispatcher.scheduler.advanceUntilIdle()

        // Refresh
        viewModel.refreshCategory()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.newsState.value).isEqualTo(
            defaultUiState.copy(news = ARTICLES_GENERAL.take(PAGE_SIZE))
        )
        assertThat(viewModel.currentPaginator.pageToLoad).isEqualTo(1)
    }

    @Test
    fun `changing category should load new data`() = runBlocking {
        // Load initial category data
        testDispatcher.scheduler.advanceUntilIdle()
        // Load next page
        viewModel.loadNextPage()
        testDispatcher.scheduler.advanceUntilIdle()
        // Load next page
        viewModel.loadNextPage()
        testDispatcher.scheduler.advanceUntilIdle()


        // Change category
        viewModel.onCategoryChange(tickerCategory)
        testDispatcher.scheduler.advanceUntilIdle()
        // Load next page
        viewModel.loadNextPage()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.newsState.value).isEqualTo(
            defaultUiState.copy(
                selectedCategory = tickerCategory,
                news = ARTICLES_TICKER.take(PAGE_SIZE * 2)
            )
        )
        assertThat(viewModel.currentPaginator.pageToLoad).isEqualTo(2)
    }

    private fun setupRepositoryMocks() {
        // Setup mock for General category

        val MOCK_DELAY = 20L

        coEvery {
            newsRepository.getNews(NewsCategory.General, 0)
        } coAnswers {
            delay(MOCK_DELAY)
            Result.success(ARTICLES_GENERAL.take(PAGE_SIZE))
        }
        coEvery {
            newsRepository.getNews(NewsCategory.General, 1)
        } coAnswers {
            delay(MOCK_DELAY)
            Result.success(ARTICLES_GENERAL.drop(PAGE_SIZE).take(PAGE_SIZE))
        }
        coEvery {
            newsRepository.getNews(NewsCategory.General, 2)
        } coAnswers {
            delay(MOCK_DELAY)
            Result.success(emptyList())
        }

        // Setup mock for Ticker category
        coEvery {
            newsRepository.getNews(tickerCategory, 0)
        } coAnswers {
            delay(MOCK_DELAY)
            Result.success(ARTICLES_TICKER.take(PAGE_SIZE))
        }
        coEvery {
            newsRepository.getNews(tickerCategory, 1)
        } coAnswers {
            delay(MOCK_DELAY)
            Result.success(ARTICLES_TICKER.drop(PAGE_SIZE).take(PAGE_SIZE))
        }
    }

    private fun createTestArticles(titlePrefix: String): List<NewsArticle> {
        return ('a'..'z').map { symbol ->
            NewsArticle(
                date = "2024-01-${symbol.code}",
                title = "$titlePrefix $symbol",
                content = "Test content",
                source = "test.com",
                url = "https://test.com/news/$symbol",
                symbols = emptyList(),
                tags = emptyList()
            )
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}