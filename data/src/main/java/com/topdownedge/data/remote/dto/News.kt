package com.topdownedge.data.remote.dto

import com.topdownedge.domain.entities.NewsArticle
import kotlinx.serialization.Serializable
import java.net.URL

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

fun NewsArticleDto.toNewsArticle() = NewsArticle(
    date = date,
    title = title,
    content = content,
    url = link,
    source = runCatching { URL(link).host }.getOrDefault(""),
    symbols = symbols,
    tags = tags
)
