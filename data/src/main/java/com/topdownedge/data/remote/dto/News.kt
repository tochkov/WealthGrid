package com.topdownedge.data.remote.dto

/**
 * Data transfer object representing a news article from the EODHD API
 */
data class NewsArticleDto(
    val date: String,
    val title: String,
    val content: String,
    val link: String,
    val symbols: List<String>,
    val tags: List<String>,
    val sentiment: SentimentDto
)

/**
 * Data class representing sentiment analysis for a news article
 */
data class SentimentDto(
    val polarity: Int,
    val neg: Double,
    val neu: Double,
    val pos: Double
)