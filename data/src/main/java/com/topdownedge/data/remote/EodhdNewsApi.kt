package com.topdownedge.data.remote

import com.topdownedge.data.remote.dto.NewsArticleDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

/**
 * @note Generated with the help of Claude - https://claude.ai/chat/e1273053-50ad-4f9c-a88b-925e6763204d
 *
 * Ktorfit interface for accessing EODHD financial news API endpoints.
 * API Documentation: https://eodhd.com/financial-apis/stock-market-financial-news-api
 *
 * This API provides access to financial news articles with the following capabilities:
 * - Fetch news by ticker symbol
 * - Fetch news by specific financial topics
 * - Includes sentiment analysis
 * - Supports pagination
 * - Date range filtering
 */
interface EodhdNewsApi {
    /**
     * Fetches news articles for a specific ticker symbol.
     *
     * @param symbol The ticker symbol to get news for (e.g., "AAPL.US")
     * @param limit Optional number of results to return (1-1000, default: 50)
     * @param offset Optional offset for pagination (min: 0, default: 0)
     * @param fromDate Optional start date for news articles (format: YYYY-MM-DD)
     * @param toDate Optional end date for news articles (format: YYYY-MM-DD)
     * @return List of news articles related to the specified ticker
     */
    @GET("api/news")
    suspend fun getNewsForSymbol(
        @Query("s") symbol: String,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("from") fromDate: String? = null,
        @Query("to") toDate: String? = null
    ): List<NewsArticleDto>

    /**
     * Fetches news articles for a specific topic.
     *
     * @param topic The topic tag to get news for. See [SUPPORTED_TOPICS] for valid values
     * @param limit Optional number of results to return (1-1000, default: 50)
     * @param offset Optional offset for pagination (min: 0, default: 0)
     * @param fromDate Optional start date for news articles (format: YYYY-MM-DD)
     * @param toDate Optional end date for news articles (format: YYYY-MM-DD)
     * @return List of news articles related to the specified topic
     */
    @GET("api/news")
    suspend fun getNewsForTopic(
        @Query("t") topic: String,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("from") fromDate: String? = null,
        @Query("to") toDate: String? = null
    ): List<NewsArticleDto>

    companion object {
        /**
         * List of supported topics for the getNewsForTopic endpoint.
         * These topics can be used to filter news articles by specific financial themes.
         */
        val SUPPORTED_TOPICS = listOf(
            "balance sheet",
            "capital employed",
            "class action",
            "company announcement",
            "consensus eps estimate",
            "consensus estimate",
            "credit rating",
            "discounted cash flow",
            "dividend payments",
            "earnings estimate",
            "earnings growth",
            "earnings per share",
            "earnings release",
            "earnings report",
            "earnings results",
            "earnings surprise",
            "estimate revisions",
            "european regulatory news",
            "financial results",
            "fourth quarter",
            "free cash flow",
            "future cash flows",
            "growth rate",
            "initial public offering",
            "insider ownership",
            "insider transactions",
            "institutional investors",
            "institutional ownership",
            "intrinsic value",
            "market research reports",
            "net income",
            "operating income",
            "present value",
            "press releases",
            "price target",
            "quarterly earnings",
            "quarterly results",
            "ratings",
            "research analysis and reports",
            "return on equity",
            "revenue estimates",
            "revenue growth",
            "roce",
            "roe",
            "share price",
            "shareholder rights",
            "shareholder",
            "shares outstanding",
            "split",
            "strong buy",
            "total revenue",
            "zacks investment research",
            "zacks rank"
        )
    }
}

