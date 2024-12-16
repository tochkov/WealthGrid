package com.topdownedge.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data transfer object representing a news article from the EODHD API
 */
@Serializable
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
@Serializable
data class SentimentDto(
    val polarity: Double,
    val neg: Double,
    val neu: Double,
    val pos: Double
)